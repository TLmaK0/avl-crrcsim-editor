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

import com.abajar.crrcsimeditor.UnitConversor;

/**
 *
 * @author Hugo
 */
public class ZRelativeToCG extends UnitConversorTypeAdapter{
    private UnitConversor unitConversor = new UnitConversor();

    public ZRelativeToCG(){
        super();
    }
    
    public ZRelativeToCG(String unit, Float origin){
        super(unit, origin);
    }

    @Override
    public Float unmarshal(Float v) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}