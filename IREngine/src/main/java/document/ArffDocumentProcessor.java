package document;

import com.google.gson.FieldAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 *
 */

/**
 * document.ArffDocumentProcessor data type.
 * Responsible for processing the files of extension arff.
 */
public class ArffDocumentProcessor implements DocumentProcessor {


    private final Path path;

    /**
     * document.ArffDocumentProcessor class constructor.
     * Parses the file so it can generate a documents with the
     * respective identifier, data stream and URI.
     */
    public ArffDocumentProcessor(Path p) throws IOException {
        if (!Files.isRegularFile(p) || !Files.exists(p)) throw new FileNotFoundException();
        this.path = p;
    }


    /**
     * Returns the processed documents.
     *
     * @return processed documents
     */
    public static List<Doc> process(File file) {
        List<Doc> aux = new ArrayList<>();
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
                            aux.add(new Doc(Integer.parseInt(doc_id), clean_line, file.toURI()));
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


    @Override
    public DocumentIterator iterator() {
        return new ArffDocumentIterator(path);
    }

    private class ArffDocumentIterator extends DocumentIterator implements Iterator<Doc> {

        private final Pattern p1 = Pattern.compile("\"([^\"]*)\""); // get text between quotation marks
        private final BufferedReader br;

        ArffDocumentIterator(Path path) {
            super(path);
            try {
                this.br = Files.newBufferedReader(path);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        protected void fetchNextDocument() {
            String line;
            try {
                do {
                    line = br.readLine();
                } while (line != null && (line.isEmpty() || line.charAt(0) == '@'));
                if (line == null) currentDoc = null;
                else {
                    int doc_id = Integer.parseInt(line.substring(0, line.indexOf(',')));
                    String data = null;
                    Matcher m1 = p1.matcher(line); // i really don't want to do this, but for legacy reasons i will allow it
                    if (m1.find()) {
                        data = m1.group(0); // i don't need to this here -> .replaceAll("[^a-zA-Z ]", " ").toLowerCase();
                    }
                    currentDoc = new Doc(doc_id, data, path.toUri());
                }
            } catch (IOException e) {
                currentDoc = null;
            }
        }

    }
}
