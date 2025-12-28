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

import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.events._
import org.eclipse.swt.awt.SWT_AWT

import java.awt.Frame
import com.jogamp.opengl._
import com.jogamp.opengl.awt.GLJPanel
import com.jogamp.opengl.fixedfunc.{GLLightingFunc, GLMatrixFunc}
import com.jogamp.opengl.util.FPSAnimator

import com.abajar.avleditor.ac3d.{AC3DLoader, AC3DModel}

class Viewer3DGL(parent: Composite, style: Int) extends Composite(parent, style) {
  setLayout(new FillLayout())

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

  // Create AWT Frame embedded in SWT
  private val awtComposite = new Composite(this, SWT.EMBEDDED)
  awtComposite.setLayout(new FillLayout())
  private val awtFrame = SWT_AWT.new_Frame(awtComposite)

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
      glInitialized = true
    }

    override def dispose(drawable: GLAutoDrawable): Unit = {}

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
      if (e.getButton == java.awt.event.MouseEvent.BUTTON1) isDragging = true
      else if (e.getButton == java.awt.event.MouseEvent.BUTTON3) isPanning = true
    }

    override def mouseReleased(e: java.awt.event.MouseEvent): Unit = {
      isDragging = false
      isPanning = false
    }

    override def mouseClicked(e: java.awt.event.MouseEvent): Unit = {
      if (e.getClickCount == 2) zoomToFit()
    }
  })

  glCanvas.addMouseMotionListener(new java.awt.event.MouseMotionAdapter {
    override def mouseDragged(e: java.awt.event.MouseEvent): Unit = {
      if (isDragging) {
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
    zoom = 1.0f
  }

  def clearModel(): Unit = {
    model = None
    vertices = Array()
    colors = Array()
  }

  def setScale(scale: Float): Unit = {
    displayScale = scale
  }

  def setShowDimensions(show: Boolean): Unit = {
    showDimensions = show
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

      gl.glPopMatrix()

      if (showDimensions) drawDimensionLines(gl)
    }

    gl.glFlush()
  }

  private def drawDimensionLines(gl: GL2): Unit = {
    gl.glDisable(GLLightingFunc.GL_LIGHTING)
    gl.glColor3f(0.0f, 1.0f, 1.0f)
    gl.glLineWidth(2.0f)
    gl.glEnable(GL2.GL_LINE_STIPPLE)
    gl.glLineStipple(1, 0x00FF.toShort)

    gl.glPushMatrix()
    gl.glScalef(modelScale, modelScale, modelScale)
    gl.glTranslatef(-centerX, -centerY, -centerZ)

    val offsetY = modelMinY - (modelMaxY - modelMinY) * 0.15f
    val offsetZ = modelMinZ - (modelMaxZ - modelMinZ) * 0.1f
    val offsetX = modelMaxX + (modelMaxX - modelMinX) * 0.15f
    val capSize = (modelMaxX - modelMinX) * 0.02f

    // Wingspan
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

    // Length
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

    gl.glPopMatrix()
    gl.glDisable(GL2.GL_LINE_STIPPLE)
    gl.glLineWidth(1.0f)
    gl.glEnable(GLLightingFunc.GL_LIGHTING)
  }
}
