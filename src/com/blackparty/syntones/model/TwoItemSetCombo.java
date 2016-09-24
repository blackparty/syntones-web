package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "two_item_set_combo_tbl")
public class TwoItemSetCombo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="combination")
	private String combination;
	
	@Column(name="recom_song")
	private String recom_song;

	public TwoItemSetCombo() {
		super();
	}

	public TwoItemSetCombo(Long id, String combination, String recom_song) {
		super();
		this.id = id;
		this.combination = combination;
		this.recom_song = recom_song;
	}

	public TwoItemSetCombo(String combination, String recom_song) {
		super();
		this.combination = combination;
		this.recom_song = recom_song;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCombination() {
		return combination;
	}

	public void setCombination(String combination) {
		this.combination = combination;
	}

	public String getRecom_song() {
		return recom_song;
	}

	public void setRecom_song(String recom_song) {
		this.recom_song = recom_song;
	}

}
