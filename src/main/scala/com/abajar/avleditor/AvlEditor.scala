/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.avleditor;

import com.abajar.avleditor.avl.AVL
import com.abajar.avleditor.avl.AVLS
import com.abajar.avleditor.crrcsim.CRRCSim
import com.abajar.avleditor.crrcsim.CRRCSimFactory
import com.abajar.avleditor.crrcsim.CRRCSimRepository
import com.abajar.avleditor.crrcsim.MetersConversor
import com.abajar.avleditor.crrcsim.MetersConversorInverted
import com.abajar.avleditor.crrcsim.XRelativeToCG
import com.abajar.avleditor.crrcsim.XRelativeToCGInverted
import com.abajar.avleditor.crrcsim.MultiUnit
import com.abajar.avleditor.crrcsim.YRelativeToCG
import com.abajar.avleditor.crrcsim.YRelativeToCGInverted
import com.abajar.avleditor.crrcsim.ZRelativeToCG
import com.abajar.avleditor.crrcsim.ZRelativeToCGInverted
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.util.Properties
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger
import java.lang.reflect.Method
import java.util.ArrayList

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller

import org.eclipse.swt.widgets.{Event, TreeItem, TableItem, Listener}
import org.eclipse.swt.events._
import org.eclipse.swt.SWT
import scala.collection.JavaConverters._

import swt.MainWindow
import swt.AvlResultsWindow
import swt.dsl.TableFieldWritable
import swt.dsl.TableFieldReadOnly
import swt.dsl.TableFieldFile
import com.abajar.avleditor.view.annotations
import java.lang.reflect.Field
import com.abajar.avleditor.view.annotations.AvlEditorNode
import com.abajar.avleditor.view.annotations.AvlEditorField
import com.abajar.avleditor.view.annotations.AvlEditorFileField
import com.abajar.avleditor.view.annotations.AvlEditorReadOnly
import com.abajar.avleditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS
import com.abajar.avleditor.swt.MenuOption._
import com.abajar.avleditor.swt.dsl.TableField
import com.abajar.avleditor.avl.connectivity.AvlRunner


object AvlEditor{

    val logger = Logger.getLogger(AvlEditor.getClass.getName)

    val CONFIGURATION_ROOT = System.getProperty("user.home") + "/.avleditor"
    val CONFIGURATION_PATH = CONFIGURATION_ROOT + "/configuration.xml"

    var configuration = new Properties()

    val dir = new File(CONFIGURATION_ROOT)
    if (!dir.exists) dir.mkdir

    var crrcsim = new CRRCSimFactory().create()
    var currentFile: Option[File] = None

    try {
      configuration.loadFromXML(new FileInputStream(CONFIGURATION_PATH))
    } catch {
      case ex: IOException =>
        logger.log(Level.INFO, "Config file doesn't exists");
    }

    Runtime.getRuntime().addShutdownHook(new Thread(){
      override def run: Unit = {
        try {
          configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), "Configuration file")
        } catch {
          case ex: Exception =>
            logger.log(Level.FINE, "Unable to store configuration file", ex)
        }
      }
    })

    val window = new MainWindow(
        handleClickButton,
        treeSourceHandler,
        handleTreeEvent,
        handleClickMenu,
        propertiesSourceHandler,
        handleClickProperties
      )

    // Set up property change callback to update 3D viewer when scale changes
    swt.dsl.Widget.setPropertyChangeCallback(() => {
      window.treeNodeSelected.foreach {
        case graphics: com.abajar.avleditor.crrcsim.Graphics =>
          window.viewer3D.setScale(graphics.getScale)
        case section: com.abajar.avleditor.avl.geometry.Section =>
          // Update selected section in 3D viewer when properties change
          selectSectionIn3D(section)
        case _ => // Do nothing for other types
      }
      // Reload AVL surfaces when any property changes
      loadAvlSurfaces()
    })

    // Set up section update callback for 3D editing
    window.viewer3D.setSectionUpdateCallback((surfaceIdx: Int, sectionIdx: Int, x: Float, y: Float, z: Float, chord: Float) => {
      window.display.asyncExec(new Runnable {
        override def run(): Unit = {
          updateSectionFromViewer(surfaceIdx, sectionIdx, x, y, z, chord)
        }
      })
    })

    // Set up log handler to display logs in footer BEFORE logging anything
    val logFooterHandler = new LogFooterHandler(window.display, window.footerLabel)
    Logger.getLogger("").addHandler(logFooterHandler)

    // Set up log window
    val logWindow = new swt.LogWindow(window.display, logFooterHandler.logHistory)

    // Set up AVL results window
    val avlResultsWindow = new AvlResultsWindow(window.display)

    // Add click listener to footer to open log window
    window.footerLabel.addListener(SWT.MouseDown, new Listener {
      override def handleEvent(e: Event): Unit = {
        logWindow.open()
      }
    })

    // Check and ensure AVL is available on startup (now logs will be captured)
    logger.log(Level.INFO, "Application starting...")
    logger.log(Level.INFO, "Checking AVL availability...")
    AvlManager.ensureAvlAvailable(configuration)
    logger.log(Level.INFO, "Application initialized successfully")

    def apply():Unit = {
      window.disableAllButtons
      loadLastFile()
      logger.log(Level.INFO, "Ready")
      window.show
    }

    private def loadLastFile(): Unit = {
      Option(configuration.getProperty("crrcsim.lastFile")).foreach { lastFilePath =>
        val file = new File(lastFilePath)
        if (file.exists() && file.getName.endsWith(".avle")) {
          try {
            logger.log(Level.INFO, s"Loading last file: ${file.getAbsolutePath}")
            currentFile = Some(file)
            open(file)
          } catch {
            case ex: Exception =>
              logger.log(Level.WARNING, s"Failed to load last file: ${ex.getMessage}")
          }
        }
      }
    }

    private def handleClickButton(button: ENABLE_BUTTONS): Unit = {
      window.treeNodeSelected match {
        case Some(nodeSelected) => {
          button.click(nodeSelected, window.treeNodeSelectedParent.get)
          window.refreshTree
        }
        case None => throw new Exception("Button click without node selected")
      }
    }

    private def handleClickMenu(menuOption: MenuOption): Unit = menuOption match {
      case Save => save
      case SaveAs => saveFile
      case Open => openFile
      case ExportAsAvl => exportAsAVL
      case ExportAsCRRCSim => exportAsCRRCsim
      case RunAvl => runAvl
      case SetAvlExecutable => setAvlExecutable
      case ClearAvlConfiguration => clearAvlConfiguration
    }

    def exportAsAVL(avlFile: Path): Unit = {
      AVLS.avlToFile(this.crrcsim.getAvl(), avlFile, avlFile.getParent())
    }

    private def saveAs(file: File) = {
      new CRRCSimRepository().storeToFile(file, crrcsim)
    }

    private def open(file: File) = {
      crrcsim = new CRRCSimRepository().restoreFromFile(file)
      window.refreshTree
      // Auto-load 3D model if available
      load3DModel()
    }

    private def load3DModel(): Unit = {
      val graphics = crrcsim.getGraphics
      if (graphics != null) {
        val modelPath = graphics.getModel
        window.viewer3D.setScale(graphics.getScale)
        if (modelPath != null && modelPath.nonEmpty) {
          val file = new File(modelPath)
          if (file.exists) {
            val loaded = window.viewer3D.loadModel(modelPath)
            logger.info(s"Auto-loaded 3D model: $loaded")
          } else {
            window.viewer3D.clearModel()
          }
        } else {
          window.viewer3D.clearModel()
        }
      }
      // Load AVL surfaces
      loadAvlSurfaces()
    }

    private def loadAvlSurfaces(): Unit = {
      import scala.collection.JavaConverters._
      val avl = crrcsim.getAvl()
      if (avl != null && avl.getGeometry() != null) {
        val surfaces = avl.getGeometry().getSurfaces().asScala.map { surface =>
          val dX = surface.getdX()
          val dY = surface.getdY()
          val dZ = surface.getdZ()
          surface.getSections().asScala.map { section =>
            (section.getXle() + dX, section.getYle() + dY, section.getZle() + dZ, section.getChord())
          }.toArray
        }.toArray
        window.viewer3D.setAvlSurfaces(surfaces)
      }
    }

    private def updateSectionFromViewer(surfaceIdx: Int, sectionIdx: Int, x: Float, y: Float, z: Float, chord: Float): Unit = {
      val avl = crrcsim.getAvl()
      if (avl == null || avl.getGeometry() == null) return

      val surfaces = avl.getGeometry().getSurfaces()
      if (surfaceIdx >= surfaces.size()) return

      val surface = surfaces.get(surfaceIdx)
      val sections = surface.getSections()
      if (sectionIdx >= sections.size()) return

      val section = sections.get(sectionIdx)

      // Subtract surface offsets to get local coordinates
      val dX = surface.getdX()
      val dY = surface.getdY()
      val dZ = surface.getdZ()

      section.setXle(x - dX)
      section.setYle(y - dY)
      section.setZle(z - dZ)
      section.setChord(chord)

      // Refresh properties table if this section is selected
      window.treeNodeSelected.foreach {
        case s: com.abajar.avleditor.avl.geometry.Section if s eq section =>
          loadPropertiesForTreeItem(section)
        case _ =>
      }

      // Reload AVL surfaces to reflect the change
      loadAvlSurfaces()
    }

    private def openFile = {
      window.showOpenDialog(
        configuration.getProperty("crrcsim.save", "~/"),
        Array("AVL Editor files (*.avle)", "AVL Editor file (*.crr)"),
        Array("avle", "crr")
      ) match {
        case Some(file) =>
          configuration.setProperty("crrcsim.save", file.getAbsolutePath())
          currentFile = Some(file)
          open(file)
          if (file.getName.endsWith(".avle")) {
            setLastFile(file)
          }
        case None =>
      }
    }

    private def save = {
      currentFile match {
        case Some(file) =>
          saveAs(file)
          setLastFile(file)
          logger.log(Level.INFO, s"Saved: ${file.getAbsolutePath}")
        case None =>
          saveFile
      }
    }

    private def saveFile = {
      val path = this.configuration.getProperty("crrcsim.save", "~/")
      showSaveDialog(path, "AVL Editor file (*.avle)", "avle").foreach(file => {
        this.configuration.setProperty("crrcsim.save", file.getAbsolutePath())
        currentFile = Some(file)
        this.saveAs(file)
        setLastFile(file)
      })
    }

    private def setLastFile(file: File): Unit = {
      configuration.setProperty("crrcsim.lastFile", file.getAbsolutePath())
      try {
        configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), "Configuration file")
      } catch {
        case ex: Exception =>
          logger.log(Level.FINE, "Unable to save configuration", ex)
      }
    }

    def exportAsAVL: Unit = {
      val path = this.configuration.getProperty("crrcsim.save", "~/")
      this.showSaveDialog(path, "AVL file (*.avl)","avl").foreach(file => {
        this.configuration.setProperty("crrcsim.save",file.getAbsolutePath())
        exportAsAVL(Paths.get(file.getPath()))
      })
    }

    def exportAsCRRCsim: Unit = {
      val path = this.configuration.getProperty("crrcsim.save", "~/")
      this.showSaveDialog(path, "CRRCsim file (*.xml)", "xml").foreach(file => {
        this.configuration.setProperty("crrcsim.save",file.getAbsolutePath())
        if (existsAvlExecutable) exportAsCRRCsim(file)
      })
    }

    def runAvl: Unit = {
      if (!existsAvlExecutable) {
        logger.log(Level.WARNING, "AVL executable not configured")
        return
      }

      val avl = this.crrcsim.getAvl()
      val geometry = avl.getGeometry()
      val errors = geometry.validate()

      if (!errors.isEmpty) {
        import scala.collection.JavaConverters._
        for (error <- errors.asScala) {
          logger.log(Level.WARNING, s"Validation error: $error")
        }
        logger.log(Level.SEVERE, "Model validation failed. Please fix the errors above before running AVL.")
        return
      }

      logger.log(Level.INFO, "Running AVL analysis...")
      val avlPath = this.configuration.getProperty("avl.path")
      val originPath = this.crrcsim.getOriginPath()

      new Thread(new Runnable {
        def run(): Unit = {
          try {
            logger.log(Level.INFO, s"Starting AvlRunner with path: $avlPath")
            val (elevationAngle, azimuthAngle) = window.viewer3D.getViewAngles
            // Adjust azimuth by +90 degrees to match AVL's coordinate system
            val runner = new AvlRunner(avlPath, avl, originPath, azimuthAngle + 90, elevationAngle)
            logger.log(Level.INFO, "AvlRunner finished, getting calculation...")
            val calculation = runner.getCalculation()
            val geometryPlotPath = runner.getGeometryPlotPath()
            val trefftzPlotPath = runner.getTrefftzPlotPath()
            logger.log(Level.INFO, s"Got calculation: $calculation")

            window.display.asyncExec(new Runnable {
              def run(): Unit = {
                logger.log(Level.INFO, "AVL analysis completed successfully")
                // Open plot window with generated images
                val plotWindow = new swt.AvlPlotWindow(window.display)
                plotWindow.open(geometryPlotPath, trefftzPlotPath)
                avlResultsWindow.open(calculation)
              }
            })
          } catch {
            case ex: Throwable =>
              ex.printStackTrace()
              window.display.asyncExec(new Runnable {
                def run(): Unit = {
                  logger.log(Level.SEVERE, s"Error running AVL: ${ex.getMessage}", ex)
                }
              })
          }
        }
      }).start()
    }

    private def existsAvlExecutable: Boolean = {
      Option(this.configuration.getProperty("avl.path")).isDefined
    }

    private def exportAsCRRCsim(file: File): Unit = {
      crrcsim.calculate(this.configuration.getProperty("avl.path"), this.crrcsim.getOriginPath())

      val avl = this.crrcsim.getAvl()
      val lengthUnit = avl.getLengthUnit()
      val centerOfMass = crrcsim.getCenterOfMass()
      val m = JAXBContext.newInstance(classOf[CRRCSim]).createMarshaller()

      m.setAdapter(new XRelativeToCG(lengthUnit, centerOfMass.getX()))
      m.setAdapter(new YRelativeToCG(lengthUnit, centerOfMass.getY()))
      m.setAdapter(new ZRelativeToCG(lengthUnit, centerOfMass.getZ()))
      m.setAdapter(new XRelativeToCGInverted(lengthUnit, centerOfMass.getX()))
      m.setAdapter(new YRelativeToCGInverted(lengthUnit, centerOfMass.getY()))
      m.setAdapter(new ZRelativeToCGInverted(lengthUnit, centerOfMass.getZ()))
      m.setAdapter(new MetersConversor(new MultiUnit(lengthUnit, avl.getMassUnit(), avl.getTimeUnit())))
      m.setAdapter(new MetersConversorInverted(new MultiUnit(lengthUnit, avl.getMassUnit(), avl.getTimeUnit())))
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)

      val fos = new FileOutputStream(file)
      m.marshal(crrcsim, fos)
      fos.close()
    }

    private def setAvlExecutable = {
      window.showOpenDialog(
        this.configuration.getProperty("avl.path", "~/"),
        "AVL executable",
        "*") match {
          case Some(file) =>
            this.configuration.setProperty("avl.path", file.getAbsolutePath())
            try {
                this.configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), "Configuration file")
                logger.log(Level.INFO, s"AVL executable set to: ${file.getAbsolutePath()}")
            } catch {
              case ex: Exception =>
                logger.log(Level.FINE, "Unable to save configuation", ex)
            }
          case None =>
      }
    }

    private def clearAvlConfiguration = {
      this.configuration.remove("avl.path")
      try {
        this.configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), "Configuration file")
        logger.log(Level.INFO, "AVL configuration cleared. Application will download AVL on next export.")

        // Optionally trigger re-download immediately
        logger.log(Level.INFO, "Re-checking AVL availability...")
        AvlManager.ensureAvlAvailable(configuration)
      } catch {
        case ex: Exception =>
          logger.log(Level.SEVERE, "Unable to clear AVL configuration", ex)
      }
    }

    private def showSaveDialog(path: String, description: String, extensions: String): Option[File] = {
      window.showSaveDialog(
        configuration.getProperty("crrcsim.save", "~/"),
        description,
        extensions
      )
    }

    private def extractProperties(data: Any) = {
      val objClass = data.getClass
      val regularFields = for{
        field <- objClass.getDeclaredFields
        if (field.isAnnotationPresent(classOf[annotations.AvlEditorField]))
      } yield new TableFieldWritable(
        data,
        field,
        field.getAnnotation(classOf[AvlEditorField]).text(),
        field.getAnnotation(classOf[AvlEditorField]).help()
      )
      val fileFields = for{
        field <- objClass.getDeclaredFields
        if (field.isAnnotationPresent(classOf[annotations.AvlEditorFileField]))
      } yield {
        logger.info(s"Found file field: ${field.getName}")
        new TableFieldFile(
          data,
          field,
          field.getAnnotation(classOf[AvlEditorFileField]).text(),
          field.getAnnotation(classOf[AvlEditorFileField]).help(),
          field.getAnnotation(classOf[AvlEditorFileField]).extensions(),
          field.getAnnotation(classOf[AvlEditorFileField]).extensionDescription()
        )
      }
      val readOnlyFields = for{
        method <- objClass.getMethods
        if (method.isAnnotationPresent(classOf[annotations.AvlEditorReadOnly]))
      } yield new TableFieldReadOnly(
        data,
        method,
        method.getAnnotation(classOf[AvlEditorReadOnly]).text(),
        method.getAnnotation(classOf[AvlEditorReadOnly]).help()
      )
      regularFields ++ fileFields ++ readOnlyFields
    }

    private def loadPropertiesForTreeItem(data: Any) = {
      window.properties.setItemCount(extractProperties(data).length)
      window.properties.clearAll
    }

    private def propertiesSourceHandler(index: Integer): TableField = {
      extractProperties(window.treeNodeSelected.get)(index)
    }

    private def treeSourceHandler(parentData: Option[Any], index: Integer): (String, Any, Integer) = {
      val node = parentData match {
        case Some(data) => getChilds(data)(index)
        case None => (crrcsim.toString, crrcsim)
      }

      val childs = getChilds(node._2)

      (node._1, node._2, childs.length)
    }

    private def handleTreeEvent(data: Any): Unit = {
      val objClass = data.getClass
      val parentClass = window.treeNodeSelectedParent.map(_.getClass.getName).getOrElse("No Parent")
      // Load properties BEFORE button layout to ensure itemCount is correct
      loadPropertiesForTreeItem(data)
      if (objClass.isAnnotationPresent(classOf[annotations.AvlEditor])) {
        val crrcsimAnnotations = objClass.getAnnotation(classOf[annotations.AvlEditor]).asInstanceOf[annotations.AvlEditor]
        window.buttonsEnableOnly(crrcsimAnnotations.buttons.toList)
      } else {
        window.disableAllButtons
      }

      // Load 3D model if Graphics node is selected
      data match {
        case graphics: com.abajar.avleditor.crrcsim.Graphics =>
          val modelPath = graphics.getModel
          logger.info(s"Graphics selected, model path: $modelPath")
          // Set scale for dimension display
          window.viewer3D.setScale(graphics.getScale)
          if (modelPath != null && modelPath.nonEmpty) {
            val file = new File(modelPath)
            logger.info(s"File exists: ${file.exists}, absolute: ${file.getAbsolutePath}")
            if (file.exists) {
              val loaded = window.viewer3D.loadModel(modelPath)
              logger.info(s"Model loaded: $loaded")
            } else {
              window.viewer3D.clearModel()
            }
          } else {
            window.viewer3D.clearModel()
          }
          window.viewer3D.clearSelectedSection()
        case section: com.abajar.avleditor.avl.geometry.Section =>
          // Find the parent surface and section index
          selectSectionIn3D(section)
        case _ =>
          // Clear section selection for other nodes
          window.viewer3D.clearSelectedSection()
      }
    }

    private def selectSectionIn3D(section: com.abajar.avleditor.avl.geometry.Section): Unit = {
      val avl = crrcsim.getAvl()
      if (avl == null || avl.getGeometry() == null) return

      val surfaces = avl.getGeometry().getSurfaces()
      var surfaceIdx = 0
      var found = false
      while (surfaceIdx < surfaces.size() && !found) {
        val surface = surfaces.get(surfaceIdx)
        val sections = surface.getSections()
        var sectionIdx = 0
        while (sectionIdx < sections.size() && !found) {
          if (sections.get(sectionIdx) eq section) {
            found = true
            val dX = surface.getdX()
            val dY = surface.getdY()
            val dZ = surface.getdZ()
            window.viewer3D.setSelectedSection(
              surfaceIdx, sectionIdx,
              section.getXle() + dX,
              section.getYle() + dY,
              section.getZle() + dZ,
              section.getChord()
            )
          }
          sectionIdx += 1
        }
        surfaceIdx += 1
      }
    }

    private def handleClickProperties(data: Any): Unit = data match {
      case tableField: TableField => window.help.setText(tableField.help)
    }

    private def getChilds(node: Any): scala.collection.immutable.List[(String, Any)] = node match {
      case childs: java.util.List[_] =>
        childs.asScala.toList.map(child => (child.toString, child.asInstanceOf[Any]))
      case node =>
        node.getClass.getMethods.foldLeft(List[(String, Any)]())(
          (nodes, method)=>
            if (method.isAnnotationPresent(classOf[AvlEditorNode])){
              nodes :+ getNameNodePair(method, node)
            } else {
              nodes
            }
        )
    }

    private def getNameNodePair(method: Method, parentNode: Any) = {
      val name = method.getAnnotation(classOf[AvlEditorNode]).name
      val node = method.invoke(parentNode)
      (if (name == "Node") node.toString else name , node)
    }
}

