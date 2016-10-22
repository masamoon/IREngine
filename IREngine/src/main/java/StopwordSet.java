import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 16/10/2016.
 */
public class StopwordSet {

    ArrayList<String> stopwords = new ArrayList<String>();

    public StopwordSet(){
        URL path = this.getClass().getResource("stopwords_english.txt");
        if(path==null) {
            System.out.print("stopword list not found");
        }

        File f = null;
        try {
            f = new File(path.toURI());
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null)
            {
                stopwords.add(line);
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }


    }


    public boolean contains(String word){
        if (stopwords.contains(word))
            return true;
        else
            return false;
    }
}
