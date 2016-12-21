package search; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 *
 */

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import asg.cliche.example.HelloWorld;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.TreeMultimap;

import index.IndexEntry;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import tokenizer.StopwordSet;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.*;
import java.util.stream.Stream;

/**
 * search.Searcher data type, which is reponsible for querying the index.
 */
public class Searcher {
    long i = 0;
    long numfiles;
    String pathToIndex;
    String pathToSW;
    HashMap<Integer,String> metadata;
    int N;

    /**
     * search.Searcher class constructor.
     */
    public Searcher() {
        System.out.println("[x] Starting up...");
        pathToIndex = "resources/output/";
        pathToSW = "resources/stopwords_english.txt";
        System.out.println("[x] Loading metadata...");
        metadata = loadMetadata();
        System.out.println("[x] Performing additional operations...");
        N = countLines(pathToIndex+"metadata.idx");
    }

    public static void main(String[] args) {
        //Searcher searcher = new Searcher();
        //searcher.query("software");
        try {
            ShellFactory.createConsoleShell("IREngine", "", new Searcher())
                    .commandLoop(); // and three.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Command
    /**
     * Search query operation.
     * @param query word to be searched
     * @param topr show top N results
     */
    public void search(String query,int topr) {
        long startTime = System.nanoTime();

        //TreeMultimap<Integer,Double> weights = TreeMultimap.create();
        //Map<Integer, Double> weights = new HashMap<>();


        Set<SearchEntry> weights = searchTerm(query);

        System.out.println("Search results for: " + query);


        matchTitles(weights,topr);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(String.format("Duration: %.4f sec\n", (float) duration / 1000000000));
    }

    /**
     * Applies the stemming operation using the Porter Stemmer.
     */
    public String stem(String a) {
        englishStemmer stema = new englishStemmer();
        stema.setCurrent(a);
        stema.stem();
        return stema.getCurrent();
    }

    public Set<SearchEntry> searchTerm(String query) {

        Set<SearchEntry> weights = new TreeSet<>();
        String stemmedQ = stem(query);


        String line = null;
        //System.out.println(i*100/(numfiles)+"% completed");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(pathToIndex + query.charAt(0) + ".idx"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            while ((line = in.readLine()) != null) {
                int idx = line.indexOf(":");
                String term = line.substring(0, idx);

    /*String docId = term.split("=")[0];
    Double weight = Double.parseDouble(docId.split("\\[")[0]);
    weights.put(weight,Integer.parseInt(docId));*/
                if (term.contains(stemmedQ)) {
                    String rline = line.substring(idx);
                    // System.out.println(line);
                    //String docId = rline.substring(1,rline.indexOf("="));

                    //System.out.print(docId[1]);
                    String[] allOccur = rline.split(";");
                    int df = allOccur.length - 1;
                    double idf; /// calculate
                    // map < term , idf * tfQ>

                    // query(tf)*idf
                    for (String str : allOccur) {
                        if (str.isEmpty()) continue;
                        int idxEq = str.indexOf('=');
                        int idxSq = str.indexOf('[');
                        String docId = str.substring(1, idxEq);
                        String wL = str.substring(idxEq + 1, idxSq);

                        int doc_id = Integer.parseInt(docId);
                        double wl = Double.parseDouble(wL);
                        SearchEntry searchEntry = new SearchEntry(doc_id, wl);
                        weights.add(searchEntry);
                        // weights.merge(doc_id,wl,(o,n) -> (o==null? 0.0: o) + n);
                        //String tmp= str.substring(str.indexOf("=")+1,str.indexOf('['));
                        // System.out.println(tmp);
                        //String wR = tmp.substring(0,tmp.indexOf("["));
                        //Double weight = Double.parseDouble(wR);
                        //System.out.println(weight);
                        //weights.put(Integer.parseInt(docId),weight);
                    }
                }

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return weights;
    }


    @Command
    /**
     * Performs a ranked search with more than one term on the query
     * @param query searching query (might contain more than one word)
     * @param topr show top N results
     */
    public void csearch(String query,int topr) {


        Path basepath = Paths.get("");
        URI stop = basepath.resolve(pathToSW).toUri();
        String[] clean_query = query.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
        StringBuilder sbuilder = new StringBuilder();

        long startTime = System.nanoTime();

        for (String s : clean_query) {
            sbuilder.append(s + " ");
        }

        query = sbuilder.toString();

        StopwordSet stopwordSet = null;
        try {
            stopwordSet = new StopwordSet(stop);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final MultimapBuilder.ListMultimapBuilder<Object, Object> builder = MultimapBuilder.hashKeys().linkedListValues();

        Multimap<String, Integer> tokens = builder.build(); //token ->posições


        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(query);
        String token;
        int idx = 1;
        int start = boundary.first();

        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {
            if (end - start > 2) { // filter words by size (also filters spaces)
                token = query.substring(start, end);

                String stemmedStr = stem(token);

                if (!stopwordSet.contains(token) && !stopwordSet.contains(stemmedStr)) { // to not tokenize the stopwords!!
                    token = stemmedStr;
                    tokens.put(token, idx);

                }
            }
            idx++;
        }

        double sumw = 0;
        for (String term : tokens.keySet()) {
            // merged_index.put(term,new HashMap<>());
            //Map<Integer,Tuple<Double,List<Integer>>> entry = merged_index.get(term);
            Collection<Integer> pos = tokens.get(term);
            int t_frequency = pos.size();
            double tf = 1 + Math.log10(t_frequency); // term frequency
            sumw += tf * tf;
        }

        double norm = 1 / Math.sqrt(sumw);





        TreeMultimap<String, IndexEntry> query_index = TreeMultimap.create();
        TreeSet<SearchEntry> ranked_results = new TreeSet<>();
        TreeMap<Integer, Double> results = new TreeMap<>();

        for (String entry : tokens.keySet()) {
           // System.out.println(entry);
            Set<SearchEntry> weights = searchTerm(entry);
            int df = weights.size();
            if(df == 0)
                df = 1;
            double idf = Math.log10(N / df);

            Collection<Integer> pos = tokens.get(entry);
            int t_frequency = pos.size();
            double tf = 1 + Math.log10(t_frequency); // term frequency

            double tfidf = tf * idf * norm;

            for (SearchEntry en : weights) {

                //ranked_results.add(new SearchEntry(en.getDocId(), en.getWeight() * tfidf));

                if (results.containsKey(en.getDocId())) {
                    double oldW = results.get(en.getDocId());
                    double newW = (en.getWeight() * tfidf) + oldW;
                    results.put(en.getDocId(), newW);
                    for(Object rankedr : ranked_results.toArray()){
                        SearchEntry srank = (SearchEntry) rankedr;
                        if(srank.getDocId() == en.getDocId())
                            ranked_results.remove(rankedr);
                    }
                    ranked_results.add(new SearchEntry(en.getDocId(), newW));
                } else {
                    results.put(en.getDocId(), en.getWeight() * tfidf);
                    ranked_results.add(new SearchEntry(en.getDocId(), en.getWeight() * tfidf));
                }
            }

        }

        int c = 0;

        matchTitles(ranked_results,topr);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(String.format("Duration: %.4f sec\n", (float) duration / 1000000000));




    }

    public int countLines(String filename) {
        InputStream is = null;
        int count = 0;
        boolean empty = true;
        try {
            is = new BufferedInputStream(new FileInputStream(filename));

            try {
                byte[] c = new byte[1024];

                int readChars = 0;
                empty = true;
                while ((readChars = is.read(c)) != -1) {
                    empty = false;
                    for (int i = 0; i < readChars; ++i) {
                        if (c[i] == '\n') {
                            ++count;
                        }
                    }
                }

            } finally {
                is.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (count == 0 && !empty) ? 1 : count;

    }

    public void matchTitles(Set<SearchEntry> weights,int topr) {
        BufferedReader meta = null;
        try {
            meta = new BufferedReader(new FileReader(pathToIndex+"metadata.idx"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        String metaLine = "";
        String title = "";
        String did = "";
        int count = 0;

       // HashMap<Integer,String> metadata = loadMetadata();

        for (Object entry : weights.toArray()) {
            SearchEntry sentry = (SearchEntry) entry;
            int w = sentry.getDocId();
            ArrayList<Integer> toExclude = new ArrayList<Integer>();
            try {
                meta = new BufferedReader(new FileReader(pathToIndex+"metadata.idx"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (count < topr) {

                if(metadata.containsKey(w)){
                    title = metadata.get(w);
                    System.out.println("title: " + title + " | docid: " + w + "| weight: " + sentry.getWeight());
                    toExclude.add(w);
                }

                if (!toExclude.contains(w))
                    System.out.println("docid: " + w + " : weight->" + sentry.getWeight());
                count++;
            } else {
                break;
            }
        }

    }

    public HashMap<Integer,String> loadMetadata(){
        BufferedReader meta = null;
        String metaLine = "";
        HashMap<Integer,String> metadata = new HashMap<>();
        try {
            meta = new BufferedReader(new FileReader(pathToIndex+"metadata.idx"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String title = "";
        String did = "";
        try {
            while ((metaLine = meta.readLine()) != null) {
                // title = metaLine.substring(0,metaLine.indexOf(":"));
                String metalineres[] = metaLine.split(":");
                String parentIds[] = metaLine.split("-");
                String parentId = "";
                if (parentIds.length > 1) {
                    parentId = parentIds[1];
                }
                if (metalineres.length > 1)
                    did = metalineres[1];
                else
                    did = "0";
                title = metalineres[0];
                // did = metaLine.substring(metaLine.indexOf(":"),metaLine.length()-1);

                //System.out.println(did);
                int intdid = 0;
                try {
                    intdid = Integer.parseInt(did);
                } catch (NumberFormatException e) {
                    intdid = 0;
                }
                //System.out.println(intdid +" ## "+weights.get(w));
                metadata.put(intdid,title);



            }
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("[x] loaded metadata");
        return metadata;
    }

    @Command
    public void setIndex(String s){
        pathToIndex = s;
    }

    @Command
    public void setPathToSW(String s){
        pathToSW = s;
    }

    @Command
    public void quit(){
        System.out.println("[x]exiting...");
        System.exit(0);
    }
}
