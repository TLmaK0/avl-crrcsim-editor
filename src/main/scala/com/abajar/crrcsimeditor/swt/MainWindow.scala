package com.abajar.crrcsimeditor.swt

import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt._
import org.eclipse.swt.custom._

import dsl.LayoutData._

object MainWindow {
  def show = {
    dsl.Shell { shell => {
      val layout = new GridLayout
      layout.numColumns = 3
      shell setLayout layout
   
      new Tree(shell, SWT.VIRTUAL | SWT.BORDER)
        .layoutData(new GridData(GridData.FILL_HORIZONTAL))
        .addListener(SWT.SetData, new Listener {
          def handleEvent(event: Event) = {
            val item = event.item.asInstanceOf[TreeItem]
            item setText "test"
            item setItemCount 0
          }
        })
     
      new Table(shell, SWT.VIRTUAL | SWT.BORDER)
        .addListener(SWT.SetData, new Listener {
          def handleEvent(event: Event) = {
            val item = event.item.asInstanceOf[TableItem]
            item setText "Item"
          }
        });

      new StyledText(shell, SWT.READ_ONLY)
        .text("styleText")
      shell pack
    }}
  }
}
