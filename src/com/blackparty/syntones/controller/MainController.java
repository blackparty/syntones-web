package com.blackparty.syntones.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.SearchProcess;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.SampleModel;
import com.blackparty.syntones.model.SearchResultModel;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.SampleModelService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;

@Controller
public class MainController {
	@Autowired
	ArtistService as;

	@Autowired
	SongService ss;

	@Autowired
	SongWordBankService sservice;

	@Autowired
	ArtistWordBankService aservice;

	@Autowired
	SampleModelService sampleModelService;

	@RequestMapping(value = "/")
	public String defaultPage() {
		return "index";

	}

	@RequestMapping(value = "/index")
	public ModelAndView indexPage() {
		ModelAndView mav = new ModelAndView("index", "message",
				"Running MainController.index().");

		return mav;
	}

	/*
	 * @RequestMapping(value = "/upload") public ModelAndView
	 * save(@RequestParam(value = "input") String input){ ModelAndView mav = new
	 * ModelAndView("index"); SampleModel sm = new SampleModel(input);
	 * sampleModelService.addSampleModel(sm); return mav; }
	 */

	@RequestMapping(value = "/search")
	public ModelAndView searchSong(
			@RequestParam(value = "searchSong") String searchSong,
			HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("index");

		SearchProcess sp = new SearchProcess();
		SearchResultModel searchResult = sp.SearchProcess(searchSong,
				aservice.fetchAllWordBank(), sservice.fetchAllWordBank(),
				ss.fetchAllSong(), as.fetchAllArtist());

		ArrayList<Song> songsPrio = new ArrayList<>();
		ArrayList<Song> songs = new ArrayList<>();
		ArrayList<Artist> artists = as.getArtists(searchResult.getArtists());

		if (!searchResult.isArtistNan() && !searchResult.isSongNan()) {
			for (Song song : ss.getSongs(searchResult.getSongs())) {
				for (Artist artist : artists) {
					if (song.getArtist().getArtistId() == artist.getArtistId()) {
						songsPrio.add(song);
					} else {
						songs.add(song);
					}

				}

			}
			request.getSession().setAttribute("resultSongPrio", songsPrio);
			request.getSession().setAttribute("resultArtist", artists);
			request.getSession().setAttribute("resultSong", songs);
			return mav;
		} else if (searchResult.isSongNan() && !searchResult.isArtistNan()) {
			request.getSession().setAttribute("resultArtist", artists);
			return mav;
		} else if (searchResult.isArtistNan() && !searchResult.isSongNan()) {
			
			request.getSession().setAttribute("resultSongPrio", ss.getSongs(searchResult.getSongs()));
			return mav;
		} else {
			request.getSession().setAttribute("resultMessage",
					"Artist/Song not found");
			return mav;
		}
	}

}
