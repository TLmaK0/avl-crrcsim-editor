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

import org.eclipse.swt.widgets.{Composite, Canvas}
import org.eclipse.swt.events._
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.layout.FillLayout

import com.abajar.avleditor.ac3d.{AC3DLoader, AC3DModel, Vec3}

class Viewer3D(parent: Composite, style: Int) extends Canvas(parent, style | SWT.NO_BACKGROUND) {

  private var model: Option[AC3DModel] = None
  private var vertices: Array[Float] = Array()
  private var colors: Array[Float] = Array()

  // Camera parameters
  private var rotationX: Float = 20.0f
  private var rotationY: Float = 45.0f
  private var zoom: Float = 1.0f
  private var panX: Float = 0.0f
  private var panY: Float = 0.0f

  // Mouse tracking
  private var lastMouseX: Int = 0
  private var lastMouseY: Int = 0
  private var isDragging: Boolean = false
  private var isPanning: Boolean = false

  // Model bounds
  private var centerX: Float = 0
  private var centerY: Float = 0
  private var centerZ: Float = 0
  private var modelScale: Float = 1.0f

  // Model dimensions (in model units)
  private var modelMinX: Float = 0
  private var modelMaxX: Float = 0
  private var modelMinY: Float = 0
  private var modelMaxY: Float = 0
  private var modelMinZ: Float = 0
  private var modelMaxZ: Float = 0

  // Display scale (1:N factor, e.g. 10 means 1:10)
  private var displayScale: Float = 1.0f
  private var showDimensions: Boolean = true

  addPaintListener(new PaintListener {
    override def paintControl(e: PaintEvent): Unit = {
      render(e.gc)
    }
  })

  addMouseListener(new MouseAdapter {
    override def mouseDown(e: MouseEvent): Unit = {
      lastMouseX = e.x
      lastMouseY = e.y
      if (e.button == 1) isDragging = true
      else if (e.button == 3) isPanning = true
    }

    override def mouseUp(e: MouseEvent): Unit = {
      isDragging = false
      isPanning = false
    }

    override def mouseDoubleClick(e: MouseEvent): Unit = {
      zoomToFit()
    }
  })

  addMouseMoveListener(new MouseMoveListener {
    override def mouseMove(e: MouseEvent): Unit = {
      if (isDragging) {
        rotationY += (e.x - lastMouseX) * 0.5f
        rotationX += (e.y - lastMouseY) * 0.5f
        lastMouseX = e.x
        lastMouseY = e.y
        redraw()
      } else if (isPanning) {
        panX += (e.x - lastMouseX) * 0.5f
        panY -= (e.y - lastMouseY) * 0.5f
        lastMouseX = e.x
        lastMouseY = e.y
        redraw()
      }
    }
  })

  addMouseWheelListener(new MouseWheelListener {
    override def mouseScrolled(e: MouseEvent): Unit = {
      zoom *= (1.0f + e.count * 0.1f)
      if (zoom < 0.1f) zoom = 0.1f
      if (zoom > 10.0f) zoom = 10.0f
      redraw()
    }
  })

  def loadModel(filePath: String): Boolean = {
    AC3DLoader.load(filePath) match {
      case Some(m) =>
        model = Some(m)
        val (verts, cols, _) = AC3DLoader.getTriangles(m)
        vertices = verts
        colors = cols
        calculateBounds()
        zoomToFit()
        redraw()
        true
      case None =>
        model = None
        vertices = Array()
        colors = Array()
        redraw()
        false
    }
  }

  def zoomToFit(): Unit = {
    if (vertices.isEmpty) return

    // Reset camera to default view
    panX = 0.0f
    panY = 0.0f
    rotationX = 20.0f
    rotationY = 45.0f

    // Calculate zoom to fit the model in the viewport
    val bounds = getBounds()
    val viewportWidth = bounds.width
    val viewportHeight = bounds.height
    val viewportSize = math.min(viewportWidth, viewportHeight).toFloat

    if (viewportSize > 50) {
      // Model is normalized to ~100 units by modelScale
      // We want it to fill about 70% of the viewport
      zoom = (viewportSize * 0.35f) / 100.0f
    } else {
      zoom = 1.0f
    }

    redraw()
  }

  def clearModel(): Unit = {
    model = None
    vertices = Array()
    colors = Array()
    redraw()
  }

  def setScale(scale: Float): Unit = {
    displayScale = scale
    redraw()
  }

  def setShowDimensions(show: Boolean): Unit = {
    showDimensions = show
    redraw()
  }

  private def calculateBounds(): Unit = {
    if (vertices.isEmpty) return

    var minX = Float.MaxValue
    var minY = Float.MaxValue
    var minZ = Float.MaxValue
    var maxX = Float.MinValue
    var maxY = Float.MinValue
    var maxZ = Float.MinValue

    var i = 0
    while (i < vertices.length) {
      val x = vertices(i)
      val y = vertices(i + 1)
      val z = vertices(i + 2)
      minX = math.min(minX, x)
      minY = math.min(minY, y)
      minZ = math.min(minZ, z)
      maxX = math.max(maxX, x)
      maxY = math.max(maxY, y)
      maxZ = math.max(maxZ, z)
      i += 3
    }

    centerX = (minX + maxX) / 2
    centerY = (minY + maxY) / 2
    centerZ = (minZ + maxZ) / 2

    // Store bounds for dimension display
    modelMinX = minX
    modelMaxX = maxX
    modelMinY = minY
    modelMaxY = maxY
    modelMinZ = minZ
    modelMaxZ = maxZ

    val sizeX = maxX - minX
    val sizeY = maxY - minY
    val sizeZ = maxZ - minZ
    val maxSize = math.max(math.max(sizeX, sizeY), sizeZ)
    modelScale = if (maxSize > 0) 100.0f / maxSize else 1.0f
  }

  private def render(gc: GC): Unit = {
    val bounds = getBounds()
    val width = bounds.width
    val height = bounds.height

    // Clear background
    gc.setBackground(getDisplay.getSystemColor(SWT.COLOR_DARK_GRAY))
    gc.fillRectangle(0, 0, width, height)

    if (vertices.isEmpty) {
      gc.setForeground(getDisplay.getSystemColor(SWT.COLOR_WHITE))
      gc.drawString("No model loaded", 10, 10, true)
      gc.drawString("Select a Graphics node", 10, 30, true)
      gc.drawString("with a valid .ac file", 10, 50, true)
      return
    }

    val centerXScreen = width / 2 + panX.toInt
    val centerYScreen = height / 2 + panY.toInt

    // Convert rotation to radians
    val radX = math.toRadians(rotationX).toFloat
    val radY = math.toRadians(rotationY).toFloat
    val cosX = math.cos(radX).toFloat
    val sinX = math.sin(radX).toFloat
    val cosY = math.cos(radY).toFloat
    val sinY = math.sin(radY).toFloat

    // Transform and project vertices
    val projectedPoints = new Array[(Int, Int, Float, Int)](vertices.length / 3)

    var i = 0
    var pIdx = 0
    while (i < vertices.length) {
      // Center the model
      var x = (vertices(i) - centerX) * modelScale * zoom
      var y = (vertices(i + 1) - centerY) * modelScale * zoom
      var z = (vertices(i + 2) - centerZ) * modelScale * zoom

      // Rotate around Y axis
      val x1 = x * cosY - z * sinY
      val z1 = x * sinY + z * cosY

      // Rotate around X axis
      val y1 = y * cosX - z1 * sinX
      val z2 = y * sinX + z1 * cosX

      // Simple orthographic projection
      val screenX = centerXScreen + x1.toInt
      val screenY = centerYScreen - y1.toInt

      // Get color
      val colorIdx = i / 3 * 4
      val r = if (colorIdx < colors.length) (colors(colorIdx) * 255).toInt else 180
      val g = if (colorIdx + 1 < colors.length) (colors(colorIdx + 1) * 255).toInt else 180
      val b = if (colorIdx + 2 < colors.length) (colors(colorIdx + 2) * 255).toInt else 180
      val colorValue = (r << 16) | (g << 8) | b

      projectedPoints(pIdx) = (screenX, screenY, z2, colorValue)
      pIdx += 1
      i += 3
    }

    // Draw triangles (simple wireframe for now)
    var t = 0
    while (t < projectedPoints.length - 2) {
      val p1 = projectedPoints(t)
      val p2 = projectedPoints(t + 1)
      val p3 = projectedPoints(t + 2)

      if (p1 != null && p2 != null && p3 != null) {
        // Calculate average color
        val r = ((p1._4 >> 16) & 0xFF)
        val g = ((p1._4 >> 8) & 0xFF)
        val b = (p1._4 & 0xFF)

        val color = new org.eclipse.swt.graphics.Color(getDisplay, r, g, b)
        gc.setForeground(color)

        // Draw wireframe triangle
        gc.drawLine(p1._1, p1._2, p2._1, p2._2)
        gc.drawLine(p2._1, p2._2, p3._1, p3._2)
        gc.drawLine(p3._1, p3._2, p1._1, p1._2)

        color.dispose()
      }
      t += 3
    }

    // Draw dimension lines if enabled
    if (showDimensions && vertices.nonEmpty) {
      drawDimensionLines(gc, centerXScreen, centerYScreen, cosX, sinX, cosY, sinY)
    }

    // Draw info
    gc.setForeground(getDisplay.getSystemColor(SWT.COLOR_WHITE))
    gc.drawString(s"Triangles: ${projectedPoints.length / 3}", 10, 10, true)

    if (showDimensions && vertices.nonEmpty) {
      val wingspan = (modelMaxX - modelMinX) / displayScale
      val length = (modelMaxY - modelMinY) / displayScale
      gc.drawString(f"Wingspan: $wingspan%.2f m | Length: $length%.2f m | Scale 1:${displayScale.toInt}", 10, 30, true)
    }

    gc.drawString("Drag to rotate, Right-drag to pan, Scroll to zoom", 10, height - 20, true)
  }

  private def drawDimensionLines(gc: GC, centerXScreen: Int, centerYScreen: Int,
                                  cosX: Float, sinX: Float, cosY: Float, sinY: Float): Unit = {
    // Create cyan color for dimension lines
    val dimColor = new org.eclipse.swt.graphics.Color(getDisplay, 0, 255, 255)
    gc.setForeground(dimColor)
    gc.setLineStyle(SWT.LINE_DASH)
    gc.setLineWidth(2)

    // Helper to project a point
    def project(px: Float, py: Float, pz: Float): (Int, Int) = {
      val x = (px - centerX) * modelScale * zoom
      val y = (py - centerY) * modelScale * zoom
      val z = (pz - centerZ) * modelScale * zoom

      val x1 = x * cosY - z * sinY
      val z1 = x * sinY + z * cosY
      val y1 = y * cosX - z1 * sinX

      (centerXScreen + x1.toInt, centerYScreen - y1.toInt)
    }

    // Draw wingspan line (X axis) at the back of model, slightly below
    val offsetY = modelMinY - (modelMaxY - modelMinY) * 0.15f
    val offsetZ = modelMinZ - (modelMaxZ - modelMinZ) * 0.1f

    val (wsX1, wsY1) = project(modelMinX, offsetY, offsetZ)
    val (wsX2, wsY2) = project(modelMaxX, offsetY, offsetZ)
    gc.drawLine(wsX1, wsY1, wsX2, wsY2)

    // Draw end caps for wingspan
    val capSize = 8
    gc.drawLine(wsX1, wsY1 - capSize, wsX1, wsY1 + capSize)
    gc.drawLine(wsX2, wsY2 - capSize, wsX2, wsY2 + capSize)

    // Draw length line (Y axis) at the right side of model
    val offsetX = modelMaxX + (modelMaxX - modelMinX) * 0.15f

    val (lenX1, lenY1) = project(offsetX, modelMinY, offsetZ)
    val (lenX2, lenY2) = project(offsetX, modelMaxY, offsetZ)
    gc.drawLine(lenX1, lenY1, lenX2, lenY2)

    // Draw end caps for length
    gc.drawLine(lenX1 - capSize, lenY1, lenX1 + capSize, lenY1)
    gc.drawLine(lenX2 - capSize, lenY2, lenX2 + capSize, lenY2)

    // Draw measurement labels near the lines
    val wingspan = (modelMaxX - modelMinX) / displayScale
    val length = (modelMaxY - modelMinY) / displayScale

    gc.setLineStyle(SWT.LINE_SOLID)
    gc.drawString(f"$wingspan%.2fm", (wsX1 + wsX2) / 2 - 25, math.max(wsY1, wsY2) + 5, true)
    gc.drawString(f"$length%.2fm", lenX1 + 10, (lenY1 + lenY2) / 2 - 8, true)

    gc.setLineWidth(1)
    dimColor.dispose()
  }
}
