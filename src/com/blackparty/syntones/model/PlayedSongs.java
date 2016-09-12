package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "played_songs_tbl")
public class PlayedSongs {

	@Id
	
	@Column(name = "session_id")
	private String session_id;

	@Column(name = "track_id")
	private String track_id;

	public PlayedSongs() {
		super();
	}

	public PlayedSongs(String session_id, String track_id) {
		super();
		this.session_id = session_id;
		this.track_id = track_id;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

}
