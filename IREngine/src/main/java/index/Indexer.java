package index;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.MultimapBuilder.ListMultimapBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.Memory;
import utils.Tuple;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class Indexer {
    private static final ListMultimapBuilder<String, Object> builder = MultimapBuilder.treeKeys(String::compareTo).linkedListValues();
    private static final Path destination = Paths.get("resources", "output");


  //  private Map<String, Map<Integer, List<Integer>>> index;
    //private Map<String, Map<Integer, Double>> tfidf_index; // term -> map < doc_id , weight >

    private URI serializeTo;
    private final int mmem; //maxMemory
    private int serNum;

    BufferedWriter[] bufferedWriters;

    char[] alphabet;


    // private Map<String,Map<Integer,Integer>> tf_index;

    //TODO: merged index->  term: doc_id[weight,[pos list]]

    //private Map<String,Map<Integer, Tuple<Double,List<Integer>>>> merged_index;
    private Multimap<String, IndexEntry> merged_index;


//token -> Map< doc_ids, "weight:pos">

    public Indexer(int mem) {
        serNum = 0;
       // index = new TreeMap<>();
       // tfidf_index = new TreeMap<>();
        merged_index = builder.build();

        serializeTo = null;
        mmem = mem;

        alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        /*alphabet = new char[26];
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet[c - 'a'] = c;
        }*/

        bufferedWriters = new BufferedWriter[alphabet.length];

        for(int i =0; i<alphabet.length;i++){
            try {
                bufferedWriters[i] = Files.newBufferedWriter(destination.resolve(alphabet[i] + ".idx"), CREATE, APPEND, WRITE);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }



    public void setSerializeTo(URI uri) {
        this.serializeTo = uri;
    }
/*
    public index.Indexer(URI uri) {
        this();
        //load(uri);
        load();
    }*/

    public void index(Multimap<String, Integer> tokens, Integer docId) {
        double sumw = 0.0;


        for (String term : tokens.keySet()) {
            // merged_index.put(term,new HashMap<>());
            //Map<Integer,Tuple<Double,List<Integer>>> entry = merged_index.get(term);
            Collection<Integer> pos = tokens.get(term);
            int t_frequency = pos.size();
            double tf = 1 + Math.log10(t_frequency); // term frequency
            sumw += tf * tf;
        }

        double norm = 1 / Math.sqrt(sumw);

        for (String term : tokens.keySet()) {
            //merged_index.put(term,new ArrayList<>());
            //Map<Integer,Tuple<Double,List<Integer>>> entry = merged_index.get(term);
            Collection<Integer> pos = tokens.get(term);
            int t_frequency = pos.size();
            double tf = 1 + Math.log10(t_frequency); // term frequency


            IndexEntry indexEntry = new IndexEntry(docId, tf * norm, new ArrayList<>(pos));
            //entry.put(docId,new Tuple(tf*norm,new ArrayList<Integer>(pos)));
            merged_index.put(term, indexEntry);

        }

        if (Memory.getCurrentMemory() >= (mmem * 0.85)) {
           // System.out.println("utils.Memory usage is high - Saving Index current state before the next tf-idf indexation");
            free();
            System.gc();
            System.gc();
           // System.out.println("Saved");
        }




    }



    private void free() {
        //merge();
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

        serNum++;


       // try (BufferedWriter bufferedWriter = Files.newBufferedWriter(destination.resolve(serNum + ".idx"), CREATE, APPEND, WRITE)) {
            for (String term : merged_index.keySet()) {

                int i = 0;

                // System.out.println("serializing "+term);
                for(int j= 0; i<alphabet.length; j++){

                    if(term.charAt(0) == alphabet[j]){
                        i = j;
                        break;
                    }
                    //System.out.println(alphabet[j]+" "+alphabet.length);
                }

                try {
                    //System.out.println("kek");
                    bufferedWriters[i].append(term).append(':');
                    //out.print(term + ":");

                    for (IndexEntry idx : merged_index.get(term)) {
                        bufferedWriters[i].append(idx.toString()).append(';');
                        //System.out.println("kek"+term );
                    }
                    bufferedWriters[i].append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
       /* } catch (IOException e) {
            e.printStackTrace();
        }*/

        //PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("resources/output/" + serNum + ".idx", true)));

       /* for (String term : merged_index.keySet()) {
            // System.out.println("serializing "+term);
            out.print(term + ":");
            for (IndexEntry idx : merged_index.get(term)) {
                out.print(idx.toString() + ";");
            }
            out.println();

        }*/

       // merged_index = builder.build();
        merged_index.clear();
    }




}
