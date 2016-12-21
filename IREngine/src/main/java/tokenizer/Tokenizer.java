package tokenizer; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 *
 */

import org.tartarus.snowball.ext.englishStemmer;

import com.google.common.collect.*;

import java.io.IOException;
import java.net.URI;
import java.text.BreakIterator;
import java.util.*;

import document.Doc;

/**
 * tokenizer.Tokenizer data type, which is responsible for generating tokens
 * from a list of Documents
 */
public class Tokenizer {

    private final MultimapBuilder.ListMultimapBuilder<Object, Object> builder;
    private StopwordSet stopwordSet;
    private englishStemmer stema = new englishStemmer();

    //private Map<String, Map<Integer, List<Integer>>> tokens; //tokens: token -> docid -> posiçoes
    //private Map<String,List<Integer>> tokens; //token->posições
    //private Multimap<String,Integer> tokens; //token ->posições

    /**
     * tokenizer.Tokenizer class constructor.
     */
    public Tokenizer(URI stopUR) throws IOException {
        //this.tokens = new HashMap<>();
        //this.tokens = ArrayListMultimap.create();
        this.stopwordSet = new StopwordSet(stopUR);
        builder = MultimapBuilder.hashKeys().linkedListValues();
    }

    /**
     * Applies the stemming operation using the Porter Stemmer.
     */
    private String stem(String a) {
        stema.setCurrent(a);
        stema.stem();
        return stema.getCurrent();
    }

    /**
     * Iterates each word in the data stream of the
     * doc to generate the set of tokens.
     *
     * @param doc document.Doc to tokenize and stem
     */
    public Multimap<String, Integer> tokenize(Doc doc) {
        Multimap<String, Integer> tokens = builder.build(); //token ->posições

        //Scanner sc = new Scanner(doc.getDataStream());
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(doc.getDataStream());
        String token;
        int idx = 1;
        int start = boundary.first();

        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {
            if (end - start > 2) { // filter words by size (also filters spaces)
                token = doc.getDataStream().substring(start, end);

                //  System.out.println(token);

                String stemmedStr = stem(token);


                if (!stopwordSet.contains(token) && !stopwordSet.contains(stemmedStr)) { // to not tokenize the stopwords!!
                    token = stemmedStr;
                /*if (!tokens.containsKey(token)) {
                    //tokens.put(token, new HashMap<>());
                    tokens.put(token, new ArrayList<>());
                    tokens.get(token).add(idx);
                }
                else{
                    tokens.get(token).add(idx);
                }*/
                    // System.out.println(stemmedStr);
                    tokens.put(token, idx);
                    //if (!tokens.get(token).containsKey(doc.getId()))
                    //  tokens.get(token).put(doc.getId(), new ArrayList<>());

                    //tokens.get(token).get(doc.getId()).add(idx);
                }
            }
            idx++;
        }

        return tokens;

    }

    /**
     * Retrieves the set of tokens.
     *
     * @return Set of strings.
     */
    /*public Multimap<String,  Integer> getTokens() {
        return tokens;
    }*/

    /**
     * Auxiliar function to print every token.
     */
   /* public void printTokens() {
        System.out.println(tokens.entrySet().size());
        for (Map.Entry<String, Map<Integer, List<Integer>>> entry : tokens.entrySet()) {
            System.out.println(entry.getKey() + " : ");
            for (Map.Entry<Integer, List<Integer>> nested_entry : entry.getValue().entrySet()) {
                System.out.println("- " + nested_entry.getKey() + ": " + nested_entry.getValue());
            }
        }
    }*/


}
