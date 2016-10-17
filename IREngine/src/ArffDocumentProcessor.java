import java.io.*;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andre on 17/10/2016.
 */
public class ArffDocumentProcessor implements DocumentProcessor {

    private Document document;


    public ArffDocumentProcessor(File file,URI uri){

        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null)
            {

                if(line.charAt(0)!='@'){
                    // String[] words = instring.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"); //remove puncuation all lower case
                    Pattern p1 = Pattern.compile("\"([^\"]*)\""); // get text between quotation marks
                    Pattern p2 = Pattern.compile("^(?!\\s*$)[0-9\\s]{8}$"); //get every occurrence of 8 numbers (doc id)
                    Matcher m1 = p1.matcher(line);
                    Matcher m2 = p2.matcher(line);
                    while (m1.find()) {
                        System.out.println(m1.group(1));
                    }
                    document = new Document(Integer.parseInt(m2.group(1)),m1.group(1),uri);
                }
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Document process(){




        return this.document;
    }
}
