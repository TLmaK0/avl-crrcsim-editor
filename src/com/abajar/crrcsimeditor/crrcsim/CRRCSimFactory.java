/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.AVLGeometry;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class CRRCSimFactory {
    static final Logger logger = Logger.getLogger(CRRCSimFactory.class.getName());

    public CRRCSim createFromXml(FileInputStream xml){
        CRRCSim crrcsim = null;
        Unmarshaller u;
        try {
            JAXBContext context;
            int version = this.getVersion(xml);

            switch(version){
                case 10:
                    context = JAXBContext.newInstance(AVLGeometry.class);
                    u = context.createUnmarshaller();
                    AVL avl = new AVL();
                    avl.setGeometry((AVLGeometry)u.unmarshal(xml));
                    crrcsim = this.create(avl);
                    break;
                default:
                    context = JAXBContext.newInstance(CRRCSim.class);
                    u = context.createUnmarshaller();
                    crrcsim = (CRRCSim)u.unmarshal(xml);
                    break;
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return crrcsim;
    }

    private int getVersion(FileInputStream xml) throws UnsupportedEncodingException, IOException{
        FileChannel fc = xml.getChannel();
        char[] buf = new char[1000];
        Reader r = new InputStreamReader(xml, "UTF-8");
        StringBuilder s = new StringBuilder();
        int n = r.read(buf);
        s.append(buf, 0, n);
        fc.position(0);
        int version = 10;
        if (s.toString().contains("<crrcSim")) version = 13;
        return version;
    }

    private CRRCSim create(AVL avl){
        CRRCSim crrcsim = new CRRCSim();
        crrcsim.setAvl(avl);
        return crrcsim;
    }

    public CRRCSim create() {
        CRRCSim crrcsim = new CRRCSim();
        crrcsim.setAvl(new AVL());
        return crrcsim;
    }
}
