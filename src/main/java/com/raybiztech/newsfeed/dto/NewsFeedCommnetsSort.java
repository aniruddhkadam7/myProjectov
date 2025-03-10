package com.raybiztech.newsfeed.dto;

import java.util.Comparator;

public class NewsFeedCommnetsSort implements Comparator<FeedPostCommentsDto> {

	@Override
	public int compare(FeedPostCommentsDto o1, FeedPostCommentsDto o2) {
		return o2.getId().compareTo(o1.getId());
	}

}
