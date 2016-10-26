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

        //Small smaple corpus
        String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/sample/";
        //Big sample corpus
        //String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/corpusBig/";

        /**
         * Execution:
         * crd - Corpus Reader to iterate through the directory of documents
         * dps - List of Document Processors
         * idx - Boolean Indexer
         */
        URI uri = URI.create(path);
        CorpusReader crd = new CorpusReader(uri);
        List<DocumentProcessor> dps = crd.getDocumentProcessors();

        Indexer idx = new Indexer();
        /**
         * Iterate through the Document Processors to
         * Process, tokenize and index each document
         */
        for (DocumentProcessor dp : dps) {

            List<Document> doc = dp.process();

            for (Document d: doc) {
                Tokenizer tokenizer = new Tokenizer(d.getDataStream());
                for (String token : tokenizer.getTokens()) {
                    idx.index(d, token);
                }

            }
        }

        idx.getBooleanIndex();
        /* Examples of index operations */
        /*
        //Print full index
        idx.printIndex();

        //Examples of boolean index query
        System.out.println("Contains the term ammonia? " + (idx.contains("ammonia")? "yes":"no"));
        System.out.println("Contains the term raquel? " + (idx.contains("raquel")? "yes":"no"));

        System.out.println("Contains the term effect in the document with id 20308399?" + (idx.contains("effect",20308399)? "yes":"no"));
        System.out.println("Contains the term effect in the document with id 210308399?" + (idx.contains("effect",210308399)? "yes":"no"));
        //Example to get document frequency for a term
        System.out.println("Document frequency for the term \"ammonia\":" + idx.getDocFrequency("ammonia"));
        */
    }
}
