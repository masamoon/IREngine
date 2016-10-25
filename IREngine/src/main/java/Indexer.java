import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Indexer {

    private Map<String, Map<Integer, Integer>> index;


    public Indexer() {
        index = new HashMap<>();
    }

    public Indexer(URI uri) {
        this();
        load(uri);
    }

    public void index(Document doc, String token) {

        if (index.containsKey(token)) {

            Map<Integer, Integer> entry = index.get(token);

            if (entry.containsKey(doc.getId())) {
                /*Integer occurr = entry.get(doc.getId());
                occurr += 1;*/
                entry.replace(doc.getId(), entry.get(doc.getId()) + 1);

            } else {
                entry.put(doc.getId(), 1);
            }

            index.replace(token, entry);


        } else {
            Map<Integer, Integer> newentry = new HashMap<Integer, Integer>();
            newentry.put(doc.getId(), 1);
            index.put(token, newentry);
        }
    }


    private void serialize() {
        //TODO: use Gson to store internal structure (index)
    }

    private void load(URI uri) {
        //TODO: use Gson to load previous stored structure (index)
    }

    public void printIndex() {
        for (Map.Entry<String, Map<Integer, Integer>> entry : index.entrySet()) {

            for (Map.Entry<Integer, Integer> nested_entry : entry.getValue().entrySet()) {
                if (nested_entry.getValue() > 0)
                    System.out.println(entry.getKey() + " : " + nested_entry.getKey() + " : " + nested_entry.getValue());

            }
        }
    }

}
