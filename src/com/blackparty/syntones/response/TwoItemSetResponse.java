package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.TwoItemSet;

public class TwoItemSetResponse {

	private Message message;
	private List<TwoItemSet> two_item_set_list;

	public TwoItemSetResponse() {
		super();
	}

	public TwoItemSetResponse(Message message, List<TwoItemSet> two_item_set_list) {
		super();
		this.message = message;
		this.two_item_set_list = two_item_set_list;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<TwoItemSet> getTwo_item_set_list() {
		return two_item_set_list;
	}

	public void setTwo_item_set_list(List<TwoItemSet> two_item_set_list) {
		this.two_item_set_list = two_item_set_list;
	}

}
