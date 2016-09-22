package com.blackparty.syntones.endpoint;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.core.AssociationRule;
import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.Song;

import com.blackparty.syntones.model.TemporaryDB;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.ThreeItemSetCombo;
import com.blackparty.syntones.model.TwoItemSet;
import com.blackparty.syntones.model.TwoItemSetCombo;

import com.blackparty.syntones.model.Tag;

import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.ListenResponse;
import com.blackparty.syntones.response.LogoutResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.RemovePlaylistResponse;
import com.blackparty.syntones.response.RemoveToPlaylistResponse;

import com.blackparty.syntones.response.ThreeItemSetResponse;
import com.blackparty.syntones.response.TwoItemSetResponse;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongService;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import com.blackparty.syntones.response.TagsResponse;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.TagService;
import com.sun.media.rtsp.protocol.Request;

@RestController
@Component
public class MusicEndpoint {

	@Autowired
	private PlayedSongsService playedSongsService;

	@Autowired
	private SongService songService;
	@Autowired
	private PlaylistService playlistService;
	@Autowired
	private PlaylistSongService playlistSongService;
	@Autowired
	private TagService tagService;

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
	public ListenResponse listen(@RequestBody List<TemporaryDB> temporaryDB) {
		System.out.println("Received request to listen.");

		ListenResponse listenResponse = new ListenResponse();
		Message message = new Message();

		if (temporaryDB != null) {
			message.setMessage("I GOT THE SONG ID AND THE USER ID!");
			for (TemporaryDB a : temporaryDB) {

				System.out.println("Listen: \nSong ID: " + a.getSong_id() + "User ID: " + a.getUser_id());

			}
			try {
				playedSongsService.saveTemporaryDB(temporaryDB);
			} catch (DataIntegrityViolationException e) {

			}
		} else {
			message.setMessage("NO SONG ID AND USER ID<|3");
		}
		message.setFlag(true);
		listenResponse.setMessage(message);

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

	@RequestMapping(value = "/getTwoItemSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public TwoItemSetResponse getTwoItemSet() {
		System.out.println("Received request to get Two Item Sets");
		TwoItemSetResponse twoItemSetResponse = new TwoItemSetResponse();
		Message message = new Message();
		message.setMessage("FETCHING TWO ITEM SETS");

		List<TwoItemSet> two_item_set_list = playedSongsService.getTwoItemSet();
		if (!two_item_set_list.isEmpty()) {
			message = new Message("Fetching two item set is successful.", true);
		} else {
			message = new Message("Query returns zero results.", true);
		}

		twoItemSetResponse.setMessage(message);
		twoItemSetResponse.setTwo_item_set_list(two_item_set_list);
		return twoItemSetResponse;
	}

	@RequestMapping(value = "/getThreeItemSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ThreeItemSetResponse getThreeItemSet() {
		System.out.println("Received request to get Two Item Sets");
		ThreeItemSetResponse threeItemSetResponse = new ThreeItemSetResponse();
		Message message = new Message();
		message.setMessage("FETCHING THREE ITEM SETS");

		List<ThreeItemSet> three_item_set_list = playedSongsService.getThreeItemSet();
		if (!three_item_set_list.isEmpty()) {
			message = new Message("Fetching three item set is successful.", true);
		} else {
			message = new Message("Query returns zero results.", true);
		}

		threeItemSetResponse.setMessage(message);
		threeItemSetResponse.setThree_item_set_list(three_item_set_list);
		return threeItemSetResponse;
	}

	@RequestMapping(value = "/logoutProcess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public LogoutResponse logout() throws SQLException {
		System.out.println("Received request logout");
		LogoutResponse logoutResponse = new LogoutResponse();
		Message message = new Message();
		message.setMessage("You have logged out");
		List<TemporaryDB> temporaryDB = playedSongsService.getTemporaryDB();

		for (TemporaryDB a : temporaryDB) {
			System.out.println(a.getUser_id());
			PlayedSongs playedSongs = new PlayedSongs();
			playedSongs.setTrack_id(String.valueOf(a.getSong_id()));
			playedSongs.setUser_id(a.getUser_id());
			playedSongsService.savePlayedSongs(playedSongs);
		}

		playedSongsService.truncateTemporaryDB();

		List<PlayedSongs> played_songs_list = playedSongsService.getPlayedSongs();

		System.out.println("AR - ADMIN CONTROLLER");
		AssociationRule ar = new AssociationRule();

		ArrayList<String> track_id_list = ar.getUniqueOneItemTracks(played_songs_list);
		ArrayList<String> session_id_list = ar.getUniqueSessions(played_songs_list);

		if (session_id_list.size() > 1) {

			playedSongsService.truncateOneItemSetCount();
			playedSongsService.truncateTwoItemSet();
			playedSongsService.truncateThreeItemSet();

			int[][] oneItemBasket = ar.getOneItemBasket(played_songs_list, session_id_list, track_id_list);

			ArrayList<OneItemSetCount> one_item_set_count_list = ar.getOneItemCount(session_id_list, oneItemBasket,
					played_songs_list, track_id_list);
			playedSongsService.insertOneItemSetCount(one_item_set_count_list);
			ArrayList<TwoItemSetCombo> two_item_set_combo_list = ar.getTwoItemCombo(track_id_list);

			int[][] twoItemBasket = ar.getTwoItemBasket(two_item_set_combo_list, oneItemBasket, track_id_list,
					session_id_list);

			ArrayList<TwoItemSet> two_item_set_list = ar.getTwoItemSet(one_item_set_count_list, track_id_list,
					twoItemBasket, two_item_set_combo_list, session_id_list);

			playedSongsService.insertTwoItemSet(two_item_set_list);

			ArrayList<ThreeItemSetCombo> three_item_set_combo_list = ar.getThreeItemCombo(track_id_list);
			int[][] threeItemBasket = ar.getThreeItemBasket(oneItemBasket, track_id_list, three_item_set_combo_list,
					twoItemBasket, two_item_set_combo_list, session_id_list);

			ArrayList<ThreeItemSet> three_item_set_list = ar.getThreeItemSet(three_item_set_combo_list,
					two_item_set_list, threeItemBasket, session_id_list);

			playedSongsService.insertThreeItemSet(three_item_set_list);

		}

		logoutResponse.setMessage(message);
		return logoutResponse;
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
