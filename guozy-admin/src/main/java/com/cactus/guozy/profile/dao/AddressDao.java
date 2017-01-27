package com.cactus.guozy.profile.dao;

import org.apache.ibatis.annotations.Param;

import com.cactus.guozy.profile.domain.User;

public interface AddressDao {
	
	/**
	 * 根据用户Id查询用户对象
	 */
	User readUserById(Long id);
	
	User readUserWithAvatarById(Long id);
	
	byte[] readAvatarById(Long id);
	
	/**
     * 根据手机号查询用户对象
     */
    User readUserByPhone(String phone);
    
    int insertUser(User user);
    
    int updatePassword(@Param("passwd") String passwd, @Param("id") Long id);

    String readPhoneById(Long id);
}
