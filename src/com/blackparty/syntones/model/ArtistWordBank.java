package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="artist_word_bank")
public class ArtistWordBank {

	@Id
	@Column(name="word_id")
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "artist_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long wordId;
	
	@Column(name="artist_id")
	private long artistId;
	
	@Column(name="word")
	private String word;
	
	@Column(name="max_count")
	private int maxCount;
	
	@Column(name="tf")
	private int tf;
	
	@Column(name="idf")
	private float idf;
	
	@Column(name="step3")
	private float step3;
	
	public ArtistWordBank(){
		
	}

	public long getWordId() {
		return wordId;
	}

	public void setWordId(long wordId) {
		this.wordId = wordId;
	}

	public long getArtistId() {
		return artistId;
	}

	public void setArtistId(long artistId) {
		this.artistId = artistId;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public float getIdf() {
		return idf;
	}

	public void setIdf(float idf) {
		this.idf = idf;
	}

	public float getStep3() {
		return step3;
	}

	public void setStep3(float step3) {
		this.step3 = step3;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	
}
