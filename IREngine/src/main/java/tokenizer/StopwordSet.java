package tokenizer; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import java.io.*;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;


/**
 * tokenizer.StopwordSet data type, which is responsible for
 * storing the stop word list.
 */
public class StopwordSet {

    Set<String> stopwords = new HashSet<>();

    /**
     * tokenizer.StopwordSet class constructor.
     * Retrieves the file that contains the stopword list
     * and adds it to structure without repetitions (Set).
     */
    public StopwordSet(URI uri) {
        File f = new File(uri.getPath());
        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                stopwords.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Verifies if the tokenizer.StopwordSet contains a given word.
     *
     * @param word String to check if it is present in the set.
     * @return 1 if contains, 0 otherwise.
     */
    public boolean contains(String word) {
        return stopwords.contains(word);
    }

    /**
     * Returns the stop words set.
     *
     * @return Set of strings.
     */
    public Set getSet() {
        return stopwords;
    }
}