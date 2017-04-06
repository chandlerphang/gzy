package com.cactus.guozy.core.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.common.json.Jsons;
import com.cactus.guozy.common.service.BaseServiceImpl;
import com.cactus.guozy.core.dao.SalerConnectionDao;
import com.cactus.guozy.core.dao.SalerDao;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.SalerConnection;
import com.cactus.guozy.core.domain.SalerLock;
import com.cactus.guozy.core.domain.SalerStatus;
import com.cactus.guozy.core.dto.PushMsg;
import com.cactus.guozy.core.service.MsgPushService;
import com.cactus.guozy.core.service.SalerLockManager;
import com.cactus.guozy.core.service.SalerService;
import com.cactus.guozy.core.service.exception.MessagePushException;
import com.cactus.guozy.core.util.StreamCapableTransactionalOperationAdapter;
import com.cactus.guozy.profile.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("salerService")
public class SalerServiceImpl extends BaseServiceImpl<Saler> implements SalerService {

	@Autowired
	private SalerDao salerDao;
	
	@Autowired
	private SalerConnectionDao salerConnectionDao;
	
	@Resource(name="getuiPushService")
	protected MsgPushService pushService;
	
	@Resource(name="passwdEncoder")
	protected PasswordEncoder pwdEncoder;
	
	@Resource(name = "streamingTransactionCapableUtil")
    protected StreamingTransactionCapableUtil transUtil;
	
	@Autowired
	SalerLockManager lockManager;

	@Override
	public BaseDao<Saler> getBaseDao() {
		return salerDao;
	}
	
	@Override
	@Transactional
	public Saler tryLoginSaler(String phone, String passwd, String channelId) {
		Saler user = new Saler();
		user.setPhone(phone);
		
		user = salerDao.selectOne(user);
		if(user == null) {
			throw new BizException("010303", "账号不存在");
		}
		
		if(pwdEncoder.matches(passwd, user.getPassword())) {
			// 判断导购员是否离线，非离线状态下的导购员不需要重复登录
			if(!user.getStatus().equals(SalerStatus.OFF_LINE.getCode())) {
				throw new BizException("500", "账号已经登录");
			}
			
			// 更新导购员为在线状态
			user.setStatus(SalerStatus.ON_LINE.getCode());
			// 更新最近一次活动时间
			user.setLastActiveTime(new Date());
			// 设置该导购员对应的推送ChannelId
			user.setChannelId(channelId);
			salerDao.updateByPrimaryKey(user);
			
//			// 广播导购员状态变化
//			PushMsg msg = PushMsg.builder().msgType(0)
//					.subjectId(user.getId())
//					.extra(SalerStatus.ON_LINE.getDesc())
//					.build();
//			try {
//				pushService.pushMsgToAllUser(Jsons.DEFAULT.toJson(msg));
//			} catch (MessagePushException e) {
//				throw new BizException("500", "导购员状态同步失败"); 
//			}
			
			return user;
		} else {
			throw new BizException("010304", "密码错误");
		}
	}
	
	@Override
	@Transactional
	public void logoutSaler(Long salerId) {
		Saler saler = salerDao.selectByPrimaryKey(salerId);
		if(saler.getStatus().equals(SalerStatus.OFF_LINE.getCode())) {
			throw new BizException("500", "导购员已离线");
		}
		
		// 更新导购员为离线状态
		saler.setStatus(SalerStatus.OFF_LINE.getCode());
		// 更新最近一次活动时间
		saler.setLastActiveTime(new Date());
		salerDao.updateByPrimaryKey(saler);
		
//		// 广播导购员状态变化
//		PushMsg msg = PushMsg.builder().msgType(0)
//				.subjectId(saler.getId())
//				.extra(SalerStatus.OFF_LINE.getDesc())
//				.build();
//		try {
//			pushService.pushMsgToAllUser(Jsons.DEFAULT.toJson(msg));
//		} catch (MessagePushException e) {
//			throw new BizException("500", "导购员状态同步失败"); 
//		}
	}
	
	@Override
	@Transactional
	public void setBusy(Long salerId) {
		Saler saler = salerDao.selectByPrimaryKey(salerId);
		if(!saler.getStatus().equals(SalerStatus.ON_LINE.getCode())) {
			throw new BizException("500", "导购员非在线");
		}
		
		// 更新导购员为忙碌状态
		saler.setStatus(SalerStatus.BUSY.getCode());
		// 更新最近一次活动时间
		saler.setLastActiveTime(new Date());
		salerDao.updateByPrimaryKey(saler);
		
//		// 广播导购员状态变化
//		PushMsg msg = PushMsg.builder().msgType(0)
//				.subjectId(saler.getId())
//				.extra(SalerStatus.BUSY.getDesc())
//				.build();
//		try {
//			pushService.pushMsgToAllUser(Jsons.DEFAULT.toJson(msg));
//		} catch (MessagePushException e) {
//			throw new BizException("500", "导购员状态同步失败"); 
//		}
	}
	
	@Override
	@Transactional
	public void noBusy(Long salerId) {
		Saler saler = salerDao.selectByPrimaryKey(salerId);
		if(!saler.getStatus().equals(SalerStatus.BUSY.getCode())) {
			throw new BizException("500", "导购员非忙碌");
		}
		
		// 更新导购员为在线状态
		saler.setStatus(SalerStatus.ON_LINE.getCode());
		// 更新最近一次活动时间
		saler.setLastActiveTime(new Date());
		salerDao.updateByPrimaryKey(saler);
		
//		// 广播导购员状态变化
//		PushMsg msg = PushMsg.builder().msgType(0)
//				.subjectId(saler.getId())
//				.extra(SalerStatus.ON_LINE.getDesc())
//				.build();
//		try {
//			pushService.pushMsgToAllUser(Jsons.DEFAULT.toJson(msg));
//		} catch (MessagePushException e) {
//			throw new BizException("500", "导购员状态同步失败"); 
//		}
	}
	
	@Override
	public String tryToConnect(Saler saler, User user, String usrChannelId) {
		if(log.isDebugEnabled()) {
			log.debug("用户 [" + user.getId() + ", " + user.getNickname() + "] 请求与导购员 [" + saler.getId() + ", " + saler.getNickname() + "] 建立通话");
		}
		
		// 判断用户是否有授权
		if(user.getCanLineToSaler() == false) {
			if(log.isDebugEnabled()) {
				log.debug("用户未授权");
			}
			throw new BizException("403", "用户禁止通话");
		}
		// 判断导购员在线状态
		if(saler.getStatus() != SalerStatus.ON_LINE.getCode()) {
			if(log.isDebugEnabled()) {
				log.debug("导购员非在线");
			}
			throw new BizException("500", "导购员非在线");
		}
		
		Long usrId = user.getId();
		Object lock = lockManager.acquireLock(saler);
		if(lock == null) {
			throw new BizException("500", "获取锁失败");
		}
		
		// 加锁过程中导购员的状态可能已经发生变化，这里重载后进行二次判断
		final Saler newsaler = getById(saler.getId());
		if(newsaler.getStatus() != SalerStatus.ON_LINE.getCode()) {
			lockManager.releaseLock(lock);
			throw new BizException("500", "导购员非在线");
		}
		// 导购员在线，建立通话链路状态跟踪
		final SalerConnection sc = SalerConnection.builder()
    			.salerId(newsaler.getId())
    			.salerCid(newsaler.getChannelId())
    			.userId(user.getId())
    			.userCid(usrChannelId)
    			.startTime(new Date())
    			.status("REQ_SEND")
    			.build();
		final boolean[] response = {false};
        try {
            transUtil.runTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                	// 更新导购员为忙碌状态
                	newsaler.setStatus(SalerStatus.BUSY.getCode());
            		// 更新最近一次活动时间
                	newsaler.setLastActiveTime(new Date());
            		salerDao.updateByPrimaryKey(newsaler);
            		
            		// 插入Saler Connection对象
            		int rowsAffected = salerConnectionDao.insert(sc);
            		
                    response[0] = rowsAffected == 1;
                }

                @Override
                public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
                    return false;
                }
            }, RuntimeException.class);
        } catch (RuntimeException e) {
        	response[0] = false;
        }
        // 释放锁
        lockManager.releaseLock(lock);
        
		// 尝试向导购员推送消息
		PushMsg msg = PushMsg.builder().msgType(1)
				.subjectId(usrId)
				.extra(usrChannelId)
				.build();
		try {
			if(log.isDebugEnabled()) {
				log.debug("向导购员 [" + saler.getId() + ", " + saler.getNickname() + " 推送请求通话消息");
			}
			pushService.pushMsgToSaler(saler.getChannelId(), Jsons.DEFAULT.toJson(msg));
			pushService.pushMsgToSaler(saler.getChannelId(), "用户请求导购", false);
		} catch (MessagePushException e) {
			log.error("消息推送失败: " + e.getMessage());
			throw new BizException("500", "消息推送失败", e);
		}
		
		return null;
	}
	
	
	@Override
	public String tryToConnect1(Saler saler, User user, String usrChannelId) {
		if(log.isDebugEnabled()) {
			log.debug("用户 [" + user.getId() + ", " + user.getNickname() + "] 请求与导购员 [" + saler.getId() + ", " + saler.getNickname() + "] 建立通话");
		}
		
		// 判断用户是否有授权
		if(user.getCanLineToSaler() == false) {
			if(log.isDebugEnabled()) {
				log.debug("用户未授权");
			}
			throw new BizException("403", "用户禁止通话");
		}
		// 判断导购员在线状态
		if(saler.getStatus() != SalerStatus.ON_LINE.getCode()) {
			if(log.isDebugEnabled()) {
				log.debug("导购员非在线");
			}
			throw new BizException("500", "导购员非在线");
		}
		
		Long usrId = user.getId();
		Object lock = lockManager.acquireLock(saler);
		if(lock == null) {
			throw new BizException("500", "获取锁失败");
		}
		
		// reload Saler
		final Saler newsaler = getById(saler.getId());
		if(newsaler.getStatus() != SalerStatus.ON_LINE.getCode()) {
			lockManager.releaseLock(lock);
			throw new BizException("500", "导购员非在线");
		}
		
		final SalerConnection sc = SalerConnection.builder()
    			.salerId(newsaler.getId())
    			.salerCid(newsaler.getChannelId())
    			.userId(user.getId())
    			.userCid(usrChannelId)
    			.startTime(new Date())
    			.status("REQ_SEND")
    			.build();
		final boolean[] response = {false};
        try {
            transUtil.runTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                	// 更新导购员为忙碌状态
                	newsaler.setStatus(SalerStatus.BUSY.getCode());
            		// 更新最近一次活动时间
                	newsaler.setLastActiveTime(new Date());
            		salerDao.updateByPrimaryKey(newsaler);
            		
            		// 插入Saler Connection对象
            		int rowsAffected = salerConnectionDao.insert(sc);
                    response[0] = rowsAffected == 1;
                }

                @Override
                public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
                    return false;
                }
            }, RuntimeException.class);
        } catch (RuntimeException e) {
        	response[0] = false;
        }
		
		// 尝试向导购员推送消息
		PushMsg msg = PushMsg.builder().msgType(1)
				.subjectId(usrId)
				.extra(usrChannelId)
				.build();
		try {
			if(log.isDebugEnabled()) {
				log.debug("向导购员 [" + saler.getId() + ", " + saler.getNickname() + " 推送请求通话消息");
			}
			pushService.pushMsgToSaler(saler.getChannelId(), Jsons.DEFAULT.toJson(msg));
			pushService.pushMsgToSaler(saler.getChannelId(), "用户请求导购", false);
		} catch (MessagePushException e) {
			lockManager.releaseLock(lock);
			log.error("消息推送失败: " + e.getMessage());
			throw new BizException("500", "消息推送失败", e);
		}
		
		Long connId = sc.getId();
		boolean lockAcquired = false;
		int count = 0;		
		while (!lockAcquired) {
			SalerConnection newsc = salerConnectionDao.selectByPrimaryKey(connId);
			lockAcquired = !"REQ_SEND".equals(newsc.getStatus());
			if (!lockAcquired) {
				count++;
				if (count >= 30) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			} else {
				if("SALER_ACK".equals(newsc.getStatus())) {
					return newsc.getHomeId();
				} else {
					// 更新导购员为在线状态
					saler.setStatus(SalerStatus.ON_LINE.getCode());
					// 更新最近一次活动时间
					saler.setLastActiveTime(new Date());
					salerDao.updateByPrimaryKey(saler);
					break;
				}
			}
		}
		
		lockManager.releaseLock(lock);
		return null;
	}
	
	@Override
	@Transactional
	public void salerConfirmConnect(Saler saler, Long usrId, String usrChannelId, String homeId) {
		if(log.isDebugEnabled()) {
			log.debug("导购员: " +  saler.getId() + "确认与用户: " + usrId + " 建立通话");
		}
		
		SalerConnection connection = salerConnectionDao.selectLastConnection(saler, usrId);
		connection.setHomeId(homeId);
		connection.setStatus("SALER_ACK");
		
		salerConnectionDao.updateByPrimaryKey(connection);
		
		PushMsg msg = PushMsg.builder().msgType(2)
				.subjectId(saler.getId())
				.extra(homeId)
				.build();
		try {
			if(log.isDebugEnabled()) {
				log.debug("向用户: " + usrId + " 推送导购员确认通话消息");
			}
			pushService.pushMsgToUser(usrChannelId, Jsons.DEFAULT.toJson(msg));
		} catch (MessagePushException e) {
			log.error("消息推送失败: " + e.getMessage());
			throw new BizException("500", "消息推送失败", e);
		}
		
//		// 广播导购员状态变化
//		msg = PushMsg.builder().msgType(0)
//				.subjectId(saler.getId())
//				.extra(SalerStatus.BUSY.getDesc())
//				.build();
//		try {
//			pushService.pushMsgToAllUser(Jsons.DEFAULT.toJson(msg));
//		} catch (MessagePushException e) {
//			log.error("消息推送失败: " + e.getMessage());
//			throw new BizException("500", "导购员状态同步失败"); 
//		}
	}
	
	@Override
	public void salerRejectConnect(Saler saler, Long usrId, String usrChannelId) {
		if(log.isDebugEnabled()) {
			log.debug("导购员: " +  saler.getId() + "拒绝与用户: " + usrId + " 建立通话");
		}
		
		SalerConnection connection = salerConnectionDao.selectLastConnection(saler, usrId);
		connection.setUserCid(usrChannelId);
		connection.setStatus("SALER_REJ");
		
		salerConnectionDao.updateByPrimaryKey(connection);
		
		
		PushMsg msg = PushMsg.builder().msgType(3)
				.subjectId(saler.getId())
				.build();
		try {
			if(log.isDebugEnabled()) {
				log.debug("向用户: " + usrId + " 推送导购员拒绝通话消息");
			}
			pushService.pushMsgToUser(usrChannelId, Jsons.DEFAULT.toJson(msg));
		} catch (MessagePushException e) {
			log.error("消息推送失败: " + e.getMessage());
		}
	}
	
	@Override
	public void userConfirmConnect(Saler saler, Long usrId) {
		Object lock = lockManager.acquireLock(saler);
		if(log.isDebugEnabled()) {
			log.debug("用户: " + usrId + " , 导购员: " +  saler.getId() + " 成功建立通话");
		}
		lockManager.releaseLock(lock);
	}

	@Override
	@Transactional
	public boolean acquireLock(Saler saler) {
		int count = salerDao.countSalerLock(saler);
        if (count == 0) {
        	// 对于同一个Saler，可能会有多个线程进入该段逻辑。SalerLock的主键是SalerId，
        	// 因此只会有一个线程成功插入数据，也即只有一个线程能成功获取到锁
            try {
                SalerLock sl = SalerLock.builder()
                		.salerId(saler.getId())
                		.locked(true)
                		.lastUpdated(System.currentTimeMillis())
                		.build();
                salerDao.insertSalerLock(sl);
                return true;
            } catch (PersistenceException e) {
                return false;
            }
        }

        Long timeToLive = System.currentTimeMillis() - 10000;
        int rowsAffected = salerDao.acquireSalerLock(saler, System.currentTimeMillis(), timeToLive);
        return rowsAffected == 1;
	}

	@Override
	public boolean releaseLock(Saler saler) {
		final boolean[] response = {false};
        try {
            transUtil.runTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                    int rowsAffected = salerDao.releaseSalerLock(saler);
                    response[0] = rowsAffected == 1;
                }

                @Override
                public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
                    return true;
                }
            }, RuntimeException.class);
        } catch (RuntimeException e) {
            log.error(String.format("Could not release saler lock (%s)", saler.getId()), e);
        }
        return response[0];
	}

	@Override
	public void userDisConnect(Saler saler, Long usrId, String homeId) {
		if(log.isDebugEnabled()) {
			log.debug("用户 " + usrId + " 请求结束通话 [salerId: " +  saler.getId() + " , homeId: " + homeId);
		}
		
		if(saler.getStatus() == SalerStatus.ON_LINE.getCode()) {
			return;
		}
		
		PushMsg msg = PushMsg.builder().msgType(5)
				.subjectId(usrId)
				.extra(homeId)
				.build();
		try {
			if(log.isDebugEnabled()) {
				log.debug("向导购员: " + saler.getId() + " 推送通话结束消息");
			}
			pushService.pushMsgToSaler(saler.getChannelId(), Jsons.DEFAULT.toJson(msg));
		} catch (MessagePushException e) {
			log.error("消息推送失败: " + e.getMessage());
			throw new BizException("500", "导购员状态同步失败"); 
		}
		
		// 更新导购员为忙碌状态
		saler.setStatus(SalerStatus.ON_LINE.getCode());
		// 更新最近一次活动时间
		saler.setLastActiveTime(new Date());
		salerDao.updateByPrimaryKey(saler);
		
//		// 广播导购员状态变化
//		msg = PushMsg.builder().msgType(0)
//				.subjectId(saler.getId())
//				.extra(SalerStatus.ON_LINE.getDesc())
//				.build();
//		try {
//			pushService.pushMsgToAllUser(Jsons.DEFAULT.toJson(msg));
//		} catch (MessagePushException e) {
//			log.error("消息推送失败: " + e.getMessage());
//			throw new BizException("500", "导购员状态同步失败"); 
//		}
	}

	@Override
	public void salerDisConnect(Saler saler, Long usrId, String usrChannelId, String homeId) {
		if(log.isDebugEnabled()) {
			log.debug("导购员: " +  saler.getId() + "请求结束通话[userId: " + usrId + " , homeId: " + homeId);
		}
		
		PushMsg msg = PushMsg.builder().msgType(5)
				.subjectId(saler.getId())
				.extra(homeId)
				.build();
		try {
			if(log.isDebugEnabled()) {
				log.debug("向用户: " + usrId + " 推送通话结束消息");
			}
			pushService.pushMsgToUser(usrChannelId, Jsons.DEFAULT.toJson(msg));
		} catch (MessagePushException e) {
			log.error("消息推送失败: " + e.getMessage());
			throw new BizException("500", "导购员状态同步失败"); 
		} 
		
		
		// 更新导购员为忙碌状态
		saler.setStatus(SalerStatus.ON_LINE.getCode());
		// 更新最近一次活动时间
		saler.setLastActiveTime(new Date());
		salerDao.updateByPrimaryKey(saler);
		
		// 广播导购员状态变化
//		msg = PushMsg.builder().msgType(0)
//				.subjectId(saler.getId())
//				.extra(SalerStatus.ON_LINE.getDesc())
//				.build();
//		try {
//			pushService.pushMsgToAllUser(Jsons.DEFAULT.toJson(msg));
//		} catch (MessagePushException e) {
//			log.error("消息推送失败: " + e.getMessage());
//			throw new BizException("500", "导购员状态同步失败"); 
//		}
	}
}
