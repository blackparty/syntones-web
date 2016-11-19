package com.blackparty.syntones.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.core.SearchProcess;
import com.blackparty.syntones.core.Tagger;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.ArtistWordBank;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.SearchResultModel;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.model.TagSong;
import com.blackparty.syntones.model.TagSynonym;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.response.SynonymResponse;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.SampleModelService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;
import com.blackparty.syntones.service.TagService;
import com.blackparty.syntones.service.TagSongService;
import com.blackparty.syntones.service.TagSynonymService;
import com.blackparty.syntones.service.UserService;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

@Controller
@RequestMapping("/admin")
public class MainController {
	@Autowired
	private SampleModelService sampleModelService;
	@Autowired
	private TagSynonymService tagSynonymService;
	@Autowired
	private TagService tagService;
	@Autowired
	private SongService songService;
	@Autowired
	private TagSongService tagSongService;
	@Autowired
	private UserService userService;
	@Autowired
	private ArtistService artistService;

	@Autowired
	SongWordBankService swService;
	@Autowired
	ArtistWordBankService awService;

	@RequestMapping(value = "/tagSongs")
	public ModelAndView tagSongs() {
		ModelAndView mav = new ModelAndView("index");
		try {
			// getting all songs
			List<Song> songs = songService.getAllSongs();
			// getting all tags
			List<Tag> tags = tagService.getAllTags();
			// gets its corresponding synonyms
			for (int i = 0; i < tags.size(); i++) {
				List<TagSynonym> synonyms = tagSynonymService
						.getTagSynonym(tags.get(i).getId());
				tags.get(i).setSynonyms(synonyms);
			}

			Tagger tagger = new Tagger();
			for (int i = 0; i < songs.size(); i++) {
				List<TagSong> tagSong = tagger.start(songs.get(i), tags);
				for (TagSong ts : tagSong) {
					System.out.println(ts.toString());
				}
				// save
				tagSongService.saveBatchTagSong(tagSong);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/getSynonym", method = RequestMethod.GET)
	public void getSynonymTest() {
		try {
			List<Tag> tags = tagService.getAllTags();
			for (Tag t : tags) {
				System.out.println(t.toString());
			}

			ArrayList<SynonymResponse> synonymLists = new ArrayList<>();
			for (Tag s : tags) {
				HttpResponse<JsonNode> response = Unirest
						.get("https://wordsapiv1.p.mashape.com/words/"
								+ s.getTag() + "/synonyms")
						.header("X-Mashape-Key",
								"UCoDyzYDh1mshy874KnxdaKo8Ae2p1qWHK9jsnHu66tUeO7oPs")
						.header("Accept", "application/json").asJson();
				Gson gson = new Gson();
				SynonymResponse synonymResponse = gson.fromJson(response
						.getBody().toString(), SynonymResponse.class);
				synonymResponse.setTag(s);
				System.out.println(synonymResponse.toString());
				synonymLists.add(synonymResponse);
			}

			for (int i = 0; i < synonymLists.size(); i++) {
				List<TagSynonym> t = new ArrayList<>();
				List<String> list = synonymLists.get(i).getSynonyms();
				for (int j = 0; j < list.size(); j++) {
					TagSynonym ts = new TagSynonym(synonymLists.get(i).getTag()
							.getId(), list.get(j));
					t.add(ts);
				}
				// saving
				tagSynonymService.saveBatchSynonym(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/")
	public ModelAndView defaultPage(HttpServletRequest request,
			HttpSession session) {
		ModelAndView mav = new ModelAndView();
		try {
			if (session.getAttribute("username").equals("")) {
				request.getSession().invalidate();
				mav.setViewName("login");
				return mav;
			} else if (request.getSession().getMaxInactiveInterval() <= 0) {
				request.getSession().invalidate();
				mav.setViewName("login");
				return mav;
			} else {
				String offset = (String) request.getParameter("offSet");
				int size;
				List<Song> songList;
				if (offset != null) {
					int offsetreal = Integer.parseInt(offset) - 1;
					offsetreal = offsetreal * 10;
					songList = songService.displaySong(offsetreal);
				} else {
					songList = songService.displaySong(0);
					size = (int) songService.songCount();
					session.setAttribute("size", size / 10);
				}

				mav.addObject("offset", offset);
				mav.addObject("songList", songList);
				mav.setViewName("index");
				return mav;
			}
		} catch (NullPointerException e) {
			request.getSession().invalidate();
			mav.setViewName("login");
			return mav;
		}
	}

	@RequestMapping(value = "/index")
	public ModelAndView indexPage(HttpServletRequest request,
			HttpSession session) {
		ModelAndView mav = new ModelAndView("index");
		if (request.getSession().getMaxInactiveInterval() <= 0) {
			request.getSession().invalidate();
			mav.setViewName("login");
			return mav;
		} else {
			String offset = (String) request.getParameter("offSet");
			int size;
			List<Song> songList;
			if (offset != null) {
				int offsetreal = Integer.parseInt(offset);
				offsetreal = offsetreal * 10;
				songList = songService.displaySong(offsetreal - 1);
			} else {
				songList = songService.displaySong(0);
				size = (int) songService.songCount();
				session.setAttribute("size", size / 10);
			}

			mav.addObject("offset", offset);
			mav.addObject("songList", songList);
			mav.setViewName("index");
			return mav;
		}
	}

	@RequestMapping(value = "/addNewTag")
	public ModelAndView showAddNewTag() {
		ModelAndView mav = new ModelAndView("addNewTag");
		return mav;
	}

	@RequestMapping(value = "/saveTag", method = RequestMethod.POST)
	public ModelAndView saveTag(@RequestParam("tagName") String name) {
		ModelAndView mav = new ModelAndView("index");
		Tag tag = new Tag();
		tag.setTag(name);
		try {
			tagService.addTag(tag);

		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("system_message", "saving sucessful.");
		return mav;

	}

	@RequestMapping(value = "/play", method = RequestMethod.GET)
	public ModelAndView playSong(@HeaderParam("Range") String range) {
		ModelAndView mav = new ModelAndView("songInfo");
		String audio = "C:/Users/Courtney Love/Desktop/Syntones/Songs/Uploaded/";

		File asset = new File(audio);
		MediaResource rs = new MediaResource();
		Response response = null;
		try {
			System.out.println("received request for playing a music");
			// response = rs.buildStream(asset, range);
			System.out.println(response.getEntity().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("response", response);
		return mav;
	}

	@RequestMapping(value = "/loginAdmin")
	public ModelAndView loginAdmin(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			if (username != null && password != null) {
				Message message;
				message = userService.authenticateAdmin(user);
				if (message.getFlag()) {
					request.getSession().setMaxInactiveInterval(60 * 60);
					session.setAttribute("username", username);
					session.setMaxInactiveInterval(60 * 60);
					List<Song> songs = songService.displaySong(0);
					mav.addObject("songList", songs);
					int size = (int) songService.songCount();
					session.setAttribute("size", (size / 10));
					mav.setViewName("index");

				} else {
					mav.addObject("err_message", message.getMessage());
					mav.setViewName("login");
				}
			} else {
				mav.addObject("err_message", "Enter valid user");
			}

		} catch (Exception e) {
			mav.addObject("err_message", "Enter valid user");

			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping("/logoutAdmin")
	public String mainpage(HttpServletRequest request) {
		request.getSession().invalidate();

		return "login";

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView searchAdmin(@RequestParam("input") String searchString,
			HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		SearchProcess sp = new SearchProcess();
		List<Song> songs = songService.getAllSongs();
		List<SongWordBank> swbs = swService.fetchAllWordBank();
		List<Artist> artists = artistService.getAllArtists();
		List<ArtistWordBank> awbs = awService.fetchAllWordBank();
		// Song
		System.out.println("SEARCH");
		System.out.println(songs.get(0).getSongTitle() + " >> "
				+ songs.get(0).getSongId());

		SearchResultModel searchResult = sp.searchProcess(searchString, swbs,
				songs, artists, awbs);
		songs = songService.getSongs(searchResult.getSongs());
		artists = artistService.getArtists(searchResult.getArtists());

		// SearchResultModel searchResult =
		// sp.SearchProcess(searchString.trim(), abservice.fetchAllWordBank(),
		// sbservice.fetchAllWordBank(), songService.getAllSongs(),
		// artistService.getAllArtists());
		// List<Song> songs = songService.getSongs(searchResult.getSongs());
		// List<Artist> artists =
		// artistService.getArtists(searchResult.getArtists());

		ArrayList<Song> sprio = new ArrayList<Song>();
		ArrayList<Song> lprio = new ArrayList<Song>();
		if (artists.isEmpty()) {
			if (songs.isEmpty()) {
				System.out.println("here");
				mav.addObject("search_err", "Not Found");
			} else {
				mav.addObject("songList", songs);
			}
		} else if (songs.isEmpty()) {
			if (artists.isEmpty()) {
				System.out.println("here");
				mav.addObject("search_err", "Not Found.");
			} else {
				songs = songService.getSongbyArtist(artists);
				mav.addObject("songList", songs);
			}
		} else {
			for (Song song : songs) {
				for (Artist artist : artists) {
					if (song.getArtist().getArtistId() == artist.getArtistId()) {
						sprio.add(song);
					} else {
						lprio.add(song);
					}
				}
			}
			sprio.addAll(lprio);
			mav.addObject("songList", sprio);
		}

		mav.addObject("songList", songs);
		mav.setViewName("index");
		mav.addObject("searchflag", "flag");
		return mav;
	}
	/*
	 * @RequestMapping(value = "/upload") public ModelAndView
	 * save(@RequestParam(value = "input") String input){ ModelAndView mav = new
	 * ModelAndView("index"); SampleModel sm = new SampleModel(input);
	 * sampleModelService.addSampleModel(sm); return mav; }
	 */

}
