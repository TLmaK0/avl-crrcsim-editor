package com.abajar.crrcsimeditor.swt

import org.eclipse.swt.widgets.{Event, TreeItem, Listener, Display, TableItem}
import org.eclipse.swt.layout._
import org.eclipse.swt._

import dsl.Widget._
import dsl.ShellBuilder._
import com.abajar.crrcsimeditor.crrcsim.CRRCSim
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode
import java.lang.reflect.Method
import java.util.ArrayList

object MainWindow {
  val display = new Display
  var crrcSim: CRRCSim = _

  def show = dsl.ShellBuilder( display, { shell => {
    val layout = new GridLayout
    layout.numColumns = 3
    shell setLayout layout
    val mainWindow = this

    shell.addTree(SWT.VIRTUAL | SWT.BORDER)
      .layoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL))
      .subscribe(SWT.SetData, new Listener {
        def handleEvent(event: Event) = {
          mainWindow.handleTreeEvent(event)
        }
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

  def loadCRRCSim(crrcSim: CRRCSim) = {
    this.crrcSim = crrcSim; 
  }

  private def handleTreeEvent(event: Event) = {
    val item = event.item.asInstanceOf[TreeItem]

    val parentItem = item.getParentItem

    val node = if (parentItem == null) 
      (this.crrcSim.toString, this.crrcSim)
    else
      getChilds(parentItem.getData)(event.index)

    val childs = getChilds(node._2)

    item.setData(node._2)
    item.setText(node._1)
    item.setItemCount(childs.length)
  }

  private def getChilds(node: Any): List[(String, Any)] = node match {
    case childs: List[(String, Any)] =>
      childs
    case node =>
      node.getClass.getMethods.foldLeft(List[(String, Any)]())(
        (nodes, method)=>
          if (method.isAnnotationPresent(classOf[CRRCSimEditorNode]))
            nodes :+ getNameNodePair(method, node)
          else 
            nodes
      )
  }

  private def getNameNodePair(method: Method, parentNode: Any) = {
    val name = method.getAnnotation(classOf[CRRCSimEditorNode]).name
    val node = method.invoke(parentNode)
    (if (name == "Node") node.toString else name , node)
  }
}
