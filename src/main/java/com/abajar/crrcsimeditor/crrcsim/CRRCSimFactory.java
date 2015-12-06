/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.AVL;

/**
 *
 * @author Hugo
 */
public class CRRCSimFactory {
    public CRRCSim create(AVL avl){
        return new CRRCSim(avl);
    }

    public CRRCSim create(){
        return new CRRCSim();
    }
}