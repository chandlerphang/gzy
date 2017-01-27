package com.cactus.guozy.profile.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.common.service.BaseServiceImpl;
import com.cactus.guozy.profile.dao.UserDao;
import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.User;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	protected UserDao userDao;

	@Resource(name="cacheManager")
	protected CacheManager cacheManager;
	
	@Resource(name="passwdEncoder")
	protected PasswordEncoder pwdEncoder;

	@Override
	public User saveUser(User user) {
		userDao.insertUser(user);
		return user;
	}

	@Override
	public User exists(String phone, String passwd) {
		User user = new User();
		user.setPhone(phone);
		
		user = userDao.selectOne(user);
		if(user == null) {
			throw new BizException("010303", "账号不存在");
		}
		
		if(user!=null && pwdEncoder.matches(passwd, user.getPassword())) {
			return user;
		}
		
		throw new BizException("010304", "密码错误");
	}

	@Override
	public User readUserById(Long userId) {
		return userDao.readUserById(userId);
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public User createUserFromId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createNewUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createRegisteredUserRoles(User user) {
	}

	@Override
	public String encodePassword(String rawPassword) {
		return pwdEncoder.encode(rawPassword);
	}

	@Override
	public boolean isPasswordValid(String rawPassword, String encodedPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User findUserByPhone(String phone) {
		return userDao.readUserByPhone(phone);
	}

	@Override
	public User saveUser(User user, boolean register) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public User registerUser(String phone, String rawpasswd) {
		User olduser = findUserByPhone(phone);
		if(olduser != null) {
			return null;
		} else {
			String password = pwdEncoder.encode(rawpasswd);
			User user = new User();
			user.setPhone(phone);
			user.setPassword(password);
			return saveUser(user);
		}
	}

	@Override
	public boolean changePasswd(String passwd, Long id) {
		return userDao.updatePassword( pwdEncoder.encode(passwd), id) > 0;
	}

	@Override
	public String getPhoneById(Long id) {
		return userDao.readPhoneById(id);
	}

	@Override
	public List<User> findAllUsers() {
		return userDao.readAllUsers();
	}

	@Override
	@Transactional
	public boolean updateAvatarUrl(Long id, String newurl) {
		return userDao.updateAvatarUrl(id, newurl) > 0;
	}

	@Override
	@Transactional
	public boolean updateNickname(Long id, String nickname) {
		return userDao.updateNickname(id, nickname) > 0;
	}

	@Override
	public List<Address> findAddressesForUser(Long userid) {
		return userDao.readAddressForUser(userid);
	}

	@Override
	@Transactional
	public boolean addNewAddress(Address address, Long uid) {
		if(address.getIsDefault() == true) {
			userDao.unDefaultAddr(uid);
		}
		
		return userDao.addNewAddress(address, uid) > 0;
	}

	@Override
	public BaseDao<User> getBaseDao() {
		return userDao;
	}
}
