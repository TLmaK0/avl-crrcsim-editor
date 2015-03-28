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

class MainWindow(buttonClickHandler: (ENABLE_BUTTONS) => Unit, treeUpdateHandler: (Event) => Unit, treeClickHandler: (Any) => Unit) {
  implicit class AddButtonCoolBarAndRegister(toolBar: ToolBar){
    def addButtonRegister(text: String, button: ENABLE_BUTTONS, callback: (SelectionEvent) => Unit): ToolBar = {
      buttonsMap += ((button, toolBar.addButtonAndReturn(text, button, callback)))
      return toolBar
    }
  }

  val buttonsMap = collection.mutable.Map[ENABLE_BUTTONS, ToolItem]() 

  val display = new Display

  def notifyButtonClick(se: SelectionEvent)= buttonClickHandler(se.getSource.asInstanceOf[Widget].getData.asInstanceOf[ENABLE_BUTTONS])

  def notifyTreeClick(se: SelectionEvent)= treeClickHandler(se.item.getData)

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

  def show = dsl.ShellBuilder( display, { shell => {
    val layout = new GridLayout
    layout.numColumns = 3
    shell setLayout layout

    shell.addMenu(menu => {
        menu.addSubmenu("File")
          .addItem("Save as...")
          .addItem("Open...")
          .addItem("Export As Avl")
          .addItem("Export As CRRCSim")

        menu.addSubmenu("Edit")
          .addItem("Set AVL executable")
     })

    shell.addToolBar(SWT.BORDER)
      .layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1))
      .addButtonRegister("Add Surface", 
        ENABLE_BUTTONS.ADD_SURFACE, notifyButtonClick)
      .addButtonRegister("Add Section", 
        ENABLE_BUTTONS.ADD_SECTION, notifyButtonClick)
      .addButtonRegister("Add Control", 
        ENABLE_BUTTONS.ADD_CONTROL, notifyButtonClick)
      .addButtonRegister("Add Mass", 
        ENABLE_BUTTONS.ADD_MASS, notifyButtonClick)
      .addButtonRegister("Add Body", 
        ENABLE_BUTTONS.ADD_BODY, notifyButtonClick)
      .addButtonRegister("Delete", 
        ENABLE_BUTTONS.DELETE, notifyButtonClick)
      .addButtonRegister("Add Change Log", 
        ENABLE_BUTTONS.ADD_CHANGELOG, notifyButtonClick)
      .addButtonRegister("Add Battery", 
        ENABLE_BUTTONS.ADD_BATTERY, notifyButtonClick)
      .addButtonRegister("Add Shaft", 
        ENABLE_BUTTONS.ADD_SHAFT, notifyButtonClick)
      .addButtonRegister("Add Engine", 
        ENABLE_BUTTONS.ADD_ENGINE, notifyButtonClick)
      .addButtonRegister("Add Engine Data", 
        ENABLE_BUTTONS.ADD_DATA, notifyButtonClick)
      .addButtonRegister("Add Engine Idle Data", 
        ENABLE_BUTTONS.ADD_DATA_IDLE, notifyButtonClick)
      .addButtonRegister("Add Simple Trust", 
        ENABLE_BUTTONS.ADD_SYMPLE_TRUST, notifyButtonClick)
      .addButtonRegister("Add Collision Point", 
        ENABLE_BUTTONS.ADD_COLLISION_POINT, notifyButtonClick)

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
}
