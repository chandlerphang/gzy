package com.cactus.guozy.profile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.User;

public interface UserDao extends BaseDao<User> {
	
	/**
	 * 根据用户Id查询用户对象
	 */
	User readUserById(Long id);
	
	/**
     * 根据手机号查询用户对象
     */
    User readUserByPhone(String phone);
    
    int insertUser(User user);
    
    int updatePassword(@Param("passwd") String passwd, @Param("id") Long id);

    String readPhoneById(Long id);
    
    List<User> readAllUsers();
    
    int updateAvatarUrl(@Param("id") Long id, @Param("newurl")String newurl);
    int updateNickname(@Param("id") Long id, @Param("newname")String newname);
    
    List<Address> readAddressForUser(Long id);
    
    int addNewAddress(@Param("addr") Address addr, @Param("uid") Long uid);
    
    List<User> readSalersByShopId(Long sid);
    
    int unDefaultAddr(Long uid);
}
