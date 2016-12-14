import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

public class CsvDocumentProcessor  implements DocumentProcessor{
    public CsvDocumentProcessor(){}

    public static void process(File file, int mem, URI stopURI){
        //List<Doc> aux = new ArrayList<>();
        Tokenizer tokenizer;
        Indexer idx;
        try {
            Reader in = new FileReader(file);

            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().withSkipHeaderRecord().parse(in);
            StringBuilder clean_line;
            idx = new Indexer(mem);
            for (CSVRecord record : records) {
                tokenizer = new Tokenizer(stopURI);
                clean_line  = new StringBuilder();
                try{
                    String title = record.get("Title");
                    String[] words;
                    if(title.length() > 0) {
                        words = title.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+"); //remove puncuation all lower case

                        StringBuilder builder = new StringBuilder();
                        for (String s : words) {
                            builder.append(" ").append(s);
                        }
                        clean_line.append(builder.toString());
                    }
                }catch(IllegalArgumentException e){

                }
                try{
                    String body = record.get("Body");
                    Document doc = Jsoup.parse(body);
                    String words[] = doc.body().text().replace(doc.select("code").text(),"").replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
                    StringBuilder builder = new StringBuilder();
                    for (String s : words) {
                        builder.append(" ").append(s);

                    }
                    clean_line.append(builder.toString());
                }catch(IllegalArgumentException e){
                    e.printStackTrace();
                }

                tokenizer.tokenize(new Doc(Integer.parseInt(record.get("Id")), clean_line.toString(), file.toURI()));
                idx.index(tokenizer.getTokens());
                idx.tfIdfIndex();

            }
            idx.merge();
            idx.printMergedIndex();

            idx.serialize();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return aux;
    }


}
