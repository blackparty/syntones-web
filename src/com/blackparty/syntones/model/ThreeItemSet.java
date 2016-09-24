package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "three_item_set_tbl")
public class ThreeItemSet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "track_id")
	private String track_id;

	@Column(name = "count")
	private int count;

	@Column(name = "confidence")
	private float confidence;

	@Column(name = "recom_song")
	private String recom_song;

	public ThreeItemSet() {
		super();
	}

	public ThreeItemSet(String track_id, String recom_song, int count, float confidence) {
		super();
		this.track_id = track_id;
		this.count = count;
		this.confidence = confidence;
		this.recom_song = recom_song;
	}

	public ThreeItemSet(String track_id, int count) {
		super();
		this.track_id = track_id;
		this.count = count;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	public String getRecom_song() {
		return recom_song;
	}

	public void setRecom_song(String recom_song) {
		this.recom_song = recom_song;
	}

}
