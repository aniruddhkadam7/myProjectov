package com.raybiztech.newsfeed.builder;


import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.newsfeed.business.FeedPostComments;
import com.raybiztech.newsfeed.dao.NewsFeedDao;
import com.raybiztech.newsfeed.dto.FeedPostCommentsDto;
import com.raybiztech.newsfeed.dto.NewsFeedCommnetsSort;

@Component("feedPostCommentsBuilder")
public class FeedPostCommentsBuilder {

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	NewsFeedDao newsFeedDao;

	public FeedPostCommentsDto toDto(FeedPostComments feedPostComments) {
		FeedPostCommentsDto dto = null;

		if (feedPostComments != null) {
			dto = new FeedPostCommentsDto();
			dto.setId(feedPostComments.getId());
			dto.setCommentDate(feedPostComments.getCommentDate().toString("dd-MMM-yyyy hh:mm a"));
			dto.setEmployeeComment(employeeBuilder
					.createEmployeeDTO(feedPostComments.getEmployeeComment()));
			dto.setComments(feedPostComments.getComments());
			dto.setNumberOfLikes(feedPostComments.isNumberOfLikes());

		}
		return dto;
	}

	public FeedPostComments toEntity(FeedPostCommentsDto feedPostCommentsDto) {
		FeedPostComments feedPostComments = null;
		if (feedPostCommentsDto != null) {
			feedPostComments = new FeedPostComments();
			feedPostComments.setId(feedPostCommentsDto.getId());
			feedPostComments.setComments(feedPostCommentsDto.getComments());
			feedPostComments.setNumberOfLikes(feedPostCommentsDto
					.isNumberOfLikes());
			feedPostComments.setCommentDate(new Second());
			feedPostComments.setEmployeeComment(newsFeedDao.findBy(
					Employee.class, feedPostCommentsDto.getEmployeeComment()
							.getId()));

		}
		return feedPostComments;

	}

	public Set<FeedPostCommentsDto> toDtoSet(
			Set<FeedPostComments> feedPostCommentsList) {

		TreeSet<FeedPostCommentsDto> feedPostCommentsDtos = new TreeSet<FeedPostCommentsDto>(new NewsFeedCommnetsSort());
		for (FeedPostComments feedPostComments : feedPostCommentsList) {
			feedPostCommentsDtos.add(toDto(feedPostComments));
		}
		return feedPostCommentsDtos;

	}

}
