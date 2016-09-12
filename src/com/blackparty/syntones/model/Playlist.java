package com.blackparty.syntones.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@Table(name="playlist_tbl")
public class Playlist {

	@Id
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "play_list_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	@Column(name="playlist_id")
	private Long playListId;
	
	@Column(name="playlist_name")
	private String playlistName;
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="playlists")
	private Set<Song> songs;
	
	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "user_id")
	private User user;

	
	@Transient
	private List<Song> songList;
	@Transient
	private String[] songIdList;

	public Playlist() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Song> getSongList() {
		return songList;
	}

	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

	public String[] getSongIdList() {
		return songIdList;
	}

	public void setSongIdList(String[] songIdList) {
		this.songIdList = songIdList;
	}

	public Long getPlayListId() {
		return playListId;
	}

	public void setPlayListId(Long playListId) {
		this.playListId = playListId;
	}

	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}

	public Set<Song> getSongs() {
		return songs;
	}

	public void setSongs(Set<Song> songs) {
		this.songs = songs;
	}
	
}
