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

  def clearModel(): Unit = {
    model = None
    vertices = Array()
    colors = Array()
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

    // Draw info
    gc.setForeground(getDisplay.getSystemColor(SWT.COLOR_WHITE))
    gc.drawString(s"Triangles: ${projectedPoints.length / 3}", 10, 10, true)
    gc.drawString("Drag to rotate, Right-drag to pan, Scroll to zoom", 10, height - 20, true)
  }
}
