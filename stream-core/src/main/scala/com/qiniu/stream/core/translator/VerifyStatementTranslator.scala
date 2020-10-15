package com.qiniu.stream.core.translator

import com.amazon.deequ.{VerificationResult, VerificationSuite}
import com.amazon.deequ.checks.CheckStatus
import com.qiniu.stream.core.PipelineContext
import com.qiniu.stream.core.config.{SinkTable, VerifyStatement}
import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import com.qiniu.stream.core.exceptions.DataQualityVerificationException
import com.qiniu.stream.core.sink.{BatchWriter, StreamWriter}
import org.apache.spark.sql.DataFrame

case class VerifyStatementTranslator(verify: VerifyStatement) extends StatementTranslator {
  private val executingVerificationsMsg = s"Executing data quality check over dataset %s"
  private val validationsPassedMsg = s"Verifications passed over dataset %s"
  private val validationsFailedExceptionMsg = s"Verifications failed over dataset: %s"

  override def translate(pipelineContext: PipelineContext): Unit = {
    log.info(executingVerificationsMsg.format(verify.input))
    val dataset = pipelineContext.sparkSession.table(verify.input)
    val verificationCheckResult = VerificationSuite().onData(dataset).addChecks(verify.dqChecks).run()

    logCheckResult(verificationCheckResult)

    val checkResult = checkResultsAsDataFrame(pipelineContext.sparkSession,verificationCheckResult)

    saveCheckResult(verify.output, checkResult)

    if (verificationCheckResult.status == CheckStatus.Error) {
      throw DataQualityVerificationException(validationsFailedExceptionMsg.format(verify.input))
    }

  }

  private def logCheckResult(verificationCheckResult: VerificationResult) = {
    verificationCheckResult.status match {
      case CheckStatus.Success => log.info(validationsPassedMsg.format(verify.input))
      case CheckStatus.Error => log.error(validationsFailedExceptionMsg.format(verify.input))
      case CheckStatus.Warning => log.warn(validationsFailedExceptionMsg.format(verify.input))
    }
  }

  private def saveCheckResult(table: SinkTable, dataFrame: DataFrame) = {
    val writer = if (table.streaming)
      new StreamWriter
    else
      new BatchWriter
    writer.write(dataFrame, table)

  }



}
