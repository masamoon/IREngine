import java.io.File;

public class PdfDocumentProcessor implements DocumentProcessor {

    private Document document;

    public PdfDocumentProcessor(File file) {

    }


    public Document process() {
        return document;
    }
}
