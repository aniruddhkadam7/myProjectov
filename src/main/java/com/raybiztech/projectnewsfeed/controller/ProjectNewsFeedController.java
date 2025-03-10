package com.raybiztech.projectnewsfeed.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.raybiztech.projectnewsfeed.dto.ProjectFeedPostDto;
import com.raybiztech.projectnewsfeed.dto.ProjectFeedpostCommentsDto;
import com.raybiztech.projectnewsfeed.service.ProjectNewsService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/projectnewsfeed")
public class ProjectNewsFeedController {

	@Autowired
	ProjectNewsService projectNewsService;

	Logger logger = Logger.getLogger(ProjectNewsFeedController.class);

	@RequestMapping(value = "/", params = { "projectid" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectFeedPostDto> getFeedPosts(@RequestParam Long projectid) {
		return projectNewsService.getFeedPosts(projectid);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Long addFeedPost(@RequestBody ProjectFeedPostDto feedPostDto) {
		return projectNewsService.addFeedPost(feedPostDto);
	}

	@RequestMapping(value = "/{feedpostid}", method = RequestMethod.POST)
	@ResponseBody
	public void addFeedPostComments(
			@PathVariable("feedpostid") Long feedpostid,
			@RequestBody ProjectFeedpostCommentsDto feedPostCommentsDtoDto) {
		projectNewsService.addFeedPostComments(feedpostid,
				feedPostCommentsDtoDto);
	}

	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadImage(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		Iterator<String> itr = request.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			logger.info("In news Fedd Controller@@@@@@@@@@ "
					+ request.getParameter("empId"));
			projectNewsService.uploadImage(mpf, request.getParameter("postid"));
		}
	}

	@RequestMapping(value = "/milestoneNewsFeed", params = { "projectid",
			"milestoneId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectFeedPostDto> getMilestoneFeedPosts(
			@RequestParam Long projectid, @RequestParam Long milestoneId) {
		return projectNewsService.getMilestoneNewsFeed(projectid, milestoneId);
	}

	@RequestMapping(value = "/invoiceNewsFeed", params = { "projectid",
			"invoiceId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectFeedPostDto> getInvoiceNewsFeed(
			@RequestParam Long projectid, @RequestParam Long invoiceId) {
		return projectNewsService.getInvoiceNewsFeed(projectid, invoiceId);
	}
}
