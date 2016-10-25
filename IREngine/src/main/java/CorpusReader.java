import java.net.URI;
import java.util.List;

public class CorpusReader {

    private URI uri;
    private List<DocumentProcessor> dps;

    public CorpusReader(URI uri) {
        this.uri = uri;

        //create dp (in list) according to extension here!
    }

    public List<DocumentProcessor> getDocumentProcessors() {
        return dps;
    }

}
