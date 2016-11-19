package com.blackparty.syntones.model;

public class IdfModel {

	private String term;
	private int count;
	private float idf;
	private long soaId;
	
	public IdfModel(){
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getIdf() {
		return idf;
	}

	public void setIdf(float idf) {
		this.idf = idf;
	}

	public long getSoaId() {
		return soaId;
	}

	public void setSoaId(long soaId) {
		this.soaId = soaId;
	}
	
	
}
