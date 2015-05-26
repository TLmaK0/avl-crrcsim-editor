package com.abajar.crrcsimeditor.swt.dsl

import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.custom._
import org.eclipse.swt.events._;
import org.eclipse.swt._
import java.nio.file.Path;

object Shell{
  import Widget._

  implicit class ShellWrapper(shell: Shell){

    def start = {
      val display = shell.getDisplay
      shell.open
      while (!shell.isDisposed) if (!display.readAndDispatch) display.sleep
      display.dispose
    }

    def addTree(style: Int, handler: SelectionEvent => Unit): Tree = {
      val tree = new Tree(shell, style)
      tree.addSelectionListener(new SelectionAdapter(){
        override def widgetSelected(se: SelectionEvent) = handler(se) 
      })
      return tree
    }

    def addTable(style: Int): Table = {
      val table = new Table(shell, style)
      table.setLinesVisible(true)
      table.setHeaderVisible(true)
      table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true))
      return table
    }

    def addStyledText(style: Int) = new StyledText(shell, style)
    def addToolBar(style: Int) = new ToolBar(shell, style)
    
    val openFileDialog = new FileDialog(shell, SWT.OPEN)

    val saveFileDialog = new FileDialog(shell, SWT.SAVE)

    def addMenu(menuDecorator: Menu => Unit): Menu = {
      val menu = new Menu(shell, SWT.BAR)
      menuDecorator(menu)
      shell.setMenuBar(menu)
      return menu
    }
  }

  def apply(display: Display, buildScreen: (Shell) => Unit ): Shell = {
    val shell = new Shell(display)
    buildScreen(shell)
    return shell
  }
}

