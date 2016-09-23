package com.blackparty.syntones.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="playlist_song_tbl")
public class PlaylistSong {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="song_id")
	private long songId;
	@Column(name="playlist_id")
	private long playlistId;

	@Transient
	private User user;
	
	public PlaylistSong(){
		
	}
	
	public PlaylistSong(long songId,long playlistId){
		this.songId = songId;
		this.playlistId = playlistId;
	}

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public long getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(long playlistId) {
		this.playlistId = playlistId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "PlaylistSong [id=" + id + ", songId=" + songId + ", playlistId=" + playlistId + ", user=" + user + "]";
	}


	
	
	
}
