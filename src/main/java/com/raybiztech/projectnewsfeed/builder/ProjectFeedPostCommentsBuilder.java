package com.raybiztech.projectnewsfeed.builder;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.newsfeed.dao.NewsFeedDao;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPostComments;
import com.raybiztech.projectnewsfeed.dto.ProjectFeedpostCommentsDto;
import com.raybiztech.projectnewsfeed.dto.ProjectNewsFeedCommnetsSort;
@Component("projectFeedPostCommentsBuilder")
public class ProjectFeedPostCommentsBuilder {
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	NewsFeedDao newsFeedDao;

	public ProjectFeedpostCommentsDto toDto(
			ProjectFeedPostComments projectFeedPostComments) {
		ProjectFeedpostCommentsDto dto = null;

		if (projectFeedPostComments != null) {
			dto = new ProjectFeedpostCommentsDto();
			dto.setId(projectFeedPostComments.getId());
			dto.setCommentDate(projectFeedPostComments.getCommentDate()
					.toString("dd-MMM-yyyy hh:mm a"));
			dto.setEmployeeComment(employeeBuilder
					.createEmployeeDTO(projectFeedPostComments
							.getEmployeeComment()));
			dto.setComments(projectFeedPostComments.getComments());

		}
		return dto;
	}

	public ProjectFeedPostComments toEntity(
			ProjectFeedpostCommentsDto feedPostCommentsDto) {
		ProjectFeedPostComments feedPostComments = null;
		if (feedPostCommentsDto != null) {
			feedPostComments = new ProjectFeedPostComments();
			feedPostComments.setCommentDate(new Second());
			feedPostComments.setEmployeeComment(newsFeedDao.findBy(
					Employee.class, feedPostCommentsDto.getEmployeeComment()
							.getId()));
			feedPostComments.setComments(feedPostCommentsDto.getComments());

		}
		return feedPostComments;
	}

	public Set<ProjectFeedpostCommentsDto> toDtoSet(
			Set<ProjectFeedPostComments> feedPostCommentsList) {

		SortedSet<ProjectFeedpostCommentsDto> feedPostCommentsDtos = new TreeSet<ProjectFeedpostCommentsDto>(new ProjectNewsFeedCommnetsSort());
		for (ProjectFeedPostComments feedPostComments : feedPostCommentsList) {
			feedPostCommentsDtos.add(toDto(feedPostComments));
		}
		return feedPostCommentsDtos;

	}

}
