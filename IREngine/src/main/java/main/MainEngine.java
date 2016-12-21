package main; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import index.Indexer;
import org.apache.commons.cli.*;
import reader.CorpusReader;

/**
 * Main Program for Information Retrieval Engine
 */
public class MainEngine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Options options = new Options();
        options.addOption("c", "corpus",true, "Path that contain the Corpus");
        options.addOption("i","index",true, "where to save the generated index");
        options.addOption("m","utils.Memory",true,"Define maximum memory to use");
        options.addOption("s","stopword list",true,"Define a path to the stopword list");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String pathToCorpus = null;
        String stopwordsList = null;
        String pathToIndex = null;
        String memory = null;

        if(cmd.hasOption("c")) {
            pathToCorpus = cmd.getOptionValue("c");
        }
        else {
            System.out.println("<ERROR> must define a path to the corpus");
        }

        if(cmd.hasOption("s")){
            stopwordsList = cmd.getOptionValue("s");
        }
        else{
            System.out.println("<ERROR> must define a path to the stopword list");
        }
        if(cmd.hasOption("i")){
            pathToIndex = cmd.getOptionValue("i");
        }
        else{
            System.out.println("<ERROR> must define a path to store the index");
        }
        if(cmd.hasOption("m")){
            memory = cmd.getOptionValue("m");
        }else{
            System.out.println("<ERROR> must define max memory used in the indexing");
        }

        Path basepath = Paths.get("");

        long startTime = System.nanoTime();

        URI corpusUri = basepath.resolve(pathToCorpus).toUri(); //URI.create(System.getProperty("user.dir").replace("\\", "/") + "/resources/corpusBig/Answers.csv");
        //URI corpusUri = URI.create(System.getProperty("user.dir").replace("\\", "/") + "/resources/csv/Answers_mini.csv");
        URI stop = basepath.resolve(stopwordsList).toUri();
        URI serTo = basepath.resolve(pathToIndex).toUri(); // URI.create(System.getProperty("user.dir").replace("\\", "/") + "resources/output");

        CorpusReader crd = new CorpusReader(corpusUri, stop);

        Indexer idx = new Indexer(Integer.parseInt(memory));
        idx.setSerializeTo(serTo); // TODO: move to constructor
        crd.getProcessedDocuments(Integer.parseInt(memory));

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(String.format("Duration: %.4f sec\n", (float) duration / 1000000000));
    }
}
    /* OFFICIAL MAIN:
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("c", "corpus",true, "Path that contain the Corpus");
        options.addOption("i","index",true, "File to load from or serialize to the indexer");
        //options.addOption("it","indexTo", true, "File where to save the index");
        options.addOption("b","boolean", true, "File where to save the boolean index");
        options.addOption("t","times",false,"Prints runtime times");
        options.addOption("m","utils.Memory",true,"Define maximum memory to use");


        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser clip = new DefaultParser();
        try{
            CommandLine cmd = clip.parse(options, args);
            if (args.length < 1) {
                formatter.printHelp("Possible usage: \n", options);
            } else {
                long startTime = System.nanoTime();
                int memoryToUse;
                if (cmd.hasOption("m")) {
                    memoryToUse = Integer.parseInt(cmd.getOptionValue("m"));
                } else {
                    memoryToUse = 512;
                }
                URI corpusUri, index, booleanIndex;
                index.Indexer idx = new index.Indexer(memoryToUse);
                if(cmd.hasOption("i")){
                    index = URI.create(cmd.getOptionValue("i"));
                }
                else{
                    index = URI.create("resources/output/tfidfIndexResult.json");
                }


                if(cmd.hasOption("c")) {
                    corpusUri = URI.create(cmd.getOptionValue("c"));
                    //TODO: path do stopword file como argumento!
                    reader.CorpusReader crd = new reader.CorpusReader(corpusUri, URI.create(System.getProperty("user.dir").replace("\\", "/") + "/resources/stopwords_english.txt"));

                    idx.setSerializeTo(index);
                   // idx.load(index);
                    crd.getProcessedDocuments(memoryToUse);
                    //idx.load(index);
                    //List<document.Doc> dsp = crd.getProcessedDocuments();
                    /*tokenizer.Tokenizer tokenizer = new tokenizer.Tokenizer();
                    for (document.Doc d : dsp) {
                        tokenizer.tokenize(d);
                    }
                    idx.index(tokenizer.getTokens());*/
                //}
/*
                if(cmd.hasOption("b")){
                    booleanIndex =  URI.create(cmd.getOptionValue("b"));
                    idx.load(index);
                    idx.getBooleanIndex(booleanIndex);
                }

                //more execution actions here...
                //idx.printIndex();
                if(cmd.hasOption("t")){
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime);
                    System.out.println(String.format("Duration: %.4f sec\n", (float) duration / 1000000000));
                }
            }
        }catch (ParseException e){
            formatter.printHelp("Possible usage: \n", options);
        }catch(IllegalArgumentException e) {
            System.out.printf("Invalid path: %s \n",e.getMessage().split(":")[1]);
        }

    }*/
