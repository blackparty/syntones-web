package com.blackparty.syntones.controller;

import java.io.File;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.response.SynonymResponse;
import com.blackparty.syntones.service.SampleModelService;
import com.blackparty.syntones.service.TagService;
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
	private TagService tagService;

	@RequestMapping(value = "/getSynonym")
	public void getSynonymTest(@RequestParam("word") String word) {

		try {
			HttpResponse<JsonNode> response = Unirest
					.get("https://wordsapiv1.p.mashape.com/words/" + word + "/synonyms")
					.header("X-Mashape-Key", "UCoDyzYDh1mshy874KnxdaKo8Ae2p1qWHK9jsnHu66tUeO7oPs")
					.header("Accept", "application/json").asJson();
			//System.out.println(response.getBody());
			
			Gson gson = new Gson();
			SynonymResponse sResponse = gson.fromJson(response.getBody().toString(),SynonymResponse.class);
			System.out.println("REESPOSNONER: "+sResponse.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/")
	public String defaultPage() {
		return "index";

	}

	@RequestMapping(value = "/index")
	public ModelAndView indexPage() {
		ModelAndView mav = new ModelAndView("index", "system_message", "Running MainController.index().");
		return mav;
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

	/*
	 * @RequestMapping(value = "/upload") public ModelAndView
	 * save(@RequestParam(value = "input") String input){ ModelAndView mav = new
	 * ModelAndView("index"); SampleModel sm = new SampleModel(input);
	 * sampleModelService.addSampleModel(sm); return mav; }
	 */

}
