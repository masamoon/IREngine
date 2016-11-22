import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
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

    public static List<Doc> process(File file){
        List<Doc> aux = new ArrayList<>();
        try {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().withSkipHeaderRecord().parse(in);
            StringBuilder clean_line;
            for (CSVRecord record : records) {
                clean_line  = new StringBuilder();
                try{
                    clean_line.append(record.get("Title"));
                    clean_line.append(" : ");
                }catch(IllegalArgumentException e){}
                aux.add(new Doc(Integer.parseInt(record.get("Id")), clean_line.toString(), file.toURI()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return aux;
    }


}
