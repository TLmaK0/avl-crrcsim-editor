/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Hugo
 */
public class CRRCSimRepository {
    static final Logger logger = Logger.getLogger(CRRCSimRepository.class.getName());

    public CRRCSim restoreFromFile(File file) throws FileNotFoundException{
        FileInputStream data = new FileInputStream(file);

        CRRCSim crrcsim = null;
        Unmarshaller u;
        try {
            JAXBContext context;
            int version = this.getVersion(data);

            logger.log(Level.INFO, "Loading file version {0}", version);
            switch(version){
                case 10:
                    context = JAXBContext.newInstance(AVLGeometry.class);
                    u = context.createUnmarshaller();
                    AVL avl = new AVL();
                    avl.setGeometry((AVLGeometry)u.unmarshal(data));
                    crrcsim = new CRRCSimFactory().create(avl);
                    break;
                case 13:
                    context = JAXBContext.newInstance(CRRCSim.class);
                    u = context.createUnmarshaller();
                    crrcsim = (CRRCSim)u.unmarshal(data);
                    break;
                default:
                    ObjectInput input = new ObjectInputStream (data);
                    crrcsim = (CRRCSim)input.readObject();
                    break;
            }
        } catch (Exception ex) {
ex.printStackTrace();
            logger.log(Level.SEVERE, null, ex);
        }
        return crrcsim;
    }

    public void storeToFile(File file, CRRCSim crrcsim){
        OutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(fileStream);
            output.writeObject(crrcsim);
            output.close();
        } catch (IOException ex) {
ex.printStackTrace();
            Logger.getLogger(CRRCSimRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileStream.close();
            } catch (IOException ex) {
ex.printStackTrace();
                Logger.getLogger(CRRCSimRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int getVersion(FileInputStream xml) throws UnsupportedEncodingException, IOException{
        FileChannel fc = xml.getChannel();
        char[] buf = new char[1000];
        Reader r = new InputStreamReader(xml, "UTF-8");
        StringBuilder s = new StringBuilder();
        int n = r.read(buf);
        s.append(buf, 0, n);
        fc.position(0);
        int version = 15;
        if (s.toString().contains("<crrcSim")) version = 13;
        else if (s.toString().contains("<avlGeometry>")) version = 10;
        return version;
    }

    
}
