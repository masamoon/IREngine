import java.io.File;

public class TxtDocumentProcessor implements DocumentProcessor {

    private Document document;
    public TxtDocumentProcessor(File file){

    }

    @Override
    public Document process() {
        return document;
    }
}
