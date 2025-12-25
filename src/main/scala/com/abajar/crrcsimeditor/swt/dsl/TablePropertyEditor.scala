/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

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
  def text(): String = textArg
  def help(): String = helpArg

  def value: String = {
    field.setAccessible(true)
    Option(field.get(instance)) match {
      case Some(result) => result.toString
      case None => ""
    }
  }

  def value_=(value: String): Unit = {
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
  def text(): String = textArg
  def help(): String = helpArg

  def value: String = {
    method.setAccessible(true)
    Option(method.invoke(instance)) match {
      case Some(result) => result.toString
      case None => ""
    }
  }

  //TODO: Raise exception
  final def value_=(value: String): Unit = { }
}

class TableFieldFile(
    protected val instance: Any,
    protected val field: Field,
    val textArg: String,
    helpArg: String,
    val extensions: Array[String],
    val extensionDescription: String
) extends TableField {
  def text(): String = textArg
  def help(): String = helpArg

  def value: String = {
    field.setAccessible(true)
    Option(field.get(instance)) match {
      case Some(result) => result.toString
      case None => ""
    }
  }

  def value_=(value: String): Unit = {
    field.setAccessible(true)
    field.set(instance, value)
  }

  def isFileField: Boolean = true
}
