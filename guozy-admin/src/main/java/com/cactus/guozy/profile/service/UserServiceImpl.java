package com.cactus.guozy.profile.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.common.service.BaseServiceImpl;
import com.cactus.guozy.core.service.lbs.Utils;
import com.cactus.guozy.profile.dao.UserAddressDao;
import com.cactus.guozy.profile.dao.UserDao;
import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.domain.UserAddress;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	protected UserDao userDao;
	
	@Autowired
	protected UserAddressDao userAddrDao;
	
	@Resource(name="addrService")
	protected AddressService addrService;

	@Resource(name="cacheManager")
	protected CacheManager cacheManager;
	
	@Resource(name="passwdEncoder")
	protected PasswordEncoder pwdEncoder;
	
	@Resource(name="userServiceExtensionManager")
	protected UserServiceExtensionManager extMgr;
	
	@Override 
	public User tryLogin(String phone, String passwd){
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
	public User findUserByPhone(String phone) {
		return userDao.readUserByPhone(phone);
	}
	
	@Override
	public User saveUser(User user) {
		userDao.insertUser(user);
		return user;
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
			User user = User.builder()
					.password(password)
					.phone(phone)
					.canLineToSaler(true)
					.deactivated(false)
					.dateCreated(new Date())
					.nickname("usr_" + phone)
					.build();
			
			userDao.insert(user);
			
			// 触发用户注册 后置处理
			extMgr.postRegisterUser(user);
			return user;
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
	public List<UserAddress> findAddressesForUser(Long userid) {
		return userAddrDao.readByUserId(userid);
	}

	@Override
	@Transactional
	public boolean addNewAddress(Address address, Long uid, Boolean isDefault) {
		Map<String, String> json = Utils.getGeocoderLatitude(address.getAddrLine1()+address.getAddrLine2());
		if(json.get("lat") != null && json.get("lng") != null) {
			address.setLat(Double.valueOf(json.get("lat")));
			address.setLng(Double.valueOf(json.get("lng")));
		}
		
		int status = addrService.save(address);
		if(status != 1) {
			throw new RuntimeException();
		}
		
		if(isDefault != null && isDefault == true) {
			userAddrDao.unDefaultAddr(uid);
		}
		
		UserAddress userAddr = new UserAddress();
		userAddr.setAddrId(address.getId());
		userAddr.setUserId(uid);
		userAddr.setName("XX");
		userAddr.setIsDefault(isDefault);
		
		status = userAddrDao.insert(userAddr);
		if(status != 1) {
			throw new RuntimeException();
		}
		
		return true;
	}
	
	@Override
	@Transactional
	public boolean updateAddress(Address address, Long uid, Long userAddressId, Boolean isDefault) {
		Map<String, String> json = Utils.getGeocoderLatitude(address.getAddrLine1()+address.getAddrLine2());
		if(json.get("lat") != null && json.get("lng") != null) {
			address.setLat(Double.valueOf(json.get("lat")));
			address.setLng(Double.valueOf(json.get("lng")));
		}
		
		if(isDefault != null && isDefault == true) {
			userAddrDao.unDefaultAddr(uid);
		}
		
		int status = addrService.update(address);
		if(status != 1) {
			throw new RuntimeException();
		}
		
		UserAddress userAddr = userAddrDao.selectByPrimaryKey(userAddressId);
		userAddr.setIsDefault(isDefault);
		
		userAddrDao.updateByPrimaryKey(userAddr);
		return true;
	}
	
	@Override
	@Transactional
	public void deleteAddress(Long addrId, Long uid) {
		UserAddress userAddr = userAddrDao.selectByPrimaryKey(addrId);
		addrService.delete(userAddr.getAddrId());
		userAddrDao.deleteByPrimaryKey(addrId);
	}  

	@Override
	public BaseDao<User> getBaseDao() {
		return userDao;
	}

}
