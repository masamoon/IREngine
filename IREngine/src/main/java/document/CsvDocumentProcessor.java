package document;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.IntStream;

import index.Indexer;
import tokenizer.Tokenizer;

/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

public class CsvDocumentProcessor implements DocumentProcessor {
    private final Path path;

    public CsvDocumentProcessor(Path p) throws IOException {
        if (!Files.isRegularFile(p) || !Files.exists(p)) throw new FileNotFoundException();
        this.path = p;
    }

    public static void process(File file, int mem, URI stopURI) {
        Tokenizer tokenizer;
        Indexer idx;
        try {
            Reader in = new FileReader(file);

            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().withSkipHeaderRecord().parse(in);
            StringBuilder clean_line;
            idx = new Indexer(mem);
            for (CSVRecord record : records) {
                tokenizer = new Tokenizer(stopURI);
                clean_line = new StringBuilder();
                try {
                    String title = record.get("Title");
                    String[] words;
                    if (title.length() > 0) {
                        words = title.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+"); //remove puncuation all lower case

                        StringBuilder builder = new StringBuilder();
                        for (String s : words) {
                            builder.append(" ").append(s);
                        }
                        clean_line.append(builder.toString());
                    }
                } catch (IllegalArgumentException e) {

                }
                try {
                    String body = record.get("Body");
                    Document doc = Jsoup.parse(body);
                    String words[] = doc.body().text().replace(doc.select("code").text(), "").replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
                    StringBuilder builder = new StringBuilder();
                    for (String s : words) {
                        builder.append(" ").append(s);

                    }
                    clean_line.append(builder.toString());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                tokenizer.tokenize(new Doc(Integer.parseInt(record.get("Id")), clean_line.toString(), file.toURI()));
                idx.index(tokenizer.getTokens());
                idx.tfIdfIndex();

            }
            idx.merge();
            idx.printMergedIndex();

            idx.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return aux;
    }


    @Override
    public DocumentIterator iterator() {
        return new CSVIterator(path);
    }

    private class CSVIterator extends DocumentIterator implements Iterator<Doc> {
        private final Iterator<CSVRecord> recordsIterator;

        CSVIterator(Path path) {
            super(path);
            try {
                recordsIterator = CSVFormat.RFC4180
                        .withHeader()
                        .withSkipHeaderRecord()
                        .parse(Files.newBufferedReader(path))
                        .iterator();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        @Override
        protected void fetchNextDocument() {
            if (!recordsIterator.hasNext()) {
                currentDoc = null;
            } else {
                CSVRecord record = recordsIterator.next();
                int docid = Integer.parseInt(record.get("Id"));
                StringBuilder data = new StringBuilder(record.get("Title")).append(' ').append(record.get("Body"));

                /*String title = record.get("Title"); // following lines should be done somewhere else
                        /*.codePoints()
                        .filter(ch -> Character.isAlphabetic(ch) || Character.isSpaceChar(ch))
                        .map(ch -> Character.toLowerCase(ch))
                        .forEach(clean_line::appendCodePoint);*/

                /*String body = record.get("Body"); // following lines should be done somewhere else (and should be done using jsoup or wtv
                        /*.codePoints()
                        .filter(ch -> Character.isAlphabetic(ch) || Character.isSpaceChar(ch))
                        .map(ch -> Character.toLowerCase(ch))
                        .forEach(clean_line::appendCodePoint);
                            */
                currentDoc = new Doc(docid, data.toString(), path.toUri());
            }


        }
    }
}