package com.blackparty.syntones.controller;



import java.io.File;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.service.SampleModelService;

@Controller
@RequestMapping("/admin")
public class MainController {
	@Autowired
	SampleModelService sampleModelService;
	
	
	@RequestMapping(value = "/")
	public String defaultPage(){
		return "index"; 
		
	}
	@RequestMapping(value = "/index")
	public ModelAndView indexPage() {
		ModelAndView mav = new ModelAndView("index", "system_message", "Running MainController.index().");
		return mav;
	}
	
	@RequestMapping(value="/play",method=RequestMethod.GET)
	public ModelAndView playSong(@HeaderParam("Range") String range){
		ModelAndView mav = new ModelAndView("songInfo");
		String audio = "C:/Users/Courtney Love/Desktop/Syntones/Songs/Uploaded/";
		
		File asset = new File(audio);
		MediaResource rs = new MediaResource();
		Response response = null;
		try{
			System.out.println("received request for playing a music");
			//response = rs.buildStream(asset, range);
			System.out.println(response.getEntity().toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("response", response);
		return mav;
	}
	
	/*@RequestMapping(value = "/upload")
	public ModelAndView save(@RequestParam(value = "input") String input){
		ModelAndView mav = new ModelAndView("index");
		SampleModel sm = new SampleModel(input);
		sampleModelService.addSampleModel(sm);
		return mav;
	}*/
	
}
