package com.cactus.guozy.profile;

import org.springframework.web.context.request.WebRequest;

import com.cactus.guozy.common.WakaRequestContext;
import com.cactus.guozy.profile.domain.User;

public class UserState {

	public static final String USER_REQ_ATTR_NAME = "cur_user";

	public static User getUser() {
		if (WakaRequestContext.instance().getWebRequest() == null) {
			return null;
		}
		WebRequest request = WakaRequestContext.instance().getWebRequest();
		return (User) request.getAttribute(USER_REQ_ATTR_NAME, WebRequest.SCOPE_REQUEST);
	}

	public static void setUser(User user) {
		WebRequest request = WakaRequestContext.instance().getWebRequest();
		assert request != null;

		request.setAttribute(USER_REQ_ATTR_NAME, user, WebRequest.SCOPE_REQUEST);
	}
	
}
