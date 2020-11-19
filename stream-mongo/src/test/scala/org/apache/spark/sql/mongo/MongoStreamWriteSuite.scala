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

package org.apache.spark.sql.mongo

import com.mongodb.{BasicDBObject, MongoClient}
import com.qiniu.stream.core.PipelineRunner
import com.qiniu.stream.core.config.{PipelineConfig, Settings}
import de.flapdoodle.embed.mongo.config.{ImmutableMongodConfig, MongodConfig, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.{MongodExecutable, MongodProcess, MongodStarter}
import de.flapdoodle.embed.process.runtime.Network
import org.apache.spark.sql.streaming.StreamTest
import org.scalatest.BeforeAndAfter

class MongoStreamWriteSuite extends StreamTest with BeforeAndAfter {

  val starter: MongodStarter = MongodStarter.getDefaultInstance

  val port: Int = Network.getFreeServerPort
  var mongodConfig: MongodConfig = _
  var mongod: MongodProcess = _
  var mongodExecutable: MongodExecutable = _
  var mongo: MongoClient = _

  before {
    mongodConfig = ImmutableMongodConfig.builder().version(Version.Main.PRODUCTION)
      .net(new Net(port, Network.localhostIsIPv6))
      .build
    mongodExecutable = starter.prepare(mongodConfig)
    mongod = mongodExecutable.start
    mongo = new MongoClient("localhost", port)
  }

  after {
    if (mongo != null) {
      mongo.close()
    }
    if (mongodExecutable != null) {
      mongodExecutable.stop()
    }
  }

  test("Basic Write Mongo") {
    withTempDir { checkpointDir => {
      val db = mongo.getDB("test")
      db.createCollection("testCol", new BasicDBObject)
      val pipeLineConfig = PipelineConfig.fromClassPath("write/mongo.dsl",
        Settings.load().withValue("stream.debug", "true"),
        Map("port" -> port.toString, "checkPointDir" -> checkpointDir.getCanonicalPath))
      PipelineRunner(pipeLineConfig).run()
      assert(db.getCollection("testCol").count() == 10)
    }
    }
  }


}