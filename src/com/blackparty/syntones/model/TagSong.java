package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="tag_song_tbl")
public class TagSong {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="tag")
	private String tag;
	@Column (name="song_id")
	private long songId;
	@Column(name="affinity")
	private float affinity;
	
	public TagSong(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public float getAffinity() {
		return affinity;
	}

	public void setAffinity(float affinity) {
		this.affinity = affinity;
	}

	@Override
	public String toString() {
		return "TagSong [tag=" + tag + ", songId=" + songId + ", affinity=" + affinity + "]";
	}
	
	
	
	
}
