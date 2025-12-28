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

import org.eclipse.swt.widgets.{Display, Shell, Canvas}
import org.eclipse.swt.custom.{CTabFolder, CTabItem}
import org.eclipse.swt.events._
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.{GC, Image}
import org.eclipse.swt.layout.FillLayout
import java.nio.file.Path
import java.util.logging.{Logger, Level}

class AvlPlotWindow(display: Display) {
  private val logger = Logger.getLogger(classOf[AvlPlotWindow].getName)

  private var geometryImage: Option[Image] = None
  private var trefftzImage: Option[Image] = None
  private var shell: Shell = _

  // Zoom and pan state
  private var geometryZoom: Float = 1.0f
  private var geometryPanX: Float = 0.0f
  private var geometryPanY: Float = 0.0f
  private var trefftzZoom: Float = 1.0f
  private var trefftzPanX: Float = 0.0f
  private var trefftzPanY: Float = 0.0f
  private var lastMouseX: Int = 0
  private var lastMouseY: Int = 0
  private var isDragging: Boolean = false

  def open(geometryPath: Path, trefftzPath: Path): Unit = {
    // Load images
    geometryImage = loadImage(geometryPath)
    trefftzImage = loadImage(trefftzPath)

    // Create shell
    shell = new Shell(display, SWT.SHELL_TRIM)
    shell.setText("AVL Plots")
    shell.setSize(900, 700)
    shell.setLayout(new FillLayout())

    val tabFolder = new CTabFolder(shell, SWT.BORDER)
    tabFolder.setSimple(false)

    // Create geometry tab
    val geometryCanvas = new Canvas(tabFolder, SWT.DOUBLE_BUFFERED)
    val geometryTab = new CTabItem(tabFolder, SWT.NONE)
    geometryTab.setText("Geometry")
    geometryTab.setControl(geometryCanvas)

    // Create trefftz tab
    val trefftzCanvas = new Canvas(tabFolder, SWT.DOUBLE_BUFFERED)
    val trefftzTab = new CTabItem(tabFolder, SWT.NONE)
    trefftzTab.setText("Trefftz")
    trefftzTab.setControl(trefftzCanvas)

    tabFolder.setSelection(geometryTab)

    // Paint listeners
    geometryCanvas.addPaintListener(new PaintListener {
      override def paintControl(e: PaintEvent): Unit = {
        renderImage(e.gc, geometryCanvas, geometryImage, "No geometry plot available",
          geometryZoom, geometryPanX, geometryPanY)
      }
    })

    trefftzCanvas.addPaintListener(new PaintListener {
      override def paintControl(e: PaintEvent): Unit = {
        renderImage(e.gc, trefftzCanvas, trefftzImage, "No Trefftz plot available",
          trefftzZoom, trefftzPanX, trefftzPanY)
      }
    })

    // Add mouse listeners
    addCanvasListeners(geometryCanvas, isGeometry = true)
    addCanvasListeners(trefftzCanvas, isGeometry = false)

    // Dispose images when shell closes
    shell.addDisposeListener(new DisposeListener {
      override def widgetDisposed(e: DisposeEvent): Unit = {
        geometryImage.foreach(_.dispose())
        trefftzImage.foreach(_.dispose())
      }
    })

    shell.open()
  }

  private def addCanvasListeners(canvas: Canvas, isGeometry: Boolean): Unit = {
    canvas.addMouseListener(new MouseAdapter {
      override def mouseDown(e: MouseEvent): Unit = {
        lastMouseX = e.x
        lastMouseY = e.y
        isDragging = true
      }

      override def mouseUp(e: MouseEvent): Unit = {
        isDragging = false
      }

      override def mouseDoubleClick(e: MouseEvent): Unit = {
        if (isGeometry) {
          geometryZoom = 1.0f; geometryPanX = 0.0f; geometryPanY = 0.0f
        } else {
          trefftzZoom = 1.0f; trefftzPanX = 0.0f; trefftzPanY = 0.0f
        }
        canvas.redraw()
      }
    })

    canvas.addMouseMoveListener(new MouseMoveListener {
      override def mouseMove(e: MouseEvent): Unit = {
        if (isDragging) {
          val dx = e.x - lastMouseX
          val dy = e.y - lastMouseY
          if (isGeometry) {
            geometryPanX += dx; geometryPanY += dy
          } else {
            trefftzPanX += dx; trefftzPanY += dy
          }
          lastMouseX = e.x
          lastMouseY = e.y
          canvas.redraw()
        }
      }
    })

    canvas.addMouseWheelListener(new MouseWheelListener {
      override def mouseScrolled(e: MouseEvent): Unit = {
        val zoomFactor = 1.0f + e.count * 0.1f
        if (isGeometry) {
          geometryZoom *= zoomFactor
          geometryZoom = math.max(0.1f, math.min(10.0f, geometryZoom))
        } else {
          trefftzZoom *= zoomFactor
          trefftzZoom = math.max(0.1f, math.min(10.0f, trefftzZoom))
        }
        canvas.redraw()
      }
    })
  }

  private def renderImage(gc: GC, canvas: Canvas, image: Option[Image], placeholder: String,
                          zoom: Float, panX: Float, panY: Float): Unit = {
    val bounds = canvas.getBounds()
    val width = bounds.width
    val height = bounds.height

    gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE))
    gc.fillRectangle(0, 0, width, height)

    image match {
      case Some(img) =>
        val imgBounds = img.getBounds()
        val imgWidth = imgBounds.width
        val imgHeight = imgBounds.height

        val baseScaleX = width.toDouble / imgWidth
        val baseScaleY = height.toDouble / imgHeight
        val baseScale = math.min(baseScaleX, baseScaleY)
        val scale = baseScale * zoom

        val scaledWidth = (imgWidth * scale).toInt
        val scaledHeight = (imgHeight * scale).toInt

        val x = ((width - scaledWidth) / 2 + panX).toInt
        val y = ((height - scaledHeight) / 2 + panY).toInt

        gc.drawImage(img, 0, 0, imgWidth, imgHeight, x, y, scaledWidth, scaledHeight)

        gc.setForeground(display.getSystemColor(SWT.COLOR_GRAY))
        gc.drawString(f"Zoom: ${zoom * 100}%.0f%% (double-click to reset, scroll to zoom, drag to pan)", 5, height - 18, true)

      case None =>
        gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY))
        val textWidth = gc.textExtent(placeholder).x
        gc.drawString(placeholder, (width - textWidth) / 2, height / 2, true)
    }
  }

  private def loadImage(path: Path): Option[Image] = {
    if (path != null && java.nio.file.Files.exists(path)) {
      try {
        val img = new Image(display, path.toString)
        logger.log(Level.INFO, "Loaded image: " + path)
        Some(img)
      } catch {
        case e: Exception =>
          logger.log(Level.WARNING, "Failed to load image: " + path + " - " + e.getMessage)
          None
      }
    } else {
      None
    }
  }
}
