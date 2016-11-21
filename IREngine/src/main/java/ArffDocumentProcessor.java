import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

/**
 * ArffDocumentProcessor data type.
 * Responsible for processing the files of extension arff.
 */
public class ArffDocumentProcessor implements DocumentProcessor {

    /**
     * ArffDocumentProcessor class constructor.
     * Parses the file so it can generate a documents with the
     * respective identifier, data stream and URI.
     */
    public ArffDocumentProcessor() {
    }


    /**
     * Returns the processed documents.
     *
     * @return processed documents
     */
    public static List<Document> process(File file) {
        List<Document> aux = new ArrayList<>();
        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.charAt(0) != '@') {
                        Pattern p1 = Pattern.compile("\"([^\"]*)\""); // get text between quotation marks
                        Matcher m1 = p1.matcher(line);
                        String[] doc_id_split = line.split(",");
                        String doc_id = doc_id_split[0];
                        while (m1.find()) {
                            //     System.out.println(m2.group(0));
                            String[] words = m1.group(0).replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+"); //remove puncuation all lower case
                            StringBuilder builder = new StringBuilder();
                            for (String s : words) {
                                builder.append(" ").append(s);
                            }
                            String clean_line = builder.toString();
                            aux.add(new Document(Integer.parseInt(doc_id), clean_line, file.toURI()));
                        }

                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return aux;
    }


}
