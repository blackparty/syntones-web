package com.blackparty.syntones.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.metamodel.relational.IllegalIdentifierException;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "song_tbl")
public class Song {

	@Id
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "song_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	@Column(name = "song_id")
	private long songId;

	@Column(name = "song_title")
	private String songTitle;

	@Transient
	private String artistName;

	@Transient
	private int distance;

	@Transient
	private File file;
	
	// connects the song to the artist class

	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "artist_id")
	private Artist artist;
	
	//private long artistID;
	
	@Column(name = "lyrics",columnDefinition = "text")
	private String lyrics = "";
	
	@Column(name = "file_path")
	private String filePath;
	
	@Column(name = "vector_space", columnDefinition ="text")
	private String vectorSpace;
	
	public String getVectorSpace() {
		return vectorSpace;
	}

	public void setVectorSpace(String vectorSpace) {
		this.vectorSpace = vectorSpace;
	}

	public Song() {

	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public void setLyrics(List<String> lyrics) {
		// adding nextline tag
		for (int i = 0; i < lyrics.size(); i++) {
			this.lyrics = this.lyrics.concat(lyrics.get(i).concat("\\n"));
		}
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Song(String songTitle, String artistName) {
		this.artistName = artistName;
		this.songTitle = songTitle;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getLyrics() {
		return lyrics;
	}

	public long getSongId() {
		return songId;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public Artist getArtist() {
		return artist;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return this.distance;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	public File getFile(){
		return file;
	}

	@Override
	public String toString() {
		return "Song [songTitle=" + songTitle + ", artistName=" + artistName + "]";
	}


}
