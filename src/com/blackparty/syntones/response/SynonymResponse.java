package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Tag;

public class SynonymResponse {
	private List<String> synonyms;
	private Tag tag;
	public SynonymResponse(){
	}
	public List<String> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}
	public Tag getTag() {
		return tag;
	}
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	@Override
	public String toString() {
		return "SynonymResponse [synonyms=" + synonyms + ", tag=" + tag + "]";
	}
	

	
	
	
}
