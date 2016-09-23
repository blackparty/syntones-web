package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.ArtistDAO;
import com.blackparty.syntones.model.Artist;

@Service
public class ArtistService {

	@Autowired
	private ArtistDAO artistDAO;
	
	
	public List<Artist> getAllArtists()throws Exception{
		return artistDAO.getAllArtist();
	}
	
	public void addArtist(Artist artist){
		Artist fetchedArtist = getArtist(artist.getArtistName());
		if(fetchedArtist == null){
			artistDAO.addArtist(artist);
		}else{
			System.out.println(artist.getArtistName()+" already exists on the Database.");
		}
	}
	
	public Artist getArtist(String artistName){
		Artist artistResult = artistDAO.getArtist(artistName);
		return artistResult;
	}
	
	public Artist getArtist(long artistId){
		Artist artistResult = artistDAO.getArtist(artistId);
		return artistResult;
	}
}
