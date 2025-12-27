/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.avleditor.swt

import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{GridLayout, GridData}
import org.eclipse.swt.widgets.{Display, Shell, Table, TableColumn, TableItem, Label, Composite, Button, Group}
import org.eclipse.swt.events.{SelectionAdapter, SelectionEvent}
import com.abajar.avleditor.avl.runcase.AvlCalculation

class AvlResultsWindow(display: Display) {

  private var shell: Shell = _

  def open(calculation: AvlCalculation): Unit = {
    if (shell != null && !shell.isDisposed) {
      shell.dispose()
    }

    shell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX)
    shell.setText("AVL Results")
    shell.setSize(600, 700)

    val layout = new GridLayout(2, true)
    shell.setLayout(layout)

    val config = calculation.getConfiguration
    val stab = calculation.getStabilityDerivatives

    // Configuration group
    val configGroup = new Group(shell, SWT.NONE)
    configGroup.setText("Configuration")
    configGroup.setLayout(new GridLayout(2, false))
    configGroup.setLayoutData(new GridData(GridData.FILL_BOTH))

    addRow(configGroup, "Sref", f"${config.getSref}%.4f")
    addRow(configGroup, "Cref", f"${config.getCref}%.4f")
    addRow(configGroup, "Bref", f"${config.getBref}%.4f")
    addRow(configGroup, "Velocity", f"${config.getVelocity}%.4f")
    addRow(configGroup, "Alpha", f"${config.getAlpha}%.4f")
    addRow(configGroup, "CLtot", f"${config.getCLtot}%.6f")
    addRow(configGroup, "CDvis", f"${config.getCDvis}%.6f")
    addRow(configGroup, "Cmtot", f"${config.getCmtot}%.6f")
    if (config.getE != null) {
      addRow(configGroup, "e (efficiency)", f"${config.getE}%.4f")
    }

    // Stability Derivatives group
    val stabGroup = new Group(shell, SWT.NONE)
    stabGroup.setText("Stability Derivatives")
    stabGroup.setLayout(new GridLayout(2, false))
    stabGroup.setLayoutData(new GridData(GridData.FILL_BOTH))

    addRow(stabGroup, "CLa", f"${stab.getCLa}%.6f")
    addRow(stabGroup, "CLq", f"${stab.getCLq}%.6f")
    addRow(stabGroup, "Cma", f"${stab.getCma}%.6f")
    addRow(stabGroup, "Cmq", f"${stab.getCmq}%.6f")
    addRow(stabGroup, "CYb", f"${stab.getCYb}%.6f")
    addRow(stabGroup, "CYp", f"${stab.getCYp}%.6f")
    addRow(stabGroup, "CYr", f"${stab.getCYr}%.6f")
    addRow(stabGroup, "Clb", f"${stab.getClb}%.6f")
    addRow(stabGroup, "Clp", f"${stab.getClp}%.6f")
    addRow(stabGroup, "Clr", f"${stab.getClr}%.6f")
    addRow(stabGroup, "Cnb", f"${stab.getCnb}%.6f")
    addRow(stabGroup, "Cnp", f"${stab.getCnp}%.6f")
    addRow(stabGroup, "Cnr", f"${stab.getCnr}%.6f")

    // Control Derivatives group
    val controlGroup = new Group(shell, SWT.NONE)
    controlGroup.setText("Control Derivatives")
    controlGroup.setLayout(new GridLayout(4, false))
    val controlGridData = new GridData(GridData.FILL_HORIZONTAL)
    controlGridData.horizontalSpan = 2
    controlGroup.setLayoutData(controlGridData)

    addControlHeader(controlGroup)
    addControlRow(controlGroup, "CL", stab.getCLd)
    addControlRow(controlGroup, "CY", stab.getCYd)
    addControlRow(controlGroup, "Cl", stab.getCld)
    addControlRow(controlGroup, "Cm", stab.getCmd)
    addControlRow(controlGroup, "Cn", stab.getCnd)

    // Close button
    val closeButton = new Button(shell, SWT.PUSH)
    closeButton.setText("Close")
    val buttonGridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false)
    buttonGridData.horizontalSpan = 2
    closeButton.setLayoutData(buttonGridData)
    closeButton.addSelectionListener(new SelectionAdapter {
      override def widgetSelected(e: SelectionEvent): Unit = {
        shell.close()
      }
    })

    shell.open()
  }

  private def addRow(parent: Composite, name: String, value: String): Unit = {
    val nameLabel = new Label(parent, SWT.NONE)
    nameLabel.setText(name)
    nameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false))

    val valueLabel = new Label(parent, SWT.NONE)
    valueLabel.setText(value)
    valueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false))
  }

  private def addControlHeader(parent: Composite): Unit = {
    val empty = new Label(parent, SWT.NONE)
    empty.setText("")

    val d1Label = new Label(parent, SWT.NONE)
    d1Label.setText("d1")
    d1Label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false))

    val d2Label = new Label(parent, SWT.NONE)
    d2Label.setText("d2")
    d2Label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false))

    val d3Label = new Label(parent, SWT.NONE)
    d3Label.setText("d3")
    d3Label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false))
  }

  private def addControlRow(parent: Composite, name: String, values: Array[Float]): Unit = {
    val nameLabel = new Label(parent, SWT.NONE)
    nameLabel.setText(name)

    for (i <- 0 until 3) {
      val valueLabel = new Label(parent, SWT.NONE)
      valueLabel.setText(f"${values(i)}%.6f")
      valueLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false))
    }
  }

  def isOpen: Boolean = {
    shell != null && !shell.isDisposed && shell.isVisible
  }
}
