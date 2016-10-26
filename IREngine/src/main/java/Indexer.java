import java.net.URI;
import java.util.*;

public class Indexer {

    private Map<String, Map<Integer, List<Integer>>> index;


    public Indexer() {
        index = new HashMap<>();
    }

    public Indexer(URI uri) {
        this();
        load(uri);
    }

    public void index(Document doc, String token) {
        //index: token -> docid -> posiçoes

        if (!index.containsKey(token))
            index.put(token, new HashMap<>());

        Map<Integer, List<Integer>> entry = index.get(token);
        Scanner sc = new Scanner(doc.getDataStream());

        int idx = 0;

        List<Integer> positions = new ArrayList<>();
        while(sc.hasNext()) {
            if (Tokenizer.stem(sc.next()).equals(token)) {
                positions.add(idx);
            }
            idx++;
        }
        if (!entry.containsKey(doc.getId())){

            entry.put(doc.getId(), positions);
        }
        else {
            entry.replace(doc.getId(), positions);
        }

    }


    private void serialize() {
        //TODO: use Gson to store internal structure (index)
    }

    private void load(URI uri) {
        //TODO: use Gson to load previous stored structure (index)
    }

    public void printIndex() {
        System.out.println(index.entrySet().size());
        for(Map.Entry<String, Map<Integer,List<Integer>>> entry : index.entrySet()){
            System.out.println(entry.getKey() +" : ");
            for(Map.Entry<Integer,List<Integer>> nested_entry : entry.getValue().entrySet()){
                System.out.println("- " + nested_entry.getKey() + ": " + nested_entry.getValue());
        }
        }
    }

    public boolean contains(String term){
        return false;
    }

    public boolean contains(String term, Document doc){
        return false;
    }


}
