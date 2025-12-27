/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.avleditor.avl;

import java.io.OutputStream;
import java.io.Serializable;

/**
 *
 * @author hfreire
 */
public interface AVLSerializable extends  Serializable{
    public void writeAVLData(OutputStream out);

    public void writeAVLMassData(OutputStream out);
}