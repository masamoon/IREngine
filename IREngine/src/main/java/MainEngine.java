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
        long startTime = System.nanoTime();
        String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/corpusBig/";

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
                  //    System.out.println(d.getDataStream());
                //System.out.println(doc.getDataStream());
                Tokenizer tokenizer = new Tokenizer(d.getDataStream());
                //System.out.println(doc.getUri() + " - " + doc.getId());
                for (String token : tokenizer.getTokens()) {
                    idx.index(d, token);
                }
            }
        }

        //idx.printIndex();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
        System.out.println(String.format("Runtime: %.4f seconds\n",(float)duration/1000000000));
    }
}
