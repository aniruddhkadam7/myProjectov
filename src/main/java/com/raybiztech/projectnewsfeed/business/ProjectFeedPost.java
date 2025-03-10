package com.raybiztech.projectnewsfeed.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.business.Invoice;

public class ProjectFeedPost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3656389644673966566L;

	private Long id;

	private Employee postedBy;

	private String post;

	private Second postDate;

	private String postImageData;

	private Set<ProjectFeedPostComments> feedpostcomments;

	private Project project;

	private Milestone milestone;

	private Invoice invoice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(Employee postedBy) {
		this.postedBy = postedBy;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public Second getPostDate() {
		return postDate;
	}

	public void setPostDate(Second postDate) {
		this.postDate = postDate;
	}

	public String getPostImageData() {
		return postImageData;
	}

	public void setPostImageData(String postImageData) {
		this.postImageData = postImageData;
	}

	public Set<ProjectFeedPostComments> getFeedpostcomments() {
		return feedpostcomments;
	}

	public void setFeedpostcomments(
			Set<ProjectFeedPostComments> feedpostcomments) {
		this.feedpostcomments = feedpostcomments;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

}
