package com.project.seqAnalysis.algorithms;

import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.dataStructures.creators.AbstractionCreator;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clasp_AGP.tries.Trie;

/**
 * Created by astaputhra on 13/4/15.
 */
public class AbstractAlogrithamClass {

    public double minSupAbsolute;
    public double minSupRelative;
    public String algorithmName;
    public long mainMethodStart;
    public long mainMethodEnd;
    public long overallStart;
    public long overallEnd;
    public long postProcessingStart, postProcessingEnd;
    public AbstractionCreator abstractionCreator;
    public int numberOfFrequentPatterns;
    public Trie frequentAtomsTrie;
    public boolean findClosedPatterns;
    public boolean executePruningMethods;
    public long joinCount;

    public double getMinSupAbsolute() {
        return minSupAbsolute;
    }

    public void setMinSupAbsolute(double minSupAbsolute) {
        this.minSupAbsolute = minSupAbsolute;
    }

    public double getMinSupRelative() {
        return minSupRelative;
    }

    public void setMinSupRelative(double minSupRelative) {
        this.minSupRelative = minSupRelative;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public long getMainMethodStart() {
        return mainMethodStart;
    }

    public void setMainMethodStart(long mainMethodStart) {
        this.mainMethodStart = mainMethodStart;
    }

    public long getMainMethodEnd() {
        return mainMethodEnd;
    }

    public void setMainMethodEnd(long mainMethodEnd) {
        this.mainMethodEnd = mainMethodEnd;
    }

    public long getOverallStart() {
        return overallStart;
    }

    public void setOverallStart(long overallStart) {
        this.overallStart = overallStart;
    }

    public long getOverallEnd() {
        return overallEnd;
    }

    public void setOverallEnd(long overallEnd) {
        this.overallEnd = overallEnd;
    }

    public long getPostProcessingStart() {
        return postProcessingStart;
    }

    public void setPostProcessingStart(long postProcessingStart) {
        this.postProcessingStart = postProcessingStart;
    }

    public long getPostProcessingEnd() {
        return postProcessingEnd;
    }

    public void setPostProcessingEnd(long postProcessingEnd) {
        this.postProcessingEnd = postProcessingEnd;
    }

    public AbstractionCreator getAbstractionCreator() {
        return abstractionCreator;
    }

    public void setAbstractionCreator(AbstractionCreator abstractionCreator) {
        this.abstractionCreator = abstractionCreator;
    }

    public int getNumberOfFrequentPatterns() {
        return numberOfFrequentPatterns;
    }

    public void setNumberOfFrequentPatterns(int numberOfFrequentPatterns) {
        this.numberOfFrequentPatterns = numberOfFrequentPatterns;
    }

    public Trie getFrequentAtomsTrie() {
        return frequentAtomsTrie;
    }

    public void setFrequentAtomsTrie(Trie frequentAtomsTrie) {
        this.frequentAtomsTrie = frequentAtomsTrie;
    }

    public boolean isFindClosedPatterns() {
        return findClosedPatterns;
    }

    public void setFindClosedPatterns(boolean findClosedPatterns) {
        this.findClosedPatterns = findClosedPatterns;
    }

    public boolean isExecutePruningMethods() {
        return executePruningMethods;
    }

    public void setExecutePruningMethods(boolean executePruningMethods) {
        this.executePruningMethods = executePruningMethods;
    }

    public long getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(long joinCount) {
        this.joinCount = joinCount;
    }
}
