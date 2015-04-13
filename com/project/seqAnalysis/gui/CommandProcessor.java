package com.project.seqAnalysis.gui;

/*
 * Copyright (c) 2008-2015 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.AlgoCM_ClaSP;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.AlgoClaSP;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreatorStandard_Map;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.AlgoCloSpan;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.AlgoCMSPADE;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.AlgoSPADE;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.candidatePatternsGeneration.CandidateGenerator_Qualitative;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator_FatBitmap;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spam.AlgoCMSPAM;
import com.project.seqAnalysis.algorithms.sequentialpatterns.spam.AlgoSPAM;
import com.project.seqAnalysis.tools.dataset_converter.TransactionDatabaseConverter;
import com.project.seqAnalysis.tools.resultConverter.ResultConverter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This class executes commands from the command line interface or
 * graphical interface to run the algorithms.
 * 
 * @author Philippe Fournier-Viger
 */
public class CommandProcessor {

	/**
	 * This method run an algorithm. It is called from the GUI interface or when
	 * the user run the jar file from the command line.
	 * 
	 * @param algorithmName
	 *            the name of the algorithm
	 * @param inputFile
	 *            the input file for the algorithm
	 * @param outputFile
	 *            the output file for the algorithm
	 * @param parameters
	 *            the parameters of the algorithm
	 * @throws IOException  exception if an error occurs
	 */
	static void runAlgorithm(String algorithmName,
			String inputFile, String outputFile, String[] parameters) throws IOException {
		// System.out.println("C" + algorithmName);

		// **** CHECK IF ARFF AS INPUT FILE *****
		// FIRST WE WILL CHECK IF IT IS AN ARFF FILE...
		// IF YES, WE WILL CONVERT IT TO SPMF FORMAT FIRST,
		// THEN WE WILL RUN THE ALGORITHM, AND FINALLY CONVERT THE RESULT SO
		// THAT IT CAN
		// BE SHOWED TO THE USER.
		// This map is to store the mapping from ItemID to Attribute value for
		// the conversion
		// from ARFF to SPMF.
		Map<Integer, String> mapItemAttributeValue = null;
		// This variable store the path of the original output file
		String originalOutputFile = null;
		// This variable store the path of the original input file
		String originalInputFile = null;
		// If the file is ARFF
		if (inputFile != null
				&& (inputFile.endsWith(".arff") || inputFile.endsWith(".ARFF"))) {
			// Convert it
			TransactionDatabaseConverter converter = new TransactionDatabaseConverter();
			System.out.println("Converting ARFF to SPMF format.");
			// save the file paths selected by the user
			originalOutputFile = outputFile;
			originalInputFile = inputFile;
			// change the ouptut file path to a temporary file
			inputFile = inputFile + ".tmp";
			outputFile = outputFile + ".tmp";
			mapItemAttributeValue = converter.convertARFFandReturnMap(
					originalInputFile, inputFile, Integer.MAX_VALUE);
			System.out.println("Conversion completed.");
		}

		// ****** NEXT WE WILL APPLY THE DESIRED ALGORITHM ******
		// There is a if condition for each algorithm.
		// I will not describe them one by one because it is
		// straightforward.
	 if ("SPAM".equals(algorithmName)) {
			AlgoSPAM algo = new AlgoSPAM();
			if (parameters.length >=2 && "".equals(parameters[1]) == false) {
				algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
			}
			if (parameters.length >=3 && "".equals(parameters[2]) == false) {
				algo.setMaxGap(getParamAsInteger(parameters[2]));
			}
			algo.runAlgorithm(inputFile, outputFile,
					getParamAsDouble(parameters[0]));
			algo.printStatistics();
		} else if ("CM-SPAM".equals(algorithmName)) {
			AlgoCMSPAM algo = new AlgoCMSPAM();
			if (parameters.length >=2 && "".equals(parameters[1]) == false) {
				algo.setMinimumPatternLength(getParamAsInteger(parameters[1]));
			}
			if (parameters.length >=3 && "".equals(parameters[2]) == false) {
				algo.setMaximumPatternLength(getParamAsInteger(parameters[2]));
			}
			// get the required items if any (optional)
			if (parameters.length >=4 && parameters[3] != null && parameters[3].isEmpty() != true) {
				String[] itemsString = parameters[3].split(",");
				int[] requiredItems = new int[itemsString.length];
				for (int i = 0; i < itemsString.length; i++) {
					requiredItems[i] = Integer.parseInt(itemsString[i]);
				}
				algo.setMustAppearItems(requiredItems);
			}
			if (parameters.length >=5 && "".equals(parameters[4]) == false) {
				algo.setMaxGap(getParamAsInteger(parameters[4]));
			}
			boolean outputSeqIdentifiers = false;
			if (parameters.length >=6 && "".equals(parameters[5]) == false) {
				outputSeqIdentifiers = getParamAsBoolean(parameters[5]);
			}
			
			// execute the algorithm with minsup = 2 sequences (50 %)
			algo.runAlgorithm(inputFile, outputFile,
					getParamAsDouble(parameters[0]),outputSeqIdentifiers); // minsup = 106 k = 1000
														// BMS
			algo.printStatistics();
		} else if ("SPADE".equals(algorithmName)) {
			AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative
					.getInstance();
			IdListCreator idListCreator = IdListCreator_FatBitmap.getInstance();
			CandidateGenerator candidateGenerator = CandidateGenerator_Qualitative
					.getInstance();

			double minSupport = getParamAsDouble(parameters[0]);
			boolean outputSeqIdentifiers = false;
			if (parameters.length >=2 && "".equals(parameters[1]) == false) {
				outputSeqIdentifiers = getParamAsBoolean(parameters[1]);
			}


			AlgoSPADE algo = new AlgoSPADE(minSupport, true, abstractionCreator);

			/*
			 * if("".equals(parameters[1]) == false){
			 * algo.setMaximumPatternLength(getParamAsInteger(parameters[1])); }
			 */
			com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase sd = new com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase(
					abstractionCreator, idListCreator);
			sd.loadFile(inputFile, minSupport);

			algo.runAlgorithm(sd, candidateGenerator, true, false, outputFile,outputSeqIdentifiers);
			System.out.println(algo.printStatistics());
		} else if ("CM-SPADE".equals(algorithmName)) {
			AbstractionCreator abstractionCreator = AbstractionCreator_Qualitative
					.getInstance();
			IdListCreator idListCreator = IdListCreator_FatBitmap.getInstance();
			CandidateGenerator candidateGenerator = CandidateGenerator_Qualitative
					.getInstance();

			double minSupport = getParamAsDouble(parameters[0]);
			
			boolean outputSeqIdentifiers = false;
			if (parameters.length >=2 && "".equals(parameters[1]) == false) {
				outputSeqIdentifiers = getParamAsBoolean(parameters[1]);
			}

			AlgoCMSPADE algo = new AlgoCMSPADE(minSupport, true,
					abstractionCreator);

			/*
			 * if("".equals(parameters[1]) == false){
			 * algo.setMaximumPatternLength(getParamAsInteger(parameters[1])); }
			 */
			com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase sd = new com.project.seqAnalysis.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase(
					abstractionCreator, idListCreator);
			sd.loadFile(inputFile, minSupport);

			algo.runAlgorithm(sd, candidateGenerator, true, false, outputFile,outputSeqIdentifiers);
			System.out.println(algo.printStatistics());
		} else if ("ClaSP".equals(algorithmName)) {
			double minSupport = getParamAsDouble(parameters[0]);
			
			boolean outputSeqIdentifiers = false;
			if (parameters.length >=2 && "".equals(parameters[1]) == false) {
				outputSeqIdentifiers = getParamAsBoolean(parameters[1]);
			}

			com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator abstractionCreator = com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator_Qualitative
					.getInstance();
			com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreator idListCreator = IdListCreatorStandard_Map
					.getInstance();
			com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase sd = new com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase(
					abstractionCreator, idListCreator);

			double relativeMinSup = sd.loadFile(inputFile, minSupport);

			AlgoClaSP algo = new AlgoClaSP(relativeMinSup, abstractionCreator,
					true, true);

			/*
			 * if("".equals(parameters[1]) == false){
			 * algo.setMaximumPatternLength(getParamAsInteger(parameters[1])); }
			 */

			algo.runAlgorithm(sd, true, false, outputFile, outputSeqIdentifiers);
			System.out.println(algo.printStatistics());
		} else if ("CM-ClaSP".equals(algorithmName)) {
			double minSupport = getParamAsDouble(parameters[0]);
			
			boolean outputSeqIdentifiers = false;
			if (parameters.length >=2 && "".equals(parameters[1]) == false) {
				outputSeqIdentifiers = getParamAsBoolean(parameters[1]);
			}

			com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator abstractionCreator = com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator_Qualitative
					.getInstance();
			com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.idlists.creators.IdListCreator idListCreator = IdListCreatorStandard_Map
					.getInstance();
			com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase sd = new com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase(
					abstractionCreator, idListCreator);

			double relativeMinSup = sd.loadFile(inputFile, minSupport);

			AlgoCM_ClaSP algo = new AlgoCM_ClaSP(relativeMinSup,
					abstractionCreator, true, true);

			/*
			 * if("".equals(parameters[1]) == false){
			 * algo.setMaximumPatternLength(getParamAsInteger(parameters[1])); }
			 */

			algo.runAlgorithm(sd, true, false, outputFile, outputSeqIdentifiers);
			System.out.println(algo.printStatistics());
		} else if ("CloSpan".equals(algorithmName)) {
			com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items.creators.AbstractionCreator abstractionCreator = com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items.creators.AbstractionCreator_Qualitative
					.getInstance();

			double minSupport = getParamAsDouble(parameters[0]);
			
			boolean outputSeqIdentifiers = false;
			if (parameters.length >=2 && "".equals(parameters[1]) == false) {
				outputSeqIdentifiers = getParamAsBoolean(parameters[1]);
			}

			AlgoCloSpan algo = new AlgoCloSpan(minSupport, abstractionCreator,
					true, true);

			/*
			 * if("".equals(parameters[1]) == false){
			 * algo.setMaximumPatternLength(getParamAsInteger(parameters[1])); }
			 */
			com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items.SequenceDatabase sd = new com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items.SequenceDatabase();
			sd.loadFile(inputFile, minSupport);

			algo.runAlgorithm(sd, true, false, outputFile,outputSeqIdentifiers,algorithmName);
			System.out.println(algo.printStatistics());

			// ///////////////////////////////////////////////////////////end
			// adding by Antonio
			// Gomariz//////////////////////////////////////////////////////////

		}

		// IF THE FILE WAS AN ARFF FILE, WE NEED TO CONVERT BACK THE RESULT
		// SO THAT IT IS PRESENTED IN TERMS OF VALUES
		if (mapItemAttributeValue != null) {
			ResultConverter converter = new ResultConverter();
			System.out
					.println("Post-processing to show result in terms of ARFF attribute values.");
			converter.convert(mapItemAttributeValue, outputFile,
					originalOutputFile);
			System.out.println("Post-processing completed.");
			// delete the temporary files
			// System.out.println("Delete : " + outputFile);
			File file = new File(outputFile);
			file.delete();
			// System.out.println("Delete : " + inputFile);
			File file2 = new File(inputFile);
			file2.delete();
			// set the original outputFile and inputFile
			outputFile = originalOutputFile;
			inputFile = originalInputFile;
		}
	}

	/**
	 * Method to convert a parameter given as a string to a double. For example,
	 * convert something like "50%" to 0.5.
	 * 
	 * @param value
	 *            a string
	 * @return a double
	 */
	private static double getParamAsDouble(String value) {
		if (value.contains("%")) {
			value = value.substring(0, value.length() - 1);
			return Double.parseDouble(value) / 100d;
		}
		return Double.parseDouble(value);
	}

	/**
	 * Method to transform a string to an integer
	 * 
	 * @param value
	 *            a string
	 * @return an integer
	 */
	private static int getParamAsInteger(String value) {
		return Integer.parseInt(value);
	}
	
	/**
	 * Method to transform a string to an boolean
	 * 
	 * @param value         a string
	 * @return a boolean
	 */
	private static boolean getParamAsBoolean(String value) {
		return Boolean.parseBoolean(value);
	}

	/**
	 * Method to get a parameter as a string. Note: this method just return the
	 * string taken as parameter.
	 * 
	 * @param value
	 *            a string
	 * @return a string
	 */
	private static String getParamAsString(String value) {
		return value;
	}

}
