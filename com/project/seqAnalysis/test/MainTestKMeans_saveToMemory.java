package com.project.seqAnalysis.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

import com.project.seqAnalysis.algorithms.clustering.distanceFunctions.DistanceEuclidian;
import com.project.seqAnalysis.algorithms.clustering.distanceFunctions.DistanceFunction;
import com.project.seqAnalysis.algorithms.clustering.kmeans.AlgoKMeans;
import com.project.seqAnalysis.patterns.cluster.ClusterWithMean;
import com.project.seqAnalysis.patterns.cluster.DoubleArray;

/**
 *  Example of how to use the KMEans algorithm, in source code.
 */
public class MainTestKMeans_saveToMemory {
	
	public static void main(String []args) throws NumberFormatException, IOException{
		
		String input = fileToPath("configKmeans.txt");
		// we request 3 clusters
		int k=3;
		
		// Here we specify that we want to use the euclidian distance
		DistanceFunction distanceFunction = new DistanceEuclidian(); 
		// Alternative distance functions are also available such as:
//		DistanceFunction distanceFunction = new DistanceManathan(); 
//		DistanceFunction distanceFunction = new DistanceCosine(); 
//		DistanceFunction distanceFunction = new DistanceCorrelation(); 
//		DistanceFunction distanceFunction = new DistanceJaccard(); 
		
		// Apply the algorithm
		AlgoKMeans algoKMeans = new AlgoKMeans();  
		List<ClusterWithMean> clusters = algoKMeans.runAlgorithm(input, k, distanceFunction);
		algoKMeans.printStatistics();
		
		// Print the clusters found by the algorithm
		// For each cluster:
		int i=0;
		for(ClusterWithMean cluster : clusters) {
			System.out.println("Cluster " + i++);
			// For each data point:
			for(DoubleArray dataPoint : cluster.getVectors()) {
				System.out.println("   " + dataPoint);
			}
		}
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestKMeans_saveToMemory.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
	
	
}
