package com.abajar.crrcsimeditor.swt

import org.eclipse.swt.widgets.{Event, TreeItem, Listener, Display, TableItem}
import org.eclipse.swt.layout._
import org.eclipse.swt._
import org.eclipse.swt.events._;
import org.eclipse.swt.widgets._

import dsl.Widget._
import dsl.ShellBuilder._
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

class MainWindow(buttonClickHandler: (ENABLE_BUTTONS) => Unit, treeUpdateHandler: (Event) => Unit, treeClickHandler: (Any) => Unit, menuClickHandler: (MenuOption) => Unit) {

  implicit class AddButtonCoolBarAndRegister(toolBar: ToolBar){
    def addButtonRegister(text: String, callback: (ENABLE_BUTTONS) => (SelectionEvent) => Unit, button: ENABLE_BUTTONS): ToolBar = {
      buttonsMap += ((button, toolBar.addButtonAndReturn(text, callback(button))))
      return toolBar
    }
  }

  val buttonsMap = collection.mutable.Map[ENABLE_BUTTONS, ToolItem]() 

  val display = new Display

  def notifyButtonClick(buttonType: ENABLE_BUTTONS) = (se: SelectionEvent) => buttonClickHandler(buttonType)

  def notifyTreeClick(se: SelectionEvent)= treeClickHandler(se.item.getData)

  def notifyMenuClick(menuOption: MenuOption) = (se: SelectionEvent) => menuClickHandler(menuOption)

  def disableAllButtons = {
    for{
      button <- buttonsMap.values
    } yield button.setEnabled(false)
  }

  def buttonsEnableOnly(buttons: scala.collection.immutable.List[ENABLE_BUTTONS]) = {
    disableAllButtons
    for{
      button <- buttons
      if buttonsMap.contains(button)
    } yield buttonsMap(button).setEnabled(true)
  }

  def showOpenDialog(path: String, description: String, 
      extension: String): Option[File] 
      = showOpenDialog(path, description, Array(extension))

  def showOpenDialog(path: String, description: String, 
      extensions: Array[String]): Option[File] 
      = shell.openFileDialog.setExtensions(extensions).show
    
  private val shell = dsl.ShellBuilder( display, { shell => {
    val layout = new GridLayout
    layout.numColumns = 3
    shell setLayout layout

    shell.addMenu(menu => {
        menu.addSubmenu("File")
          .addItem("Save as...", notifyMenuClick(MenuOption.SaveAs))
          .addItem("Open...", notifyMenuClick(MenuOption.Open))
          .addItem("Export As Avl", notifyMenuClick(MenuOption.ExportAsAvl))
          .addItem("Export As CRRCSim", notifyMenuClick(MenuOption.ExportAsCRRCSim))

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
      .addButtonRegister("Add Change Log", notifyButtonClick, ENABLE_BUTTONS.ADD_CHANGELOG)
      .addButtonRegister("Add Battery", notifyButtonClick, ENABLE_BUTTONS.ADD_BATTERY)
      .addButtonRegister("Add Shaft", notifyButtonClick, ENABLE_BUTTONS.ADD_SHAFT)
      .addButtonRegister("Add Engine", notifyButtonClick, ENABLE_BUTTONS.ADD_ENGINE)
      .addButtonRegister("Add Engine Data", notifyButtonClick, ENABLE_BUTTONS.ADD_DATA)
      .addButtonRegister("Add Engine Idle Data", notifyButtonClick, ENABLE_BUTTONS.ADD_DATA_IDLE)
      .addButtonRegister("Add Simple Trust", notifyButtonClick, ENABLE_BUTTONS.ADD_SYMPLE_TRUST)
      .addButtonRegister("Add Collision Point", notifyButtonClick, ENABLE_BUTTONS.ADD_COLLISION_POINT)

    shell.addTree(SWT.VIRTUAL | SWT.BORDER, notifyTreeClick)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL))
      .setSourceHandler(treeUpdateHandler)
      .setItemCount(1)

    shell.addTable(SWT.VIRTUAL | SWT.BORDER)
      .setSourceHandler((event: Event) => {
        val item = event.item.asInstanceOf[TableItem]
        item setText "Item"
      })

    shell.addStyledText(SWT.READ_ONLY)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL))
      .text("styleText")

    shell pack
  }})

  def show = shell.start
}
