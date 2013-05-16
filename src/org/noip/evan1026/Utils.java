package org.noip.evan1026;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static ArrayList<String> readFile(String path){

        ArrayList<String> output = new ArrayList<String>();
        BufferedReader    br     = null;
        File              file   = new File(path);
        
        if(!file.exists()){
            return output;
        }
        
        try {

            String line;

            br = new BufferedReader(new FileReader(path));

            while ((line = br.readLine()) != null){
                output.add(line);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                if (br != null){
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        return output;
    }

    public static void writeFile(String path, List<String> contents) throws IOException{

        /*File file = new File(path);
        file.delete();
        file.createNewFile();*/
        
        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String line : contents){
            bw.write(line + "\n");
        }

        bw.close();
    }
}
