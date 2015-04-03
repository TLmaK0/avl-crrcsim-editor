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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import swt.MainWindow
import org.eclipse.swt.widgets.{Event, TreeItem}
import org.eclipse.swt.events._;
import com.abajar.crrcsimeditor.view.annotations.CRRCSimEditorNode
import java.lang.reflect.Method
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS
import com.abajar.crrcsimeditor.swt.MenuOption._

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
        handleClickMenu
      )
    
    window.show

    updateEnabledEditExportAsCRRCsimMenuItem

    private def handleClickButton(button: ENABLE_BUTTONS) = {
      println(button)
    }

    private def handleClickMenu(menuOption: MenuOption): Unit = menuOption match {
      case SaveAs => saveFile
      case Open => openFile
      case ExportAsAvl => exportAsAVL
      case ExportAsCRRCSim => exportAsCRRCsim
      case SetAvlExecutable => setAvlExecutable
    }

    def exportAsAVL(avlFile: Path): Unit = {
      AVLS.avlToFile(this.crrcsim.getAvl(), avlFile, avlFile.getParent());
    }

    def saveAs(file: File) = {
      new CRRCSimRepository().storeToFile(file, crrcsim);
    }

    def open(file: File) = {
      crrcsim = new CRRCSimRepository().restoreFromFile(file);
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
      exportAsCRRCsim(file)
    }

    private def exportAsCRRCsim(file: File): Unit = {
      this.crrcsim.calculate(this.configuration.getProperty("avl.path"), this.crrcsim.getOriginPath())
      val avl = this.crrcsim.getAvl()

      val fos = new FileOutputStream(file)
      val context = JAXBContext.newInstance(classOf[CRRCSim].getName)
      val m = context.createMarshaller()
      m.setAdapter(new XRelativeToCG(avl.getLengthUnit(), this.crrcsim.getCenterOfMass().getX()))
      m.setAdapter(new YRelativeToCG(avl.getLengthUnit(), this.crrcsim.getCenterOfMass().getY()))
      m.setAdapter(new ZRelativeToCG(avl.getLengthUnit(), this.crrcsim.getCenterOfMass().getZ()))
      m.setAdapter(new XRelativeToCGInverted(avl.getLengthUnit(), this.crrcsim.getCenterOfMass().getX()))
      m.setAdapter(new YRelativeToCGInverted(avl.getLengthUnit(), this.crrcsim.getCenterOfMass().getY()))
      m.setAdapter(new ZRelativeToCGInverted(avl.getLengthUnit(), this.crrcsim.getCenterOfMass().getZ()))
      m.setAdapter(new MetersConversor(new MultiUnit(avl.getLengthUnit(), avl.getMassUnit(), avl.getTimeUnit())))
      m.setAdapter(new MetersConversorInverted(new MultiUnit(avl.getLengthUnit(), avl.getMassUnit(), avl.getTimeUnit())))
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
      m.marshal(this.crrcsim, fos)
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
            updateEnabledEditExportAsCRRCsimMenuItem
            try {
                this.configuration.storeToXML(new FileOutputStream(CONFIGURATION_PATH), null)
            } catch { 
              case ex: Exception =>
                logger.log(Level.FINE, null, ex)
            }
          case None =>
      }
    }

    private def updateEnabledEditExportAsCRRCsimMenuItem = {
      //TODO: Enable menu option export to CRRCSim
      throw new Exception("TODO")
    }

    private def showSaveDialog(path: String, description: String, extensions: String): File = {
      //TODO: save dialog
      throw new Exception("TODO")
    }

    
    private def treeSourceHandler(event: Event) = {
      val item = event.item.asInstanceOf[TreeItem]

      val parentItem = item.getParentItem

      val node = if (parentItem == null) 
        (crrcsim.toString, crrcsim)
      else
        getChilds(parentItem.getData)(event.index)

      val childs = getChilds(node._2)

      item.setData(node._2)
      item.setText(node._1)
      item.setItemCount(childs.length)
    }

    private def handleTreeEvent(data: Any): Unit = {
      val objClass = data.getClass
      if (objClass.isAnnotationPresent(classOf[com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor])) {
        val crrcsimAnnotations = objClass.getAnnotation(classOf[com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor]).asInstanceOf[com.abajar.crrcsimeditor.view.annotations.CRRCSimEditor]
        window.buttonsEnableOnly(crrcsimAnnotations.buttons.toList)
      }else window.disableAllButtons
    }

    private def getChilds(node: Any): scala.collection.immutable.List[(String, Any)] = node match {
      case childs: List[(String, Any)] =>
        childs
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
