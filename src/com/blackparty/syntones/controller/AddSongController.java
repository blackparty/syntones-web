package com.blackparty.syntones.controller;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
//import java.rmi.UnknownHostException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.ArtistIDFProcess;
import com.blackparty.syntones.core.ID3Extractor;
import com.blackparty.syntones.core.LyricsExtractor;
import com.blackparty.syntones.core.SongIDFProcess;
import com.blackparty.syntones.core.Summarize;
import com.blackparty.syntones.core.Tagger;
import com.blackparty.syntones.core.TrackSearcher;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.ArtistWordBank;
import com.blackparty.syntones.model.IDFReturn;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.model.TagSong;
import com.blackparty.syntones.model.TagSynonym;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.SongLineService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;
import com.blackparty.syntones.service.TagService;
import com.blackparty.syntones.service.TagSongService;
import com.blackparty.syntones.service.TagSynonymService;

@Controller
@RequestMapping(value = "/admin")
public class AddSongController {
	@Autowired
	private ArtistService as;
	@Autowired
	private SongService ss;
	@Autowired
	private PlayedSongsService playedSongsService;
	@Autowired
	private SongLineService songLineService;
	@Autowired
	private TagSynonymService tagSynonymService;
	@Autowired
	private TagSongService tagSongService;
	@Autowired
	private SongService songService;
	@Autowired
	private TagService tagService;
	@Autowired SongWordBankService swService;
	@Autowired ArtistWordBankService awService;

	@RequestMapping(value = "/fetchLyrics")
	public ModelAndView fetchLyrics(@RequestParam("songTitle") String songTitle,
			@RequestParam("artistName") String artistName, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("showLyrics");
		LyricsExtractor le = new LyricsExtractor();
		List<String> lyrics = null;
		try {
			lyrics = le.getSongLyrics(artistName, songTitle);
		} catch (SocketTimeoutException timeout) {
			mav.addObject("system_message", "timeoue exception, please click \"read\" again.");
			mav.setViewName("mp3Details");
			mav.addObject("artistName", artistName);
			mav.addObject("songTitle", songTitle);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("system_message",
					"Cant find lyrics due to unknown artist / title. Please provide correct song details");
			mav.setViewName("askDetails");
			return mav;
		}
		HttpSession session = request.getSession();
		mav.addObject("artistName", artistName);
		mav.addObject("songTitle", songTitle);
		mav.addObject("system_message", "fetcing lyrics successful.");
		session.setAttribute("lyrics", lyrics);
		return mav;
	}

	@RequestMapping(value = "/checkDetails", method = RequestMethod.POST)
	public ModelAndView askDetails(@RequestParam("artistName") String artistName,
			@RequestParam("songTitle") String songTitle, HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// validates given artist and song title
		// validate the information via net
		System.out.println("Validating song details provided by the admin.");
		Song song = new Song();
		song.setArtistName(artistName);
		song.setSongTitle(songTitle);
		Song songResult = new Song();
		try {
			TrackSearcher ts = new TrackSearcher();
			songResult = ts.search(song);
			if(songResult != null){
				System.out.println("SONG RESULT! = " + songResult);
				mav.addObject("succ_message", "Details has been validated.");
				mav.addObject("artistName", songResult.getArtistName());
				mav.addObject("songTitle", songResult.getSongTitle());				
				mav.setViewName("mp3Details");
				

				//fetch lyrics
				System.out.println("i'm here");
				request.getSession().setAttribute("file", request.getSession().getAttribute("file"));
				System.out.println("Reading tags successful.");
				mav.addObject("succ_message", "Reading on the mp3 tags is successful.");
				mav.addObject("artistName", song.getArtistName());
				mav.addObject("songTitle", song.getSongTitle());
				LyricsExtractor lyricsExtractor = new LyricsExtractor();
				List<String> lyrics = lyricsExtractor.getSongLyrics(song.getArtistName(), song.getSongTitle());
				System.out.println("saving song and file to session.");
				System.out.println(song.displayTitleAndArtist());
				request.getSession().setAttribute("lyrics", lyrics);
								
				
			}else{
				mav.addObject("artistName",artistName);
				mav.addObject("songTitle", songTitle);
				mav.addObject("err_message", "An error occured, click \"save\" ");
			}
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
			mav.addObject("err_message", "Exception occured.\nPlease enter song lyrics manually.");
			mav.setViewName("showLyrics");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mav.addObject("err_message", "Exception occured.\nPlease enter song lyrics manually.");
			mav.setViewName("showLyrics");
		}
		return mav;
	}

	@RequestMapping(value = "/saveSong")
	public ModelAndView saveSong(@RequestParam("artistName") String artistName,
			@RequestParam("songTitle") String songTitle, HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		System.out.print("Saving artist to the server...");
		// save the artist to the database
		Artist artist = new Artist();
		artist.setArtistName(artistName);
		System.out.println("saveSong().artistName " + artistName);
		as.addArtist(artist);
		// add the file, lyrics and artist to song object
		Song song = new Song();
		song.setSongTitle(songTitle);
		song.setArtistName(artistName);
		List<String> lyrics = (List) request.getSession().getAttribute("lyrics");
		if(lyrics != null){
			song.setLyrics(lyrics);
			song.setFlag(true);
		}else{
			ArrayList<String> lyrics1 = new ArrayList();
			song.setFlag(false);
			String lyrics_input = request.getParameter("lyrics");
			String[] temp = lyrics_input.split("\n");
			for(String str : temp){
				lyrics1.add(str);
			}
			song.setLyrics(lyrics1);
		}
		song.setFile((File) request.getSession().getAttribute("file"));
		System.out.print("Saving song to the server...");
		// save song to the database

		try {
			long songId = ss.addSong(song);
			song.setSongId(songId);
		
		//word bank process
			List<Song> songs = ss.getAllSongs();
			int songCount = ss.songCount();
			int artistCount = as.artistCount();
			List<Artist> artists = as.getAllArtists();
			if (!songs.isEmpty() && !artists.isEmpty()) {
				SongIDFProcess sip = new SongIDFProcess();
				IDFReturn ir = sip.SongIdfProcess(songs, songCount);
				
				ArtistIDFProcess aip = new ArtistIDFProcess();
				IDFReturn irArtist = aip.ArtistIdfProcess(artists, artistCount);
				
				awService.updateWordBank(irArtist.getAwb());
				as.updateBatchAllArtist(irArtist.getArtists());
				
				swService.updateWordBank(ir.getSwb());				
				ss.updateBatchAllSongs(ir.getSongs());
			}
			/*			if (!songs.isEmpty() && !artists.isEmpty()) {
				SongWordBankProcess swb = new SongWordBankProcess();
				TemporaryModel tm = swb.WBSongProcess((ArrayList<Song>) songs);
				songs = tm.getSongs();
				List<SongWordBank> words = tm.getWords();

				ss.updateBatchAllSongs(songs);
				sservice.updateWordBank(words);

				ArtistWordBankProcess awb = new ArtistWordBankProcess();
				tm = awb.WBArtistProcess((ArrayList<Artist>) artists);

				as.updateBatchAllArtist(tm.getArtists());
				aservice.updateWordBank(tm.getAwords());
			}
			

			songLineService.truncateTable();
			// rework global line ranking
			List<Song> songList = songService.getAllSongsFromDb();
			Summarize summarize = new Summarize();
			List<SongLine> songLines = summarize.start(songList);
			songLineService.saveBatchSongLines(songLines);
			List<SongLine> finishedSongLine = songLineService.getAllLines();
			System.out.println("FINISHED SONGLINE");
			for (SongLine sl : finishedSongLine) {
				System.out.println(sl.toString());
			}
			List<Tag> tags = tagService.getAllTags();
			
			
			
			// gets its corresponding synonyms
			for (int i = 0; i < tags.size(); i++) {
				List<TagSynonym> synonyms = tagSynonymService.getTagSynonym(tags.get(i).getId());
				tags.get(i).setSynonyms(synonyms);
			}
			Tagger tagger = new Tagger();

			List<TagSong> tagSong = tagger.start(song, tags);
			for (TagSong ts : tagSong) {
				System.out.println(ts.toString());
			}
			// save
			tagSongService.saveBatchTagSong(tagSong);
*/
		} catch (Exception e) {
			e.printStackTrace();
		}
//
//		HttpSession session = request.getSession();
//		session.invalidate();
		String offset = (String) request.getParameter("offSet");
		int size;
		List<Song> songList;
		if (offset != null) {
			int offsetreal = Integer.parseInt(offset);
			offsetreal = offsetreal * 10;
			songList = songService.displaySong(offsetreal);
		} else {
			songList = songService.displaySong(0);
			size = (int) songService.songCount();
			session.setAttribute("size", size / 10);
		}
		mav.addObject("songList", songList);
		System.out.println("song saved successfully.");
		mav.setViewName("index");
		mav.addObject("succ_message", "Song saved successfully.");
		return mav;
	}

	@RequestMapping(value = "/readMp3")
	public ModelAndView readMp3(@RequestParam(value = "file") MultipartFile multiPartFile, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		try {
			System.out.println(multiPartFile.getOriginalFilename());
			File file = new File(multiPartFile.getOriginalFilename());
			multiPartFile.transferTo(file);
			System.out.println("file name: " + file.getName());
			// FileCopy fc = new FileCopy();
			Song song = null;
			// boolean flag = fc.copyFileUsingFileStreams(file);
			ID3Extractor id3 = new ID3Extractor();
			song = id3.readTags(file.getAbsolutePath());
			HttpSession session = request.getSession();
			if (song.getArtistName() == null) {
				mav.addObject("err_message",
						"Cant read any tags on the given file. Please provide title and artist instead.");
				mav.setViewName("askDetails");
				System.out.println("cant read any tags on the given file");
				session.setAttribute("file", file);
			} else {
				
				//fetch lyrics
				request.getSession().setAttribute("file", file);
				System.out.println("Reading tags successful.");
				mav.addObject("succ_message", "Reading on the mp3 tags is successful.");
				mav.addObject("artistName", song.getArtistName());
				mav.addObject("songTitle", song.getSongTitle());
				LyricsExtractor lyricsExtractor = new LyricsExtractor();
				List<String> lyrics = lyricsExtractor.getSongLyrics(song.getArtistName(), song.getSongTitle());
				System.out.println("saving song and file to session.");
				System.out.println(song.displayTitleAndArtist());
				request.getSession().setAttribute("lyrics", lyrics);
				
				mav.setViewName("mp3Details");
			}
		}catch(HttpStatusException e){
			System.out.println("Attempting to get lyrics to the internet had failed.");
			mav.addObject("system_message", "Attempting to get lyrics to the internet had failed.");
			mav.setViewName("askDetails");
		}catch(UnknownHostException e){
			e.printStackTrace();
			mav.setViewName("askDetails");
		}
		catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("askDetails");
		}
		return mav;
	}
	

	@RequestMapping(value="/viewLyrics")
	public ModelAndView viewSong(@RequestParam("songId") long songId){
		ModelAndView mav = new ModelAndView("viewLyrics");
		try {
			Song song = songService.getSong(songId);
			mav.addObject("song",song);
		} catch (Exception e) {
			System.out.println("something went wrong!");
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/addSongPage")
	public ModelAndView showAddSongPage() {
		ModelAndView mav = new ModelAndView("addSong");
		return mav;
	}
	
	@RequestMapping(value="/edit")
	public ModelAndView updateSong(@RequestParam("songId") long songId) throws Exception{
		ModelAndView mav = new ModelAndView("updateSong");
		Song song = songService.getSong(songId);
		mav.addObject("song",song);
		return mav;
	}

	@RequestMapping(value="/updateSong")
	public ModelAndView saveUpdatedSong(@RequestParam("songId") long songId,
			@RequestParam("lyrics") String lyrics, HttpServletRequest request, HttpSession session) throws Exception{
		ModelAndView mav = new ModelAndView("index");
		try{
			//word bank process
	/*		List<Song> songs = ss.getAllSongs();
			if (!songs.isEmpty()) {
				SongWordBankProcess swb = new SongWordBankProcess();
				TemporaryModel tm = swb.WBSongProcess((ArrayList<Song>) songs);
				songs = tm.getSongs();
				List<SongWordBank> words = tm.getWords();

				ss.updateBatchAllSongs(songs);
				sservice.updateWordBank(words);
			}
			*/

		} catch (Exception e) {
			e.printStackTrace();
		}
//
//		HttpSession session = request.getSession();
//		session.invalidate();
		String offset = (String) request.getParameter("offSet");
		int size;
		List<Song> songList;
		if (offset != null) {
			int offsetreal = Integer.parseInt(offset);
			offsetreal = offsetreal * 10;
			songList = songService.displaySong(offsetreal);
		} else {
			songList = songService.displaySong(0);
			size = (int) songService.songCount();
			session.setAttribute("size", size / 10);
		}
		mav.addObject("songList", songList);
		System.out.println("song saved successfully.");
		mav.setViewName("index");
		mav.addObject("succ_message", "Song saved successfully.");
		return mav;
	}
}
