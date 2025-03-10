package com.raybiztech.newsfeed.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.newsfeed.business.FeedPost;
import com.raybiztech.newsfeed.dao.NewsFeedDao;
import com.raybiztech.newsfeed.dto.FeedPostDto;

@Component("feedPostBuilder")
public class FeedPostBuilder {

    @Autowired
    FeedPostCommentsBuilder feedPostCommentsBuilder;
    @Autowired
    EmployeeBuilder employeeBuilder;
    @Autowired
    NewsFeedDao newsFeedDao;

    public FeedPostDto toDto(FeedPost feedPost) {
        FeedPostDto feedPostDto = null;
        if (feedPost != null) {

            feedPostDto = new FeedPostDto();
            feedPostDto.setId(feedPost.getId());
            feedPostDto.setPost(feedPost.getPost());
            feedPostDto.setPostDate(feedPost.getPostDate().toString("dd-MMM-yyyy hh:mm a"));
            feedPostDto.setPostedBy(employeeBuilder
                    .createEmployeeDTO(feedPost.getPostedBy()));

            String orignalPath = feedPost.getPostImageData();
            if (orignalPath != null) {
                String splitedPath = orignalPath.substring(orignalPath.lastIndexOf("/") + 1);
                feedPostDto.setPostImageData("../newsFeedPics/" + splitedPath);
            }
            feedPostDto.setFeedPostCommentsList(feedPostCommentsBuilder
                    .toDtoSet(feedPost.getFeedpostcomments()));
        }

        return feedPostDto;
    }

    public List<FeedPostDto> toDtoList(List<FeedPost> feedPosts) {
        List<FeedPostDto> list = null;
        if (feedPosts != null) {
            list = new ArrayList<FeedPostDto>();
            for (FeedPost feedPost : feedPosts) {
                list.add(toDto(feedPost));
            }
        }
        return list;
    }

    public FeedPost toEntity(FeedPostDto feedPostDto) {
        FeedPost feedPost = null;
        if (feedPostDto != null) {
            feedPost = new FeedPost();
            feedPost.setId(feedPostDto.getId());
            feedPost.setPost(feedPostDto.getPost());
            feedPost.setPostDate(new Second());
            feedPost.setPostImageData(feedPostDto.getPostImageData());
            feedPost.setPostedBy(newsFeedDao.findBy(Employee.class, feedPostDto
                    .getPostedBy().getId()));
        }
        return feedPost;
    }

}
