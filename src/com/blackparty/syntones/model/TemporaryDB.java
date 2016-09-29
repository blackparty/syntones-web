package com.blackparty.syntones.model;


import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "temporary_db_tbl")
public class TemporaryDB {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "user_id")
	private String user_id;

	@Column(name = "song_id")
	private long song_id;

	@Column(name="played_date",nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	public TemporaryDB() {
		super();
	}

	public TemporaryDB(Long id, String user_id, long song_id) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.song_id = song_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public long getSong_id() {
		return song_id;
	}

	public void setSong_id(long song_id) {
		this.song_id = song_id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	
	
}
