/**
 * Aveiro University, Department of Electronics, Telecommunications and Informatics.
 * MIECT - Information Retrieval
 * 2016/2017
 * Andre Lopes - 67833
 * Raquel Rocha - 62196
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.commons.cli.*;

import javax.print.URIException;

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
        options.addOption("i","index",true, "File to load from or serialize to the indexer");
        //options.addOption("it","indexTo", true, "File where to save the index");
        options.addOption("b","boolean", true, "File where to save the boolean index");
        options.addOption("t","times",false,"Prints runtime times");
        options.addOption("m","Memory",true,"Define maximum memory to use");


        HelpFormatter formatter = new HelpFormatter();
        /* CLI arguments parser & execution accordingly */
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
                Indexer idx = new Indexer(memoryToUse);
                if(cmd.hasOption("i")){
                    index = URI.create(cmd.getOptionValue("i"));
                }
                else{
                    index = URI.create("resources/output/tfidfIndexResult.json");
                }


                if(cmd.hasOption("c")) {
                    corpusUri = URI.create(cmd.getOptionValue("c"));
                    CorpusReader crd = new CorpusReader(corpusUri);

                    idx.setSerializeTo(index);
                    idx.load(index);
                    crd.getProcessedDocuments(memoryToUse);
                    //idx.load(index);
                    //List<Doc> dsp = crd.getProcessedDocuments();
                    /*Tokenizer tokenizer = new Tokenizer();
                    for (Doc d : dsp) {
                        tokenizer.tokenize(d);
                    }
                    idx.index(tokenizer.getTokens());*/
                }

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

    }
}
