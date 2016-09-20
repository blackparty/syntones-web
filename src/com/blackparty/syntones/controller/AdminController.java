package com.blackparty.syntones.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.ArtistWordBankProcess;
import com.blackparty.syntones.core.FileCopy;
import com.blackparty.syntones.core.ID3Extractor;
import com.blackparty.syntones.core.LyricsExtractor;
import com.blackparty.syntones.core.SongWordBankProcess;
import com.blackparty.syntones.core.TrackSearcher;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.TemporaryModel;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;

@Controller
public class AdminController {
	@Autowired
	ArtistService as;

	@Autowired
	SongService ss;
	
	@Autowired
	SongWordBankService sservice;

	@Autowired
	ArtistWordBankService aservice;
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
				File file = new File(multiPartFile.getOriginalFilename());
				multiPartFile.transferTo(file);
				System.out.println("file name: " + file.getName());
				FileCopy fc = new FileCopy();
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
					Song songResult = new Song();
					TrackSearcher ts = new TrackSearcher();
					songResult = ts.search(song);

					// extracting lyrics to the database
					LyricsExtractor le = new LyricsExtractor();
					List<String> lyrics = le.getSongLyrics(songResult.getArtistName(), songResult.getSongTitle());

					System.out.println(lyrics);

					mav.addObject("artistName", songResult.getArtistName());
					mav.addObject("songTitle", songResult.getSongTitle());
					request.getSession().setAttribute("song", songResult);
					request.getSession().setAttribute("lyrics", lyrics);
					request.getSession().setAttribute("file",file);
				}
			} else {
				System.out.println("Saving song to the server...");
				// save the artist to the database
				Artist artist = new Artist(artistName);
				as.addArtist(artist);

				// add the file, lyrics and artist to song object
				Song song = (Song) request.getSession().getAttribute("song");
				song.setArtist(artist);
				song.setLyrics((List) request.getSession().getAttribute("lyrics"));
				song.setFile((File)request.getSession().getAttribute("file"));
				// save song to the database
				ss.addSong(song);
				
				//word bank process
				ArrayList<Song> songs = ss.fetchAllSong();
				ArrayList<Artist> artists = as.fetchAllArtist();
				if (!songs.isEmpty() && !artists.isEmpty()) {
					SongWordBankProcess swb = new SongWordBankProcess();
					TemporaryModel tm = swb.WBSongProcess(songs);
					songs = tm.getSongs();
					ArrayList<SongWordBank> words = tm.getWords();

					ss.updateAllSong(songs);
					sservice.updateWordBank(words);

					ArtistWordBankProcess awb = new ArtistWordBankProcess();
					tm = awb.WBArtistProcess(artists);

					as.updateAllArtist(tm.getArtists());
					aservice.updateWordBank(tm.getAwords());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

}
