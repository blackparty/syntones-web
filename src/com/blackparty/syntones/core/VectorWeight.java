package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class VectorWeight {
	public List<Float> getVectorWeight(List<String> hashedLines) {
		float[][] levenshteinSimilarityWeight = getLevenshteinSimilartyWeight(hashedLines);
		ArrayList<Float> vectorWeight = new ArrayList<>();
		System.out.println("Getting Vector Weight");
		for (int i = 0; i < hashedLines.size(); i++) {
			float sum = 0;
			for (int j = 0; j < hashedLines.size(); j++) {
				if (i != j) {
					sum = sum + levenshteinSimilarityWeight[i][j];
				}
			}
			sum = ((float) 1 / hashedLines.size()) * sum;
			System.out.println("Line " + (i + 1) + " = " + sum);
			vectorWeight.add(sum);
		}
		return vectorWeight;
	}

	public float[][] getLevenshteinSimilartyWeight(List<String> hashedLines) {
		System.out.println("Setting Levenshtein Distance matrix: ");
		int[][] ldMatrix = setLDMatrix(hashedLines);
		System.out.println("Setting MaxLen Matrix");
		int[][] maxLenMatrix = setMaxLenMatrix(hashedLines);
		System.out.println("Setting LSW Matrix");
		float[][] levenshteinSimilartyWeight = new float[hashedLines.size()][hashedLines.size()];
		for (int i = 0; i < hashedLines.size(); i++) {
			for (int j = 0; j < hashedLines.size(); j++) {
				float result = (float) (maxLenMatrix[i][j] - ldMatrix[i][j]) / maxLenMatrix[i][j];
				levenshteinSimilartyWeight[i][j] = result;
				// System.out.printf("%.2f ",result);
			}
			// System.out.println("");
		}
		return levenshteinSimilartyWeight;
	}

	public int[][] setMaxLenMatrix(List<String> hashedLines) {
		int[][] maxLenMatrix = new int[hashedLines.size()][hashedLines.size()];
		for (int i = 0; i < hashedLines.size(); i++) {
			for (int j = 0; j < hashedLines.size(); j++) {
				int temp = 0;
				if (hashedLines.get(i).length() > hashedLines.get(j).length()) {
					temp = hashedLines.get(i).length();
				} else {
					temp = hashedLines.get(j).length();
				}
				maxLenMatrix[i][j] = temp;
				// System.out.print(temp + " ");
			}
			// System.out.println("");
		}
		return maxLenMatrix;
	}

	public int[][] setLDMatrix(List<String> hashedLines) {
		int[][] ldMatrix = new int[hashedLines.size()][hashedLines.size()];

		for (int i = 0; i < hashedLines.size(); i++) {
			for (int j = 0; j < hashedLines.size(); j++) {
				int t = StringUtils.getLevenshteinDistance(hashedLines.get(i), hashedLines.get(j));
				ldMatrix[i][j] = t;
				// System.out.print(t + " ");
			}
			// System.out.println("");
		}
		return ldMatrix;
	}
}
