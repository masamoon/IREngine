import java.net.URI;
import java.util.List;

public class MainEngine {

    public static void main(String[] args){
        URI uri = URI.create("documents");
        CorpusReader crd = new CorpusReader(uri);
        List<DocumentProcessor> dps = crd.getDocumentProcessors();
        Indexer idx = new Indexer();
        for(DocumentProcessor dp: dps)
        {
            Document doc = dp.process();
            //TODO: save to DocIndexer structure (LISTA)
            Tokenizer tokenizer = new Tokenizer(doc.getDataStream());
            for(String token : tokenizer.getTokens()){
                idx.index(doc, token);
            }

        }



    }
}
