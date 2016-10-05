import java.util.ArrayList;

/**
 * Created by Andre on 30/09/2016.
 */
public class Indexer {

    private ArrayList<Token> tokens;

    public Indexer(ArrayList<Token> tokens){

        this.tokens = tokens;

    }

    public Index createIndex(){
        return new Index();
    }

    public void index(){}
}
