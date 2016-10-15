import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Tokenizer {

    private String dataStream;
    private Set<String> tokens;

    StopwordSet stopwordSet;

    public Tokenizer(String dataStream){
        this.dataStream = dataStream;
        this.tokens = new HashSet<>();
        this.stopwordSet = new StopwordSet();

        tokenize();
        stopping();
        stemming();

    }

    private void tokenize(){
        Scanner sc = new Scanner(dataStream);
        while(sc.hasNext())
            tokens.add(sc.next());

    }

    private void stopping(){

        for(String t : tokens){
            if (stopwordSet.contains(t)){
                tokens.remove(t);
            }
        }
    }

    private void stemming(){}


    public Set<String> getTokens() {
        return tokens;
    }


}
