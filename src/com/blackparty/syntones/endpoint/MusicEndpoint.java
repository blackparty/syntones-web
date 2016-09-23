package com.blackparty.syntones.endpoint;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.model.TagSong;
import com.blackparty.syntones.response.GeneratePlaylistResponse;
import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.ListenResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.RemovePlaylistResponse;
import com.blackparty.syntones.response.RemoveToPlaylistResponse;
import com.blackparty.syntones.response.SongListResponse;
import com.blackparty.syntones.response.TagsResponse;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongLineService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.TagService;
import com.blackparty.syntones.service.TagSongService;

@RestController
@Component
public class MusicEndpoint {
	@Autowired
	private SongService songService;
	@Autowired
	private PlaylistService playlistService;
	@Autowired
	private PlaylistSongService playlistSongService;
	@Autowired
	private TagService tagService;
	@Autowired
	private TagSongService tagSongService;
	@Autowired private ArtistService artistService;
	@Autowired private SongLineService songLineService;
	
	
	
	@RequestMapping(value = "/generatePlaylistByArtist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse generatePlaylistByArtist(@RequestBody String artistName) {
		GeneratePlaylistResponse generatePlaylistResponse = new GeneratePlaylistResponse();
		System.out.println("Received request to generate playlist for : "+artistName);
		Message message = new Message();
		try{
			//getting all songs of the artist
			artistName = artistName.replace("\"", "");
			Artist artist = artistService.getArtist(artistName);
			List<Long> artistSongIds = songService.getAllSongsByArtist(artist);	List<SongLine> songLines = songLineService.getAllLines();
			ArrayList<Long> songIds = new ArrayList<>();
			HashMap<Long, Long> map = new HashMap<>();
			for(long s: artistSongIds){
				System.out.println("Artist song id: "+s);
			}
			for(int i=0;i<songLines.size();i++){
				for(int j=0; j<artistSongIds.size();j++){
					if(songLines.get(i).getSongId() == artistSongIds.get(j)){
						System.out.println("hit!!");
						long id1 = 0;
						long id2 = 0;
						if(i == 0){
							id1 = songLines.get(i+1).getSongId();
						}else{
							id1 = songLines.get(i+1).getSongId();
							id2 = songLines.get(i-1).getSongId();
						}
						if(!map.containsKey(id1)){
							map.put(id1,id1);
							songIds.add(id1);
							System.out.println("Adding song :"+id1);
						} 
						if(!map.containsKey(id2)){
							map.put(id2,id2);
							songIds.add(id2);
							System.out.println("Adding song :"+id2);
						}
					}
				}
			}
			//adding artist's song
			List<Song> songList = new ArrayList<>();
			int counter = 0;
			if(!songIds.isEmpty()){
				for(Long id:songIds){
					if(counter < 10){
						Song song = songService.getSong(id);
						songList.add(song);
					}
					counter++;
				}
				System.out.println("Generating Songs successful.");
				for(Song s:songList){
					System.out.println(s.toString());
				}
				message.setMessage("Generating Songs successful.");
				message.setFlag(true);
				generatePlaylistResponse.setSongs(songList);
			}else{
				message.setFlag(false);
				System.out.println("List is empty. No lists of songs generated.");
				message.setMessage("List is empty. No lists of songs generated.");
			}
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			System.out.println("error happend on the web service");
			message.setMessage("error happend on the web service");
			generatePlaylistResponse.setMessage(message);
			return generatePlaylistResponse;
		}
		generatePlaylistResponse.setMessage(message);
		return generatePlaylistResponse;
	}
	
	@RequestMapping(value = "/saveGeneratedPlaylist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse saveGeneratedPlaylist(@RequestBody Playlist playlist) {
		System.out
				.println("Received request to save the generated playlist from : " + playlist.getUser().getUsername());
		GeneratePlaylistResponse generatedPlaylistResponse = new GeneratePlaylistResponse();
		Message message = new Message();
		long[] songIds = playlist.getSongIds();
		for (long s : songIds) {
			System.out.println(s);
		}
		try {
			long playlistId = playlistService.addGeneratedPlaylist(playlist);
			System.out.println("PLAYLIST ID: " + playlistId);
			ArrayList<PlaylistSong> pls = new ArrayList<>();
			for (long s : songIds) {
				PlaylistSong playlistSong = new PlaylistSong();
				playlistSong.setSongId(s);
				playlistSong.setPlaylistId(playlistId);
				pls.add(playlistSong);
			}
			for (PlaylistSong f : pls) {
				System.out.println(f.toString());
			}
			playlistSongService.savebatchPlaylistSong(pls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		generatedPlaylistResponse.setMessage(message);
		return generatedPlaylistResponse;
	}
	
	

	

	@RequestMapping(value = "/generatePlaylistByTags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse generatePlaylist(@RequestBody String[] tags) {
		GeneratePlaylistResponse generatePlaylistResponse = new GeneratePlaylistResponse();
		System.out.println("Received request to generate playlist for : ");
		List<TagSong> tagsongs = new ArrayList<>();
		Message message = new Message();
		try {
			for (String s : tags) {
				System.out.println(s);
				tagsongs = tagSongService.getSongsByTags(s);
			}
			ArrayList<Song> songs = new ArrayList<>();
			for (TagSong ts : tagsongs) {
				System.out.println(ts.toString());
				Song song = songService.getSong(ts.getSongId());
				songs.add(song);
			}
			for (Song s : songs) {
				System.out.println(s.toStringFromDB());
			}
			if (songs.size() > 0) {
				generatePlaylistResponse.setSongs(songs);
				message.setFlag(true);
				generatePlaylistResponse.setMessage(message);
			} else {
				message.setFlag(false);
				message.setMessage("no songs can be found with the given tag(s)");
				generatePlaylistResponse.setMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatePlaylistResponse;
	}

	@RequestMapping(value = "/getAllTags", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public TagsResponse getAllTags() {
		TagsResponse tagResponse = new TagsResponse();
		List<Tag> tags = null;
		Message message = new Message();
		try {
			tags = tagService.getAllTags();
		} catch (Exception e) {
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

	@RequestMapping(value = "/removePlaylist")
	public RemovePlaylistResponse removePlaylist(@RequestBody Playlist playlist) {
		System.out.println("Received request to remove playlist from: " + playlist.getUser().getUsername());
		RemovePlaylistResponse removePlaylistResponse = new RemovePlaylistResponse();
		Message message = new Message();
		try {
			playlistService.removePlaylist(playlist);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the webservice");
		}
		message.setFlag(true);
		removePlaylistResponse.setMessage(message);
		return removePlaylistResponse;
	}

	@RequestMapping(value = "/listen", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ListenResponse listen(@RequestBody Song song) {
		System.out.println("Received request to listen.");
		String audio = "D:/Our_Files1/Eric/School/Thesis/Syntones/Songs/Uploaded/50450/500700.mp3";
		File file = new File(audio);
		MediaResource mediaResource = new MediaResource();
		ListenResponse listenResponse = new ListenResponse();

		// insert service that will fetch recommended songs to be added on
		// listenresponse

		return listenResponse;
	}

	@RequestMapping(value = "/listenPlaylist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ListenResponse listen(@RequestBody Playlist playlist) {
		System.out.println("Received request to listen for playlist: " + playlist.getPlaylistId() + " from: "
				+ playlist.getUser().getUsername());
		ListenResponse lResponse = new ListenResponse();
		// insert service that will fetch recommended songs to be added on
		// listenresponse
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateObject = new Date();
		System.out.println("Date and time: " + dateObject);

		return lResponse;
	}

	@RequestMapping(value = "/savePlaylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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

	@RequestMapping(value = "/removeToPlaylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public RemoveToPlaylistResponse removeToPlaylist(@RequestBody PlaylistSong playlistSong) {
		RemoveToPlaylistResponse rtpResponse = new RemoveToPlaylistResponse();
		Message message = new Message();
		try {
			playlistSongService.removeToPlaylist(playlistSong);
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Exception occured in the web service");
			message.setFlag(false);
		}
		message.setFlag(true);
		message.setMessage("Remove success");
		rtpResponse.setMessage(message);
		return rtpResponse;
	}

	@RequestMapping(value = "/addToPlaylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse addToPlaylist(@RequestBody PlaylistSong playlistSong) {
		LibraryResponse libraryResponse = new LibraryResponse();
		Message message = new Message();
		try {
			playlistSongService.addToplaylist(playlistSong);
			List<Playlist> playlists = playlistService.getPlaylist(playlistSong.getUser());
			libraryResponse.setRecentlyPlayedPlaylists(playlists);
			message.setMessage("saving successful.");
		} catch (Exception e) {
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

	@RequestMapping(value = "/playPlaylist")
	public PlaylistSongsResponse playPlaylist(@RequestBody long id) {
		PlaylistSongsResponse ppResponse = new PlaylistSongsResponse();
		Playlist playlist = new Playlist();
		Message message = new Message();
		try {
			playlist = playlistService.getSongsFromPlaylist(id);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the web service");
		}
		message.setFlag(true);
		ppResponse.setPlaylist(playlist);
		return ppResponse;
	}
}
