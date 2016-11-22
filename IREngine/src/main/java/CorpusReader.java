/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Corpus Reader data type, which is responsible for reading
 * the files and select the respective Doc Processor.
 */
public class CorpusReader {

    private URI uri;
    private List<Doc> docsToProcess;

    /**
     * Corpus Reader class constructor.
     * Gets the URI of the directory/file to iterate.
     */
    public CorpusReader() {
        docsToProcess = new ArrayList<>();
    }

    public CorpusReader(URI uri) {
        this();
        this.uri = uri;
    }

    /**
     * Iterates the files collection to retrieve the respective
     * list of Doc Processors. Verifies if the URI corresponds
     * to a File or a Directory path and invokes getDocumentProcessor
     * for each File.
     *
     * @return List of document processors.
     */
    public List<Doc> getProcessedDocuments() {
        File aux = new File(uri.getPath());
        if (aux.isDirectory()) {
            for (File file : aux.listFiles()) {
                getDocumentProcessor(file);
            }
        } else {
            getDocumentProcessor(aux);
        }

        return docsToProcess;
    }

    /**
     * Verifies the extension of the file and adds
     * the respective Doc Processor to the list.
     *
     * @param file file to retrieve a processor.
     */
    public void getDocumentProcessor(File file) {
        String path = file.getPath();
        if (path.contains(".")) {
            String extension = path.substring(path.lastIndexOf(".") + 1);
            switch (extension) {
                case "arff":
                    docsToProcess.addAll(ArffDocumentProcessor.process(file));
                    break;
                case "csv":
                    docsToProcess.addAll(CsvDocumentProcessor.process(file));
                    break;
              }

        }
    }

}
