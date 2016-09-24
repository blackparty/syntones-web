package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class ThreeItemSetCombo {

	private String track_id;

	private String recom_song;

	public ThreeItemSetCombo() {
		super();
	}

	public ThreeItemSetCombo(String track_id, String recom_song) {
		super();
		this.track_id = track_id;
		this.recom_song = recom_song;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

	public String getRecom_song() {
		return recom_song;
	}

	public void setRecom_song(String recom_song) {
		this.recom_song = recom_song;
	}

}
