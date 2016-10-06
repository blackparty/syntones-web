package com.blackparty.syntones.controller;

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

import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.SongService;

@Controller
public class ArController {

	@Autowired
	SongService ss;

	@Autowired
	PlayedSongsService playedSongsService;

	@RequestMapping(value = "/playIt")
	public String viewAllSongs(HttpServletRequest request, HttpServletResponse response, ModelMap map)
			throws Exception {
		System.out.println("-- VIEW SONG LIST --");

		List<Song> song_list = ss.getAllSongs();

		map.addAttribute("songList", song_list);
		return "playSong";

	}

	@RequestMapping(value = "/viewSong", method = RequestMethod.GET)
	public String viewSong(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		System.out.println("-- SONG INFO --");

		String song_id = String.valueOf(request.getParameter("id"));

		map.addAttribute("songId", song_id);

		return "songInfo";

	}

	@RequestMapping(value = "/playThisSong", method = RequestMethod.POST)
	public String storePlayedSong(HttpServletRequest request, ModelMap map) {
		System.out.println("-- PLAY SONG --");

		String song_id = request.getParameter("song_id");
		String user_id = request.getParameter("user_id");

		boolean playedSongExists = playedSongsService.checkIfPlayedSongExists(user_id, song_id);
		System.out.println("EXISTS:" + playedSongExists);
		if (playedSongExists == false) {
			PlayedSongs playedSongs = new PlayedSongs();
			playedSongs.setUser_id(user_id);
			playedSongs.setTrack_id(song_id);
			System.out.println("EXISTS:" + playedSongExists);
			playedSongsService.savePlayedSongs(playedSongs);
		}

		return "playSong";
	}

}
