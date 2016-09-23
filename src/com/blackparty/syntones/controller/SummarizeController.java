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
	private StopWordService stopWordService;

	@RequestMapping(value = "/commonWordsToDB")
	public ModelAndView commonWordsToDB() {
		ModelAndView mav = new ModelAndView("index");

		try {
			// List<String> lines = songLineService.getAllLines();
			// stopWords = stopWordService.getStopWords();
			// commonWords = wordCounter.count(lines, stopWords);
			// commonWordService.deleteCommonWords();
			// commonWordService.saveBatchCommonWords(commonWords);

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
		ArrayList<SongLine> globalSongLine = new ArrayList();

		try {
			List<SongLine> songLines = summarize.start(songList);
			songLineService.saveBatchSongLines(songLines);
			List<SongLine> finishedSongLine = songLineService.getAllLines();

			for (SongLine sl : finishedSongLine) {
				System.out.println(sl.toString());
			}
			System.out.println("FINISHED GLOBAL SUMMARIZE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

}
