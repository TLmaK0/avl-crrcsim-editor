/*
 * Copyright (C) 2015  Hugo Freire Gil 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.avl;

import com.abajar.crrcsimeditor.avl.geometry.Body;
import com.abajar.crrcsimeditor.avl.geometry.Section;
import com.abajar.crrcsimeditor.avl.geometry.Surface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Hugo
 */
public class AVLS {
    public static void avlToFile(AVL avl, Path file, Path origin) throws IOException {
        FileOutputStream fos = new FileOutputStream(file.toFile());
        avl.getGeometry().writeAVLData(fos);
        fos.close();

        String fileMassPath = file.toString().replace(".avl", ".mass");
        File fileMass = new File(fileMassPath);
        fos = new FileOutputStream(fileMass);
        avl.writeAVLMassData(fos);
        fos.close();

        Path dest = file.getParent();

        for(Body body : avl.getGeometry().getBodies()){
            if (!body.getBFILE().isEmpty() && !Files.exists(dest.resolve(body.getBFILE()))) Files.copy(origin.resolve(body.getBFILE()), dest.resolve(body.getBFILE()));
        }

        for(Surface surface : avl.getGeometry().getSurfaces()){
            for(Section section : surface.getSections()){

                if (!section.getAFILE().isEmpty() && !Files.exists(dest.resolve(section.getAFILE()))) Files.copy(origin.resolve(section.getAFILE()), dest.resolve(section.getAFILE()));
            }
        }
    }
}