package com.raybiztech.projectnewsfeed.dto;

import java.util.Comparator;

public class ProjectNewsFeedCommnetsSort implements Comparator<ProjectFeedpostCommentsDto> {

	@Override
	public int compare(ProjectFeedpostCommentsDto o1, ProjectFeedpostCommentsDto o2) {
		return o2.getId().compareTo(o1.getId());
	}

}
