package com.blackparty.syntones.model;

import java.util.List;

public class IDFReturn {
	List<SongWordBank> swb;
	List<Song> songs;
	List<ArtistWordBank> awb;
	List<Artist> artists;
	
	public IDFReturn(){
		
	}

	public List<SongWordBank> getSwb() {
		return swb;
	}

	public void setSwb(List<SongWordBank> swb) {
		this.swb = swb;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs2) {
		this.songs = songs2;
	}

	public List<ArtistWordBank> getAwb() {
		return awb;
	}

	public void setAwb(List<ArtistWordBank> awb) {
		this.awb = awb;
	}

	public List<Artist> getArtists() {
		return artists;
	}

	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}
	
	
	
}
