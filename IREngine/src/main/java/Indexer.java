import java.io.FileWriter;
import java.io.IOException;
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
        return index.containsKey(term);
    }

    public boolean contains(String term, int doc){
        if(!index.containsKey(term))
            return false;
        else
            if(!index.get(term).containsKey(doc))
                return false;

        return true;
    }

    /**
     * Retrieves the amount of documents that contain
     * the given term
     * @param term
     * @return
     */
    public int getDocFrequency(String term){
        if(!index.containsKey(term))
            return 0;
        return index.get(term).size();

    }

    public void getBooleanIndex(){
        // term,document frequency,list of documents
        try {
            FileWriter fw = new FileWriter("resources/output/booleanIndexResult.txt", true);
            for (Map.Entry<String, Map<Integer, List<Integer>>> entry : index.entrySet()) {
                fw.write(String.format("%s : %d -> [ ", entry.getKey(), entry.getValue().size()));
                for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                    fw.write(String.format("%s ", nested_entry.getKey()));
                }
                fw.write("]\n");
            }
            fw.close();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }


}
