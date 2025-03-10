package com.raybiztech.projectnewsfeed.dto;

import java.util.Set;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceQueryDTO;

public class ProjectFeedPostDto {

	private Long id;

	private EmployeeDTO postedBy;

	private String post;

	private String postDate;

	private String postImageData;

	private Set<ProjectFeedpostCommentsDto> feedPostCommentsList;

	private ProjectDTO project;

	private MilestoneDTO milestone;

	private Long invoiceId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeDTO getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(EmployeeDTO postedBy) {
		this.postedBy = postedBy;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getPostImageData() {
		return postImageData;
	}

	public void setPostImageData(String postImageData) {
		this.postImageData = postImageData;
	}

	public Set<ProjectFeedpostCommentsDto> getFeedPostCommentsList() {
		return feedPostCommentsList;
	}

	public void setFeedPostCommentsList(
			Set<ProjectFeedpostCommentsDto> feedPostCommentsList) {
		this.feedPostCommentsList = feedPostCommentsList;
	}

	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	public MilestoneDTO getMilestone() {
		return milestone;
	}

	public void setMilestone(MilestoneDTO milestone) {
		this.milestone = milestone;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

}
