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

object MainWindow{
  object Buttons extends Enumeration {
    type Buttons = Value
    val AddSurface, AddSection, AddControl, AddMass, AddBody,
      Delete, AddChangeLog, AddBattery, AddShaft, AddEngine,
      AddEngineData, AddEngineIdleData, AddSimpleTrust, 
      AddCollisionPoint = Value
  }
}

import MainWindow.Buttons._

class MainWindow(buttonClickHandler: (Buttons) => Unit, treeUpdateHandler: (Event) => Unit) {
  implicit class AddButtonCoolBarAndRegister(toolBar: ToolBar){
    def addButtonRegister(text: String, button: Buttons, callback: (SelectionEvent) => Unit): ToolBar = {
      buttonsMap += ((button, toolBar.addButtonAndReturn(text, button, callback)))
      return toolBar
    }
  }

  val buttonsMap = collection.mutable.Map[Buttons, ToolItem]() 

  val display = new Display

  def notifyButtonClick(se: SelectionEvent)= buttonClickHandler(se.getSource.asInstanceOf[Widget].getData.asInstanceOf[Buttons])

  def disableButton(button: Buttons) = {
    buttonsMap(button).setEnabled(false) 
  }

  def show = dsl.ShellBuilder( display, { shell => {
    val layout = new GridLayout
    layout.numColumns = 3
    shell setLayout layout
    val mainWindow = this

    shell.addToolBar(SWT.BORDER)
      .layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1))
      .addButtonRegister("Add Surface", AddSurface, notifyButtonClick)
      .addButtonRegister("Add Section", AddSection, notifyButtonClick)
      .addButtonRegister("Add Control", AddControl, notifyButtonClick)
      .addButtonRegister("Add Mass", AddMass, notifyButtonClick)
      .addButtonRegister("Add Body", AddBody, notifyButtonClick)
      .addButtonRegister("Delete", Delete, notifyButtonClick)
      .addButtonRegister("Add Change Log", AddChangeLog, notifyButtonClick)
      .addButtonRegister("Add Battery", AddBattery, notifyButtonClick)
      .addButtonRegister("Add Shaft", AddShaft, notifyButtonClick)
      .addButtonRegister("Add Engine", AddEngine, notifyButtonClick)
      .addButtonRegister("Add Engine Data", AddEngineData, notifyButtonClick)
      .addButtonRegister("Add Engine Idle Data", AddEngineIdleData, notifyButtonClick)
      .addButtonRegister("Add Simple Trust", AddSimpleTrust, notifyButtonClick)
      .addButtonRegister("Add Collision Point", AddCollisionPoint, notifyButtonClick)

    shell.addTree(SWT.VIRTUAL | SWT.BORDER)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL))
      .subscribe(SWT.SetData, new Listener {
        def handleEvent(event: Event) = treeUpdateHandler(event)
      }).setItemCount(1)

    shell.addTable(SWT.VIRTUAL | SWT.BORDER)
      .subscribe(SWT.SetData, new Listener {
        def handleEvent(event: Event) = {
          val item = event.item.asInstanceOf[TableItem]
          item setText "Item"
        }
      })

    shell.addStyledText(SWT.READ_ONLY)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL))
      .text("styleText")

    shell pack
  }})
}
