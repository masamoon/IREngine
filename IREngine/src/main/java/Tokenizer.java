import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import java.util.*;

public class Tokenizer {

    private String dataStream;
    private Set<String> tokens;
    private Set<String> stem_tokens;
   // private String algorithm;

    StopwordSet stopwordSet;

    englishStemmer stem = new englishStemmer();

    public Tokenizer(String dataStream){
       // System.out.println("tokenizing");
        this.dataStream = dataStream;
        this.tokens = new HashSet<>();
        this.stem_tokens = new HashSet<>();
        this.stopwordSet = new StopwordSet();
        //this.algorithm = algorithm;

        tokenize();
        stopping();
        Iterator<String> it = tokens.iterator();
        while(it.hasNext()) {
            String stem_token = stemming(it.next());
            stem_tokens.add(stem_token);

        }
        //printTokens();

    }

    private void tokenize(){
        Scanner sc = new Scanner(dataStream);

        while(sc.hasNext())
            tokens.add(sc.next());

    }

    private void stopping(){
        ArrayList<String> toremove = new ArrayList<String>();

        for(String t : tokens){
            if (stopwordSet.contains(t)){
                //tokens.remove(t);
                toremove.add(t);
            }
        }

        for(String t : toremove){
            tokens.remove(t);
        }


    }

    private String stemming(String str){

        try {
            stem = new englishStemmer();

            stem.setCurrent(str);
            stem.stem();
            return stem.getCurrent();


        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public Set<String> getTokens() {
        return stem_tokens;
    }

    public void printTokens(){

        System.out.println("STEMMED TOKENS:");

        for(String token : stem_tokens){
            System.out.println(token);
        }

    }


}
