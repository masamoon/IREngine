import java.io.File;
import java.util.List;

public class PdfDocumentProcessor implements DocumentProcessor {

    private List<Document> documents;

    public PdfDocumentProcessor(File file) {

    }


    public List<Document> process() {
        return documents;
    }
}
