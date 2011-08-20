/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.connectivity;

import com.abajar.crrcsimeditor.avl.StabilityDerivatives;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author hfreire
 */
public class AvlRunner {
        OutputStream stdin;
        InputStream stderr;
        InputStream stdout;
        Process process;
        Scanner scanner;

    public AvlRunner(String avlPath, String avlFile) throws IOException{
        ProcessBuilder pb = new ProcessBuilder(avlPath, avlFile);
        pb.redirectErrorStream(true);

        process = pb.start();
        stdin = process.getOutputStream ();
        stdout = process.getInputStream ();
        scanner = new Scanner(stdout);
    }

    public void calculate() throws IOException{
        sendCommand("oper\nx\n");
    }

    public StabilityDerivatives getStabilityDerivatives() throws IOException{
        sendCommand("st\n");
        StabilityDerivatives st = new StabilityDerivatives();

        st.setSref(readFloat("Sref ="));
        st.setCref(readFloat("Cref ="));
        st.setBref(readFloat("Bref ="));


        return st;
    }

    public void close() throws InterruptedException, IOException{
        sendCommand("\n\nq\n");
       flush();
        process.waitFor();
    }

    private void sendCommand(String command) throws IOException{
System.out.print("\nSending: ");
System.out.println("-->" + command + "<--");
        stdin.write(command.getBytes());
        stdin.flush();
    }

    private void flush() throws IOException{
        String line;
         BufferedReader brCleanUp =  new BufferedReader (new InputStreamReader (stdout));
            while ((line = brCleanUp.readLine ()) != null) {
                System.out.println ("[Stdout] " + line);
            }

        //brCleanUp.close();
    }

    private Float readFloat(String pattern){
        scanner.findWithinHorizon(pattern, 0);
        return Float.parseFloat(scanner.next());
    }
}
