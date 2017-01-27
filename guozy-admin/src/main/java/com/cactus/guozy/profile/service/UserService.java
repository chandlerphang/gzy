package com.cactus.guozy.profile.service;

import java.util.List;

import com.cactus.guozy.common.service.BaseService;
import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.User;

public interface UserService extends BaseService<User> {
	
	List<User> findAllUsers();
	
	User findUserByPhone(String phone);
	
	User saveUser(User user);
	
	User saveUser(User user, boolean register);

	User registerUser(String phone, String password);
	
	User exists(String phone, String passwd);
	
	boolean changePasswd(String passwd, Long id);

	User readUserById(Long userId);

	void deleteUser(User user);

	User createUserFromId(Long userId);

	User createNewUser();

	void createRegisteredUserRoles(User user);

	String encodePassword(String rawPassword);

	boolean isPasswordValid(String rawPassword, String encodedPassword);
	
	String getPhoneById(Long id);
	
	boolean updateAvatarUrl(Long id, String newurl);
	
	boolean updateNickname(Long id, String name);
	
	List<Address> findAddressesForUser(Long userid);
	
	boolean addNewAddress(Address address, Long uid);
}
