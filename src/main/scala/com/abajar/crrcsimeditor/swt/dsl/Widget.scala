package com.abajar.crrcsimeditor.swt.dsl

import org.eclipse.swt.custom._
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._

object Widget{
  implicit class SetTextWrapper[T <: {def setText(text:String)}](val subject:T) {
    def text(text : String) : T = {
      subject.setText(text)
      return subject
    }
  }

  implicit class SetLayoutDataWrapper[T <: {def setLayoutData(data:Object)}](val subject:T) {
    def layoutData(data: GridData): T = {
      subject.setLayoutData(data)
      return subject
    }
  }

  implicit class SetAddListenerWrapper[T <: {def addListener(eventType: Int, listener:Listener)}](val subject:T) {
    def subscribe(eventType: Int, listener: Listener): T = {
      subject.addListener(eventType, listener)
      return subject
    }
  }
}
