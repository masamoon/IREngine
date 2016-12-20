package search; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 *
 */

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import asg.cliche.example.HelloWorld;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * search.Searcher data type, which is reponsible for querying the index.
 */
public class Searcher {
    long i=0;
    long numfiles;
    /**
     * search.Searcher class constructor.
     */
    public Searcher() {

    }

    public static void main(String[] args){
        Searcher searcher = new Searcher();
        //searcher.query("software");
        try {
            ShellFactory.createConsoleShell("IREngine", "", new Searcher())
                    .commandLoop(); // and three.
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Search query operation.
     */
    @Command
    public void search(String query) {
        long startTime = System.nanoTime();

        TreeMultimap<Integer,Double> weights = TreeMultimap.create();

        String stemmedQ = stem(query);




        String line = null;
        //System.out.println(i*100/(numfiles)+"% completed");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("resources/output/"+query.charAt(0)+".idx"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            while((line = in.readLine()) != null){
                String term = line.substring(0,line.indexOf(":"));
                String rline = line.substring(line.indexOf(":"),line.length()-1);
    /*String docId = term.split("=")[0];
    Double weight = Double.parseDouble(docId.split("\\[")[0]);
    weights.put(weight,Integer.parseInt(docId));*/
                if(term.equals(stemmedQ)) {
                    // System.out.println(line);
                    String docId = rline.substring(1,rline.indexOf("="));

                    //System.out.print(docId[1]);
                    String[] allOccur = rline.split(";");
                    for(String str: allOccur){
                        String wL = str.substring(0,str.indexOf("="));
                        String tmp= str.substring(str.indexOf("=")+1,str.length());
                       // System.out.println(tmp);
                        String wR = tmp.substring(0,tmp.indexOf("["));
                        Double weight = Double.parseDouble(wR);
                        //System.out.println(weight);
                        weights.put(Integer.parseInt(docId),weight);

                    }



                }

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }




        System.out.println("Search results for: "+query);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(String.format("Duration: %.4f sec\n", (float) duration / 1000000000));
        for(Integer docid : weights.keySet()){
            System.out.println(docid+" : "+weights.get(docid));

        }
    }

    /**
     * Applies the stemming operation using the Porter Stemmer.
     */
    public String stem(String a) {
        englishStemmer stema = new englishStemmer();
        stema.setCurrent(a);
        stema.stem();
        return stema.getCurrent();
    }

}
