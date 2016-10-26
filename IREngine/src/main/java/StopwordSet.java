import java.io.*;
import java.util.HashSet;
import java.util.Set;


public class StopwordSet {

    Set<String> stopwords = new HashSet<>();

    public StopwordSet() {
        File f = new File("./resources/stopwords_english.txt");
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

    public Set getSet() {
        return stopwords;
    }
}
