package com.blackparty.syntones.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.SongService;

@Controller
public class ArController {

	@Autowired
	private SongService ss;

	@RequestMapping(value = "/playIt")
	public String viewAllSongs(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		System.out.println("-- VIEW SONG LIST --");

		List<Song> song_list = ss.getAllSongsFromDb();

		map.addAttribute("songList", song_list);
		return "playSong";

	}

}
