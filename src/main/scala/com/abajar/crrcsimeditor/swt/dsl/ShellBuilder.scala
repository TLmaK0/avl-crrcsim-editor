package com.abajar.crrcsimeditor.swt.dsl

import org.eclipse.swt.widgets._
import org.eclipse.swt.custom._
import org.eclipse.swt.events._;
import org.eclipse.swt._

object ShellBuilder{
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

    def addTable(style: Int) = new Table(shell, style)
    def addStyledText(style: Int) = new StyledText(shell, style)
    def addToolBar(style: Int) = new ToolBar(shell, style)
    def addMenu(menuDecorator: Menu => Unit): Menu = {
      val menu = new Menu(shell, SWT.BAR)
      menuDecorator(menu)
      shell.setMenuBar(menu)
      return menu
    }
  }

  def apply(display: Display, buildScreen: (Shell) => Unit ): Unit = {
    val shell = new Shell(display)
    buildScreen(shell)
    shell start
  }
}

