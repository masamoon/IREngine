import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {

    private Map<String, Map<Integer, List<Integer>>> index;

   // private Map<String,Map<Integer,Integer>> tf_index;

    private Map<String,Map<Integer,Double>> tfidf_index; // term -> map < doc_id , weight >

    private Map<Integer,Integer> num_tokens; // docid -> num_tokens
    public int num_docs;


//token -> Map< doc_ids, "weight:pos">

    public Indexer() {
        index = new HashMap<>();
        tfidf_index = new HashMap<>();
        this.num_docs = 0;
    }

    public Indexer(URI uri) {
        this();
        load(uri);
    }

    public void index(Map<String, Map<Integer, List<Integer>>> tokens) {
        for (Map.Entry<String, Map<Integer, List<Integer>>> entry : tokens.entrySet()) {
            if (!index.containsKey(entry.getKey()))
                index.put(entry.getKey(), entry.getValue());
            else {
                for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                    if (!index.get(entry.getKey()).containsKey(nested_entry.getKey()))
                        index.get(entry.getKey()).put(nested_entry.getKey(), nested_entry.getValue());
                    else
                        for (int i : nested_entry.getValue())
                            index.get(entry.getKey()).get(nested_entry.getKey()).add(i);
                }
            }

        }
    }

    public void tfIdfIndex(Map<Integer,Integer> num_tokens){
        //termo , doc, peso da pesquisa (tf), posicao no doc
        //LTC.LNC policy
        for(Map.Entry<String, Map<Integer,List<Integer>>> entry : index.entrySet()) {

            ArrayList<Double> tfs = new ArrayList<>();
            String token = entry.getKey();
            tfidf_index.put(token,new HashMap<>());
            Map<Integer,Double> tf_entry = tfidf_index.get(token);
            for (Map.Entry<Integer, List<Integer>> docs : entry.getValue().entrySet()) {
                int doc_id = docs.getKey();
                double t_frequency = 1 + Math.log10(index.get(token).get(doc_id).size()); // term frequency
                tfs.add(t_frequency);
                //System.out.println("frequency: "+t_frequency);
                tf_entry.put(doc_id,t_frequency);
            }
            double norm=0;

            for(Double tf: tfs){
                norm += tf*tf;
            }

            norm = Math.sqrt(norm); //normalized weights
            for (Map.Entry<Integer, Double> to_normalize : tf_entry.entrySet()) {

                to_normalize.setValue(to_normalize.getValue()/norm);

            }


        }

    }

    public void printTfIdIndex(){

        for(Map.Entry<String,Map<Integer,Double>> entry : tfidf_index.entrySet()){
            for(Map.Entry<Integer,Double> nested_entry : entry.getValue().entrySet()){
                System.out.println(entry.getKey()+" -> "+nested_entry.getKey()+" : "+nested_entry.getValue());
            }
        }
    }

    /*
    public void tfIndex(Document doc, String token){
        if (!tf_index.containsKey(token))
            tf_index.put(token, new HashMap<>());

        Map<Integer, Integer> entry = tf_index.get(token); // doc_id -> frequency
        Scanner sc = new Scanner(doc.getDataStream());
        //List<Integer> positions = new ArrayList<>();

        while(sc.hasNext()) {
            if (Tokenizer.stem(sc.next()).equals(token)) {
                if (!entry.containsKey(doc.getId())){

                    entry.put(doc.getId(), 1);
                }
                else {
                    Integer freq = entry.get(doc.getId());
                    entry.replace(doc.getId(), ++freq);
                }
            }

        }


    }*/


    private void serialize() {
        //TODO: use Gson to store internal structure (index)
    }

    private void load(URI uri) {
        //TODO: use Gson to load previous stored structure (index)
    }

    public void printIndex() {
        System.out.println(index.entrySet().size());
        for (Map.Entry<String, Map<Integer, List<Integer>>> entry : index.entrySet()) {
            System.out.println(entry.getKey() + " : ");
            for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                System.out.println("- " + nested_entry.getKey() + ": " + nested_entry.getValue());
            }
        }
    }

    public boolean contains(String term) {
        return index.containsKey(term);
    }

    public boolean contains(String term, int doc) {
        if (!index.containsKey(term))
            return false;
        else if (!index.get(term).containsKey(doc))
            return false;

        return true;
    }

    /**
     * Retrieves the amount of documents that contain
     * the given term
     *
     * @param term
     * @return
     */
    public int getDocFrequency(String term) {
        if (!index.containsKey(term))
            return 0;
        return index.get(term).size();

    }

    public void getBooleanIndex() {
        // term,document frequency,list of documents
        try {
            FileWriter fw = new FileWriter("resources/output/booleanIndexResult.txt");
            for (Map.Entry<String, Map<Integer, List<Integer>>> entry : index.entrySet()) {
                fw.write(String.format("%s : %d -> [ ", entry.getKey(), entry.getValue().values().size()));
                for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                    fw.write(String.format("%s ", nested_entry.getKey()));
                }
                fw.write("]\n");
            }
/*
            for (Map.Entry<String, Map<Integer, List<Integer>>> entry : index.entrySet()) {
                fw.write(String.format("%s : %d -> [ ", entry.getKey(), entry.getValue().size()));
                for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                    fw.write(String.format("%s ", nested_entry.getKey()));
                }
                fw.write("]\n");
            }*/
            fw.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void incDocNum(){
        num_docs++;
    }


}
