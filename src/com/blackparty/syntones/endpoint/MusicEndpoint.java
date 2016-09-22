package com.blackparty.syntones.endpoint;



import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.ListenResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.RemovePlaylistResponse;
import com.blackparty.syntones.response.RemoveToPlaylistResponse;
import com.blackparty.syntones.response.TagsResponse;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.TagService;
import com.sun.media.rtsp.protocol.Request;

 
@RestController
@Component
public class MusicEndpoint {
	@Autowired private SongService songService; 
	@Autowired private PlaylistService playlistService;
	@Autowired private PlaylistSongService playlistSongService;
	@Autowired private TagService tagService;
	
	
	
	@RequestMapping(value="/getAllTags",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.GET)
	public TagsResponse getAllTags(){
		TagsResponse tagResponse = new TagsResponse();
		List<Tag> tags = null;
		Message message  = new Message();
		try{
			tags = tagService.getAllTags();
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage("Exception occured on the webservice");
			message.setFlag(false);
			tagResponse.setMessage(message);
			return tagResponse;
		}
		tagResponse.setTags(tags);
		message.setFlag(true);
		tagResponse.setMessage(message);
		return tagResponse;
	}
	
	
	
	@RequestMapping(value="/removePlaylist")
	public RemovePlaylistResponse removePlaylist(@RequestBody Playlist playlist){
		System.out.println("Received request to remove playlist from: "+playlist.getUser().getUsername());
		RemovePlaylistResponse removePlaylistResponse = new RemovePlaylistResponse();
		Message message = new Message();
		try{
			playlistService.removePlaylist(playlist);
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the webservice");
		}
		message.setFlag(true);
		removePlaylistResponse.setMessage(message);
		return removePlaylistResponse; 
	}
	@RequestMapping(value = "/listen", 
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public ListenResponse listen(@RequestBody Song song) {
		System.out.println("Received request to listen.");
		String audio = "D:/Our_Files1/Eric/School/Thesis/Syntones/Songs/Uploaded/50450/500700.mp3";
		File file = new File(audio);
		MediaResource mediaResource = new MediaResource();
		ListenResponse listenResponse = new ListenResponse();
	
		
		//insert service that will fetch recommended songs to be added on listenresponse
		
		return listenResponse;
	}
	
	@RequestMapping(value="/listenPlaylist",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST)
	public ListenResponse listen(@RequestBody Playlist playlist){
		System.out.println("Received request to listen for playlist: "+playlist.getPlaylistId()+" from: "+playlist.getUser().getUsername());
		ListenResponse lResponse = new ListenResponse();
		//insert service that will fetch recommended songs to be added on listenresponse
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateObject = new Date();
		System.out.println("Date and time: "+dateObject);
		
		return lResponse;
	}
	

	@RequestMapping(value="/savePlaylist",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public PlaylistResponse savePlayList(@RequestBody Playlist playlist) {
		PlaylistResponse playlistResponse = new PlaylistResponse();
		System.out.println("received request to save a playlist from: " + playlist.getUser().getUsername());
		try {
			playlistService.savePlaylist(playlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message message = new Message("", true);
		playlistResponse.setMessage(message);
		return playlistResponse;
	}
	
	
	@RequestMapping(value="/removeToPlaylist",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public RemoveToPlaylistResponse removeToPlaylist(@RequestBody PlaylistSong playlistSong){
		RemoveToPlaylistResponse rtpResponse = new RemoveToPlaylistResponse();
		Message message = new Message();
		try{
			playlistSongService.removeToPlaylist(playlistSong);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage("Exception occured in the web service");
			message.setFlag(false);
		}
		message.setFlag(true);
		message.setMessage("Remove success");
		rtpResponse.setMessage(message);
		return rtpResponse;
	}
	
	@RequestMapping(value="/addToPlaylist",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse addToPlaylist(@RequestBody PlaylistSong playlistSong){
		LibraryResponse libraryResponse = new LibraryResponse();
		Message message = new Message();
		try{
			playlistSongService.addToplaylist(playlistSong);
			List<Playlist> playlists = playlistService.getPlaylist(playlistSong.getUser());
			libraryResponse.setRecentlyPlayedPlaylists(playlists);
			message.setMessage("saving successful.");
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the web service");
			libraryResponse.setMessage(message);
			return libraryResponse;
		}
		message.setFlag(true);
		libraryResponse.setMessage(message);
		return libraryResponse;
	}
	@RequestMapping(value="/playPlaylist")
	public PlaylistSongsResponse playPlaylist(@RequestBody long id){
		PlaylistSongsResponse ppResponse = new PlaylistSongsResponse();
		Playlist playlist = new Playlist();
		Message message = new Message();
		try{
			playlist = playlistService.getSongsFromPlaylist(id);
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the web service");
		}
		message.setFlag(true);
		ppResponse.setPlaylist(playlist);
		return ppResponse;
	}
}
