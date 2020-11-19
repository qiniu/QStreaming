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

package org.apache.spark.sql.hbase

import com.qiniu.stream.core.PipelineRunner
import com.qiniu.stream.core.config.{PipelineConfig, Settings}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.zookeeper.MiniZooKeeperCluster
import org.apache.hadoop.hbase.{HBaseTestingUtility, MiniHBaseCluster, TableName}
import org.scalatest.{BeforeAndAfter, FunSuite}


class Test extends FunSuite with BeforeAndAfter {
  var server: MiniHBaseCluster = _
  var zkServer: MiniZooKeeperCluster = _

  val tblName = "test"
  val cf = "cf"
  var miniServer: HBaseTestingUtility = _

  before {
    miniServer = new HBaseTestingUtility()
    server = miniServer.startMiniCluster()
    println("hbase started")
  }

  after {

  }

  test("Basic Write Hbase") {
    val table = miniServer.createTable(TableName.valueOf(tblName), Bytes.toBytes(cf))
    val pipeLineConfig = PipelineConfig.fromClassPath("write/hbase.dsl",
      Settings.load().withValue("stream.debug", "true"),
      Map("checkPointDir" -> "test"))
    PipelineRunner(pipeLineConfig).run()
    assert(miniServer.countRows(table) == 10)

  }


}