import org.tartarus.snowball.ext.englishStemmer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Tokenizer {

    StopwordSet stopwordSet;
    englishStemmer stem = new englishStemmer();
    private String dataStream;
    private Set<String> tokens;

    public Tokenizer(String dataStream) {
        this.dataStream = dataStream;
        this.tokens = new HashSet<>();
        this.stopwordSet = new StopwordSet();

        tokenize();
        stopping();
        stemming();

    }

    private void tokenize() {
        Scanner sc = new Scanner(dataStream);
        while (sc.hasNext())
            tokens.add(sc.next());
    }

    private void stopping() {
        tokens.removeAll(stopwordSet.getSet());
    }

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


    public Set<String> getTokens() {
        return tokens;
    }

    public void printTokens() {
        for (String token : tokens) {
            System.out.println(token);
        }
    }


}
