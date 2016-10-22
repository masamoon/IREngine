import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andre on 17/10/2016.
 */
public class ArffDocumentProcessor {

    private ArrayList<Document> documents;


    public ArffDocumentProcessor(File file){
        this.documents = new ArrayList<Document>();
        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {

                if (!line.isEmpty()){
                    if (line.charAt(0) != '@') {
                        // String[] words = instring.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"); //remove puncuation all lower case
                      //  System.out.println("line = " + line);
                        Pattern p1 = Pattern.compile("\"([^\"]*)\""); // get text between quotation marks
                        Pattern p2 = Pattern.compile("(?<!\\d)\\d{8}+(?!\\d)"); //get every occurrence of 8 numbers (doc id)
                        Matcher m1 = p1.matcher(line);
                        Matcher m2 = p2.matcher(line);
                        while (m2.find() && m1.find()) {
                       //     System.out.println(m2.group(0));
                            Document document = new Document(Integer.parseInt(m2.group(0)), m1.group(0), file.toURI());
                            this.documents.add(document);
                        //    System.out.print(this.documents.get(0));

                        }

                    }
                 }
            }


        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public ArrayList<Document> process(){

        return this.documents;
    }
}