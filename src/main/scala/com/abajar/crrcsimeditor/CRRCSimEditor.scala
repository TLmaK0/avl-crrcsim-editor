/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.crrcsimeditor;

import com.abajar.crrcsimeditor.avl.AVL
import com.abajar.crrcsimeditor.avl.AVLS
import com.abajar.crrcsimeditor.crrcsim.CRRCSim
import com.abajar.crrcsimeditor.crrcsim.CRRCSimFactory
import com.abajar.crrcsimeditor.crrcsim.CRRCSimRepository
import com.abajar.crrcsimeditor.crrcsim.MetersConversor
import com.abajar.crrcsimeditor.crrcsim.MetersConversorInverted
import com.abajar.crrcsimeditor.crrcsim.XRelativeToCG
import com.abajar.crrcsimeditor.crrcsim.XRelativeToCGInverted
import com.abajar.crrcsimeditor.crrcsim.MultiUnit
import com.abajar.crrcsimeditor.crrcsim.YRelativeToCG
import com.abajar.crrcsimeditor.crrcsim.YRelativeToCGInverted
import com.abajar.crrcsimeditor.crrcsim.ZRelativeToCG
import com.abajar.crrcsimeditor.crrcsim.ZRelativeToCGInverted
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
import swt.dsl.TableFieldWritable
import swt.dsl.TableFieldReadOnly
import com.abajar.crrcsimeditor.view.annotations
import java.lang.reflect.Field
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorReadOnly
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS
import com.abajar.crrcsimeditor.swt.MenuOption._
import com.abajar.crrcsimeditor.swt.dsl.TableField


object CRRCSimEditor{

    val logger = Logger.getLogger(CRRCSimEditor.getClass.getName)

    val CONFIGURATION_ROOT = System.getProperty("user.home") + "/.crrcsimeditor"
    val CONFIGURATION_PATH = CONFIGURATION_ROOT + "/configuration.xml"

    var configuration = new Properties()

    val dir = new File(CONFIGURATION_ROOT)
    if (!dir.exists) dir.mkdir

    var crrcsim = new CRRCSimFactory().create()

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

    // Set up log handler to display logs in footer BEFORE logging anything
    val logFooterHandler = new LogFooterHandler(window.display, window.footerLabel)
    Logger.getLogger("").addHandler(logFooterHandler)

    // Set up log window
    val logWindow = new swt.LogWindow(window.display, logFooterHandler.logHistory)

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
      logger.log(Level.INFO, "Ready")
      window.show
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
      case SaveAs => saveFile
      case Open => openFile
      case ExportAsAvl => exportAsAVL
      case ExportAsCRRCSim => exportAsCRRCsim
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
    }


    private def openFile = {
      window.showOpenDialog(
        configuration.getProperty("crrcsim.save", "~/"),
        "CRRCsim editor file (*.crr)",
        "crr"
      ) match {
        case Some(file) =>
          configuration.setProperty("crrcsim.save",file.getAbsolutePath())
          open(file)
        case None =>
      }
    }

    private def saveFile = {
      val path = this.configuration.getProperty("crrcsim.save", "~/")
      showSaveDialog(path, "CRRCsim editor file (*.crr)", "crr").foreach(file => {
        this.configuration.setProperty("crrcsim.save",file.getAbsolutePath())
        this.saveAs(file)
      })
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
      (for{
        field <- objClass.getDeclaredFields
        if (field.isAnnotationPresent(classOf[annotations.CRRCSimEditorField]))
      } yield new TableFieldWritable(
        data,
        field,
        field.getAnnotation(classOf[CRRCSimEditorField]).text(),
        field.getAnnotation(classOf[CRRCSimEditorField]).help()
      )) ++
      (for{
        method <- objClass.getMethods
        if (method.isAnnotationPresent(classOf[annotations.CRRCSimEditorReadOnly]))
      } yield new TableFieldReadOnly(
        data,
        method,
        method.getAnnotation(classOf[CRRCSimEditorReadOnly]).text(),
        method.getAnnotation(classOf[CRRCSimEditorReadOnly]).help()
      ))
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
      if (objClass.isAnnotationPresent(classOf[annotations.CRRCSimEditor])) {
        val crrcsimAnnotations = objClass.getAnnotation(classOf[annotations.CRRCSimEditor]).asInstanceOf[annotations.CRRCSimEditor]
        window.buttonsEnableOnly(crrcsimAnnotations.buttons.toList)
      } else {
        window.disableAllButtons
      }
      loadPropertiesForTreeItem(data)
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
            if (method.isAnnotationPresent(classOf[CRRCSimEditorNode])){
              nodes :+ getNameNodePair(method, node)
            } else {
              nodes
            }
        )
    }

    private def getNameNodePair(method: Method, parentNode: Any) = {
      val name = method.getAnnotation(classOf[CRRCSimEditorNode]).name
      val node = method.invoke(parentNode)
      (if (name == "Node") node.toString else name , node)
    }
}

