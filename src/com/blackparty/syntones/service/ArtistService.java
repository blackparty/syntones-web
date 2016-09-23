package com.blackparty.syntones.service;

<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.util.List;
>>>>>>> origin/master

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.ArtistDAO;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.SearchModel;

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
	
	
	public ArrayList<Artist> fetchAllArtist() {
		return artistDAO.fetchAllArtist();
	}

	public Artist getArtist(long artistId) {
		Artist artistResult = artistDAO.getArtist(artistId);
		return artistResult;
	}

	public void updateAllArtist(ArrayList<Artist> artists) throws Exception {
		artistDAO.updateAllArtist(artists);
	}
	
	public ArrayList<Artist> getArtists(ArrayList<SearchModel> model){
		return artistDAO.getArtists(model);
	}

}
