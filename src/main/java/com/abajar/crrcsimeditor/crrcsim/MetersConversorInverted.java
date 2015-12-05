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

import java.io.NotSerializableException;

/**
 *
 * @author Hugo
 */
public class MetersConversorInverted extends MetersConversor{

    public MetersConversorInverted(){
        super();
    }

    public MetersConversorInverted(MultiUnit multiUnit) {
        super(multiUnit);
    }

    @Override
    public Float convert(Float quantity) {
        return -super.convert(quantity);
    }
}