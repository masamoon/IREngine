package index;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import utils.Memory;
import utils.Tuple;

public class Indexer {
    private final Memory memory;

    private Map<String, Map<Integer, List<Integer>>> index;
    private Map<String,Map<Integer,Double>> tfidf_index; // term -> map < doc_id , weight >

    private URI serializeTo;
    private final int mmem; //maxMemory
    private int serNum;



   // private Map<String,Map<Integer,Integer>> tf_index;

    //TODO: merged index->  term: doc_id[weight,[pos list]]

    //private Map<String,Map<Integer, Tuple<Double,List<Integer>>>> merged_index;
    private Multimap<String,IndexEntry> merged_index;



//token -> Map< doc_ids, "weight:pos">

    public Indexer(int mem) {
        serNum = 0;
        index = new TreeMap<>();
        tfidf_index = new TreeMap<>();
        merged_index = TreeMultimap.create();

        serializeTo = null;
        memory = new Memory();
        mmem = mem;

    }

    public void merge(){/*
        for( String term : tfidf_index.keySet()){
            if(!merged_index.containsKey(term))
                merged_index.put(term, new HashMap<>());
            Map<Integer,Double> tfidf_entry = tfidf_index.get(term);
            Map<Integer,List<Integer>> index_entry = index.get(term);
            Map<Integer, Tuple<Double,List<Integer>>> merged_entry = merged_index.get(term);

            for ( Integer docid : tfidf_entry.keySet()){
                merged_entry.put(docid,new Tuple<Double, List<Integer>>(tfidf_entry.get(docid),index_entry.get(docid)));
            }
        }

        tfidf_index = new TreeMap<>();
        index = new TreeMap<>();*/
    }

    public void setSerializeTo(URI uri){
        this.serializeTo = uri;
    }
/*
    public index.Indexer(URI uri) {
        this();
        //load(uri);
        load();
    }*/

    public void index(Multimap<String,Integer> tokens, Integer docId) {
        double sumw = 0;


        for(String term : tokens.keySet()){
           // merged_index.put(term,new HashMap<>());
            //Map<Integer,Tuple<Double,List<Integer>>> entry = merged_index.get(term);
            Collection<Integer> pos = tokens.get(term);
            int t_frequency = pos.size();
            double tf = 1 + Math.log10(t_frequency); // term frequency
            sumw+=tf*tf;

        }

        double norm = 1/Math.sqrt(sumw);

        for(String term : tokens.keySet()){
            //merged_index.put(term,new ArrayList<>());
            //Map<Integer,Tuple<Double,List<Integer>>> entry = merged_index.get(term);
            Collection<Integer> pos = tokens.get(term);
            int t_frequency = pos.size();
            double tf = 1 + Math.log10(t_frequency); // term frequency


            IndexEntry indexEntry = new IndexEntry(docId,tf*norm,new ArrayList<Integer>(pos));
            //entry.put(docId,new Tuple(tf*norm,new ArrayList<Integer>(pos)));
            merged_index.put(term,indexEntry);

        }

        if (memory.getCurrentMemory() >= (mmem*0.85)) {
            System.out.println("utils.Memory usage is high - Saving Index current state before the next tf-idf indexation");
            free();
            System.gc();
            System.out.println("Saved");
        }

        /*for (Map.Entry<String, List<Integer>> entry : tokens.entrySet()) {
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

        }*/


    }

    public void tfIdfIndex(){
        //termo , doc, peso da pesquisa (tf), posicao no doc
        //LTC.LNC policy

       /* double norm = Math.sqrt(sumw);
        for(Map.Entry<String, Map<Integer, Tuple<Double, List<Integer>>>> entry: merged_index.entrySet()){
            for(Map.Entry<Integer, Tuple<Double, List<Integer>>> nested_entry : entry.getValue().entrySet()){
                double weigth = nested_entry.getValue().x;
                weigth = weigth / norm;
                nested_entry.setValue(new Tuple<Double,List<Integer>>(weigth,nested_entry.getValue().y));
            }


        }*/

        /*if (memory.getCurrentMemory() >= (mmem*0.85)) {
            System.out.println("utils.Memory usage is high - Saving Index current state before the next tf-idf indexation");
            free();
            System.gc();
            System.out.println("Saved");
        }*/

       // serialize();
        /*for(Map.Entry<String, Map<Integer,List<Integer>>> entry : index.entrySet()) {
            ArrayList<Double> tfs = new ArrayList<>();
            String token = entry.getKey();
            tfidf_index.put(token,new HashMap<>());
            Map<Integer,Double> tf_entry = tfidf_index.get(token);
            for (Map.Entry<Integer, List<Integer>> docs : entry.getValue().entrySet()) {
                int doc_id = docs.getKey();
                double t_frequency = 1 + Math.log10(index.get(token).get(doc_id).size()); // term frequency
                tfs.add(t_frequency);
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
        if (memory.getCurrentMemory() >= (mmem*0.85)) {
            System.out.println("utils.Memory usage is high - Saving Index current state before the next tf-idf indexation");
            free();
            System.gc();
            System.out.println("Saved");
        }
        //serialize();*/
    }

    public void printTfIdIndex(){

        for(Map.Entry<String,Map<Integer,Double>> entry : tfidf_index.entrySet()){
            for(Map.Entry<Integer,Double> nested_entry : entry.getValue().entrySet()){
                System.out.println(entry.getKey()+" -> "+nested_entry.getKey()+" : "+nested_entry.getValue());
            }
        }
    }

    public void free(){
        //merge();
        serNum++;
        serialize();


    }

    public void serialize() {

        /*try{
            FileWriter fw = null;
            for( Map.Entry<String,IndexEntry> idxEntry : merged_index.entries()){
                fw = new FileWriter("resources/output/"+idxEntry.getKey().charAt(0)+".idx");
                fw.write(idxEntry.getKey()+":"+idxEntry.getValue().toString());
            }
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }*/


        try {
            FileWriter fw = new FileWriter("resources/output/"+serNum+".idx", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            for (Map.Entry<String, Collection<IndexEntry>> idxEntry : merged_index.asMap().entrySet()) {
                //System.out.println("serializing ");

                out.println(idxEntry.getKey() + ":" + idxEntry.getValue().toString());
            }


        }catch(IOException e){
            e.printStackTrace();
        }
        merged_index = TreeMultimap.create();
    }


        /*try{
            Gson gson = new Gson();

            //TODO: optional? Garantir que o ficheiro está vazio, se nao estiver então fazer merge do q está no ficheiro com o q existe neste index?
            Map<String,Map<Integer, Tuple<Double,List<Integer>>>> aux;
            Set<Character> abc = new HashSet<>();
            merged_index.keySet().forEach(key -> abc.add(key.charAt(0)));

            for (char c: abc) {
                FileWriter writer = new FileWriter("resources/output/" + c + ".json");
                aux = merged_index.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                e -> new HashMap<Integer, Tuple<Double,List<Integer>>>(e.getValue())));

                aux.entrySet().removeIf(key -> key.toString().charAt(0) != c);
                gson.toJson(aux, new TypeToken<Map<String,Map<Integer, Tuple<Double,List<Integer>>>>>(){}.getType(),writer);
                writer.close();
            }
            merged_index = new TreeMap<>();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }*/




    public void load(URI uri) {
        try {
            Gson gson = new Gson();
            merged_index = gson.fromJson(new FileReader(uri.getPath()),new TypeToken<Map<String,Map<Integer, Tuple<Double,List<Integer>>>>>(){}.getType());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void printMergedIndex(){/*
        System.out.println("printing merged index");
        for (Map.Entry<String, Map<Integer, Tuple<Double,List<Integer>>>> entry : merged_index.entrySet()) {
            System.out.println(entry.getKey() + " : ");
            for (Map.Entry<Integer, Tuple<Double,List<Integer>>> nested_entry : entry.getValue().entrySet()) {
                System.out.print("-> " +nested_entry.getKey() + ": "+nested_entry.getValue().x + ", ");
                System.out.print("[ ");
                for(Integer pos : nested_entry.getValue().y){
                    System.out.print(pos+" ");
                }
                System.out.print("]\n");
            }
        }*/
    }

    public boolean contains(String term) {
        return index.containsKey(term);
    }

    public boolean contains(String term, int doc) {
        if (!contains(term)) {
            return false;
        } else if (!index.get(term).containsKey(doc)) {
            return false;
        }
        return true;
    }


    public void getBooleanIndex(URI uri) {
        try {
            FileWriter fw = new FileWriter(uri.getPath());
            for (Map.Entry<String, Map<Integer, List<Integer>>> entry : index.entrySet()) {
                fw.write(String.format("%s : %d -> [ ", entry.getKey(), entry.getValue().values().size()));
                for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                    fw.write(String.format("%s ", nested_entry.getKey()));
                }
                fw.write("]\n");
            }

            fw.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
