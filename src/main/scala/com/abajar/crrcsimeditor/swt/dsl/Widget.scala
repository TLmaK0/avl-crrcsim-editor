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

import org.eclipse.swt.custom._
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.eclipse.swt._
import org.eclipse.swt.events._
import java.io.File

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

  implicit class SetAddListenerWrapper(tree: Tree){
    def setSourceHandler(listenerMethod: (Option[Any], Integer) => (String, Any, Integer)): Tree = {
      tree.addListener(SWT.SetData, new Listener {
        def handleEvent(event: Event) = {
          val item = event.item.asInstanceOf[TreeItem]
          val parentItem = if (item.getParentItem == null)
            None
          else
            Some(item.getParentItem.getData)
          val (title, data, itemsCount) = listenerMethod(parentItem, event.index)
          item.setData(data)
          item.setText(title)
          item.setItemCount(itemsCount)
        }
      })
      return tree
    }
  }

  implicit class SetAddListenerTableWrapper(table: Table){
    def setSourceHandler(listenerMethod: (Integer) => TableField): Table = {
      table.addListener(SWT.SetData, new Listener {
        def handleEvent(event: Event) = {
          val item = event.item.asInstanceOf[TableItem]
          val tableField = listenerMethod(table.indexOf(item))
          item.setText(0, tableField.text)
          item.setText(1, tableField.value)
          item.setData(tableField)
          table.getColumn(0).pack
          table.getColumn(1).pack
        }
      })
      return table
    }
  }

  implicit class AddColumnTableWrapper(table: Table){
    val editor = new TableEditor(table)
    editor.horizontalAlignment = SWT.LEFT
    editor.grabHorizontal = true
    editor.minimumWidth = 50

    def addColumn(title: String, editable: Boolean = false): Table = {
      val column = new TableColumn(table, SWT.NONE)
      column.setText(title)
      column.pack

      val columnNumber = table.getColumnCount - 1

      if (editable) table.addSelectionListener(new SelectionAdapter{
        override def widgetSelected(e: SelectionEvent) = {
          val oldEditor = editor.getEditor
          if (oldEditor != null) oldEditor.dispose

          val item = e.item.asInstanceOf[TableItem]

          val newEditor = new Text(table, SWT.NONE)
          newEditor.setText(item.getText(columnNumber))
          newEditor.addModifyListener(new ModifyListener{
            override def modifyText(me: ModifyEvent) = {
              val text = editor.getEditor.asInstanceOf[Text]
              editor.getItem.setText(columnNumber, text.getText)
            }
          })
          newEditor.addListener(SWT.FocusOut, new Listener{
            override def handleEvent(e: Event) = {
              val text = editor.getEditor.asInstanceOf[Text]
              val tableField = item.getData.asInstanceOf[TableField]
              tableField.value = text.getText
              editor.getEditor.dispose()
            }
          })
          newEditor.selectAll
          newEditor.setFocus

          editor.setEditor(newEditor, item, columnNumber)
        }
      })
      return table
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
    def addItem(text: String, callback: (SelectionEvent) => Unit): Menu = {
      val item = new MenuItem(menu, SWT.PUSH)
      item.setText(text)
      item.addSelectionListener(new SelectionAdapter{
        override def widgetSelected(se: SelectionEvent){
          callback(se)
        }
      })
      return menu
    }
  }

  implicit class FileDialogWrapper(fileDialog: FileDialog){
    def show: Option[File] = fileDialog.open match {
      case null => None
      case path: String => Some(new File(path))
    }

    def setExtensions(extensions: Array[String]): FileDialog = {
      fileDialog.setFilterExtensions(extensions)
      return fileDialog
    }

    def setNameExtensions(names: Array[String]): FileDialog = {
      fileDialog.setFilterNames(names)
      return fileDialog
    }
  }
}