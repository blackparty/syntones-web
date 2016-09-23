package com.blackparty.syntones.endpoint;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.response.ArtistResponse;
import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.SearchResponse;
import com.blackparty.syntones.response.SongListResponse;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@RestController
@Component
public class NavigationEndpoint {
	@Autowired
	private SongService songService;
	@Autowired
	private PlaylistService playlistService;
	@Autowired
	private PlaylistSongService playlistSongService;
	
	@Autowired ArtistService artistService;
	
	@RequestMapping(value="/getAllArtists",produces = MediaType.APPLICATION_JSON_VALUE)
	public ArtistResponse getAllArtist(){
		System.out.println("Received request to get all artists");
		ArtistResponse artistResponse = new ArtistResponse();
		Message message = new Message();
		try{
			List<Artist> artists = artistService.getAllArtists();
			artistResponse.setArtists(artists);
			message.setFlag(true);
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			artistResponse.setMessage(message);
			return artistResponse;
		}
		artistResponse.setMessage(message);
		return artistResponse;
	}
	
	
	
	@RequestMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse search(@RequestBody String searchString) {
		// wala pa ni siya gamit
		System.out.println("recived search request");
		SearchResponse sr = new SearchResponse();
		Message message = new Message();
		message.setMessage("search request \"" + searchString + "\"has been received.");
		sr.setMessage(message);
		return sr;
	}

	@RequestMapping(value = "/test")
	public String toIndex() {
		System.out.println("user dir:" + System.getProperty("user.dir"));
		return "index";
	}
	
	@RequestMapping(value = "/library",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse library(@RequestBody User user){
		LibraryResponse lResponse = new LibraryResponse();
		Message message = new Message();
		List<Playlist> playlists = null;
		try{
			playlists = playlistService.getPlaylist(user);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage("Exception occured on the we service.");
			message.setFlag(false);
			lResponse.setMessage(message);
			return lResponse;
		}
		message.setFlag(true);
		lResponse.setRecentlyPlayedPlaylists(playlists);
		lResponse.setMessage(message);
		return lResponse;
	}
	@RequestMapping(value = "/songlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public SongListResponse getSongs() {
		SongListResponse slr = new SongListResponse();
		System.out.println("Song list request is received");
		Message message;
		try{
			List<Song> songList = songService.getAllSongs();
			if(!songList.isEmpty()){
				message = new Message("Fetching all song is successful.",true);
			}else{
				message = new Message("Query returns zero results.",true);
			}
			slr.setMessage(message);
			slr.setSongList(songList);
		}catch(Exception e){
			e.printStackTrace();
			message = new Message("Exception occured on the web service.", false);
			slr.setMessage(message);
			slr.setSongList(null);
		}
		return slr;
	}
	
	@RequestMapping(value="/playlist",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST)
	public PlaylistResponse playList(@RequestBody User user){
		System.out.println("Received request to get playlists from: "+user.getUsername());
		PlaylistResponse playlistResponse = new PlaylistResponse();
		Message message = null;
		try{
			List<Playlist> playlists = playlistService.getPlaylist(user);
			if(playlists == null){
				return null;
			}
			if(!playlists.isEmpty()){
				playlistResponse.setPlaylists(playlists);
				System.out.println("Web service is responding from the playlist request with the following playlists:");
				for(Playlist e:playlists){
					Playlist pl = e;
					System.out.println("Playlist Name: "+e.getPlaylistName());
				}
				message = new Message("",true);
			}else{
				message = new Message("",false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		playlistResponse.setMessage(message);
		return playlistResponse;
	}
	
	
	@RequestMapping(value="/playlistSong",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public PlaylistSongsResponse playlistSong(@RequestBody Map<String,String> data){
		PlaylistSongsResponse playlistSongsResponse = new PlaylistSongsResponse();
		System.out.println("Received playlist-song request from: "+data.get("username"));
		
		long id = Long.parseLong(data.get("id"));
		Message message = new Message();
		try{
			Playlist playlist = new Playlist();
			playlist.setPlaylistId(id);
			playlist.setSongs(playlistSongService.getSongs(id));
			for(Song s:playlist.getSongs()){
				System.out.println(s.toString());
			}
			playlistSongsResponse.setPlaylist(playlist);
			message.setFlag(true);
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Error occured on the webservice");
		}
		playlistSongsResponse.setMessage(message);
		return playlistSongsResponse;
	}
}
