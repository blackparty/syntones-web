package com.blackparty.syntones.core;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Vector;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import net.didion.jwnl.dictionary.MorphologicalProcessor;


public class Stemmer {

	
	private int MaxWordLength = 50;
    private Dictionary dic;
    private MorphologicalProcessor morph;
    private boolean IsInitialized = false;
    public HashMap<String, String> AllWords = new HashMap<>();
    private String workingDir = System.getProperty("user.dir");
    public String jwnlDirectory = workingDir+"/jwnl/JWNLproperties.xml";

    public Stemmer() {
        try {
        	//System.out.println("WORKING DIRECTORY: "+workingDir);
            JWNL.initialize(new FileInputStream(jwnlDirectory));
            dic = Dictionary.getInstance();
            morph = dic.getMorphologicalProcessor();
            IsInitialized = true;
        } catch (FileNotFoundException e) {
            System.out.println("Error initializing Stemmer JWNLproperties not found.");
            //Runtime.getRuntime().exit(1);
        } catch (JWNLException e) {
            System.out.println("Error initializing Stemmer:");
            e.printStackTrace();
            //Runtime.getRuntime().exit(1);
        }
    }

    public void Unload() {
        dic.close();
        Dictionary.uninstall();
        JWNL.shutdown();
    }

    public String StemWordWithWordNet(String word) {
        if (!IsInitialized) {
            return word;
        }
        if (word == null) {
            return null;
        }
        if (morph == null) {
            morph = dic.getMorphologicalProcessor();
        }

        IndexWord w;
        try {
            w = morph.lookupBaseForm(POS.VERB, word);
            if (w != null) {
                return w.getLemma().toString();
            }
            
            w = morph.lookupBaseForm(POS.NOUN, word);
            if (w != null) {    
                return w.getLemma().toString();
            }
            
            w = morph.lookupBaseForm(POS.ADJECTIVE, word);
            if (w != null) {
                return w.getLemma().toString();
            }
            
            w = morph.lookupBaseForm(POS.ADVERB, word);
            if (w != null) {
                return w.getLemma().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String stem(String word) {
        String stemmedWord = AllWords.get(word);
        if (stemmedWord != null) {
            return stemmedWord;
        }
        if (containNumbers(word) == true) {
            stemmedWord = null;
        } else {
            stemmedWord = StemWordWithWordNet(word);
        }
        if (stemmedWord != null) {
            AllWords.put(word, stemmedWord);
            return stemmedWord;
        }
        AllWords.put(word, word);
        return word;
    }

    public boolean containNumbers(String word) {
        if (word.matches(".*[0-9].*")) {
            return true;
        }
        return false;
    }

    
    public boolean checkNoun(String word)throws Exception{
          IndexWord w;
          w = morph.lookupBaseForm(POS.NOUN, word);
          if(w!=null){
        	  return true;
          }
          return false;
    }
    
    public Vector stem(Vector words) {
        if (!IsInitialized) {
            return words;
        }
        for (int i = 0; i < words.size(); i++) {
            words.set(i, stem((Vector) words.get(i)));
        }
        return words;
    }
	
	
	
	
	
}
