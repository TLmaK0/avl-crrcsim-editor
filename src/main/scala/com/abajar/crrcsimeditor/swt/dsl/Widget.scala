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
  implicit class SetLayoutDataWrapper[T <: Control](val subject:T) {
    def layoutData(data: GridData): T = {
      subject.setLayoutData(data)
      subject
    }
  }

  implicit class SetAddListenerWrapper(tree: Tree){
    def setSourceHandler(listenerMethod: (Option[Any], Integer) => (String, Any, Integer)): Tree = {
      tree.addListener(SWT.SetData, new Listener {
        def handleEvent(event: Event) = {
          val item = event.item.asInstanceOf[TreeItem]
          val parentItem = Option(item.getParentItem).map(_.getData)
          val (title, data, itemsCount) = listenerMethod(parentItem, event.index)
          item.setData(data)
          item.setText(title)
          item.setItemCount(itemsCount)
        }
      })
      tree
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
      table
    }
  }

  implicit class AddColumnTableWrapper(table: Table){
    val minimumWidth = 50

    val editor = new TableEditor(table)
    editor.horizontalAlignment = SWT.LEFT
    editor.grabHorizontal = true
    editor.minimumWidth = minimumWidth

    def addColumn(title: String, editable: Boolean = false): Table = {
      val column = new TableColumn(table, SWT.NONE)
      column.setText(title)
      column.pack

      val columnNumber = table.getColumnCount - 1

      if (editable) table.addSelectionListener(new SelectionAdapter{
        override def widgetSelected(e: SelectionEvent) = {
          Option(editor.getEditor).map(_.dispose)

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
      table
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
      item
    }
  }

  implicit class AddMenuWrapper(menu: Menu){
    def addSubmenu(text: String): Menu = {
      val menuItem = new MenuItem(menu, SWT.CASCADE)
      menuItem.setText(text)
      val submenu = new Menu(menu.getShell, SWT.DROP_DOWN)
      menuItem.setMenu(submenu)
      submenu
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
      menu
    }
  }

  implicit class FileDialogWrapper(fileDialog: FileDialog){
    def show: Option[File] = Option(fileDialog.open).map(new File(_))

    def setExtensions(extensions: Array[String]): FileDialog = {
      fileDialog.setFilterExtensions(extensions)
      fileDialog
    }

    def setNameExtensions(names: Array[String]): FileDialog = {
      fileDialog.setFilterNames(names)
      fileDialog
    }
  }
}
