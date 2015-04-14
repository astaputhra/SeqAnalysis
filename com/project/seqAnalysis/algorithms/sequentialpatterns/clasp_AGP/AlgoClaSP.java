package com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.project.seqAnalysis.algorithms.AbstractAlogrithamClass;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.database.SequenceDatabase;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern;
import com.project.seqAnalysis.algorithms.GraphOutput;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.savers.Saver;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.savers.SaverIntoFile;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.savers.SaverIntoMemory;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.tries.Trie;
import com.project.seqAnalysis.tools.MemoryLogger;

/**
 * This is an implementation of the ClaSP algorithm. ClaSP was proposed by A.
 * Gomariz et al. in 2013.
 *
 * NOTE: This implementation saves the pattern to a file as soon as they are
 * found or can keep the pattern into memory, depending on what the user choose.
 *
 * Copyright Antonio Gomariz Peñalver 2013
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
 *
 * @author agomariz
 */
public class AlgoClaSP extends AbstractAlogrithamClass {

    public AlgoClaSP(double minSupAbsolute, AbstractionCreator abstractionCreator, boolean findClosedPatterns, boolean executePruningMethods) {
        this.minSupAbsolute = minSupAbsolute;
        this.abstractionCreator = abstractionCreator;
        this.findClosedPatterns = findClosedPatterns;
        this.executePruningMethods = executePruningMethods;
    }

    Saver saver = null;
    public void runAlgorithm(SequenceDatabase database, boolean keepPatterns, boolean verbose, String outputFilePath, boolean outputSequenceIdentifiers) throws IOException {
        //If we do no have any file path
        if (outputFilePath == null) {
            //The user wants to save the results in memory
            saver = new SaverIntoMemory(outputSequenceIdentifiers);
        } else {
            //Otherwise, the user wants to save them in the given file
            saver = new SaverIntoFile(outputFilePath, outputSequenceIdentifiers);
        }
        // reset the stats about memory usage
        MemoryLogger.getInstance().reset();
        //keeping the starting time
        overallStart = System.currentTimeMillis();
        //Starting ClaSP algorithm
        claSP(database, (long) minSupAbsolute, keepPatterns, verbose, findClosedPatterns, executePruningMethods);
        //keeping the ending time
        overallEnd = System.currentTimeMillis();
        //Search for frequent patterns: Finished
        saver.finish();
//        
    }

    protected void claSP(SequenceDatabase database, long minSupAbsolute, boolean keepPatterns, boolean verbose, boolean findClosedPatterns, boolean executePruningMethods) {
        //We get the initial trie whose children are the frequent 1-patterns
        frequentAtomsTrie = database.frequentItems();

        database.clear();
        database = null;

        //Inizialitation of the class that is in charge of find the frequent patterns
        FrequentPatternEnumeration_ClaSP frequentPatternEnumeration = new FrequentPatternEnumeration_ClaSP(abstractionCreator, minSupAbsolute,saver,findClosedPatterns,executePruningMethods);
        		
        this.mainMethodStart = System.currentTimeMillis();
        //We dfsPruning the search
        frequentPatternEnumeration.dfsPruning(new Pattern(), frequentAtomsTrie, verbose, null,null);
        this.mainMethodEnd = System.currentTimeMillis();
        //Once we had finished, we keep the number of frequent patterns that we found
        numberOfFrequentPatterns = frequentPatternEnumeration.getFrequentPatterns();
        
        // check the memory usage for statistics
        MemoryLogger.getInstance().checkMemory();
        
        if (verbose) {
            System.out.println("ClaSP: The algorithm takes " + (mainMethodEnd - mainMethodStart) / 1000 + " seconds and finds " + numberOfFrequentPatterns + " patterns");
        }
        //If the we are interested in closed patterns, we dfsPruning the post-processing step
        if (findClosedPatterns) {
//        	ShowTrie.showTree(this.getFrequentAtomsTrie());
            List<Entry<Pattern, Trie>> outputPatternsFromMainMethod = frequentAtomsTrie.preorderTraversal(null);

            this.postProcessingStart = System.currentTimeMillis();
            frequentPatternEnumeration.removeNonClosedPatterns(outputPatternsFromMainMethod, keepPatterns);
            this.postProcessingEnd = System.currentTimeMillis();
            numberOfFrequentPatterns = frequentPatternEnumeration.getFrequentPatterns();
            if (verbose) {
                System.out.println("ClaSP:The post-processing algorithm to remove the non-Closed patterns takes " + (postProcessingEnd - postProcessingStart) / 1000 + " seconds and finds " + numberOfFrequentPatterns + " Closed patterns");
            }
        }else{
            if(keepPatterns){
                List<Entry<Pattern, Trie>> outputPatternsFromMainMethod = frequentAtomsTrie.preorderTraversal(null);
                for(Entry<Pattern, Trie> p:outputPatternsFromMainMethod){
                    saver.savePattern(p.getKey());
                }
            }
        }
        List<GraphOutput> graphOutputs = new ArrayList<>();
        this.setAlgorithmName(algorithmName);
        saver.insert(this);
        saver.selectMethod(graphOutputs);

        numberOfFrequentPatterns = frequentPatternEnumeration.getFrequentPatterns();
        frequentPatternEnumeration.clear();
        
	        // check the memory usage for statistics
		MemoryLogger.getInstance().checkMemory();
	
	    joinCount = frequentPatternEnumeration.joinCount;
    }

    public String printStatistics() {
        StringBuilder r = new StringBuilder(200);
        r.append("=============  Algorithm - STATISTICS =============\n Total time ~ ");
        r.append(getRunningTime());
        r.append(" ms\n");
        r.append(" Frequent sequences count : ");
        r.append(numberOfFrequentPatterns);
        r.append('\n');
        r.append(" Join count : ");
        r.append(joinCount);
        r.append('\n');
        r.append(" Max memory (mb):");
	r.append(MemoryLogger.getInstance().getMaxMemory());
        r.append('\n');
        r.append(saver.print());
        r.append("\n===================================================\n");
        return r.toString();
    }
    

    public long getRunningTime() {
        return (overallEnd - overallStart);
    }
    public void clear() {
        frequentAtomsTrie.removeAll();
        abstractionCreator = null;
    }


   }
