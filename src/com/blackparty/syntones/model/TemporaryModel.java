package com.blackparty.syntones.model;

import java.util.ArrayList;

public class TemporaryModel {
	private ArrayList<Song> songs;
	private ArrayList<SongWordBank> words;
	private ArrayList<Artist> artists;
	private ArrayList<ArtistWordBank> awords;
	
	
	public TemporaryModel() {
		
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}

	public void setSongs(ArrayList<Song> songs) {
		this.songs = songs;
	}

	public ArrayList<SongWordBank> getWords() {
		return words;
	}

	public void setWords(ArrayList<SongWordBank> words) {
		this.words = words;
	}

	public ArrayList<Artist> getArtists() {
		return artists;
	}

	public void setArtists(ArrayList<Artist> artists) {
		this.artists = artists;
	}

	public ArrayList<ArtistWordBank> getAwords() {
		return awords;
	}

	public void setAwords(ArrayList<ArtistWordBank> awords) {
		this.awords = awords;
	}

}
