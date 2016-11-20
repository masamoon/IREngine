/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import org.tartarus.snowball.ext.englishStemmer;

import java.util.*;

/**
 * Tokenizer data type, which is responsible for generating tokens
 * from a given data stream.
 */
public class Tokenizer {

    StopwordSet stopwordSet;
    private Map<String, Map<Integer, List<Integer>>> tokens;
    //tokens: token -> docid -> posiçoes

    /**
     * Tokenizer class constructor. Tokenizes the given data stream and
     * invokes the stopping and stemming operations.
     */
    public Tokenizer() {
        this.tokens = new HashMap<>();
        this.stopwordSet = new StopwordSet();
    }

    /**
     * Iterates each word in the stream of data
     * to generate the set of tokens.
     */
    public void tokenize(Document doc) {
        Scanner sc = new Scanner(doc.getDataStream());
        String token;
        int idx = 1;
        while (sc.hasNext()) {
            token = sc.next();
            if(!stopwordSet.contains(token)) { // to not tokenize the stopwords!!
                token = stem(token);
                if (!tokens.containsKey(token))
                    tokens.put(token, new HashMap<>());

                if (!tokens.get(token).containsKey(doc.getId()))
                    tokens.get(token).put(doc.getId(), new ArrayList<>());

                tokens.get(token).get(doc.getId()).add(idx);
            }
            idx++;
        }
        sc.close();
    }

    /**
     * Applies the stemming operation using the Porter Stemmer.
     */
    public static String stem(String a){
        englishStemmer stema = new englishStemmer();
        stema.setCurrent(a);
        stema.stem();

        return stema.getCurrent();
    }


    /**
     * Retrieves the set of tokens.
     *
     * @return Set of strings.
     */
    public Map<String, Map<Integer, List<Integer>>> getTokens() {
        return tokens;
    }

    /**
     * Auxiliar function to print every token.
     */
    public void printTokens() {
        System.out.println(tokens.entrySet().size());
        for(Map.Entry<String, Map<Integer,List<Integer>>> entry : tokens.entrySet()){
            System.out.println(entry.getKey() +" : ");
            for(Map.Entry<Integer,List<Integer>> nested_entry : entry.getValue().entrySet()){
                System.out.println("- " + nested_entry.getKey() + ": " + nested_entry.getValue());
            }
        }
    }


}
