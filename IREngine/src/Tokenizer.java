import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Tokenizer {

    private String dataStream;
    private Set<String> tokens;

    public Tokenizer(String dataStream){
        this.dataStream = dataStream;
        this.tokens = new HashSet<>();

        tokenize();
        stopping();
        stemming();

    }

    private void tokenize(){
        Scanner sc = new Scanner(dataStream);
        while(sc.hasNext())
            tokens.add(sc.next());

    }

    private void stopping(){}

    private void stemming(){}


    public Set<String> getTokens() {
        return tokens;
    }


}
