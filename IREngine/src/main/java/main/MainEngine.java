package main; /**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import java.net.URI;

import index.Indexer;
import reader.CorpusReader;

/**
 * Main Program for Information Retrieval Engine
 */
public class MainEngine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        URI corpusUri = URI.create(System.getProperty("user.dir").replace("\\", "/") + "/resources/corpusBig/Answers.csv");
        //URI corpusUri = URI.create(System.getProperty("user.dir").replace("\\", "/") + "/resources/csv/Answers_mini.csv");
        URI stop = URI.create(System.getProperty("user.dir").replace("\\", "/") + "/resources/stopwords_english.txt");
        CorpusReader crd = new CorpusReader(corpusUri, stop);

        URI serTo = URI.create(System.getProperty("user.dir").replace("\\", "/") + "resources/output");
        Indexer idx = new Indexer(512);
        idx.setSerializeTo(serTo);
        crd.getProcessedDocuments(512);

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
