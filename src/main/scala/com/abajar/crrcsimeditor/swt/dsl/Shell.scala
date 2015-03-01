package com.abajar.crrcsimeditor.swt.dsl

import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.{Shell => SwtShell}

object Shell{
  implicit class ShellWrapper(shell: SwtShell){
    def start = {
      shell.open
      while (!shell.isDisposed) if (!display.readAndDispatch) display.sleep
      display.dispose
    }
  }

  val display = new Display

  def apply( buildScreen: (SwtShell) => Unit ): Unit = {
    val shell = new SwtShell(display)
    buildScreen(shell)
    shell start
  }
}

