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

import com.abajar.avleditor.avl.geometry.Body;
import com.abajar.avleditor.avl.geometry.BodyProfilePoint;
import com.abajar.avleditor.avl.geometry.Section;
import com.abajar.avleditor.avl.geometry.Surface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

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
            String bfile = body.getEffectiveBFILE();
            Path bfilePath = dest.resolve(bfile);

            if (!body.getProfilePoints().isEmpty()) {
                // Generate BFILE from profile points
                writeBfileFromProfilePoints(body, bfilePath);
            } else {
                // Fall back to copying existing BFILE if it exists
                String originalBfile = body.getBFILE();
                if (originalBfile != null && !originalBfile.isEmpty() && !Files.exists(bfilePath)) {
                    Path originBfile = origin.resolve(originalBfile);
                    if (Files.exists(originBfile)) {
                        Files.copy(originBfile, bfilePath);
                    }
                }
            }
        }

        for(Surface surface : avl.getGeometry().getSurfaces()){
            for(Section section : surface.getSections()){

                if (!section.getAFILE().isEmpty() && !Files.exists(dest.resolve(section.getAFILE()))) Files.copy(origin.resolve(section.getAFILE()), dest.resolve(section.getAFILE()));
            }
        }
    }

    /**
     * Write body profile points to a BFILE format file.
     * AVL expects airfoil-style format where the diameter = Y_upper - Y_lower.
     * For a round body, we write upper surface (+radius) and lower surface (-radius).
     */
    private static void writeBfileFromProfilePoints(Body body, Path bfilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(bfilePath.toFile());
        PrintStream ps = new PrintStream(fos);

        Locale locale = Locale.US;
        float length = body.getLength();
        java.util.List<BodyProfilePoint> points = body.getProfilePoints();

        if (points.isEmpty()) {
            ps.close();
            fos.close();
            return;
        }

        // Write body name as header (airfoil format convention)
        ps.println(body.getName());

        // Upper surface: from tail (x=max) to nose (x=0), with +radius
        for (int i = points.size() - 1; i >= 0; i--) {
            BodyProfilePoint point = points.get(i);
            float absoluteX = point.getX() * length;
            float y = point.getRadius();  // Upper surface = +radius
            ps.printf(locale, "%10.6f  %10.6f%n", absoluteX, y);
        }

        // Lower surface: from nose (x=0) to tail (x=max), with -radius
        // Skip the first point to avoid duplicating the nose point
        for (int i = 1; i < points.size(); i++) {
            BodyProfilePoint point = points.get(i);
            float absoluteX = point.getX() * length;
            float y = -point.getRadius();  // Lower surface = -radius
            ps.printf(locale, "%10.6f  %10.6f%n", absoluteX, y);
        }

        ps.close();
        fos.close();
    }
}