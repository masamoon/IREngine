import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Indexer {

    private Map<String, Map<Integer, Integer>> index;
    private Map<String, Integer> total_occurr;


    public Indexer() {
        index = new HashMap<>();
    }

    public Indexer(URI uri) {
        this();
        load(uri);
    }

    public void index(Document doc, String token) {
        //index: token -> docid -> count ?
        Scanner sc = new Scanner(doc.getDataStream());
        int count = 0;
        while(sc.hasNext())
            if (Tokenizer.stem(sc.next()).compareTo(token)==0) {
                count++;
            }

        if(count>0) {
            if (!index.containsKey(token))
                index.put(token, new HashMap<>());

            Map<Integer, Integer> entry = index.get(token);

            if (!entry.containsKey(doc.getId()))
                entry.put(doc.getId(), count);
            else
                entry.replace(doc.getId(), count);
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
        for(Map.Entry<String, Map<Integer,Integer>> entry : index.entrySet()){
            System.out.println(entry.getKey() +":");
            for(Map.Entry<Integer,Integer> nested_entry : entry.getValue().entrySet()){
                System.out.println("- " + nested_entry.getKey() + ": " + nested_entry.getValue());
        }
        }
    }


}
