package com.kiotfpt.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionRequest {

	private int section_id;
	private List<ItemRequest> items;

}
