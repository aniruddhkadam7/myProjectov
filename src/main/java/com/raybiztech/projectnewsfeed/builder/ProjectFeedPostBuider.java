package com.raybiztech.projectnewsfeed.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.builder.ProjectBuilder;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPost;
import com.raybiztech.projectnewsfeed.dao.ProjecNewsFeedtDao;
import com.raybiztech.projectnewsfeed.dto.ProjectFeedPostDto;

@Component("projectFeedPostBuider")
public class ProjectFeedPostBuider {

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	ProjectFeedPostCommentsBuilder projectFeedPostCommentsBUilder;
	@Autowired
	ProjectBuilder projectBuilder;
	@Autowired
	ProjecNewsFeedtDao projectDao;

	public ProjectFeedPostDto toDto(ProjectFeedPost feedPost) {

		ProjectFeedPostDto feedPostDto = null;

		if (feedPost != null) {
			feedPostDto = new ProjectFeedPostDto();
			feedPostDto = new ProjectFeedPostDto();
			feedPostDto.setId(feedPost.getId());
			feedPostDto.setPost(feedPost.getPost());
			feedPostDto.setPostDate(feedPost.getPostDate().toString(
					"dd-MMM-yyyy hh:mm a"));
			feedPostDto.setPostedBy(employeeBuilder.createEmployeeDTO(feedPost
					.getPostedBy()));

			String orignalPath = feedPost.getPostImageData();
			if (orignalPath != null) {
				String splitedPath = orignalPath.substring(orignalPath
						.lastIndexOf("/") + 1);
				feedPostDto.setPostImageData("../projectNewsFeedpics/"
						+ splitedPath);
			}
			feedPostDto.setFeedPostCommentsList(projectFeedPostCommentsBUilder
					.toDtoSet(feedPost.getFeedpostcomments()));
			feedPostDto.setProject(projectBuilder
					.convertFormProjectToProjecDTO(feedPost.getProject()));
			if (feedPost.getMilestone() != null) {
				feedPostDto.setMilestone(projectBuilder.getMilestone(feedPost
						.getMilestone().getId()));
			}

		}

		return feedPostDto;
	}

	public List<ProjectFeedPostDto> toDtoList(List<ProjectFeedPost> list) {
		List<ProjectFeedPostDto> feedpostlist = null;
		if (list != null) {
			feedpostlist = new ArrayList<ProjectFeedPostDto>();
			for (ProjectFeedPost feedPost : list) {
				feedpostlist.add(toDto(feedPost));
			}
		}
		return feedpostlist;
	}

	public ProjectFeedPost toEntity(ProjectFeedPostDto projectFeedPostDto) {
		ProjectFeedPost projectFeedPost = null;

		if (projectFeedPostDto != null) {
			projectFeedPost = new ProjectFeedPost();
			Project project = projectDao.findBy(Project.class,
					projectFeedPostDto.getProject().getId());
			Employee employee = projectDao.findBy(Employee.class,
					projectFeedPostDto.getPostedBy().getId());

			if (projectFeedPostDto.getMilestone() != null) {
				Milestone milestone = projectDao.findBy(Milestone.class,
						projectFeedPostDto.getMilestone().getId());
				projectFeedPost.setMilestone(milestone);
			} else {
				projectFeedPost.setMilestone(null);
			}

			projectFeedPost
					.setInvoice((projectFeedPostDto.getInvoiceId() != null) ? projectDao
							.findBy(Invoice.class,
									projectFeedPostDto.getInvoiceId()) : null);

			projectFeedPost.setProject(project);
			projectFeedPost.setPostedBy(employee);

			projectFeedPost.setPostDate(new Second());
			projectFeedPost.setPost(projectFeedPostDto.getPost());

		}
		return projectFeedPost;

	}
}
