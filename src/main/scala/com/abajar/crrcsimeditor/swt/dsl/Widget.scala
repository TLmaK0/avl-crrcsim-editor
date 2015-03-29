package com.abajar.crrcsimeditor.swt.dsl

import org.eclipse.swt.custom._
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.eclipse.swt._
import org.eclipse.swt.events._;

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
    def setSourceHandler(listenerMethod: Event => Unit): T = {
      subject.addListener(SWT.SetData, new Listener {
        def handleEvent(event: Event) = listenerMethod(event)
      })
      return subject
    }
  }

  implicit class AddButtonCoolBar(toolBar: ToolBar){
    def addButtonAndReturn(text: String, callback: (SelectionEvent) => Unit): ToolItem = {
      val item = new ToolItem(toolBar, SWT.PUSH)
      item.setText(text)
      item.addSelectionListener(new SelectionAdapter{
        override def widgetSelected(se: SelectionEvent){
          callback(se)
        }
      })
      return item
    }
  }

  implicit class AddMenuWrapper(menu: Menu){
    def addSubmenu(text: String): Menu = {
      val menuItem = new MenuItem(menu, SWT.CASCADE) 
      menuItem.setText(text)
      val submenu = new Menu(menu.getShell, SWT.DROP_DOWN)
      menuItem.setMenu(submenu)
      return submenu
    }

  }

  implicit class AddMenuItemWrapper(menu: Menu){
    def addItem(text: String, callback: (SelectionEvent) => Unit) = {
      val item = new MenuItem(menu, SWT.PUSH)
      item.setText(text)
      item.addSelectionListener(new SelectionAdapter{
        override def widgetSelected(se: SelectionEvent){
          callback(se)
        }
      })
      menu
    }
  }
}
