package com.raybiztech.newsfeed.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.date.Second;
import com.raybiztech.newsfeed.builder.FeedPostBuilder;
import com.raybiztech.newsfeed.builder.FeedPostCommentsBuilder;
import com.raybiztech.newsfeed.business.FeedPost;
import com.raybiztech.newsfeed.business.FeedPostComments;
import com.raybiztech.newsfeed.dao.NewsFeedDao;
import com.raybiztech.newsfeed.dto.FeedPostCommentsDto;
import com.raybiztech.newsfeed.dto.FeedPostDto;
import com.raybiztech.recruitment.utils.FileUploaderUtility;

@Transactional
@Service("feedService")
public class NewsServiceImpl implements NewsFeedService {

    @Autowired
    NewsFeedDao newsFeedDao;
    @Autowired
    FeedPostBuilder feedPostBuilder;
    @Autowired
    FeedPostCommentsBuilder feedPostCommentsBuilder;
    @Autowired
    PropBean propBean;
    @Autowired
    BiometricDAO biometricDAO;
    Logger logger = Logger.getLogger(NewsServiceImpl.class);

    @Override
    public List<FeedPostDto> getFeedPosts() {
        List<FeedPost> feedPostList = newsFeedDao.getFeedPostsByOrder();
        return feedPostBuilder.toDtoList(feedPostList);
    }

    @Override
    public Long addFeedPost(FeedPostDto feedPostDto) {
        FeedPost feedPost = feedPostBuilder.toEntity(feedPostDto);
        Long feedId = (Long) newsFeedDao.save(feedPost);
        runNeewsFeedJob(feedPost);
        return feedId;
    }

    @Override
    public void addFeedPostComments(Long feedpostid,
            FeedPostCommentsDto feedPostCommentsDto) {
        FeedPost feedPost = newsFeedDao.findBy(FeedPost.class, feedpostid);
        FeedPostComments feedPostComments = feedPostCommentsBuilder.toEntity(feedPostCommentsDto);
        feedPostComments.setFeedPost(feedPost);
        newsFeedDao.save(feedPostComments);
    }

    @Override
    public void uploadImage(MultipartFile mpf, String parameter) {
        Long postId = Long.parseLong(parameter);
        FeedPost feedPost = newsFeedDao.findBy(FeedPost.class, postId);
        logger.info("FEED POST  ID IN serviceImpl:::: " + feedPost.getId());
        FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
        try {
            newsFeedDao.update(fileUploaderUtility.uploadImage(mpf, feedPost, propBean));
        } catch (IOException ex) {
            ex.printStackTrace();
            newsFeedDao.update(feedPost);
        }

    }

    @Override
    public void deleteFeedPost(Long feedPostId) {
        FeedPost feedPost = newsFeedDao.findBy(FeedPost.class, feedPostId);
        newsFeedDao.delete(feedPost);
    }

    public void runNeewsFeedJob(FeedPost feedPost) {
        List<Employee> employees = biometricDAO.getActiveEmployees();
        for (Employee employee : employees) {
            Alert alert = new Alert();
            alert.setAlertType("NeewsFeed");
            alert.setEmployee(employee);
            alert.setMsg(feedPost.getPostedBy().getFullName()+" posted a newsfeed");
            alert.setMsgDate(new Second());
            alert.setAlertStatus(Boolean.FALSE);
            alert.setLatestSatatus(Boolean.FALSE);
            alert.setInsertOn(new Second());
            newsFeedDao.save(alert);
        }
    }
}
