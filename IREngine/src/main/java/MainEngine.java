/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import java.net.URI;
import java.util.List;

/**
 * Main Program for Information Retrieval Engine
 */
public class MainEngine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Runtime: uncomment the line below */
        long startTime = System.nanoTime();
        //Small smaple corpus
        //String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/corpus/Questions.csv";
        //Big sample corpus
      //  String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/corpusBig/";

        String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/sample/";

        /**
         * Execution:
         * crd - Corpus Reader to iterate through the directory of documents
         * dps - List of Doc Processors
         * idx - Boolean Indexer
         */
        URI uri = URI.create(path);
        CorpusReader crd = new CorpusReader(uri);
        List<Doc> dsp = crd.getProcessedDocuments();

        Indexer idx = new Indexer();
        Tokenizer tokenizer = new Tokenizer();

         /**
         * Iterate through the Doc Processors to
         * Process, tokenize and index each document
         */
        for (Doc d : dsp) {
            tokenizer.tokenize(d);
        }
        idx.index(tokenizer.getTokens());
        //tokenizer.printTokens();
        idx.getBooleanIndex();
        idx.tfIdfIndex(tokenizer.getNumTokens());
        idx.load();
      //  idx.printTfIdIndex();

        /* Examples of index operations */


        //Print full index
        //idx.printIndex();
/*
        //Examples of boolean index query
        System.out.println("Contains the term ammonia? " + (idx.contains("ammonia")? "yes":"no"));
        System.out.println("Contains the term raquel? " + (idx.contains("raquel")? "yes":"no"));

        System.out.println("Contains the term effect in the document with id 20308399?" + (idx.contains("effect",20308399)? "yes":"no"));
        System.out.println("Contains the term effect in the document with id 210308399?" + (idx.contains("effect",210308399)? "yes":"no"));
        //Example to get document frequency for a term
        System.out.println("Doc frequency for the term \"ammonia\":" + idx.getDocFrequency("ammonia"));
*/

        /* Runtime: uncomment the lines below */
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(String.format("Duration: %.4f sec\n", (float) duration / 1000000000));

    }
}
