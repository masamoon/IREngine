import java.io.File;
public class PdfDocumentProcessor implements DocumentProcessor{

    private Document document;
    public PdfDocumentProcessor(File file){

    }

    @Override
    public Document process(){
        return document;
    }
}
