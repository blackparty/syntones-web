package com.blackparty.syntones.endpoint;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.blackparty.syntones.model.TemporaryDB;
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
import com.blackparty.syntones.service.PlayedSongsService;
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
	@Autowired
	private PlayedSongsService playedSongsService;
	@Autowired
	private ArtistService artistService;
	@Autowired
	private SongLineService songLineService;

	@RequestMapping(value = "/generatePlaylistByTags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse generatePlaylistByTags(@RequestBody Tag tagObject) {
		GeneratePlaylistResponse generatePlaylistResponse = new GeneratePlaylistResponse();
		System.out.println("Received request to generate playlist for : ");
		List<TagSong> tagsongs = new ArrayList<>();
		Message message = new Message();
		try {
			System.out.println(tagObject.getTag());
			tagsongs = tagSongService.getSongsByTags(tagObject.getTag());
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

	@RequestMapping(value = "/generatePlaylistByArtist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse generatePlaylistByArtist(@RequestBody String artistName) {
		GeneratePlaylistResponse generatePlaylistResponse = new GeneratePlaylistResponse();
		System.out.println("Received request to generate playlist using artist: " + artistName);
		Message message = new Message();
		try {
			// getting all songs of the artist
			artistName = artistName.replace("\"", "");
			Artist artist = artistService.getArtist(artistName);
			List<Long> artistSongIds = songService.getAllSongsByArtist(artist);
			List<SongLine> songLines = songLineService.getAllLines();
			ArrayList<Long> songIds = new ArrayList<>();
			HashMap<Long, Long> map = new HashMap<>();
			for (long s : artistSongIds) {
				System.out.println("Artist song id: " + s);
			}
			for (int i = 0; i < songLines.size(); i++) {
				for (int j = 0; j < artistSongIds.size(); j++) {
					if (songLines.get(i).getSongId() == artistSongIds.get(j)) {
						System.out.println("hit!!");
						long id1 = 0;
						long id2 = 0;
						if (i == 0) {
							id1 = songLines.get(i + 1).getSongId();
						} else {
							id1 = songLines.get(i + 1).getSongId();
							id2 = songLines.get(i - 1).getSongId();
						}
						if (!map.containsKey(id1)) {
							map.put(id1, id1);
							songIds.add(id1);
							System.out.println("Adding song :" + id1);
						}
						if (!map.containsKey(id2)) {
							map.put(id2, id2);
							songIds.add(id2);
							System.out.println("Adding song :" + id2);
						}
					}
				}
			}
			// adding artist's song
			List<Song> songList = new ArrayList<>();
			int counter = 0;
			if (!songIds.isEmpty()) {
				for (Long id : songIds) {
					if (counter < 10) {
						Song song = songService.getSong(id);
						songList.add(song);
					}
					counter++;
				}
				System.out.println("Generating Songs successful.");
				for (Song s : songList) {
					System.out.println(s.toString());
				}
				message.setMessage("Generating Songs successful.");
				message.setFlag(true);
				generatePlaylistResponse.setSongs(songList);
			} else {
				message.setFlag(false);
				System.out.println("List is empty. No lists of songs generated.");
				message.setMessage("List is empty. No lists of songs generated.");
			}
		} catch (Exception e) {
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

	@RequestMapping(value = "/getAllTags", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public TagsResponse getAllTags() {
		TagsResponse tagResponse = new TagsResponse();
		List<String> fetchedTags;
		Message message = new Message();
		try {
			fetchedTags = tagSongService.getAvailableTags();
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Exception occured on the webservice");
			message.setFlag(false);
			tagResponse.setMessage(message);
			return tagResponse;
		}
		tagResponse.setTags(fetchedTags);
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
	public ListenResponse listen(@RequestBody List<TemporaryDB> temporaryDB) {

		ListenResponse listenResponse = new ListenResponse();
		// capture user's date and time when the music is played.
		Message message = new Message();
		try {
			Date dateObject = new Date();
			Timestamp timeStamp = new Timestamp(dateObject.getTime());
			System.out.println("Received request to listen for song from: " + temporaryDB.get(0).getUser_id()
					+ " Date and time: " + timeStamp);

			if (temporaryDB.isEmpty()) {
				message.setMessage("I GOT THE SONG ID AND THE USER ID!");
				// setting time and date to each song
				for (TemporaryDB a : temporaryDB) {
					System.out.println("Listen: \nSong ID: " + a.getSong_id() + "User ID: " + a.getUser_id());
					a.setDateTimePlayed(timeStamp);
				}
				playedSongsService.saveTemporaryDB(temporaryDB);
			} else {
				message.setMessage("NO SONG ID AND USER ID<|3");
			}
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		message.setFlag(true);
		listenResponse.setMessage(message);
		return listenResponse;

	}

	@RequestMapping(value = "/listenPlaylist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ListenResponse listenPlaylist(@RequestBody Playlist playlist) {

		ListenResponse lResponse = new ListenResponse();
		Message message = new Message();
		// insert service that will fetch recommended songs to be added on
		// listenresponse
		try {
			Date dateObject = new Date();
			Timestamp timeStamp = new Timestamp(dateObject.getTime());
			System.out.println("Received request to listen for playlist: " + playlist.getPlaylistId() + " from: "
					+ playlist.getUser().getUsername() + " Date and time: " + timeStamp);
			playlistService.updateLastPlayedPlaylist(playlist.getPlaylistId(), timeStamp);
			message.setFlag(true);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the web service.");
			lResponse.setMessage(message);
			return lResponse;
		}
		lResponse.setMessage(message);
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

	@RequestMapping(value = "/playPlaylist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public PlaylistSongsResponse playPlaylist(@RequestBody Playlist playlist) throws Exception {
		System.out.println("Received request to play a playlist: " + playlist.getPlaylistId() + " from "
				+ playlist.getUser().getUsername());
		PlaylistSongsResponse ppResponse = new PlaylistSongsResponse();
		Playlist fetchedPlaylist = new Playlist();
		Message message = new Message();
		
		try {
			fetchedPlaylist = playlistService.getSongsFromPlaylist(playlist.getPlaylistId());
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
