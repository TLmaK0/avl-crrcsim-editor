/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.swt

import org.eclipse.swt.widgets.{Event, TreeItem, Listener, Display, TableItem}
import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.layout._
import org.eclipse.swt._
import org.eclipse.swt.events._;
import org.eclipse.swt.widgets._

import dsl.Widget._
import dsl.Shell
import dsl.Shell._
import dsl.TableField
import com.abajar.crrcsimeditor.crrcsim.CRRCSim
import java.util.ArrayList
import org.eclipse.swt.widgets.Widget
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS
import java.io.File;

object MenuOption extends Enumeration {
  type MenuOption = Value
  val SaveAs, Open, ExportAsAvl, ExportAsCRRCSim, SetAvlExecutable = Value
}

import MenuOption._

class MainWindow(
      buttonClickHandler: (ENABLE_BUTTONS) => Unit,
      treeUpdateHandler: (Option[Any], Integer) => (String, Any, Integer),
      treeClickHandler: (Any) => Unit,
      menuClickHandler: (MenuOption) => Unit,
      tableUpdateHandler: (Integer) => TableField,
      tableClickHandler: (Any) => Unit
      ) {

  implicit class AddButtonCoolBarAndRegister(toolBar: ToolBar){
    def addButtonRegister(
          text: String, 
          callback: (ENABLE_BUTTONS) => (SelectionEvent) => Unit, 
          button: ENABLE_BUTTONS): ToolBar = {
      buttonsMap += ((
        button, 
        toolBar.addButtonAndReturn(text, callback(button))
      ))
      return toolBar
    }
  }

  val buttonsMap = collection.mutable.Map[ENABLE_BUTTONS, ToolItem]() 

  val display = new Display

  var tree: Tree = _
  var properties: Table = _
  var help: StyledText = _

  def notifyButtonClick(buttonType: ENABLE_BUTTONS) = 
        (se: SelectionEvent) => buttonClickHandler(buttonType)

  def notifyTreeClick(se: SelectionEvent) = 
        treeClickHandler(se.item.getData)

  def notifyTableClick(se: SelectionEvent) = 
        tableClickHandler(se.item.getData)

  def notifyMenuClick(menuOption: MenuOption) = 
        (se: SelectionEvent) => menuClickHandler(menuOption)

  def disableAllButtons = {
    for{
      button <- buttonsMap.values
    } yield button.setEnabled(false)
  }

  def buttonsEnableOnly(
        buttons: scala.collection.immutable.List[ENABLE_BUTTONS]) = {
    disableAllButtons
    for{
      button <- buttons
      if buttonsMap.contains(button)
    } yield buttonsMap(button).setEnabled(true)
  }

  def showOpenDialog(
        path: String, description: String, extension: String): Option[File] =
    showOpenDialog(path, Array(description), Array(extension))

  def showOpenDialog(
        path: String,
        descriptions: Array[String], 
        extensions: Array[String]): Option[File] =
    shell.openFileDialog.setNameExtensions(descriptions).setExtensions(addWildcard(extensions)).show

  def showSaveDialog(
        path: String, description: String, extension: String): Option[File] =
    showSaveDialog(path, Array(description), Array(extension))

  def showSaveDialog(
        path: String, 
        descriptions: Array[String], 
        extensions: Array[String]): Option[File] 
    = shell.saveFileDialog.setNameExtensions(descriptions).setExtensions(addWildcard(extensions)).show

  def refreshTree = tree.clearAll(true)

  private def addWildcard(extensions: Array[String]) = 
    extensions.map(
      extension =>  if (extension.contains("*")) extension 
                    else "*." + extension
    )
    
  private val shell = Shell( display, { shell => {
    val layout = new GridLayout
    layout.numColumns = 3
    shell setLayout layout

    shell.addMenu(menu => {
        menu.addSubmenu("File")
          .addItem("Save as...", notifyMenuClick(MenuOption.SaveAs))
          .addItem("Open...", notifyMenuClick(MenuOption.Open))
          .addItem("Export As Avl", notifyMenuClick(MenuOption.ExportAsAvl))
          .addItem(
                "Export As CRRCSim", 
                notifyMenuClick(MenuOption.ExportAsCRRCSim)
          )

        menu.addSubmenu("Edit")
          .addItem("Set AVL executable", notifyMenuClick(MenuOption.SetAvlExecutable))
     })

    shell.addToolBar(SWT.BORDER)
      .layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1))
      .addButtonRegister("Add Surface", notifyButtonClick, ENABLE_BUTTONS.ADD_SURFACE)
      .addButtonRegister("Add Section", notifyButtonClick, ENABLE_BUTTONS.ADD_SECTION)
      .addButtonRegister("Add Control", notifyButtonClick, ENABLE_BUTTONS.ADD_CONTROL)
      .addButtonRegister("Add Mass", notifyButtonClick, ENABLE_BUTTONS.ADD_MASS)
      .addButtonRegister("Add Body", notifyButtonClick, ENABLE_BUTTONS.ADD_BODY)
      .addButtonRegister("Delete", notifyButtonClick, ENABLE_BUTTONS.DELETE)

    shell.addToolBar(SWT.BORDER)
      .layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1))
      .addButtonRegister("Add Change Log", notifyButtonClick, ENABLE_BUTTONS.ADD_CHANGELOG)
      .addButtonRegister("Add Battery", notifyButtonClick, ENABLE_BUTTONS.ADD_BATTERY)
      .addButtonRegister("Add Shaft", notifyButtonClick, ENABLE_BUTTONS.ADD_SHAFT)
      .addButtonRegister("Add Engine", notifyButtonClick, ENABLE_BUTTONS.ADD_ENGINE)
      .addButtonRegister("Add Engine Data", notifyButtonClick, ENABLE_BUTTONS.ADD_DATA)
      .addButtonRegister("Add Engine Idle Data", notifyButtonClick, ENABLE_BUTTONS.ADD_DATA_IDLE)
      .addButtonRegister("Add Simple Trust", notifyButtonClick, ENABLE_BUTTONS.ADD_SYMPLE_TRUST)
      .addButtonRegister("Add Collision Point", notifyButtonClick, ENABLE_BUTTONS.ADD_COLLISION_POINT)

    tree = shell.addTree(SWT.VIRTUAL | SWT.BORDER, notifyTreeClick)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL))
      .setSourceHandler(treeUpdateHandler)

    tree.setItemCount(1)

    properties = shell.addTable(SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.BORDER, notifyTableClick)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL))
      .addColumn("Property")
      .addColumn("Value", true)
      .setSourceHandler(tableUpdateHandler)

    help = shell.addStyledText(SWT.READ_ONLY)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL))

    shell pack
  }})

  def treeNodeSelected = {
    if (tree != null){
      val items = tree.getSelection
      if (items.length > 0) Some(items(0).getData)
      else None
    }else{
      None
    }
  }

  def treeNodeSelectedParent = {
    if (tree != null){
      val items = tree.getSelection
      if (items.length > 0 && items(0).getParentItem != null) Some(items(0).getParentItem.getData)
      else None
    }else{
      None
    }
  }

  def show = shell.start
}