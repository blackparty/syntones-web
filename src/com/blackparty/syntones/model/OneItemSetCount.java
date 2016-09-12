package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "one_item_set_count_tbl")
public class OneItemSetCount {

	@Id
	
	@Column(name = "track_id")
	private String track_id;

	@Column(name = "count")
	private int count;

	public OneItemSetCount() {
		super();
	}

	public OneItemSetCount(String track_id, int count) {
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

}
