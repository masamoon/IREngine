import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArffDocumentProcessor implements DocumentProcessor {

    private Document document;

    public ArffDocumentProcessor(File file) {
        try {
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (line.charAt(0) != '@') {
                        //  System.out.println("line = " + line);
                        Pattern p1 = Pattern.compile("\"([^\"]*)\""); // get text between quotation marks
                        Pattern p2 = Pattern.compile("(?<!\\d)\\d{8}+(?!\\d)"); //get every occurrence of 8 numbers (doc id)
                        Matcher m1 = p1.matcher(line);
                        Matcher m2 = p2.matcher(line);
                        while (m2.find() && m1.find()) {
                            //     System.out.println(m2.group(0));
                            String[] words = m1.group(0).replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"); //remove puncuation all lower case
                            StringBuilder builder = new StringBuilder();
                            for (String s : words) {
                                builder.append(" ").append(s);
                            }
                            String clean_line = builder.toString();
                            this.document = new Document(Integer.parseInt(m2.group(0)), clean_line, file.toURI());
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


    public Document process() {
        return this.document;
    }
}
