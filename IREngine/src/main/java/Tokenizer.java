/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import org.tartarus.snowball.ext.englishStemmer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tokenizer data type, which is responsible for generating tokens
 * from a list of Documents
 */
public class Tokenizer {

    StopwordSet stopwordSet;
    private Map<String, Map<Integer, List<Integer>>> tokens; //tokens: token -> docid -> posiçoes
    private Map<Integer,Integer> num_tokens_doc; // doc_id -> num_tokens

    /**
     * Tokenizer class constructor.
     */
    public Tokenizer() {
        this.tokens = new HashMap<>();
        this.stopwordSet = new StopwordSet();
        this.num_tokens_doc = new HashMap<>();
    }

    /**
     * Applies the stemming operation using the Porter Stemmer.
     */
    public static String stem(String a) {
        englishStemmer stema = new englishStemmer();
        stema.setCurrent(a);
        stema.stem();

        return stema.getCurrent();
    }

    /**
     * Iterates each word in the data stream of the
     * doc to generate the set of tokens.
     *
     * @param doc Document to tokenize and stem
     */
    public void tokenize(Document doc) {
        Scanner sc = new Scanner(doc.getDataStream());
        String token;
        Integer num_token;
        int idx = 1;
        num_tokens_doc.put(doc.getId(),0);
        while (sc.hasNext()) {
            token = sc.next();

            num_token = num_tokens_doc.get(doc.getId());
            num_token++;
            num_tokens_doc.replace(doc.getId(),num_token);
            if (!stopwordSet.contains(token) && !stopwordSet.contains(stem(token))) { // to not tokenize the stopwords!!
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
     * Retrieves the set of tokens.
     *
     * @return Set of strings.
     */
    public Map<String, Map<Integer, List<Integer>>> getTokens() {
        return tokens;
    }

    public Map<Integer,Integer> getNumTokens(){
        return num_tokens_doc;
    }

    /**
     * Auxiliar function to print every token.
     */
    public void printTokens() {
        System.out.println(tokens.entrySet().size());
        for (Map.Entry<String, Map<Integer, List<Integer>>> entry : tokens.entrySet()) {
            System.out.println(entry.getKey() + " : ");
            for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                System.out.println("- " + nested_entry.getKey() + ": " + nested_entry.getValue());
            }
        }
    }


}
