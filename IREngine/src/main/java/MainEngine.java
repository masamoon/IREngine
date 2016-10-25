import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class MainEngine {

    public static void main(String[] args){
        String path = System.getProperty("user.dir").replace("\\","/") + "/documents/AA_pmids_tagged.arff";
        System.out.println(path);
        URI uri = URI.create(path);
        CorpusReader crd = new CorpusReader(uri);
       /* List<DocumentProcessor> dps = crd.getDocumentProcessors();
        Indexer idx = new Indexer();
        for(DocumentProcessor dp: dps)
        {
            Document doc = dp.process();
            //TODO: save to DocIndexer structure (LISTA)
            Tokenizer tokenizer = new Tokenizer(doc.getDataStream());
            for(String token : tokenizer.getTokens()){
                idx.index(doc, token);
            }

        }*/
        File file = new File("C:\\Users\\Andre\\InformationRetrievalEngine\\documents\\AA_pmids_tagged.arff"); //TODO: remove hardcoded path
        ArffDocumentProcessor dps = new ArffDocumentProcessor(file);
        ArrayList<Document> docs = dps.process();


        for(Document doc : docs) {

            Tokenizer tokenizer = new Tokenizer(doc.getDataStream());


        }





    }
}
