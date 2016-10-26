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
        String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/documents";

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

            Document doc = dp.process();
            Tokenizer tokenizer = new Tokenizer(doc.getDataStream());
            for (String token : tokenizer.getTokens()) {
                idx.index(doc, token);
            }

        }

        idx.printIndex();
    }
}
