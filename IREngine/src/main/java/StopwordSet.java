import java.io.*;
import java.util.ArrayList;

/**
 * Created by Andre on 16/10/2016.
 */
public class StopwordSet {
    /* TODO:
    * Modificar de List para Set
    * Get (devolver o Set)
     */

    ArrayList<String> stopwords = new ArrayList<String>();

    public StopwordSet() {

        /*URL path = this.getClass().getResource("documents/stopwords_english.txt");
        if(path==null) {
            System.out.print("stopword list not found");
        }*/

        File f = new File("./documents/stopwords_english.txt");

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


    public boolean contains(String word) {
        if (stopwords.contains(word))
            return true;
        else
            return false;
    }
}
