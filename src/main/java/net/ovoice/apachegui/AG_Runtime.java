package net.ovoice.apachegui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by peter on 29.07.16.
 */
public class AG_Runtime {
    public static void main(String[] args){

    }

    public AG_Runtime() {

    }

    public String exec(String[] agCmd) {
        Runtime rt = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = rt.exec(agCmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader stdInput = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(proc.getErrorStream()));

        // read the output from the command
        String s = null;
        String r = null;
        try {
            while ((s = stdInput.readLine()) != null) {
                r += s + '\n';
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

// read any errors from the attempted command
        try {
            while ((s = stdError.readLine()) != null) {
                r += s + '\n';
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }
}
