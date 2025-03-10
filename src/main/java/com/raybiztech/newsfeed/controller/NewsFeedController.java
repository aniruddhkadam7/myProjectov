package com.raybiztech.newsfeed.controller;

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

import com.raybiztech.newsfeed.dto.FeedPostCommentsDto;
import com.raybiztech.newsfeed.dto.FeedPostDto;
import com.raybiztech.newsfeed.service.NewsFeedService;
import com.raybiztech.recruitment.service.JobPortalService;

@Controller
@RequestMapping(value = "/newsfeed")
public class NewsFeedController {

	@Autowired
	NewsFeedService feedService;
	@Autowired
	JobPortalService jobPortalServiceImpl;

	Logger logger  = Logger.getLogger(NewsFeedController.class);
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<FeedPostDto> getFeedPosts() {
		return feedService.getFeedPosts();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Long addFeedPost(@RequestBody FeedPostDto feedPostDto) {
		return feedService.addFeedPost(feedPostDto);
	}

	@RequestMapping(value = "/{feedpostid}", method = RequestMethod.POST)
	@ResponseBody
	public void addFeedPostComments(
			@PathVariable("feedpostid") Long feedpostid,
			@RequestBody FeedPostCommentsDto feedPostCommentsDtoDto) {
		feedService.addFeedPostComments(feedpostid, feedPostCommentsDtoDto);
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

			logger.info("In news Fedd Controller@@@@@@@@@@ "+request.getParameter("empId"));
			feedService.uploadImage(mpf, request.getParameter("postid"));
		}
	}
	@RequestMapping(value="/{feedpostid}",method=RequestMethod.DELETE)
	@ResponseBody public void deleteFeedPost(@PathVariable("feedpostid") Long feedPostId){
		feedService.deleteFeedPost(feedPostId);
	}
	
}
