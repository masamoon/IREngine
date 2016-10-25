import java.net.URI;
import java.util.*;

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

        if(index.containsKey(token)){

            Map<Integer,List<Integer>> entry = index.get(token);

            for(Map.Entry<Integer,List<Integer>> nested_entry : index.get(token).entrySet()){

                List<Integer> list = nested_entry.getValue();
                list.add(doc.getId());
                Integer occurrences = nested_entry.getKey();
                occurrences +=1;
                entry.put(occurrences,list);

                index.put(token,entry);

            }

        }
        else{
            Map<Integer,List<Integer>> newentry = new HashMap<Integer, List<Integer>>();
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(doc.getId());
            newentry.put(1,list);
            index.put(token,newentry);

        }


    }


    private void serialize(){
        //TODO: use Gson to store internal structure (index)
    }

    private void load(URI uri){
        //TODO: use Gson to load previous stored structure (index)
    }

    private void printIndex() {
        for (Map.Entry<String, Map<Integer, List<Integer>>> entry : index.entrySet()) {

            for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {

                System.out.println(nested_entry.getKey() + " : " + nested_entry.getValue());

            }
        }
    }

}
