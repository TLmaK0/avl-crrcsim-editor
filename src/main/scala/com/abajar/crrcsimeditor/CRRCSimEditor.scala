package com.abajar.crrcsimeditor;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLS;
import com.abajar.crrcsimeditor.crrcsim.CRRCSim;
import com.abajar.crrcsimeditor.crrcsim.CRRCSimFactory;
import com.abajar.crrcsimeditor.crrcsim.CRRCSimRepository;
import com.abajar.crrcsimeditor.crrcsim.MetersConversor;
import com.abajar.crrcsimeditor.crrcsim.MetersConversorInverted;
import com.abajar.crrcsimeditor.crrcsim.XRelativeToCG;
import com.abajar.crrcsimeditor.crrcsim.XRelativeToCGInverted;
import com.abajar.crrcsimeditor.crrcsim.MultiUnit;
import com.abajar.crrcsimeditor.crrcsim.YRelativeToCG;
import com.abajar.crrcsimeditor.crrcsim.YRelativeToCGInverted;
import com.abajar.crrcsimeditor.crrcsim.ZRelativeToCG;
import com.abajar.crrcsimeditor.crrcsim.ZRelativeToCGInverted;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import swt.MainWindow
import org.eclipse.swt.widgets.{Event, TreeItem, TableItem}
import org.eclipse.swt.events._;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorField
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorReadOnly
import java.lang.reflect.Method
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS
import com.abajar.crrcsimeditor.swt.MenuOption._
import java.util.ArrayList
import scala.collection.JavaConverters._
import com.abajar.crrcsimeditor.view.annotations
import java.lang.reflect.Field

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
      override def run = {
        try {
          configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), "Configuration file")
        } catch {
          case ex: Exception =>
            logger.log(Level.FINE, null, ex)
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
    
    window.show

    private def handleClickButton(button: ENABLE_BUTTONS): Unit = {
      window.treeNodeSelected match {
        case Some(nodeSelected) => {
          TreeHelper.modifyTree(button, nodeSelected, window.treeNodeSelectedParent) 
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
    }

    def exportAsAVL(avlFile: Path): Unit = {
      AVLS.avlToFile(this.crrcsim.getAvl(), avlFile, avlFile.getParent())
    }

    def saveAs(file: File) = {
      new CRRCSimRepository().storeToFile(file, crrcsim)
    }

    def open(file: File) = {
      crrcsim = new CRRCSimRepository().restoreFromFile(file)
      window.refreshTree
    }


    def openFile = {
      val path = 
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

    def saveFile = {
      val path = this.configuration.getProperty("crrcsim.save", "~/")
      val file = showSaveDialog(path, "CRRCsim editor file (*.crr)", "crr")
      this.configuration.setProperty("crrcsim.save",file.getAbsolutePath())
      this.saveAs(file)
    }

    def exportAsAVL: Unit = {
      val path = this.configuration.getProperty("crrcsim.save", "~/")
      val file = this.showSaveDialog(path, "AVL file (*.avl)","avl")
      this.configuration.setProperty("crrcsim.save",file.getAbsolutePath())

      exportAsAVL(Paths.get(file.getPath()))
    }

    def exportAsCRRCsim: Unit = {
      val path = this.configuration.getProperty("crrcsim.save", "~/")
      val file = this.showSaveDialog(path, "CRRCsim file (*.xml)", "xml")
      this.configuration.setProperty("crrcsim.save",file.getAbsolutePath())
      if (existsAvlExecutable) exportAsCRRCsim(file)
    }

    private def existsAvlExecutable: Boolean = {
      val path = this.configuration.getProperty("avl.path")
      path != null && Files.exists(Paths.get(path))
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

    def setAvlExecutable = {
      val path = 
      window.showOpenDialog(
        this.configuration.getProperty("avl.path", "~/"), 
        "AVL executable", 
        "exe") match {
          case Some(file) =>
            this.configuration.setProperty("avl.path", file.getAbsolutePath())
            try {
                this.configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), null)
            } catch { 
              case ex: Exception =>
                logger.log(Level.FINE, null, ex)
            }
          case None =>
      }
    }

    private def showSaveDialog(path: String, description: String, extensions: String): File = {
      window.showSaveDialog(
        configuration.getProperty("crrcsim.save", "~/"),
        description, 
        extensions
      ) match {
        case Some(file: File) => file
        case None => null
      }
    }

    private def getFieldValue(field: Field, data: Any): String = {
      field.setAccessible(true)
      val result = field.get(data)
      return if (result == null) "" else result.toString
    }

    private def getMethodValue(method: Method, data: Any): String = {
      method.setAccessible(true)
      val result = method.invoke(data)
      return if (result == null) "" else result.toString
    }

    private def extractProperties(data: Any) = {
      val objClass = data.getClass
      (for{
        field <- objClass.getDeclaredFields
        if (field.isAnnotationPresent(classOf[annotations.CRRCSimEditorField]))
      } yield (field.getAnnotation(classOf[CRRCSimEditorField]).text(), getFieldValue(field, data), field.getAnnotation(classOf[CRRCSimEditorField]).help())) ++
      (for{
        method <- objClass.getMethods
        if (method.isAnnotationPresent(classOf[annotations.CRRCSimEditorReadOnly]))
      } yield (method.getAnnotation(classOf[CRRCSimEditorReadOnly]).text(), getMethodValue(method, data), method.getAnnotation(classOf[CRRCSimEditorReadOnly]).help()))
    }

    private def loadPropertiesForTreeItem(data: Any) = {
      window.properties.setItemCount(extractProperties(data).length)
      window.properties.clearAll
    }

    private def propertiesSourceHandler(index: Integer): (String, Any, Any) = {
      val properties = extractProperties(window.treeNodeSelected.get)

      return properties(index)
    }

    private def treeSourceHandler(parentData: Option[Any], index: Integer): (String, Any, Integer) = {
      val node = parentData match {
        case Some(data) => getChilds(data)(index)
        case None => (crrcsim.toString, crrcsim)
      }

      val childs = getChilds(node._2)

      return (node._1, node._2, childs.length)
    }

    private def handleTreeEvent(data: Any): Unit = {
      val objClass = data.getClass
      if (objClass.isAnnotationPresent(classOf[annotations.CRRCSimEditor])) {
        val crrcsimAnnotations = objClass.getAnnotation(classOf[annotations.CRRCSimEditor]).asInstanceOf[annotations.CRRCSimEditor]
        window.buttonsEnableOnly(crrcsimAnnotations.buttons.toList)
      }else window.disableAllButtons
      loadPropertiesForTreeItem(data)
    }

    private def handleClickProperties(data: Any): Unit = window.help.setText(data.toString)

    private def getChilds(node: Any): scala.collection.immutable.List[(String, Any)] = node match {
      case childs: ArrayList[Any] =>
        childs.asScala.toList.map(child => (child.toString, child))
      case node =>
        node.getClass.getMethods.foldLeft(List[(String, Any)]())(
          (nodes, method)=>
            if (method.isAnnotationPresent(classOf[CRRCSimEditorNode]))
              nodes :+ getNameNodePair(method, node)
            else 
              nodes
        )
    }

    private def getNameNodePair(method: Method, parentNode: Any) = {
      val name = method.getAnnotation(classOf[CRRCSimEditorNode]).name
      val node = method.invoke(parentNode)
      (if (name == "Node") node.toString else name , node)
    }
}
