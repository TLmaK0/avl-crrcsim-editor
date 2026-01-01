/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.avleditor.swt.dsl

import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.custom._
import org.eclipse.swt.events._;
import org.eclipse.swt._
import java.nio.file.Path;

object Shell{
  import Widget._

  implicit class ShellWrapper(shell: Shell){

    def start: Unit = {
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
      tree
    }

    def addTable(style: Int, handler: SelectionEvent => Unit): Table = {
      val table = new Table(shell, style)
      table.setLinesVisible(true)
      table.setHeaderVisible(true)
      table.addSelectionListener(new SelectionAdapter(){
        override def widgetSelected(se: SelectionEvent) = handler(se)
      })
      table
    }

    def addStyledText(style: Int): StyledText = new StyledText(shell, style)
    def addToolBar(style: Int): ToolBar = new ToolBar(shell, style)
    def addButtonBar(): Composite = {
      val composite = new Composite(shell, SWT.NONE)
      val layout = new RowLayout(SWT.HORIZONTAL)
      layout.spacing = 5
      layout.marginTop = 2
      layout.marginBottom = 2
      composite.setLayout(layout)
      composite
    }

    val openFileDialog = new FileDialog(shell, SWT.OPEN)

    val saveFileDialog = new FileDialog(shell, SWT.SAVE)

    def addMenu(menuDecorator: Menu => Unit): Menu = {
      val menu = new Menu(shell, SWT.BAR)
      menuDecorator(menu)
      shell.setMenuBar(menu)
      menu
    }
  }

  def apply(display: Display, buildScreen: (Shell) => Unit ): Shell = {
    val shell = new Shell(display)
    buildScreen(shell)
    shell
  }
}

