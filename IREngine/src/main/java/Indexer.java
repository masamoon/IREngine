import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {
    private final Memory memory;
    private Map<String, Map<Integer, List<Integer>>> index;
    private URI serializeTo;
    private final int mmem; //maxMemory

   // private Map<String,Map<Integer,Integer>> tf_index;

    private Map<String,Map<Integer,Double>> tfidf_index; // term -> map < doc_id , weight >

    //TODO: merged index->  term: doc_id[weight,[pos list]]

    private Map<String,Map<Integer,Tuple<Double,List<Integer>>>> merged_index;

    private Map<Integer,Integer> num_tokens; // docid -> num_tokens
    public int num_docs;


//token -> Map< doc_ids, "weight:pos">

    public Indexer(int mem) {
        index = new HashMap<>();
        tfidf_index = new HashMap<>();
        merged_index = new HashMap<>();
        this.num_docs = 0;
        this.serializeTo = null;
        memory = new Memory();
        this.mmem = mem;
    }

    public void merge(){

        for( String term : tfidf_index.keySet()){

            System.out.println("term: "+term);
            merged_index.put(term, new HashMap<>());

            Map<Integer,Double> tfidf_entry = tfidf_index.get(term);

            Map<Integer,List<Integer>> index_entry = index.get(term);

            Map<Integer, Tuple<Double,List<Integer>>> merged_entry = merged_index.get(term);

            for ( Integer docid : tfidf_entry.keySet()){

                merged_entry.put(docid,new Tuple<Double,List<Integer>>(tfidf_entry.get(docid),index_entry.get(docid)));
            }
        }
    }

    public void setSerializeTo(URI uri){
        this.serializeTo = uri;
    }
/*
    public Indexer(URI uri) {
        this();
        //load(uri);
        load();
    }*/

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
            if (memory.getCurrentMemory() >= (mmem*0.85)) {
                System.out.println("Memory usage is high - Saving Index current state before the next tf-idf indexation");
                free();
                System.gc();
                System.out.println("Saved");
            }
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
        //serialize();
    }

    public void printTfIdIndex(){

        for(Map.Entry<String,Map<Integer,Double>> entry : tfidf_index.entrySet()){
            for(Map.Entry<Integer,Double> nested_entry : entry.getValue().entrySet()){
                System.out.println(entry.getKey()+" -> "+nested_entry.getKey()+" : "+nested_entry.getValue());
            }
        }
    }

    /*
    public void tfIndex(Doc doc, String token){
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


    public void free(){
        serialize();
    }

    public void serialize() {
        ArrayList<IndexEntry> gindex = new ArrayList<>();
        Gson gson = new GsonBuilder().create();

        for(Map.Entry<String,Map<Integer,Double>> entry : tfidf_index.entrySet()){
            IndexEntry indexEntry = new IndexEntry();
            indexEntry.term = entry.getKey();
            for(Map.Entry<Integer,Double> nested_entry : entry.getValue().entrySet()){
               // System.out.println(entry.getKey()+" -> "+nested_entry.getKey()+" : "+nested_entry.getValue());
                indexEntry.doc_id = nested_entry.getKey();
                indexEntry.weight = nested_entry.getValue();
                gindex.add(indexEntry);

            }
        }
        try {
            FileWriter writer = new FileWriter("resources/output/tfidfIndexResult.json");
//            FileWriter writer = new FileWriter(serializeTo.getPath(),true);
            gson.toJson(gindex,writer);
            writer.write("}]\n");
            writer.close();
            tfidf_index = new HashMap<>();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    public void load(URI uri) {
        //Index gindex = new Index();
        Type indexType = new TypeToken<List<IndexEntry>>(){}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<IndexEntry> gson_index = new ArrayList<>();
        try {
            //JsonReader reader = new JsonReader(new FileReader("resources/output/tfidfIndexResult.json"));
            JsonReader reader = new JsonReader(new FileReader(uri.getPath()));
            reader.setLenient(true);
            gson_index = gson.fromJson(reader, indexType );

        }catch(IOException e){
            e.printStackTrace();
        }

        for(IndexEntry e: gson_index){
          //  System.out.println(e.term +" -> "+e.doc_id+" : "+e.weight);
            if(tfidf_index.containsKey(e.term)){

                Map<Integer,Double> tfidf_entry = tfidf_index.get(e.term);
                tfidf_entry.put(e.doc_id,e.weight);
            }
            else{
                tfidf_index.put(e.term, new HashMap<Integer, Double>());
                Map<Integer,Double> tfidf_entry = tfidf_index.get(e.term);
                tfidf_entry.put(e.doc_id,e.weight);
            }
        }
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

    public void printMergedIndex(){
        System.out.println("printing merged index");
        for (Map.Entry<String, Map<Integer, Tuple<Double,List<Integer>>>> entry : merged_index.entrySet()) {
            System.out.println(entry.getKey() + " : ");
            for (Map.Entry<Integer, Tuple<Double,List<Integer>>> nested_entry : entry.getValue().entrySet()) {
                System.out.println("- " + nested_entry.getKey() + ": "+nested_entry.getValue().x+",");
                for(Integer pos : nested_entry.getValue().y){
                    System.out.print(pos);
                }
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

    public void getBooleanIndex(URI uri) {
        // term,document frequency,list of documents
        try {
            //FileWriter fw = new FileWriter("resources/output/booleanIndexResult.txt");
            FileWriter fw = new FileWriter(uri.getPath());
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
