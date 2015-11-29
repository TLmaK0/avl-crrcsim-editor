package com.abajar.crrcsimeditor.swt.dsl

import java.lang.reflect.Field
import java.lang.reflect.Method

//TODO: This needs some kind of abstraction away from model and view

trait TableField {
  def text(): String
  def help(): String
  def value: String
  def value_=(value: String)
}

class TableFieldWritable(protected val instance: Any, protected val field: Field, val textArg: String, helpArg: String) extends TableField{
  def text() = textArg
  def help() = helpArg

  def value = {
    field.setAccessible(true)
    val result = field.get(instance)
    if (result == null) "" else result.toString
  }

  def value_=(value: String) = {
    val parsedValue = field.get(instance).asInstanceOf[Any] match {
      case float: Float => value.toFloat
      case int: Int => value.toInt
      case _ => value
    }
    field.setAccessible(true)
    field.set(instance, parsedValue)
  }
}

class TableFieldReadOnly(protected val instance: Any, protected val method: Method, val textArg: String, helpArg: String) extends TableField{
  def text() = textArg
  def help() = helpArg

  def value = {
    method.setAccessible(true)
    val result = method.invoke(instance)
    if (result == null) "" else result.toString
  }

  //TODO: Raise exception
  final def value_=(value: String) = { }
}

