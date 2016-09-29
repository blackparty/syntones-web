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

	@Column(name = "session_id")
	private Long session_id;

	@Column(name = "track_id")
	private String track_id;

	
	@Column(name="played_date",nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	public PlayedSongs() {
		super();
	}

	public PlayedSongs(Long session_id, String track_id) {
		super();
		this.session_id = session_id;
		this.track_id = track_id;
	}

	public Long getSession_id() {
		return session_id;
	}

	public void setSession_id(Long session_id) {
		this.session_id = session_id;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

}
