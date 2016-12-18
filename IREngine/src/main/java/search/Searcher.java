package search; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * search.Searcher data type, which is reponsible for querying the index.
 */
public class Searcher {

    /**
     * search.Searcher class constructor.
     */
    public Searcher(File index) {

    }

    /**
     * Search query operation.
     */
    public void query(String query) {

        Multimap<Double,Integer> weights = TreeMultimap.create();

        String stemmedQ = stem(query);
        String line = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(stemmedQ.charAt(0)+".idx"));
            while((line = in.readLine()) != null){
                String term = line.split(":")[0];
                String docId = term.split("=")[0];
                Double weight = Double.parseDouble(docId.split("\\[")[0]);
                weights.put(weight,Integer.parseInt(docId));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
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
