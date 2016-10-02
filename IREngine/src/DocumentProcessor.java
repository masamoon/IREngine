/**
 * Created by Andre on 30/09/2016.
 */
public class DocumentProcessor {

    private Reader reader;
    private Tokenizer tokenizer;

    public DocumentProcessor(){

    }

    public void process(Document document){
        reader.read(document);
    }

    public void indexFile(Document document){

    }


}
