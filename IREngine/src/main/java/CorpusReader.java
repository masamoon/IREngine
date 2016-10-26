import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CorpusReader {

    private URI uri;
    private List<DocumentProcessor> dps;

    public CorpusReader(URI uri) {
        this.uri = uri;
        dps = new ArrayList<>();
    }

    public List<DocumentProcessor> getDocumentProcessors() {
        File aux = new File(uri.getPath());
        if (aux.isDirectory()) {

            //Lets walk trought the directory!!
            for (File file : aux.listFiles()) {
                getDocumentProcessor(file);
            }
        } else {
            getDocumentProcessor(aux);
        }

        return dps;
    }

    public void getDocumentProcessor(File file) {
        String path = file.getPath();
        if (path.contains(".")) {
            String extension = path.substring(path.lastIndexOf(".") + 1);
            switch (extension) {
                case "arff":
                    dps.add(new ArffDocumentProcessor(file));
                    break;
                /*case "txt":
                    dps.add(new TxtDocumentProcessor(file));
                    break;
                case "pdf":
                    dps.add(new TxtDocumentProcessor(file));
                    break;*/
            }
        }
    }

}
