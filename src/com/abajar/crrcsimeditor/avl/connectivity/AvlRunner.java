/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.abajar.crrcsimeditor.avl.connectivity;

import com.abajar.crrcsimeditor.avl.runcase.Configuration;
import com.abajar.crrcsimeditor.avl.runcase.RunCase;
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
        String avlPath;
        String avlFileName;
        final float VELOCITY = 30; // 30m/s
    final static Logger logger = Logger.getLogger(AvlRunner.class.getName());

    public AvlRunner(String avlPath, String path, String fileName) throws IOException{
        this.avlPath = avlPath;
        this.avlFileName = fileName;
        ProcessBuilder pb = new ProcessBuilder(avlPath, this.avlFileName);
        pb.directory(new File(path).getAbsoluteFile());
        pb.redirectErrorStream(true);

        process = pb.start();
        stdin = process.getOutputStream ();
        stdout = process.getInputStream ();
    }

    public void calculate() throws IOException{
        sendCommand("oper\n");
        sendCommand("c1\n");
        sendCommand("v\n");
        sendCommand(VELOCITY + "\n\n");        //setting velocity
        sendCommand("s\n\n");
        sendCommand("x\n");
    }

    public RunCase getRunCase() throws IOException{
        String resultFile = this.avlFileName.replace(".avl", ".st");
        sendCommand("st\n");
        sendCommand(resultFile + "\n");

        InputStream fis = new FileInputStream(new File(resultFile));
        Scanner scanner = new Scanner(fis);

        RunCase runCase = new RunCase();
        runCase.getConfiguration().setVelocity(VELOCITY);

        runCase.getConfiguration().setSref(readFloat("Sref =", scanner));
        runCase.getConfiguration().setCref(readFloat("Cref =", scanner));
        runCase.getConfiguration().setBref(readFloat("Bref =", scanner));

        runCase.getConfiguration().setAlpha(readFloat("Alpha =", scanner));

        runCase.getConfiguration().setCmtot(readFloat("Cmtot =", scanner));
        runCase.getConfiguration().setCLtot(readFloat("CLtot =", scanner));
        runCase.getConfiguration().setCDvis(readFloat("CDvis =", scanner));

        runCase.getStabilityDerivatives().setCLa(readFloat("CLa = ", scanner));
        runCase.getStabilityDerivatives().setCYb(readFloat("CYb = ", scanner));
        runCase.getStabilityDerivatives().setClb(readFloat("Clb = ", scanner));
        runCase.getStabilityDerivatives().setCma(readFloat("Cma = ", scanner));
        runCase.getStabilityDerivatives().setCnb(readFloat("Cnb = ", scanner));
        runCase.getStabilityDerivatives().setCLq(readFloat("CLq = ", scanner));
        runCase.getStabilityDerivatives().setCYp(readFloat("CYp = ", scanner));
        runCase.getStabilityDerivatives().setCYr(readFloat("CYr = ", scanner));
        runCase.getStabilityDerivatives().setClp(readFloat("Clp = ", scanner));
        runCase.getStabilityDerivatives().setClr(readFloat("Clr = ", scanner));
        runCase.getStabilityDerivatives().setCmq(readFloat("Cmq = ", scanner));
        runCase.getStabilityDerivatives().setCnp(readFloat("Cnp = ", scanner));
        runCase.getStabilityDerivatives().setCnr(readFloat("Cnr = ", scanner));

        runCase.getStabilityDerivatives().getCLd()[0] = readFloat("CLd1 =", scanner);
        runCase.getStabilityDerivatives().getCLd()[1] = readFloat("CLd2 =", scanner);
        runCase.getStabilityDerivatives().getCLd()[2] = readFloat("CLd3 =", scanner);

        runCase.getStabilityDerivatives().getCYd()[0] = readFloat("CYd1 =", scanner);
        runCase.getStabilityDerivatives().getCYd()[1] = readFloat("CYd2 =", scanner);
        runCase.getStabilityDerivatives().getCYd()[2] = readFloat("CYd3 =", scanner);

        runCase.getStabilityDerivatives().getCld()[0] = readFloat("Cld1 =", scanner);
        runCase.getStabilityDerivatives().getCld()[1] = readFloat("Cld2 =", scanner);
        runCase.getStabilityDerivatives().getCld()[2] = readFloat("Cld3 =", scanner);

        runCase.getStabilityDerivatives().getCmd()[0] = readFloat("Cmd1 =", scanner);
        runCase.getStabilityDerivatives().getCmd()[1] = readFloat("Cmd2 =", scanner);
        runCase.getStabilityDerivatives().getCmd()[2] = readFloat("Cmd3 =", scanner);

        runCase.getStabilityDerivatives().getCnd()[0] = readFloat("Cnd1 =", scanner);
        runCase.getStabilityDerivatives().getCnd()[1] = readFloat("Cnd2 =", scanner);
        runCase.getStabilityDerivatives().getCnd()[2] = readFloat("Cnd3 =", scanner);

        scanner.close();
        return runCase;
    }

    public void close() throws InterruptedException, IOException{
        sendCommand("\nq\n");
       flush();
        process.waitFor();
    }

    private void sendCommand(String command) throws IOException{
        stdin.write(command.getBytes());
        stdin.flush();
    }

    private void flush() throws IOException{
        String line;
         BufferedReader brCleanUp =  new BufferedReader (new InputStreamReader (stdout));
            while ((line = brCleanUp.readLine ()) != null) {
                //logger.log(Level.INFO, "[AVL out] {0}", line);
                System.out.println("[AVL out]" + line);
            }

        //brCleanUp.close();
    }

    private Float readFloat(String pattern, Scanner scanner){
        scanner.findWithinHorizon(pattern, 0);
        logger.log(Level.INFO, "Search: {0}", pattern);
        String value = scanner.next();
        logger.log(Level.INFO, "Found: {0}", value);
        Float realValue = Float.parseFloat(value);
        logger.log(Level.INFO, "Float: {0}", realValue);
        return realValue;
    }
}
