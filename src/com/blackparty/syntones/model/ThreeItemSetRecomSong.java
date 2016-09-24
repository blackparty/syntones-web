package com.blackparty.syntones.model;

public class ThreeItemSetRecomSong {
	private float confidence;
	private String recom_song;

	public ThreeItemSetRecomSong() {
		super();
	}

	public ThreeItemSetRecomSong(float confidence, String recom_song) {
		super();
		this.confidence = confidence;
		this.recom_song = recom_song;
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
