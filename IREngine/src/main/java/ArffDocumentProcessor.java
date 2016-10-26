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

    private List<Document> documents;

    /**
     * ArffDocumentProcessor class constructor.
     * Parses the file so it can generate a documents with the
     * respective identifier, data stream and URI.
     *
     * @param file file to process
     */
    public ArffDocumentProcessor(File file) {
        this.documents = new ArrayList<>();
        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.charAt(0) != '@') {
                        //  System.out.println("line = " + line);
                        Pattern p1 = Pattern.compile("\"([^\"]*)\""); // get text between quotation marks
                        //Pattern p2 = Pattern.compile("(?<!\\d)\\d{8}+(?!\\d)"); //get every occurrence of 8 numbers (doc id)
                        Matcher m1 = p1.matcher(line);
                        //Matcher m2 = p2.matcher(line);
                        String[] doc_id_split = line.split(",");
                        String doc_id = doc_id_split[0];
                        //System.out.println(doc_id);
                        while (m1.find()) {
                            //     System.out.println(m2.group(0));
                            String[] words = m1.group(0).replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+"); //remove puncuation all lower case
                            StringBuilder builder = new StringBuilder();
                            for (String s : words) {
                                builder.append(" ").append(s);
                            }
                            String clean_line = builder.toString();
                            Document doc = new Document(Integer.parseInt(doc_id), clean_line, file.toURI());
                            this.documents.add(doc);
                            //    System.out.print(this.documents.get(0));

                        }

                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Returns the processed documents.
     *
     * @return processed documents
     */
    public List<Document> process() {
        return this.documents;
    }
}
