package com.cactus.guozy.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.service.BaseServiceImpl;
import com.cactus.guozy.core.dao.FeedbackDao;
import com.cactus.guozy.core.domain.Feedback;
import com.cactus.guozy.core.service.FeedbackService;

@Service
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback> implements FeedbackService{

	@Autowired
	protected FeedbackDao feedbackDao;
	
	@Override
	public BaseDao<Feedback> getBaseDao() {
		return feedbackDao;
	}

}
