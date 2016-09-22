package com.blackparty.syntones.response;

import java.util.List;

public class SynonymResponse {
	private List<String> synonyms;
	private String word;
	
	public SynonymResponse(){
		
		
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public List<String> getSynonyms() {
		return synonyms;
	}


	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}


	@Override
	public String toString() {
		return "SynonymResponse [synonyms=" + synonyms + ", word=" + word + "]";
	}
	
	
	
	
}
