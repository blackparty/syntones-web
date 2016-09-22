package com.blackparty.syntones.core;

import java.io.File;

import com.blackparty.syntones.model.Song;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;

public class ID3Extractor {
	private Song song;
	
	public ID3Extractor(){
		
	}
	public Song readTags(String filePath)throws Exception{
		Song song = new Song();
		System.out.println("Reading tags on the file.");
		Mp3File mp3file = new Mp3File(filePath);
		if (mp3file.hasId3v2Tag()) {
            //meta file extraction
            ID3v1 id3v1Tag = mp3file.getId3v2Tag();
            System.out.println("Track: " + id3v1Tag.getTrack());
            System.out.println("Artist: " + id3v1Tag.getArtist());
            System.out.println("Title: " + id3v1Tag.getTitle());
            System.out.println("Album: " + id3v1Tag.getAlbum());
            System.out.println("Year: " + id3v1Tag.getYear());
            System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
            System.out.println("Comment: " + id3v1Tag.getComment());
            song.setArtistName(id3v1Tag.getArtist());
            song.setSongTitle(id3v1Tag.getTitle());
        } else {
        	song.setArtistName(null);
        	song.setSongTitle(null);
            System.out.println("cant read any tags on the given file.");
        }
		return song;
	}
}
