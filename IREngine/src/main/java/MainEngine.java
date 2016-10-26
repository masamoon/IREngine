import java.net.URI;
import java.util.List;

public class MainEngine {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir").replace("\\", "/") + "/resources/documents";
        //System.out.println(path);
        URI uri = URI.create(path);
        CorpusReader crd = new CorpusReader(uri);
        List<DocumentProcessor> dps = crd.getDocumentProcessors();

        Indexer idx = new Indexer();
        for (DocumentProcessor dp : dps) {

            Document doc = dp.process();
            Tokenizer tokenizer = new Tokenizer(doc.getDataStream());
            for (String token : tokenizer.getTokens()) {
                idx.index(doc, token);
            }

        }

        idx.printIndex();
    }
}
