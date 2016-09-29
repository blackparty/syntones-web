package com.blackparty.syntones.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.ArtistWordBankProcess;
import com.blackparty.syntones.core.AssociationRule;
import com.blackparty.syntones.core.FileCopy;
import com.blackparty.syntones.core.ID3Extractor;
import com.blackparty.syntones.core.LyricsExtractor;
import com.blackparty.syntones.core.SongWordBankProcess;
import com.blackparty.syntones.core.TrackSearcher;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.TemporaryModel;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.ThreeItemSetCombo;
import com.blackparty.syntones.model.ThreeItemSetRecomSong;
import com.blackparty.syntones.model.TwoItemSet;
import com.blackparty.syntones.model.TwoItemSetCombo;
import com.blackparty.syntones.model.TwoItemSetRecomSong;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;

@Controller

@RequestMapping("/admin")

public class AdminController {
	@Autowired
	ArtistService as;
	
	@Autowired
	ArtistWordBankService aservice;

	@Autowired
	SongService ss;


	@Autowired
	SongWordBankService sservice;

	@Autowired
	PlayedSongsService playedSongsService;
	
	@RequestMapping(value = "/upload")
	public ModelAndView readSongFile(@RequestParam(value = "file") MultipartFile multiPartFile,
			@RequestParam(value = "action") String action, @RequestParam(value = "artistName") String artistName,
			@RequestParam(value = "songTitle") String songTitle, HttpServletRequest request) {
		String tempDirectory = "D:/data/temp/";
		ModelAndView mav = new ModelAndView("index");
		// please validate format of the file to be uploaded

		try {
			if (action.equals("read")) {
				// read the mp3 file first..
				// converting multipartfile into file
				System.out.println(multiPartFile.getOriginalFilename());
				File file = new File("D:/deletables/" + multiPartFile.getOriginalFilename());
				multiPartFile.transferTo(file);
				System.out.println("file name: " + file.getName());
				// FileCopy fc = new FileCopy();
				Song song = null;
				// boolean flag = fc.copyFileUsingFileStreams(file);
				ID3Extractor id3 = new ID3Extractor();
				song = id3.readTags(file.getAbsolutePath());
				if (song == null) {
					mav.addObject("message",
							"Cant read any tags on the given file. Please provide title and artist instead.");
					System.out.println("cant read any tags on the given file");
				} else {

					// validate the information via net
					// Song songResult = new Song();
					// TrackSearcher ts = new TrackSearcher();
					// songResult = ts.search(song);

					// extracting lyrics to the database
					LyricsExtractor le = new LyricsExtractor();
					List<String> lyrics = null;
					lyrics = le.getSongLyrics(song.getArtistName(), song.getSongTitle());
					if (lyrics == null) {
						// validate the information via net
						System.out.println("cant get lyrics on the given artist and title.");
						Song songResult = new Song();
						TrackSearcher ts = new TrackSearcher();
						songResult = ts.search(song);
						lyrics = le.getSongLyrics(song.getArtistName(), song.getSongTitle());
					}

					System.out.println(lyrics);

					mav.addObject("artistName", song.getArtistName());
					mav.addObject("songTitle", song.getSongTitle());
					request.getSession().setAttribute("song", song);
					request.getSession().setAttribute("lyrics", lyrics);
					request.getSession().setAttribute("file", file);
				}
			} else {
				System.out.println("Saving song to the server...");
				// save the artist to the database
				Artist artist = new Artist();
				artist.setArtistName(artistName);
				as.addArtist(artist);

				// add the file, lyrics and artist to song object
				Song song = (Song) request.getSession().getAttribute("song");
				song.setArtist(artist);
				song.setLyrics((List) request.getSession().getAttribute("lyrics"));
				song.setFile((File) request.getSession().getAttribute("file"));
				// save song to the database
				ss.addSong(song);
				
				//word bank process
				List<Song> songs = ss.getAllSongs();
				List<Artist> artists = as.getAllArtists();
				if (!songs.isEmpty() && !artists.isEmpty()) {
					SongWordBankProcess swb = new SongWordBankProcess();
					TemporaryModel tm = swb.WBSongProcess(songs);
					songs = tm.getSongs();
					List<SongWordBank> words = tm.getWords();

					ss.updateBatchAllSongs(songs);
					sservice.updateWordBank(words);

					ArtistWordBankProcess awb = new ArtistWordBankProcess();
					tm = awb.WBArtistProcess(artists);

					as.updateBatchAllArtist(tm.getArtists());
					aservice.updateWordBank(tm.getAwords());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/songList", method = RequestMethod.GET)
	public ModelAndView showSongList() {
		ModelAndView mav = new ModelAndView("songList");
		// return lists of songs to the database;
		try {
			List<Song> songList = ss.getAllSongs();
			mav.addObject("songList", songList);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("system_message", "there is an error when fetching to the database.");
		}
		return mav;
	}

	// Association Rule
	@RequestMapping(value = "/arRecom")
	public String viewAllSongs(HttpServletRequest request, HttpServletResponse response, ModelMap map)
			throws Exception {
		AssociationRule ar = new AssociationRule();

		List<PlayedSongs> played_songs_list = playedSongsService.getPlayedSongs();
		System.out.println("AR - ADMIN CONTROLLER");

		ArrayList<String> track_id_list = ar.getUniqueOneItemTracks(played_songs_list);
		ArrayList<Long> session_id_list = ar.getUniqueSessions(played_songs_list);

		int[][] oneItemBasket = ar.getOneItemBasket(played_songs_list, session_id_list, track_id_list);

		ArrayList<OneItemSetCount> one_item_set_count_list = ar.getOneItemCount(session_id_list, oneItemBasket,
				played_songs_list, track_id_list);

//		if (playedSongsService.getOneItemSetCount().isEmpty()) {
//			playedSongsService.insertOneItemSetCount(one_item_set_count_list);
//		}

		ArrayList<TwoItemSetCombo> two_item_set_combo_list = ar.getTwoItemCombo(track_id_list);

		int[][] twoItemBasket = ar.getTwoItemBasket(two_item_set_combo_list, oneItemBasket, track_id_list,
				session_id_list);

		ArrayList<TwoItemSet> two_item_set_list = ar.getTwoItemSet(one_item_set_count_list, track_id_list,
				twoItemBasket, two_item_set_combo_list, session_id_list);

//		if (playedSongsService.getTwoItemSet().isEmpty()) {
//			playedSongsService.insertTwoItemSet(two_item_set_list);
//		}

		ArrayList<ThreeItemSetCombo> three_item_set_combo_list = ar.getThreeItemCombo(track_id_list);
		int[][] threeItemBasket = ar.getThreeItemBasket(oneItemBasket, track_id_list, three_item_set_combo_list,
				twoItemBasket, two_item_set_combo_list, session_id_list);

		ArrayList<ThreeItemSet> three_item_set_list = ar.getThreeItemSet(three_item_set_combo_list, two_item_set_list,
				threeItemBasket, session_id_list);

//		if (playedSongsService.getThreeItemSet().isEmpty()) {
//			playedSongsService.insertThreeItemSet(three_item_set_list);
//		}

		ArrayList<TwoItemSetRecomSong> two_item_recom_song_list = ar.getTwoItemRecomSong(two_item_set_list);
		ArrayList<ThreeItemSetRecomSong> three_item_recom_song_list = ar.getThreeItemRecomSong(three_item_set_list);

		System.out.println("TWO ITEM RECOM");
		for (TwoItemSetRecomSong a : two_item_recom_song_list) {

			System.out.println(a.getRecom_song() + " - " + a.getConfidence());
		}
		System.out.println("\nTHREE ITEM RECOM");
		for (ThreeItemSetRecomSong b : three_item_recom_song_list) {

			System.out.println(b.getRecom_song() + " - " + b.getConfidence());
		}

		return "index";

	}

}
