package com.abajar.crrcsimeditor
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS._
import com.abajar.crrcsimeditor.view.avl.SelectorMutableTreeNode.ENABLE_BUTTONS
import com.abajar.crrcsimeditor.avl.AVLGeometry
import com.abajar.crrcsimeditor.avl.geometry._
import com.abajar.crrcsimeditor.avl.mass._
import com.abajar.crrcsimeditor.crrcsim._

object TreeHelper{
  def createNode(button: ENABLE_BUTTONS, node: Any) = button match {
    case ADD_SURFACE => node.asInstanceOf[AVLGeometry].createSurface
    case ADD_BODY => node.asInstanceOf[AVLGeometry].createBody
    case ADD_SECTION => node.asInstanceOf[Surface].createSection
    case ADD_CONTROL => node.asInstanceOf[Section].createControl
    case ADD_MASS => node.asInstanceOf[MassObject].createMass
    case ADD_CHANGELOG => node.asInstanceOf[CRRCSim].createChange
    //case ADD_CONFIG => node.asInstanceOf[CRRCsim].createConfig
    //case ADD_SOUND => node.asInstanceOf[Config].createSound
    case ADD_BATTERY => node.asInstanceOf[Power].createBattery
    case ADD_SHAFT => node.asInstanceOf[Battery].createShaft
    case ADD_ENGINE => node.asInstanceOf[Shaft].createEngine
    case ADD_DATA => node.asInstanceOf[Engine].createData 
    case ADD_DATA_IDLE => node.asInstanceOf[Engine].createDataIdle
    case ADD_SYMPLE_TRUST => node.asInstanceOf[Shaft].createSimpleTrust
    case ADD_COLLISION_POINT => node.asInstanceOf[CRRCSim].createWheel
    case DELETE => throw new Exception("TODO")
    case _ => throw new Exception(s"Button ${button} not defined")
  }
}
