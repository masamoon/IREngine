package reader; /**
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

import document.ArffDocumentProcessor;
import document.CsvDocumentProcessor;
import document.Doc;

/**
 * Corpus Reader data type, which is responsible for reading
 * the files and select the respective document.Doc Processor.
 */
public class CorpusReader {

    private URI uri, stop;
    private List<Doc> docsToProcess;

    /**
     * Corpus Reader class constructor.
     * Gets the URI of the directory/file to iterate.
     */
    public CorpusReader() {
        docsToProcess = new ArrayList<>();
    }

    public CorpusReader(URI uri, URI stopURI) {
        this();
        this.uri = uri;
        stop = stopURI;
    }

    /**
     * Iterates the files collection to retrieve the respective
     * list of document.Doc Processors. Verifies if the URI corresponds
     * to a File or a Directory path and invokes getDocumentProcessor
     * for each File.
     *
     * @return List of document processors.
     */
    public void getProcessedDocuments(int maxMem) {
        File aux = new File(uri.getPath());
        if (aux.isDirectory()) {
            for (File file : aux.listFiles()) {
                getDocumentProcessor(file,maxMem);
            }
        } else {
            getDocumentProcessor(aux,maxMem);
        }

        //return docsToProcess;
    }

    /**
     * Verifies the extension of the file and adds
     * the respective document.Doc Processor to the list.
     *
     * @param file file to retrieve a processor.
     */
    public void getDocumentProcessor(File file, int maxMem) {
        String path = file.getPath();
        if (path.contains(".")) {
            String extension = path.substring(path.lastIndexOf(".") + 1);
            switch (extension) {
                case "arff":
                    docsToProcess.addAll(ArffDocumentProcessor.process(file));
                    break;
                case "csv":
                    CsvDocumentProcessor.process(file,maxMem, stop);
                    break;
              }

        }
    }

}
