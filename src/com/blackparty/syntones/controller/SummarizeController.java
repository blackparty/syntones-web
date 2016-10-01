package com.blackparty.syntones.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.Summarize;
import com.blackparty.syntones.core.WordCounter;
import com.blackparty.syntones.model.CommonWord;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.model.StopWord;
import com.blackparty.syntones.service.CommonWordService;
import com.blackparty.syntones.service.SongLineService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.StopWordService;

@Controller
@RequestMapping(value = "/admin")
public class SummarizeController {
	@Autowired
	private SongService songService;
	@Autowired
	private SongLineService songLineService;
	@Autowired
	private CommonWordService commonWordService;

	@Autowired
	StopWordService stopWordService;

	@RequestMapping(value = "/commonWordsToDB")
	public ModelAndView commonWordsToDB() {
		ModelAndView mav = new ModelAndView("index");
		WordCounter wordCounter = new WordCounter();
		List<CommonWord> commonWords;
		List<String> stopWords;
		try {
//			List<String> lines = songLineService.getAllLines();
//			stopWords = stopWordService.getStopWords();
//			commonWords = wordCounter.count(lines, stopWords);
//			commonWordService.deleteCommonWords();
//			commonWordService.saveBatchCommonWords(commonWords);
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("system_message", "error occured");
			return mav;
		}
		mav.addObject("system_message", "counsting successful.");
		return mav;
	}

	@RequestMapping(value = "/globalSummarize")
	public ModelAndView globalSummarize() {
		ModelAndView mav = new ModelAndView("index");

		List<Song> songList = songService.getAllSongsFromDb();
		Summarize summarize = new Summarize();

		try{
			List<SongLine> songLines = summarize.start(songList);
			songLineService.saveBatchSongLines(songLines);
			List<SongLine> finishedSongLine = songLineService.getAllLines();
			
			for(SongLine sl: finishedSongLine){
				System.out.println(sl.toString());
			}
			System.out.println("FINISHED GLOBAL SUMMARIZE");
		}catch(Exception e){
			e.printStackTrace();
		}
		return mav;
	}
	// @RequestMapping(value = "/startSummarize")
	// public ModelAndView startSummarize() {
	//
	// ModelAndView mav = new ModelAndView("index");
	//
	// List<Song> songList = songService.getAllSongsFromDb();
	// // removes songs that already exists on "song_line_tbl"
	// List<Long> songIdList = songLineService.getAllSongs();
	// for (int i = 0; i < songList.size(); i++) {
	// for (int j = 0; j < songIdList.size(); j++) {
	// if (songList.get(i).getSongId() == songIdList.get(j)) {
	// songList.remove(i);
	// }
	// }
	// }
	//
	// if (songList.size() == 0) {
	// return mav; // means that the song(s) is added to the summarized
	// // list already
	// }
	//
	// Summarize summarize = new Summarize();
	//
	// try {
	// for (Song song : songList) {
	// //List<SongLine> songLines = summarize.start(song);
	// for (SongLine sl : songLines) {
	// sl.setSongId(song.getSongId());
	// songLineService.addSongLine(sl);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return mav;
	// }

}
