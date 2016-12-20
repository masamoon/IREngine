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

        //TreeMultimap<Integer,Double> weights = TreeMultimap.create();
        TreeMap<Double,Integer> weights = new TreeMap<>(Collections.reverseOrder());
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
                if(term.contains(stemmedQ)) {
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
                        //weights.put(Integer.parseInt(docId),weight);
                        weights.put(weight,Integer.parseInt(docId));

                    }
                }

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("Search results for: "+query);



        BufferedReader meta = null;
        try {
            meta = new BufferedReader(new FileReader("resources/output/metadata.idx"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        String metaLine = "";
        String title = "";
        String did = "";
        int count = 0;
        for(Double w : weights.keySet()){
            ArrayList<Integer> toExclude = new ArrayList<Integer>();
            try {
                meta = new BufferedReader(new FileReader("resources/output/metadata.idx"));
            }catch(IOException e){
                e.printStackTrace();
            }

            if(count < 10) {
                try {
                    while ((metaLine = meta.readLine()) != null) {
                        // title = metaLine.substring(0,metaLine.indexOf(":"));
                        String metalineres[] = metaLine.split(":");
                        String parentIds[] = metaLine.split("-");
                        String parentId = "";
                        if(parentIds.length > 1){
                            parentId = parentIds[1];
                        }
                        if (metalineres.length > 1)
                            did = metalineres[1];
                        else
                            did = "0";
                        title = metalineres[0];
                        // did = metaLine.substring(metaLine.indexOf(":"),metaLine.length()-1);

                        //System.out.println(did);
                        int intdid = 0;
                        try {
                            intdid = Integer.parseInt(did);
                        } catch (NumberFormatException e) {
                            intdid = 0;
                        }
                        //System.out.println(intdid +" ## "+weights.get(w));
                        if (intdid == weights.get(w)) {
                            System.out.println("title: " + title + " | docid: " + intdid+"| weight: "+w);
                            toExclude.add(intdid);
                        }
                        //System.out.println(parentId);
                       try {
                            if (Integer.parseInt(parentId) == weights.get(w)) {
                                System.out.println("Related Answer #### title: " + title + " | docid: " + weights.get(w) + "| weight: " + w);
                          //      System.out.println(parentId);
                            }
                        }catch(NumberFormatException e){

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(!toExclude.contains(weights.get(w)))
                    System.out.println(weights.get(w) + " : " + w);
            }
            else{
                break;
            }

            count++;
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(String.format("Duration: %.4f sec\n", (float) duration / 1000000000));
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
