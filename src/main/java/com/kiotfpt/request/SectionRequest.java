package com.kiotfpt.request;

import java.util.List;

public class SectionRequest {
	private int section_id;
	private List<ItemRequest> items;

	public SectionRequest() {
		super();
	}
	

	public SectionRequest(int section_id, List<ItemRequest> items) {
		super();
		this.section_id = section_id;
		this.items = items;
	}

	public int getSection_id() {
		return section_id;
	}


	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}
}
