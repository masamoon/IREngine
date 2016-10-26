/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import org.tartarus.snowball.ext.englishStemmer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Tokenizer data type, which is responsible for generating tokens
 * from a given data stream.
 */
public class Tokenizer {

    StopwordSet stopwordSet;
    englishStemmer stem = new englishStemmer();
    private String dataStream;
    private Set<String> tokens;

    /**
     * Tokenizer class constructor. Tokenizes the given data stream and
     * invokes the stopping and stemming operations.
     *
     * @param dataStream stream of data to tokenize
     */
    public Tokenizer(String dataStream) {
        this.dataStream = dataStream;
        this.tokens = new HashSet<>();
        this.stopwordSet = new StopwordSet();

        tokenize();
        stopping();
        stemming();

    }

    /**
     * Iterates each word in the stream of data
     * to generate the set of tokens.
     */
    private void tokenize() {
        Scanner sc = new Scanner(dataStream);
        while (sc.hasNext())
            tokens.add(sc.next());
    }

    /**
     * Removes all stop words from the set of
     * tokens, based on the StopwordSet object.
     */
    private void stopping() {
        tokens.removeAll(stopwordSet.getSet());
    }

    /**
     * Applies the stemming operation using the Porter Stemmer.
     */
    private void stemming() {
        Set<String> aux = new HashSet<>();
        Iterator<String> it = tokens.iterator();
        while (it.hasNext()) {
            try {
                stem = new englishStemmer();
                stem.setCurrent(it.next());
                stem.stem();
                aux.add(stem.getCurrent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tokens = aux;
    }


    /**
     * Retrieves the set of tokens.
     *
     * @return Set of strings.
     */
    public Set<String> getTokens() {
        return tokens;
    }

    /**
     * Auxiliar function to print every token.
     */
    public void printTokens() {
        for (String token : tokens) {
            System.out.println(token);
        }
    }


}
