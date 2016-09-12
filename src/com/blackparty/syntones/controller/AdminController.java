package com.blackparty.syntones.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.FileCopy;
import com.blackparty.syntones.core.ID3Extractor;
import com.blackparty.syntones.core.LyricsExtractor;
import com.blackparty.syntones.core.TrackSearcher;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.SongService;

@Controller

@RequestMapping("/admin")

public class AdminController {
	@Autowired
	ArtistService as;

	@Autowired
	SongService ss;

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
				File file = new File("E:/deletables/"+multiPartFile.getOriginalFilename());
				multiPartFile.transferTo(file);
				System.out.println("file name: " + file.getName());
				//FileCopy fc = new FileCopy();
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
					//Song songResult = new Song();
					//TrackSearcher ts = new TrackSearcher();
					//songResult = ts.search(song);

					// extracting lyrics to the database
					LyricsExtractor le = new LyricsExtractor();
					List<String> lyrics = le.getSongLyrics(song.getArtistName(), song.getSongTitle());

					System.out.println(lyrics);

					mav.addObject("artistName", song.getArtistName());
					mav.addObject("songTitle", song.getSongTitle());
					request.getSession().setAttribute("song", song);
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

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	@RequestMapping(value="/songList",method=RequestMethod.GET)
	public ModelAndView showSongList(){
		ModelAndView mav = new ModelAndView("songList");
		//return lists of songs to the database;
		try{
			List<Song> songList= ss.getAllSongs();
			mav.addObject("songList", songList);
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("system_message", "there is an error when fetching to the database.");
		}
		return mav;
	}

}
