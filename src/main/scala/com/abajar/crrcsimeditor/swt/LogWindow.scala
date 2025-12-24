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

import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{FillLayout, GridLayout, GridData}
import org.eclipse.swt.widgets.{Display, Shell, Text, Button}
import org.eclipse.swt.events.{SelectionAdapter, SelectionEvent}
import scala.collection.mutable.ListBuffer

class LogWindow(display: Display, logHistory: ListBuffer[String]) {

  private var shell: Shell = _
  private var logText: Text = _

  def open(): Unit = {
    if (shell == null || shell.isDisposed) {
      shell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX)
      shell.setText("Application Log")
      shell.setSize(800, 600)

      val layout = new GridLayout(1, false)
      shell.setLayout(layout)

      logText = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY)
      logText.setLayoutData(new GridData(GridData.FILL_BOTH))

      updateLogContent()

      val clearButton = new Button(shell, SWT.PUSH)
      clearButton.setText("Clear Log")
      clearButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false))
      clearButton.addSelectionListener(new SelectionAdapter {
        override def widgetSelected(e: SelectionEvent): Unit = {
          logHistory.clear()
          updateLogContent()
        }
      })

      shell.open()
    } else {
      updateLogContent()
      shell.setActive()
      shell.forceActive()
    }
  }

  def updateLogContent(): Unit = {
    if (logText != null && !logText.isDisposed) {
      display.asyncExec(new Runnable {
        def run(): Unit = {
          if (!logText.isDisposed) {
            logText.setText(logHistory.mkString("\n"))
            logText.setSelection(logText.getCharCount())
          }
        }
      })
    }
  }

  def isOpen: Boolean = {
    shell != null && !shell.isDisposed && shell.isVisible
  }
}
