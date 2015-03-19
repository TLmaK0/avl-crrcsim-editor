package com.abajar.crrcsimeditor.swt.dsl

import org.eclipse.swt.widgets._
import org.eclipse.swt.custom._

object ShellBuilder{
  implicit class ShellWrapper(shell: Shell){
    def start = {
      val display = shell.getDisplay
      shell.open
      while (!shell.isDisposed) if (!display.readAndDispatch) display.sleep
      display.dispose
    }

    def addTree(style: Int) = new Tree(shell, style)
    def addTable(style: Int) = new Table(shell, style)
    def addStyledText(style: Int) = new StyledText(shell, style)
    def addToolBar(style: Int) = new ToolBar(shell, style)
  }

  def apply(display: Display, buildScreen: (Shell) => Unit ): Unit = {
    val shell = new Shell(display)
    buildScreen(shell)
    shell start
  }
}

