/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.avleditor.view.avl;

import com.abajar.avleditor.crrcsim.Shaft;
import com.abajar.avleditor.crrcsim.Power;
import com.abajar.avleditor.view.annotations.AvlEditorNode;
import com.abajar.avleditor.avl.AVLGeometry;
import com.abajar.avleditor.avl.geometry.Section;
import com.abajar.avleditor.avl.geometry.Surface;
import com.abajar.avleditor.crrcsim.CRRCSim;
import com.abajar.avleditor.avl.mass.MassObject;
import com.abajar.avleditor.avl.mass.Mass;
import com.abajar.avleditor.crrcsim.Battery;
import com.abajar.avleditor.crrcsim.Engine;
import java.util.ArrayList;

interface TreeModificator{
  void modify(Object node, Object parent);
}

class AddSurface implements TreeModificator{
  public void modify(Object node, Object parent){
    ((AVLGeometry)node).createSurface();
  }
}

class AddBody implements TreeModificator{
  public void modify(Object node, Object parent){
    ((AVLGeometry)node).createBody();
  }
}

class AddSection implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Surface)node).createSection();
  }
}

class AddControl implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Section)node).createControl();
  }
}

class AddMass implements TreeModificator{
  public void modify(Object node, Object parent){
    ((MassObject)node).createMass();
  }
}

class AddChangeLog implements TreeModificator{
  public void modify(Object node, Object parent){
    ((CRRCSim)node).createChange();
  }
}

//class AddConfig implements TreeModificator{
//  public void modify(Object node, Object parent){
//    ((CRRCSim)node).createConfig();
//  }
//}
//
//class AddSound implements TreeModificator{
//  public void modify(Object node, Object parent){
//    ((Config)node).createSound();
//  }
//}
//
class AddBattery implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Power)node).createBattery();
  }
}

class AddShaft implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Battery)node).createShaft();
  }
}

class AddEngine implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Shaft)node).createEngine();
  }
}

class AddData implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Engine)node).createData();
  }
}

class AddDataIdle implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Engine)node).createDataIdle();
  }
}

class AddSimpleTrust implements TreeModificator{
  public void modify(Object node, Object parent){
    ((Shaft)node).createSimpleTrust();
  }
}

class AddCollisionPoint implements TreeModificator{
  public void modify(Object node, Object parent){
    ((CRRCSim)node).createWheel();
  }
}

class Delete implements TreeModificator{
  public void modify(Object node, Object parent){
    ((ArrayList)parent).remove(node);
  }
}


