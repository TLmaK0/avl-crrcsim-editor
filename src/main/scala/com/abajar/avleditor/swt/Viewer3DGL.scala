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

import org.eclipse.swt.widgets.{Composite, Label, Button}
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.{GridLayout, GridData, FillLayout}
import org.eclipse.swt.events._
import org.eclipse.swt.awt.SWT_AWT

import java.awt.Frame
import com.jogamp.opengl._
import com.jogamp.opengl.awt.GLJPanel
import com.jogamp.opengl.fixedfunc.{GLLightingFunc, GLMatrixFunc}
import com.jogamp.opengl.util.FPSAnimator
import com.jogamp.opengl.util.awt.TextRenderer
import java.awt.Font

import com.abajar.avleditor.ac3d.{AC3DLoader, AC3DModel}

class Viewer3DGL(parent: Composite, style: Int) extends Composite(parent, style) {
  private val gridLayout = new GridLayout(1, false)
  gridLayout.marginWidth = 0
  gridLayout.marginHeight = 0
  gridLayout.verticalSpacing = 2
  setLayout(gridLayout)

  private var model: Option[AC3DModel] = None
  @volatile private var vertices: Array[Float] = Array()
  @volatile private var colors: Array[Float] = Array()

  // Camera parameters
  @volatile private var rotationX: Float = 20.0f
  @volatile private var rotationY: Float = 45.0f
  @volatile private var zoom: Float = 1.0f
  @volatile private var panX: Float = 0.0f
  @volatile private var panY: Float = 0.0f

  // Mouse tracking
  private var lastMouseX: Int = 0
  private var lastMouseY: Int = 0
  private var isDragging: Boolean = false
  private var isPanning: Boolean = false

  // Model bounds
  @volatile private var centerX: Float = 0
  @volatile private var centerY: Float = 0
  @volatile private var centerZ: Float = 0
  @volatile private var modelScale: Float = 1.0f

  // Model dimensions
  @volatile private var modelMinX: Float = 0
  @volatile private var modelMaxX: Float = 0
  @volatile private var modelMinY: Float = 0
  @volatile private var modelMaxY: Float = 0
  @volatile private var modelMinZ: Float = 0
  @volatile private var modelMaxZ: Float = 0

  // Display scale
  @volatile private var displayScale: Float = 1.0f
  @volatile private var showDimensions: Boolean = true
  @volatile private var wireframeMode: Boolean = true
  @volatile private var showAvlSurfaces: Boolean = true

  // AVL surface data: Array of surfaces, each surface is Array of sections (x, y, z, chord)
  @volatile private var avlSurfaces: Array[Array[(Float, Float, Float, Float)]] = Array()

  // Selected section for editing (surfaceIndex, sectionIndex, x, y, z, chord)
  @volatile private var selectedSection: Option[(Int, Int, Float, Float, Float, Float)] = None
  @volatile private var isDraggingSection: Boolean = false
  @volatile private var isDraggingChord: Boolean = false
  private var sectionUpdateCallback: Option[(Int, Int, Float, Float, Float, Float) => Unit] = None

  // Create AWT Frame embedded in SWT
  private val awtComposite = new Composite(this, SWT.EMBEDDED)
  awtComposite.setLayout(new FillLayout())
  awtComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true))
  private val awtFrame = SWT_AWT.new_Frame(awtComposite)

  // Bottom bar with checkboxes and info label
  private val bottomBar = new Composite(this, SWT.NONE)
  private val bottomLayout = new GridLayout(3, false)
  bottomLayout.marginWidth = 0
  bottomLayout.marginHeight = 0
  bottomBar.setLayout(bottomLayout)
  bottomBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false))

  // Wireframe checkbox
  private val wireframeCheckbox = new Button(bottomBar, SWT.CHECK)
  wireframeCheckbox.setText("Wireframe")
  wireframeCheckbox.setSelection(true)
  wireframeCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false))
  wireframeCheckbox.addSelectionListener(new SelectionAdapter {
    override def widgetSelected(e: SelectionEvent): Unit = {
      wireframeMode = wireframeCheckbox.getSelection
    }
  })

  // AVL surfaces checkbox
  private val avlSurfacesCheckbox = new Button(bottomBar, SWT.CHECK)
  avlSurfacesCheckbox.setText("AVL")
  avlSurfacesCheckbox.setSelection(true)
  avlSurfacesCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false))
  avlSurfacesCheckbox.addSelectionListener(new SelectionAdapter {
    override def widgetSelected(e: SelectionEvent): Unit = {
      showAvlSurfaces = avlSurfacesCheckbox.getSelection
    }
  })

  // Info label for dimensions and controls
  private val infoLabel = new Label(bottomBar, SWT.NONE)
  infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false))
  infoLabel.setText("Right-drag: rotate | Middle-drag: pan | Wheel: zoom | Double-click: fit")

  // OpenGL setup using AWT GLCanvas
  private val glProfile = GLProfile.getDefault
  private val glCapabilities = new GLCapabilities(glProfile)
  glCapabilities.setDoubleBuffered(true)
  glCapabilities.setHardwareAccelerated(true)

  // Use GLJPanel instead of GLCanvas for better compatibility with SWT_AWT
  private val glCanvas = new GLJPanel(glCapabilities)
  awtFrame.setLayout(new java.awt.BorderLayout())
  awtFrame.add(glCanvas, java.awt.BorderLayout.CENTER)
  awtFrame.setVisible(true)
  glCanvas.setVisible(true)

  private var glInitialized = false
  private var textRenderer: TextRenderer = _

  glCanvas.addGLEventListener(new GLEventListener {
    override def init(drawable: GLAutoDrawable): Unit = {
      val gl = drawable.getGL.getGL2
      gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f)
      gl.glEnable(GL.GL_DEPTH_TEST)
      gl.glEnable(GLLightingFunc.GL_LIGHTING)
      gl.glEnable(GLLightingFunc.GL_LIGHT0)
      gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL)
      gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE)
      gl.glShadeModel(GLLightingFunc.GL_SMOOTH)

      val lightPos = Array(1.0f, 1.0f, 1.0f, 0.0f)
      val lightAmbient = Array(0.3f, 0.3f, 0.3f, 1.0f)
      val lightDiffuse = Array(0.8f, 0.8f, 0.8f, 1.0f)
      gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, lightPos, 0)
      gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_AMBIENT, lightAmbient, 0)
      gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, lightDiffuse, 0)

      textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14))
      glInitialized = true
    }

    override def dispose(drawable: GLAutoDrawable): Unit = {
      if (textRenderer != null) textRenderer.dispose()
    }

    override def display(drawable: GLAutoDrawable): Unit = {
      render(drawable)
    }

    override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int): Unit = {
      val gl = drawable.getGL.getGL2
      gl.glViewport(0, 0, width, height)
    }
  })

  // Mouse handling
  glCanvas.addMouseListener(new java.awt.event.MouseAdapter {
    override def mousePressed(e: java.awt.event.MouseEvent): Unit = {
      lastMouseX = e.getX
      lastMouseY = e.getY
      if (e.getButton == java.awt.event.MouseEvent.BUTTON1) {
        // Left click to drag section when one is selected
        if (selectedSection.isDefined) {
          // Check which handle is closer
          val clickedHandle = getClosestHandle(e.getX, e.getY)
          if (clickedHandle == "trailing") {
            isDraggingChord = true
          } else {
            isDraggingSection = true
          }
        }
      } else if (e.getButton == java.awt.event.MouseEvent.BUTTON3) {
        // Right click to rotate
        isDragging = true
      } else if (e.getButton == java.awt.event.MouseEvent.BUTTON2) {
        // Middle click to pan
        isPanning = true
      }
    }

    override def mouseReleased(e: java.awt.event.MouseEvent): Unit = {
      if (isDraggingSection || isDraggingChord) {
        isDraggingSection = false
        isDraggingChord = false
        // Notify callback with updated position and chord
        selectedSection.foreach { case (surfIdx, secIdx, x, y, z, chord) =>
          sectionUpdateCallback.foreach(_(surfIdx, secIdx, x, y, z, chord))
        }
      }
      isDragging = false
      isPanning = false
    }

    override def mouseClicked(e: java.awt.event.MouseEvent): Unit = {
      if (e.getClickCount == 2) zoomToFit()
    }
  })

  glCanvas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter {
    override def mouseDragged(e: java.awt.event.MouseEvent): Unit = {
      if (isDraggingSection) {
        // Move section based on mouse movement
        val sensitivity = 0.1f / zoom / displayScale
        val deltaScreenX = (e.getX - lastMouseX) * sensitivity
        val deltaScreenY = (e.getY - lastMouseY) * sensitivity
        selectedSection.foreach { case (surfIdx, secIdx, xle, yle, zle, chord) =>
          var newXle = xle - deltaScreenY  // Mouse up = forward
          var newYle = yle + deltaScreenX  // Mouse right = right
          var newZle = zle
          // Snap to nearest vertex based on screen projection
          findNearestVertexByScreenPos(e.getX, e.getY).foreach { case (vx, vy, vz) =>
            newYle = vx / displayScale
            newXle = vy / displayScale
            newZle = vz / displayScale
          }
          selectedSection = Some((surfIdx, secIdx, newXle, newYle, newZle, chord))
        }
        lastMouseX = e.getX
        lastMouseY = e.getY
      } else if (isDraggingChord) {
        // Change chord based on mouse movement (vertical = chord change)
        val sensitivity = 0.1f / zoom / displayScale
        val deltaScreenY = (e.getY - lastMouseY) * sensitivity
        selectedSection.foreach { case (surfIdx, secIdx, xle, yle, zle, chord) =>
          val newChord = scala.math.max(0.1f, chord - deltaScreenY)  // Mouse up = larger chord
          selectedSection = Some((surfIdx, secIdx, xle, yle, zle, newChord))
        }
        lastMouseX = e.getX
        lastMouseY = e.getY
      } else if (isDragging) {
        rotationY += (e.getX - lastMouseX) * 0.5f
        rotationX += (e.getY - lastMouseY) * 0.5f
        lastMouseX = e.getX
        lastMouseY = e.getY
      } else if (isPanning) {
        panX += (e.getX - lastMouseX) * 0.5f
        panY -= (e.getY - lastMouseY) * 0.5f
        lastMouseX = e.getX
        lastMouseY = e.getY
      }
    }
  })

  glCanvas.addMouseWheelListener(new java.awt.event.MouseWheelListener {
    override def mouseWheelMoved(e: java.awt.event.MouseWheelEvent): Unit = {
      val rotation = -e.getWheelRotation
      zoom *= (1.0f + rotation * 0.1f)
      if (zoom < 0.1f) zoom = 0.1f
      if (zoom > 10.0f) zoom = 10.0f
    }
  })

  // Handle resize to update AWT frame size
  awtComposite.addControlListener(new ControlAdapter {
    override def controlResized(e: ControlEvent): Unit = {
      val size = awtComposite.getSize
      java.awt.EventQueue.invokeLater(new Runnable {
        override def run(): Unit = {
          awtFrame.setSize(size.x, size.y)
          awtFrame.validate()
          glCanvas.setSize(size.x, size.y)
        }
      })
    }
  })

  // Start animator at 30 FPS after a short delay to ensure proper initialization
  private val animator = new FPSAnimator(glCanvas, 30, true)

  // Delay start to ensure AWT frame is properly realized
  new Thread(new Runnable {
    override def run(): Unit = {
      Thread.sleep(500)
      java.awt.EventQueue.invokeLater(new Runnable {
        override def run(): Unit = {
          awtFrame.validate()
          awtFrame.repaint()
          glCanvas.repaint()
          animator.start()
        }
      })
    }
  }).start()

  // Clean up on dispose
  addDisposeListener(new DisposeListener {
    override def widgetDisposed(e: DisposeEvent): Unit = {
      animator.stop()
      awtFrame.dispose()
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
        true
      case None =>
        model = None
        vertices = Array()
        colors = Array()
        false
    }
  }

  def zoomToFit(): Unit = {
    if (vertices.isEmpty) return
    panX = 0.0f
    panY = 0.0f
    rotationX = 20.0f
    rotationY = 45.0f

    // Calculate projected bounding box after rotation
    val radX = scala.math.toRadians(rotationX)
    val radY = scala.math.toRadians(rotationY)
    val cosX = scala.math.cos(radX).toFloat
    val sinX = scala.math.sin(radX).toFloat
    val cosY = scala.math.cos(radY).toFloat
    val sinY = scala.math.sin(radY).toFloat

    // Get the 8 corners of the bounding box
    val corners = Array(
      (modelMinX, modelMinY, modelMinZ),
      (modelMaxX, modelMinY, modelMinZ),
      (modelMinX, modelMaxY, modelMinZ),
      (modelMaxX, modelMaxY, modelMinZ),
      (modelMinX, modelMinY, modelMaxZ),
      (modelMaxX, modelMinY, modelMaxZ),
      (modelMinX, modelMaxY, modelMaxZ),
      (modelMaxX, modelMaxY, modelMaxZ)
    )

    var projMinX = Float.MaxValue
    var projMaxX = Float.MinValue
    var projMinY = Float.MaxValue
    var projMaxY = Float.MinValue

    // Project each corner after rotation
    for ((x, y, z) <- corners) {
      // Center the point
      val cx = (x - centerX) * modelScale
      val cy = (y - centerY) * modelScale
      val cz = (z - centerZ) * modelScale

      // Rotate around Y axis first
      val x1 = cx * cosY + cz * sinY
      val z1 = -cx * sinY + cz * cosY

      // Then rotate around X axis
      val y2 = cy * cosX - z1 * sinX

      // Project to screen (orthographic, so just use x1 and y2)
      projMinX = scala.math.min(projMinX, x1)
      projMaxX = scala.math.max(projMaxX, x1)
      projMinY = scala.math.min(projMinY, y2)
      projMaxY = scala.math.max(projMaxY, y2)
    }

    // Calculate required zoom to fit projected size in view
    // View shows 300/zoom units total (from -150/zoom to +150/zoom)
    val projWidth = projMaxX - projMinX
    val projHeight = projMaxY - projMinY
    val maxProjSize = scala.math.max(projWidth, projHeight)

    if (maxProjSize > 0) {
      zoom = 290.0f / maxProjSize
    } else {
      zoom = 1.0f
    }
  }

  def clearModel(): Unit = {
    model = None
    vertices = Array()
    colors = Array()
  }

  def getViewAngles: (Float, Float) = (rotationX, rotationY)

  def setAvlSurfaces(surfaces: Array[Array[(Float, Float, Float, Float)]]): Unit = {
    avlSurfaces = surfaces
  }

  def setScale(scale: Float): Unit = {
    displayScale = scale
    if (vertices.nonEmpty) updateInfoLabel()
  }

  def setShowDimensions(show: Boolean): Unit = {
    showDimensions = show
  }

  def setSelectedSection(surfaceIdx: Int, sectionIdx: Int, x: Float, y: Float, z: Float, chord: Float): Unit = {
    selectedSection = Some((surfaceIdx, sectionIdx, x, y, z, chord))
  }

  def clearSelectedSection(): Unit = {
    selectedSection = None
  }

  def setSectionUpdateCallback(callback: (Int, Int, Float, Float, Float, Float) => Unit): Unit = {
    sectionUpdateCallback = Some(callback)
  }

  private def getClosestHandle(mouseX: Int, mouseY: Int): String = {
    selectedSection.map { case (_, _, xle, yle, zle, chord) =>
      val leadingPos = projectToScreen(yle, xle, zle)
      val trailingPos = projectToScreen(yle, xle + chord, zle)

      (leadingPos, trailingPos) match {
        case (Some((lx, ly)), Some((tx, ty))) =>
          val distLeading = (mouseX - lx) * (mouseX - lx) + (mouseY - ly) * (mouseY - ly)
          val distTrailing = (mouseX - tx) * (mouseX - tx) + (mouseY - ty) * (mouseY - ty)
          // If closer to trailing edge, use trailing for chord
          // Otherwise use leading for position
          if (distTrailing < distLeading) "trailing"
          else "leading"
        case _ => "leading"  // Default to leading edge if projection fails
      }
    }.getOrElse("leading")
  }

  // Find nearest vertex based on screen position (2D projection)
  private def findNearestVertexByScreenPos(mouseX: Int, mouseY: Int): Option[(Float, Float, Float)] = {
    val verts = vertices
    if (verts.isEmpty) return None

    var minDistSq = Float.MaxValue
    var nearestX = 0f
    var nearestY = 0f
    var nearestZ = 0f

    var i = 0
    while (i < verts.length - 2) {
      val vx = verts(i)
      val vy = verts(i + 1)
      val vz = verts(i + 2)

      // Project vertex to screen (model coords: x=yle, y=xle, z=zle)
      projectToScreen(vx / displayScale, vy / displayScale, vz / displayScale).foreach { case (sx, sy) =>
        val dx = mouseX - sx
        val dy = mouseY - sy
        val distSq = dx * dx + dy * dy
        if (distSq < minDistSq) {
          minDistSq = distSq
          nearestX = vx
          nearestY = vy
          nearestZ = vz
        }
      }
      i += 3
    }

    // Snap threshold in pixels (15 pixels)
    val snapThresholdPx = 15
    if (minDistSq < snapThresholdPx * snapThresholdPx) {
      Some((nearestX, nearestY, nearestZ))
    } else {
      None
    }
  }

  private def projectToScreen(avlYle: Float, avlXle: Float, avlZle: Float): Option[(Int, Int)] = {
    val canvasWidth = glCanvas.getWidth
    val canvasHeight = glCanvas.getHeight
    if (canvasWidth == 0 || canvasHeight == 0) return None

    // Model coordinates: X = yle, Y = xle, Z = zle (AVL to model transform)
    val worldX = (avlYle * displayScale - centerX) * modelScale
    val worldY = (avlXle * displayScale - centerY) * modelScale
    val worldZ = (avlZle * displayScale - centerZ) * modelScale

    // OpenGL applies transforms in reverse order, so rotateY then rotateX
    val radX = scala.math.toRadians(rotationX)
    val radY = scala.math.toRadians(rotationY)
    val cosX = scala.math.cos(radX).toFloat
    val sinX = scala.math.sin(radX).toFloat
    val cosY = scala.math.cos(radY).toFloat
    val sinY = scala.math.sin(radY).toFloat

    // First rotate around Y axis (to match OpenGL's reverse order)
    val rx1 = worldX * cosY + worldZ * sinY
    val rz1 = -worldX * sinY + worldZ * cosY
    // Then rotate around X axis
    val ry2 = worldY * cosX - rz1 * sinX
    val rz2 = worldY * sinX + rz1 * cosX

    val viewX = rx1
    val viewY = ry2

    // Apply pan
    val viewXPanned = viewX + panX
    val viewYPanned = viewY + panY

    // Ortho projection: size = 150 / zoom
    val size = 150.0f / zoom
    val aspect = canvasWidth.toFloat / canvasHeight.toFloat

    // Map to screen coordinates
    val screenX = (canvasWidth / 2.0f + viewXPanned / (size * aspect) * canvasWidth / 2.0f).toInt
    val screenY = (canvasHeight / 2.0f - viewYPanned / size * canvasHeight / 2.0f).toInt

    Some((screenX, screenY))
  }

  private def isClickOnHandle(mouseX: Int, mouseY: Int): Boolean = {
    selectedSection.exists { case (_, _, xle, yle, zle, _) =>
      // Project the 3D point to 2D screen coordinates
      val canvasWidth = glCanvas.getWidth
      val canvasHeight = glCanvas.getHeight
      if (canvasWidth == 0 || canvasHeight == 0) return false

      // Apply the same transformations as in render
      val worldX = (yle * displayScale - centerX) * modelScale
      val worldY = (xle * displayScale - centerY) * modelScale
      val worldZ = (zle * displayScale - centerZ) * modelScale

      // Apply rotation
      val radX = scala.math.toRadians(rotationX)
      val radY = scala.math.toRadians(rotationY)
      val cosX = scala.math.cos(radX)
      val sinX = scala.math.sin(radX)
      val cosY = scala.math.cos(radY)
      val sinY = scala.math.sin(radY)

      // Rotate around Y axis first, then X
      val rx1 = worldX * cosY - worldZ * sinY
      val rz1 = worldX * sinY + worldZ * cosY
      val ry1 = worldY * cosX - rz1 * sinX

      // Apply zoom and pan
      val screenX = (rx1 * zoom * 20 + panX + canvasWidth / 2).toInt
      val screenY = (canvasHeight / 2 - ry1 * zoom * 20 + panY).toInt

      // Check if click is within 15 pixels of handle
      val dx = mouseX - screenX
      val dy = mouseY - screenY
      dx * dx + dy * dy < 225  // 15^2
    }
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
      minX = scala.math.min(minX, x)
      minY = scala.math.min(minY, y)
      minZ = scala.math.min(minZ, z)
      maxX = scala.math.max(maxX, x)
      maxY = scala.math.max(maxY, y)
      maxZ = scala.math.max(maxZ, z)
      i += 3
    }

    centerX = (minX + maxX) / 2
    centerY = (minY + maxY) / 2
    centerZ = (minZ + maxZ) / 2

    modelMinX = minX
    modelMaxX = maxX
    modelMinY = minY
    modelMaxY = maxY
    modelMinZ = minZ
    modelMaxZ = maxZ

    val sizeX = maxX - minX
    val sizeY = maxY - minY
    val sizeZ = maxZ - minZ
    val maxSize = scala.math.max(scala.math.max(sizeX, sizeY), sizeZ)
    modelScale = if (maxSize > 0) 100.0f / maxSize else 1.0f

    // Update info label with dimensions
    updateInfoLabel()
  }

  private def updateInfoLabel(): Unit = {
    if (isDisposed) return
    val wingspan = (modelMaxZ - modelMinZ) / displayScale
    val length = (modelMaxY - modelMinY) / displayScale
    getDisplay.asyncExec(new Runnable {
      override def run(): Unit = {
        if (!isDisposed && !infoLabel.isDisposed) {
          infoLabel.setText(f"Wingspan: $wingspan%.2f | Length: $length%.2f")
        }
      }
    })
  }

  private def render(drawable: GLAutoDrawable): Unit = {
    val verts = vertices  // Local copy for thread safety
    val cols = colors
    val gl = drawable.getGL.getGL2
    val width = drawable.getSurfaceWidth
    val height = drawable.getSurfaceHeight

    // Set up projection
    gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
    gl.glLoadIdentity()
    val aspect = width.toFloat / scala.math.max(height, 1).toFloat
    val size = 150.0f / zoom
    gl.glOrtho(-size * aspect, size * aspect, -size, size, -1000, 1000)

    // Set up modelview
    gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
    gl.glLoadIdentity()

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)

    // Camera transformations
    gl.glTranslatef(panX, panY, 0)
    gl.glRotatef(rotationX, 1, 0, 0)
    gl.glRotatef(rotationY, 0, 1, 0)

    if (verts.nonEmpty) {
      gl.glPushMatrix()
      gl.glScalef(modelScale, modelScale, modelScale)
      gl.glTranslatef(-centerX, -centerY, -centerZ)

      // Set polygon mode (wireframe or solid)
      // GL_LINE = 0x1B01, GL_FILL = 0x1B02
      if (wireframeMode) {
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, 0x1B01)
        gl.glDisable(GLLightingFunc.GL_LIGHTING)
      }

      // Draw triangles
      gl.glBegin(GL.GL_TRIANGLES)
      var i = 0
      while (i < verts.length - 8) {
        val colorIdx = i / 3 * 4
        if (colorIdx + 2 < cols.length) {
          gl.glColor3f(cols(colorIdx), cols(colorIdx + 1), cols(colorIdx + 2))
        } else {
          gl.glColor3f(0.7f, 0.7f, 0.7f)
        }

        // Calculate normal
        val v0x = verts(i); val v0y = verts(i + 1); val v0z = verts(i + 2)
        val v1x = verts(i + 3); val v1y = verts(i + 4); val v1z = verts(i + 5)
        val v2x = verts(i + 6); val v2y = verts(i + 7); val v2z = verts(i + 8)

        val ux = v1x - v0x; val uy = v1y - v0y; val uz = v1z - v0z
        val vx = v2x - v0x; val vy = v2y - v0y; val vz = v2z - v0z

        var nx = uy * vz - uz * vy
        var ny = uz * vx - ux * vz
        var nz = ux * vy - uy * vx

        val len = scala.math.sqrt(nx * nx + ny * ny + nz * nz).toFloat
        if (len > 0) { nx /= len; ny /= len; nz /= len }
        gl.glNormal3f(nx, ny, nz)

        gl.glVertex3f(verts(i), verts(i + 1), verts(i + 2))
        i += 3
      }
      gl.glEnd()

      // Restore polygon mode and lighting
      if (wireframeMode) {
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, 0x1B02)  // GL_FILL
        gl.glEnable(GLLightingFunc.GL_LIGHTING)
      }

      gl.glPopMatrix()

      if (showDimensions) drawDimensionLines(gl, drawable)
      if (showAvlSurfaces && avlSurfaces.nonEmpty) drawAvlSurfaces(gl)
      if (selectedSection.isDefined) drawSelectedSectionHandle(gl)
    }

    gl.glFlush()
  }

  private def drawAvlSurfaces(gl: GL2): Unit = {
    gl.glDisable(GLLightingFunc.GL_LIGHTING)
    gl.glDisable(GL.GL_DEPTH_TEST)
    gl.glLineWidth(2.0f)

    gl.glPushMatrix()
    gl.glScalef(modelScale, modelScale, modelScale)
    gl.glTranslatef(-centerX, -centerY, -centerZ)

    for (surface <- avlSurfaces) {
      if (surface.length >= 2) {
        // Draw surface outline - green color
        gl.glColor3f(0.0f, 1.0f, 0.0f)

        // Draw leading edge line connecting all sections
        gl.glBegin(GL.GL_LINE_STRIP)
        for ((xle, yle, zle, _) <- surface) {
          // AVL uses X forward, Y right, Z up
          // Our 3D model uses X right, Y forward, Z up
          gl.glVertex3f(yle * displayScale, xle * displayScale, zle * displayScale)
        }
        gl.glEnd()

        // Draw trailing edge line connecting all sections
        gl.glBegin(GL.GL_LINE_STRIP)
        for ((xle, yle, zle, chord) <- surface) {
          gl.glVertex3f(yle * displayScale, (xle + chord) * displayScale, zle * displayScale)
        }
        gl.glEnd()

        // Draw chord lines for each section
        for ((xle, yle, zle, chord) <- surface) {
          gl.glBegin(GL.GL_LINES)
          gl.glVertex3f(yle * displayScale, xle * displayScale, zle * displayScale)
          gl.glVertex3f(yle * displayScale, (xle + chord) * displayScale, zle * displayScale)
          gl.glEnd()
        }
      }
    }

    gl.glPopMatrix()
    gl.glLineWidth(1.0f)
    gl.glEnable(GL.GL_DEPTH_TEST)
    gl.glEnable(GLLightingFunc.GL_LIGHTING)
  }

  private def drawSelectedSectionHandle(gl: GL2): Unit = {
    selectedSection.foreach { case (_, _, xle, yle, zle, chord) =>
      gl.glDisable(GLLightingFunc.GL_LIGHTING)
      gl.glDisable(GL.GL_DEPTH_TEST)

      gl.glPushMatrix()
      gl.glScalef(modelScale, modelScale, modelScale)
      gl.glTranslatef(-centerX, -centerY, -centerZ)

      // Draw leading edge handle - yellow/orange color
      gl.glColor3f(1.0f, 0.7f, 0.0f)
      gl.glPointSize(12.0f)
      gl.glBegin(GL.GL_POINTS)
      gl.glVertex3f(yle * displayScale, xle * displayScale, zle * displayScale)
      gl.glEnd()

      // Draw trailing edge handle - lighter color
      gl.glColor3f(1.0f, 0.9f, 0.5f)
      gl.glPointSize(8.0f)
      gl.glBegin(GL.GL_POINTS)
      gl.glVertex3f(yle * displayScale, (xle + chord) * displayScale, zle * displayScale)
      gl.glEnd()

      // Draw chord line highlighted
      gl.glColor3f(1.0f, 0.7f, 0.0f)
      gl.glLineWidth(3.0f)
      gl.glBegin(GL.GL_LINES)
      gl.glVertex3f(yle * displayScale, xle * displayScale, zle * displayScale)
      gl.glVertex3f(yle * displayScale, (xle + chord) * displayScale, zle * displayScale)
      gl.glEnd()

      gl.glPopMatrix()
      gl.glPointSize(1.0f)
      gl.glLineWidth(1.0f)
      gl.glEnable(GL.GL_DEPTH_TEST)
      gl.glEnable(GLLightingFunc.GL_LIGHTING)
    }
  }

  private def drawDimensionLines(gl: GL2, drawable: GLAutoDrawable): Unit = {
    gl.glDisable(GLLightingFunc.GL_LIGHTING)
    gl.glDisable(GL.GL_DEPTH_TEST)  // Draw on top of everything
    gl.glColor3f(0.0f, 1.0f, 1.0f)  // Cyan color
    gl.glLineWidth(3.0f)
    gl.glEnable(GL2.GL_LINE_STIPPLE)
    gl.glLineStipple(2, 0x00FF.toShort)

    gl.glPushMatrix()
    gl.glScalef(modelScale, modelScale, modelScale)
    gl.glTranslatef(-centerX, -centerY, -centerZ)

    val offsetY = modelMinY - (modelMaxY - modelMinY) * 0.15f
    val offsetZ = modelMinZ - (modelMaxZ - modelMinZ) * 0.1f
    val offsetX = modelMaxX + (modelMaxX - modelMinX) * 0.15f
    val capSize = (modelMaxX - modelMinX) * 0.02f

    // Wingspan line
    gl.glBegin(GL.GL_LINES)
    gl.glVertex3f(modelMinX, offsetY, offsetZ)
    gl.glVertex3f(modelMaxX, offsetY, offsetZ)
    gl.glEnd()

    gl.glBegin(GL.GL_LINES)
    gl.glVertex3f(modelMinX, offsetY - capSize, offsetZ)
    gl.glVertex3f(modelMinX, offsetY + capSize, offsetZ)
    gl.glVertex3f(modelMaxX, offsetY - capSize, offsetZ)
    gl.glVertex3f(modelMaxX, offsetY + capSize, offsetZ)
    gl.glEnd()

    // Length line
    gl.glBegin(GL.GL_LINES)
    gl.glVertex3f(offsetX, modelMinY, offsetZ)
    gl.glVertex3f(offsetX, modelMaxY, offsetZ)
    gl.glEnd()

    gl.glBegin(GL.GL_LINES)
    gl.glVertex3f(offsetX - capSize, modelMinY, offsetZ)
    gl.glVertex3f(offsetX + capSize, modelMinY, offsetZ)
    gl.glVertex3f(offsetX - capSize, modelMaxY, offsetZ)
    gl.glVertex3f(offsetX + capSize, modelMaxY, offsetZ)
    gl.glEnd()

    gl.glDisable(GL2.GL_LINE_STIPPLE)
    gl.glLineWidth(1.0f)

    // Draw dimension text labels in 3D space (inside the transformed space)
    if (textRenderer != null) {
      val wingspanVal = (modelMaxX - modelMinX) / displayScale
      val lengthVal = (modelMaxY - modelMinY) / displayScale

      // Text scale - constant visual size regardless of zoom
      val textScale = (modelMaxX - modelMinX) * 0.006f / zoom

      textRenderer.begin3DRendering()
      textRenderer.setColor(0.0f, 1.0f, 1.0f, 1.0f)  // Cyan

      // Wingspan text - centered below the wingspan line
      val wingspanText = f"$wingspanVal%.2f m"
      val wingspanTextX = (modelMinX + modelMaxX) / 2
      val wingspanTextY = offsetY - capSize * 4
      textRenderer.draw3D(wingspanText, wingspanTextX, wingspanTextY, offsetZ, textScale)

      // Length text - beside the length line
      val lengthText = f"$lengthVal%.2f m"
      val lengthTextX = offsetX + capSize * 4
      val lengthTextY = (modelMinY + modelMaxY) / 2
      textRenderer.draw3D(lengthText, lengthTextX, lengthTextY, offsetZ, textScale)

      textRenderer.end3DRendering()
    }

    gl.glPopMatrix()

    gl.glEnable(GL.GL_DEPTH_TEST)
    gl.glEnable(GLLightingFunc.GL_LIGHTING)
  }
}
