/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author Hugo
 */
public class AVLS {
    public static void avlToFile(AVL avl, Path file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file.toFile());
        avl.getGeometry().writeAVLData(fos);
        fos.close();

        String fileMassPath = file.toString().replace(".avl", ".mass");
        File fileMass = new File(fileMassPath);
        fos = new FileOutputStream(fileMass);
        avl.getGeometry().writeAVLMassData(fos);
        fos.close();
    }
}
