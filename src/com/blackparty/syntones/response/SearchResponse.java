package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.SearchResultModel;

public class SearchResponse {
	private Message message;
	private SearchResultModel srm;
	public SearchResponse(){
	}
	public SearchResponse(Message message){
		this.message = message;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public SearchResultModel getSrm() {
		return srm;
	}
	public void setSrm(SearchResultModel srm) {
		this.srm = srm;
	}
	
	
	
}
