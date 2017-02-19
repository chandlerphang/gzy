package com.cactus.guozy.profile.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.service.BaseService;
import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.domain.UserAddress;

public interface UserService extends BaseService<User> {
	
	/**
	 * 普通用户登录
	 */
	User tryLogin(String phone, String passwd);

	List<User> findAllUsers();
	
	User findUserByPhone(String phone);
	
	User saveUser(User user);
	
	User saveUser(User user, boolean register);

	User registerUser(String phone, String password);
	
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
	
	List<UserAddress> findAddressesForUser(Long userid);
	
	boolean addNewAddress(Address address, Long uid, Boolean isDefault);

	boolean updateAddress(Address address, Long uid, Long userAddressId, Boolean isDefault);

	void deleteAddress(Long addrId, Long uid);
}
