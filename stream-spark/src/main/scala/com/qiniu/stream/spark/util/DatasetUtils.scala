package com.qiniu.stream.spark.util

import org.apache.spark.sql.DataFrame

object DatasetUtils {

  /**
    * for debug purpose
    * @param table
    */
  def showTable(table:DataFrame): Unit ={
    if (table.isStreaming) {
      table.writeStream.format("console").start()
    }else{
      table.show()
    }
  }

}
