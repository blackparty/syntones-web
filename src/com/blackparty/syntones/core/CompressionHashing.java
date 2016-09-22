package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CompressionHashing {
	private final String[] convertList = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private final int NUMBER_OF_ALPHABETS = 26;
    private ArrayList<String> hashedLines = new ArrayList<>();

    public List<String> hash(List<String> lyrics) {
        for (String line : lyrics) {
            if(line.length() == 0){
                continue;
            }
            ArrayList<Integer> tempList = new ArrayList<>();
            StringTokenizer token = new StringTokenizer(line);
            while (token.hasMoreTokens()) {
                String var = token.nextToken();
                ArrayList<Integer> hashedWord = new ArrayList();
                var = var.toLowerCase();
                for (int i = 0; i < var.length(); i++) {
                    //System.out.println(var.charAt(i));
                    hashedWord.add((int) var.charAt(i));
                }
                //System.out.println("Validating compressed word for :" + var);
                int counter = hashedWord.size();
                int sum = 0;
                for (int i = 0; i < hashedWord.size(); i++) {
                    sum += (hashedWord.get(i) * (int) Math.pow(2, --counter));
                }
                sum = sum % NUMBER_OF_ALPHABETS;

                //System.out.println("sum%mod: " + sum);
                tempList.add(sum);
            }
            String temp = "";
            for (int i : tempList) {
                temp = temp.concat(convertList[i]);
                //System.out.print(i+" ");
            }
            System.out.println(line + " : " + temp);
            hashedLines.add(temp);
        }
        return hashedLines;
    }
}
