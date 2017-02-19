package com.cactus.guozy.admin.web;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cactus.guozy.common.json.JsonResponse;
import com.cactus.guozy.common.utils.Strings;
import com.cactus.guozy.core.domain.Feedback;
import com.cactus.guozy.core.service.FeedbackService;

@Controller
public class FeedbacksController {
	
	@Autowired
	FeedbackService feedbackService;
	
	@RequestMapping(value = "/feedbacks", method = RequestMethod.GET)
	public String feedbacks() {
		return "feedbacks";
	}
	
	@RequestMapping(value = "/feedbacks", method = RequestMethod.POST)
	public void addFeedback(
			HttpServletResponse response,
			@RequestParam("nickname") String nickname,
			@RequestParam("phone") String phone,
			@RequestParam("feedback") String feedback) {
		
		if(Strings.isNullOrEmpty(nickname) || Strings.isNullOrEmpty(feedback)) {
			new JsonResponse(response).with("status", 500).with("data", "昵称或反馈不能为空").done();
		}
		
		Feedback fb= Feedback.builder()
			.nickname(nickname)
			.phone(phone)
			.feedback(feedback)
			.dateCreated(new Date())
			.build();
		
		feedbackService.save(fb);
		new JsonResponse(response).with("status", 200).with("data", "ok").done();
	}
}
