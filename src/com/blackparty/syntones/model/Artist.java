package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="artist_tbl")
public class Artist {

	@Id
	@Column(name="artist_id")
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "artist_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long artistId;
	
	
	@Column(name="artist_name")
	private String artistName;

	@Column(name="step5_tfidf")
	private float step5Tfidf;

	public Artist() {
		super();
	}

	public Artist(long artistId, String artistName) {
		super();
		this.artistId = artistId;
		this.artistName = artistName;
	}

	public long getArtistId() {
		return artistId;
	}

	public void setArtistId(long artistId) {
		this.artistId = artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	
	public float getStep5Tfidf() {
		return step5Tfidf;
	}

	public void setStep5Tfidf(float step5Tfidf) {
		this.step5Tfidf = step5Tfidf;
	}

	@Override
	public String toString() {
		return "Artist [artistId=" + artistId + ", artistName=" + artistName + "]";
	}
	
	
}
