/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.connectivity;

import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.runcase.Configuration;
import com.abajar.crrcsimeditor.avl.runcase.AvlCalculation;
import com.abajar.crrcsimeditor.avl.runcase.StabilityDerivatives;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hfreire
 */
public class AvlRunner {
        OutputStream stdin;
        InputStream stderr;
        InputStream stdout;
        Process process;
        final String avlPath;
        final String avlFileName;
        final String executionPath;
        final float VELOCITY = 30; // 30m/s
    final static Logger logger = Logger.getLogger(AvlRunner.class.getName());

    public AvlRunner(String avlPath, String executionPath, String fileName) throws IOException{
        this.avlPath = avlPath;
        this.avlFileName = fileName;
        this.executionPath = executionPath;
        ProcessBuilder pb = new ProcessBuilder(avlPath, this.avlFileName);
        pb.directory(new File(executionPath).getAbsoluteFile());
        pb.redirectErrorStream(true);

        process = pb.start();
        stdin = process.getOutputStream ();
        stdout = process.getInputStream ();
    }

    public AvlCalculation getCalculation(int elevatorPosition, int rudderPosition, int aileronPosition) throws IOException, InterruptedException{
        String resultFile = this.avlFileName.replace(".avl", ".st");
        new File(this.executionPath + "/" + resultFile).delete();

        String runFile = this.avlFileName.replace(".avl", ".run");
        new File(this.executionPath + "/" + runFile).delete();

        sendCommand("oper\n");
        sendCommand("g\n\n");

        //setting pitch moment 0
        if (elevatorPosition != -1) sendCommand("d" + elevatorPosition + " pm 0");
        
        //setting velocity
        sendCommand("c1\n");
        sendCommand("v\n");
        sendCommand(VELOCITY + "\n\n");        //setting velocity
        sendCommand("s\n\n");

        //execute run case
        sendCommand("x\n");

        sendCommand("st\n");
        sendCommand(resultFile + "\n");
        sendCommand("\nq\n");
        flush();
        process.waitFor();

        InputStream fis = new FileInputStream(new File(this.executionPath + "/" +  resultFile));
        Scanner scanner = new Scanner(fis);

        AvlCalculation runCase = new AvlCalculation();
        Configuration config = runCase.getConfiguration();

        config.setVelocity(VELOCITY);

        config.setSref(readFloat("Sref =", scanner));
        config.setCref(readFloat("Cref =", scanner));
        config.setBref(readFloat("Bref =", scanner));

        config.setAlpha(readFloat("Alpha =", scanner));

        config.setCmtot(readFloat("Cmtot =", scanner));
        config.setCLtot(readFloat("CLtot =", scanner));
        config.setCDvis(readFloat("CDvis =", scanner));

        StabilityDerivatives std = runCase.getStabilityDerivatives();
        std.setCLa(readFloat("CLa = ", scanner));
        std.setCYb(readFloat("CYb = ", scanner));
        std.setClb(readFloat("Clb = ", scanner));
        std.setCma(readFloat("Cma = ", scanner));
        std.setCnb(readFloat("Cnb = ", scanner));
        std.setCLq(readFloat("CLq = ", scanner));
        std.setCYp(readFloat("CYp = ", scanner));
        std.setCYr(readFloat("CYr = ", scanner));
        std.setClp(readFloat("Clp = ", scanner));
        std.setClr(readFloat("Clr = ", scanner));
        std.setCmq(readFloat("Cmq = ", scanner));
        std.setCnp(readFloat("Cnp = ", scanner));
        std.setCnr(readFloat("Cnr = ", scanner));

        boolean check1 = (elevatorPosition == 0 || rudderPosition == 0 || aileronPosition == 0);
        boolean check2 = (elevatorPosition == 1 || rudderPosition == 1 || aileronPosition == 1);
        boolean check3 = (elevatorPosition == 2 || rudderPosition == 2 || aileronPosition == 2);
        
        if (check1) std.getCLd()[0] = readFloat("CLd1 =", scanner);
        if (check2) std.getCLd()[1] = readFloat("CLd2 =", scanner);
        if (check3) std.getCLd()[2] = readFloat("CLd3 =", scanner);

        if (check1) std.getCYd()[0] = readFloat("CYd1 =", scanner);
        if (check2) std.getCYd()[1] = readFloat("CYd2 =", scanner);
        if (check3) std.getCYd()[2] = readFloat("CYd3 =", scanner);

        if (check1) std.getCld()[0] = readFloat("Cld1 =", scanner);
        if (check2) std.getCld()[1] = readFloat("Cld2 =", scanner);
        if (check3) std.getCld()[2] = readFloat("Cld3 =", scanner);

        if (check1) std.getCmd()[0] = readFloat("Cmd1 =", scanner);
        if (check2) std.getCmd()[1] = readFloat("Cmd2 =", scanner);
        if (check3) std.getCmd()[2] = readFloat("Cmd3 =", scanner);

        if (check1) std.getCnd()[0] = readFloat("Cnd1 =", scanner);
        if (check2) std.getCnd()[1] = readFloat("Cnd2 =", scanner);
        if (check3) std.getCnd()[2] = readFloat("Cnd3 =", scanner);

        scanner.close();
        return runCase;
    }


    private void sendCommand(String command) throws IOException{
        stdin.write(command.getBytes());
        stdin.flush();
        logger.log(Level.FINE, "Sending command: {0}", command);
    }

    private void flush() throws IOException{
        String line;
         BufferedReader brCleanUp =  new BufferedReader (new InputStreamReader (stdout));
            while ((line = brCleanUp.readLine ()) != null) {
                logger.log(Level.FINE, "[AVL out]{0}", line);
            }

        brCleanUp.close();
    }

    private Float readFloat(String pattern, Scanner scanner){
        scanner.findWithinHorizon(pattern, 0);
        String value = scanner.next();
        Float realValue = Float.parseFloat(value);
        logger.log(Level.FINE, "{0} {1}", new Object[]{pattern, realValue});
        return realValue;
    }
}
