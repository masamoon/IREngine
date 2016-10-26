import java.io.File;
import java.util.List;

public class TxtDocumentProcessor implements DocumentProcessor {

    private List<Document> documents;

    public TxtDocumentProcessor(File file) {

    }

    @Override
    public List<Document> process() {
        return documents;
    }
}
