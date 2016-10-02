/**
 * Created by Andre on 30/09/2016.
 */
public class Reader {
    private Tokenizer tokenizer;
    private Indexer indexer;

    public Reader(){

    }

    public void read(Document document){
        tokenizer.tokenize(document,indexer);
    }
}
