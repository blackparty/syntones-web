package com.blackparty.syntones.model;

public class PlayedTwoItemSet {

	private String combination;
	private String session_id;

	public PlayedTwoItemSet() {
		super();
	}

	public PlayedTwoItemSet(String combination, String session_id) {
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
