package com.cactus.guozy.profile.service;

import com.cactus.guozy.profile.domain.User;

public interface UserLoginService {
	
    User loginUser(User user);

    User loginUser(String username, String passwd);

    void logoutUser();

}
