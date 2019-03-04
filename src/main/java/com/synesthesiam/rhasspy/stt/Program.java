package com.synesthesiam.rhasspy.stt;

import java.io.ByteArrayInputStream;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

// import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.api.SpeechResult;
// import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.recognizer.Recognizer;

import static spark.Spark.*;

public class Program {
    public static void main(String[] args) throws Exception {
        Options options = new Options()
            .addOption("h", false, "Help")
            .addOption("a", true, "Acoustic model path")
            .addOption("l", true, "Language model path")
            .addOption("d", true, "Dictionary path");

        // Parse command-line options
        CommandLine cmd = new DefaultParser().parse(options, args);
        if (cmd.hasOption("h")) {
            new HelpFormatter().printHelp("stt", options);
            System.exit(0);
        }

        // Create recognizer
        // Configuration configuration = new Configuration();
        // configuration.setAcousticModelPath(cmd.getOptionValue("a"));
        // configuration.setDictionaryPath(cmd.getOptionValue("d"));
        // configuration.setLanguageModelPath(cmd.getOptionValue("l"));

        ConfigurationManager cm = new ConfigurationManager("/rhasspy/config.xml");

        Recognizer recognizer = (Recognizer)cm.lookup("recognizer");

        // Start web server
        port(12101);
        post("/stt", (request, response) -> {
                synchronized (recognizer) {
                    // Assume request POST data is a WAV in 16-bit 16Khz mono format
                    recognizer.startRecognition(new ByteArrayInputStream(request.bodyAsBytes()));

                    SpeechResult result = recognizer.getResult();
                    String hypStr = result.getHypothesis();
                    recognizer.stopRecognition();

                    // Response is the best transcription
                    return hypStr;
                }
            });

    }  // main

}  // Program
