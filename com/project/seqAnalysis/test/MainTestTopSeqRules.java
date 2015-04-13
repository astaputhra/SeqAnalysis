package com.project.seqAnalysis.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import com.project.seqAnalysis.algorithms.sequential_rules.topseqrules_and_tns.AlgoTopSeqRules;
import com.project.seqAnalysis.datastructures.redblacktree.RedBlackTree;
import com.project.seqAnalysis.input.sequence_database_array_integers.SequenceDatabase;
import com.project.seqAnalysis.algorithms.sequential_rules.topseqrules_and_tns.Rule;

/**
 *  * Example of how to use the TopSeqRules algorithm in source code.
 * @author Philippe Fournier-Viger (Copyright 2012)
 */
public class MainTestTopSeqRules {

	public static void main(String [] arg) throws IOException{
		// load database
		SequenceDatabase sequenceDatabase = new SequenceDatabase(); 
		try {
			sequenceDatabase.loadFile(fileToPath("contextPrefixSpan.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		sequenceDatabase.printDatabaseStats();
		
		int k = 3;
		double minconf = 0.8;
//
		AlgoTopSeqRules algo = new AlgoTopSeqRules();
		RedBlackTree<Rule> rules = algo.runAlgorithm(k, sequenceDatabase, minconf);
		algo.printStats();
		algo.writeResultTofile(".//output.txt");   // to save results to file
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestTopSeqRules.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
