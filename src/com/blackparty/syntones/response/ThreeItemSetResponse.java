package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.ThreeItemSet;

public class ThreeItemSetResponse {
	private Message message;
	private List<ThreeItemSet> three_item_set_list;

	public ThreeItemSetResponse() {
		super();
	}

	public ThreeItemSetResponse(Message message, List<ThreeItemSet> three_item_set_list) {
		super();
		this.message = message;
		this.three_item_set_list = three_item_set_list;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<ThreeItemSet> getThree_item_set_list() {
		return three_item_set_list;
	}

	public void setThree_item_set_list(List<ThreeItemSet> three_item_set_list) {
		this.three_item_set_list = three_item_set_list;
	}

}
