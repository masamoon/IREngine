import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.*;

public class Indexer {

    private Map<String, Map<Integer, List<Integer>>> index;

   // private Map<String,Map<Integer,Integer>> tf_index;

    private Map<String,Map<Integer,Double>> tfidf_index; // term -> map < doc_id , weight >

    public int num_docs;




    public Indexer() {
        index = new HashMap<>();
        tfidf_index = new HashMap<>();
        this.num_docs = 0;
    }

    public Indexer(URI uri) {
        this();
        load(uri);
    }

    public void incNumDocs(){
        this.num_docs++;
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

    public void tfIdfIndex(){
        //termo , doc, peso da pesquisa (tf), posicao no doc
        //LTC.LNC policy

        for(Map.Entry<String, Map<Integer,List<Integer>>> entry : index.entrySet()){

            for(Map.Entry<Integer,List<Integer>> docs : entry.getValue().entrySet()){

                String token = entry.getKey();
                int doc_id = docs.getKey();
                double t_frequency = 1+ Math.log10(index.get(token).get(doc_id).size()); // term frequency in doc
                int d_frequency = 1;//getDocFrequency(token); // document frequency of term

                double idf = Math.log10(num_docs/(d_frequency));
                double tf_idf = t_frequency * idf;
                tfidf_index.put(token, new HashMap<>());

                Map<Integer, Double> doc_list_freq = tfidf_index.get(token);
                doc_list_freq.put(doc_id,tf_idf);

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
        for(Map.Entry<String, Map<Integer,List<Integer>>> entry : index.entrySet()){
            System.out.println(entry.getKey() +" : ");
            for(Map.Entry<Integer,List<Integer>> nested_entry : entry.getValue().entrySet()){
                System.out.println("- " + nested_entry.getKey() + ": " + nested_entry.getValue());
        }
        }
    }

    /*public void printTfIndex(){
       // System.out.println(tf_index.entrySet().size());
        for(Map.Entry<String, Map<Integer,Integer>> entry : tf_index.entrySet()){
            System.out.println(entry.getKey() +" -> ");
            for(Map.Entry<Integer,Integer> nested_entry : entry.getValue().entrySet()){
                System.out.println(", " + nested_entry.getKey() + ": " + nested_entry.getValue());
            }
        }
    }*/

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

   /* public void getTfIndex(){
        // term,document id, frequency in document
        try {
            FileWriter fw = new FileWriter("resources/output/frequencyIndexResult.txt", true);
            for (Map.Entry<String, Map<Integer, Integer>> entry : tf_index.entrySet()) {
                fw.write(String.format("%s : [ ", entry.getKey()));
                for (Map.Entry<Integer, Integer> nested_entry : entry.getValue().entrySet()) {
                    fw.write(String.format("%d : %d ", nested_entry.getKey(),nested_entry.getValue()));
                }
                fw.write("]\n");
            }
            fw.close();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }*/


}
