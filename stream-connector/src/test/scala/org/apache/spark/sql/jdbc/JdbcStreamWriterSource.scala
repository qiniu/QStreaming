/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.jdbc

import java.sql.DriverManager

import com.qiniu.stream.core.PipelineRunner
import com.qiniu.stream.core.config.{ResourcePipelineConfig, Settings}
import org.apache.spark.sql.streaming.StreamTest
import org.apache.spark.util.Utils
import org.scalatest.BeforeAndAfter


class JdbcStreamWriteSuite extends  StreamTest with BeforeAndAfter  {

  val url = "jdbc:h2:mem:testdb"
  val jdbcTableName = "stream_test_table"
  val driverClassName = "org.h2.Driver"
  val createTableSql =
    s"""
       |CREATE TABLE ${jdbcTableName}(
       | name VARCHAR(32),
       | value LONG,
       | PRIMARY KEY (name)
       |)""".stripMargin

  var conn: java.sql.Connection = null

  val testH2Dialect = new JdbcDialect {
    override def canHandle(url: String): Boolean = url.startsWith("jdbc:h2")

    override def isCascadingTruncateTable(): Option[Boolean] = Some(false)
  }

  before {
    Utils.classForName(driverClassName)
    conn = DriverManager.getConnection(url)
    conn.prepareStatement(createTableSql).executeUpdate()
  }

  after {
    conn.close()
  }

  test("Basic Write") {
    val pipeline = PipelineRunner(ResourcePipelineConfig("write/jdbc.dsl"),Some(Settings.empty.withValue("stream.debug","true")))
    pipeline.run()
    val result = conn
      .prepareStatement(s"select count(*) as count from $jdbcTableName")
      .executeQuery()
    assert(result.next())
    assert(result.getInt("count") == 10)
  }


}