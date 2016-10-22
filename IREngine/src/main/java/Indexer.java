import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {

    private Map<String,Map<Integer, List<Integer>>> index;

    public Indexer(){
        index = new HashMap<>();
    }

    public Indexer(URI uri){
        this();
        load(uri);
    }

    public void index(Document doc, String token){
        //TODO:
    }


    private void serialize(){
        //TODO: use Gson to store internal structure (index)
    }

    private void load(URI uri){
        //TODO: use Gson to load previous stored structure (index)
    }
}
