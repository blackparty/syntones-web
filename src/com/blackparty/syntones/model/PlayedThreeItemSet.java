package com.blackparty.syntones.model;

public class PlayedThreeItemSet {
	private String combination;
	private String session_id;

	public PlayedThreeItemSet() {
		super();
	}

	public PlayedThreeItemSet(String combination, String session_id) {
		super();
		this.combination = combination;
		this.session_id = session_id;
	}

	public String getCombination() {
		return combination;
	}

	public void setCombination(String combination) {
		this.combination = combination;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

}
