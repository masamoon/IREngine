package utils;

import java.io.*;

/**
 * Created by Andre on 20/12/2016.
 */
public class ReadFileScript {
    public static void main (String[] args){
        String line = "";
        int i = 0;

        try {
            BufferedReader in =  new BufferedReader(new FileReader(new File("resources/corpusBig/Tags.csv")));
            while ((line = in.readLine()) != null) {
                i++;
                System.out.println(line);
                if(i>100){
                    break;
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }


    }

    }

