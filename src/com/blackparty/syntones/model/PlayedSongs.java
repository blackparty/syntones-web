package com.blackparty.syntones.model;




import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "played_songs_tbl")
public class PlayedSongs {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	@Column(name = "id")
	private long id;

	@Column(name = "user_id")
	private String user_id;

	@Column(name = "track_id")
	private String track_id;

	
	@Column(name="played_date",nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	public PlayedSongs() {
		super();
	}

	public PlayedSongs(String user_id, String track_id) {
		super();
		this.user_id = user_id;
		this.track_id = track_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

}
