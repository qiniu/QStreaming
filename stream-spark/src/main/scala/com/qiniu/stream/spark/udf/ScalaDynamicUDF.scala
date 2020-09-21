package com.qiniu.stream.spark.udf

import java.lang.reflect.Method
import java.util.UUID

import com.qiniu.stream.util.Logging
import org.apache.spark.sql.catalyst.JavaTypeInference
import org.apache.spark.sql.types.DataType


case class ClassInfo(clazz: Class[_], instance: Any, defaultMethod: Method, methods: Map[String, Method], func: String) {
  def invoke[T](args: Object*): T = {
    defaultMethod.invoke(instance, args: _*).asInstanceOf[T]
  }
}

object ClassCreateUtils {

  import scala.reflect.runtime.universe
  import scala.tools.reflect.ToolBox

  private val clazzs = new java.util.HashMap[String, ClassInfo]()
  private val classLoader = scala.reflect.runtime.universe.getClass.getClassLoader
  private val toolBox = universe.runtimeMirror(classLoader).mkToolBox()

  def apply(func: String): ClassInfo = this.synchronized {
    var clazz = clazzs.get(func)
    if (clazz == null) {
      val (className, classBody) = wrapClass(func)
      val zz = compile(prepareScala(className, classBody))
      val defaultMethod = zz.getDeclaredMethods.head
      val methods = zz.getDeclaredMethods
      clazz = ClassInfo(
        zz,
        zz.newInstance(),
        defaultMethod,
        methods = methods.map { m => (m.getName, m) }.toMap,
        func
      )
      clazzs.put(func, clazz)
      println(s"dynamic load class => $clazz")
    }
    clazz
  }

  def compile(src: String): Class[_] = {
    val tree = toolBox.parse(src)
    toolBox.compile(tree).apply().asInstanceOf[Class[_]]
  }

  def prepareScala(className: String, classBody: String): String = {
    classBody + "\n" + s"scala.reflect.classTag[$className].runtimeClass"
  }

  def wrapClass(function: String): (String, String) = {
    val className = s"dynamic_class_${UUID.randomUUID().toString.replaceAll("-", "")}"
    val classBody =
      s"""
         |class $className{
         |  $function
         |}
            """.stripMargin
    (className, classBody)
  }
}

object ScalaDynamicUDF extends Logging {

  def apply(func: String): (AnyRef, Array[DataType], DataType) = {
    val (argumentTypes, returnType) = getFunctionReturnType(func)
    (generateFunction(func, argumentTypes.length), argumentTypes, returnType)
  }

  def apply(func: String,dataType: DataType): (AnyRef, Array[DataType], DataType) = {
    val (argumentTypes, _) = getFunctionReturnType(func)
    (generateFunction(func, argumentTypes.length), argumentTypes, dataType)
  }

  //获取方法的参数类型及返回类型
  private def getFunctionReturnType(func: String): (Array[DataType], DataType) = {
    val classInfo = ClassCreateUtils(func)
    val method = classInfo.defaultMethod
    val dataType = JavaTypeInference.inferDataType(method.getReturnType)._1
    (method.getParameterTypes.map(JavaTypeInference.inferDataType).map(_._1), dataType)
  }

  //生成22个Function
  def generateFunction(func: String, argumentsNum: Int): AnyRef = {
    lazy val instance = ClassCreateUtils(func).instance
    lazy val method = ClassCreateUtils(func).methods("apply")

    argumentsNum match {
      case 0 =>
        () => method.invoke(instance)
      case 1 => (v1: Object) =>
        try {
          method.invoke(instance, v1)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 2 => (v1: Object, v2: Object) =>
        try {
          method.invoke(instance, v1, v2)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 3 => (v1: Object, v2: Object, v3: Object) =>
        try {
          method.invoke(instance, v1, v2, v3)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 4 => (v1: Object, v2: Object, v3: Object, v4: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 5 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 6 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 7 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 8 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 9 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 10 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 11 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 12 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 13 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 14 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 15 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 16 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object, v16: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 17 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object, v16: Object, v17: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 18 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object, v16: Object, v17: Object, v18: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 19 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object, v16: Object, v17: Object, v18: Object, v19: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 20 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object, v16: Object, v17: Object, v18: Object, v19: Object, v20: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 21 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object, v16: Object, v17: Object, v18: Object, v19: Object, v20: Object, v21: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }

      case 22 => (v1: Object, v2: Object, v3: Object, v4: Object, v5: Object, v6: Object, v7: Object, v8: Object, v9: Object, v10: Object, v11: Object, v12: Object, v13: Object, v14: Object, v15: Object, v16: Object, v17: Object, v18: Object, v19: Object, v20: Object, v21: Object, v22: Object) =>
        try {
          method.invoke(instance, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20, v21, v22)
        } catch {
          case e: Exception =>
            logError(e.getMessage)
            null
        }
      //------------generated functions---------------------
    }
  }



}
