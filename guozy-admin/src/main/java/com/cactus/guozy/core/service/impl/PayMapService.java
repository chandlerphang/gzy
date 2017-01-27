package com.cactus.guozy.core.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.exception.BusinessException;
import com.cactus.guozy.common.service.BaseServiceImpl;
import com.cactus.guozy.core.dao.PayMapDao;
import com.cactus.guozy.core.domain.PayMap;
import com.cactus.guozy.core.pay.PlatformType;
import com.cactus.guozy.core.service.IPayMapService;

/**
 * 交易流水业务
 */
@Service
public class PayMapService extends BaseServiceImpl<PayMap> implements IPayMapService {

    private static Logger logger = LoggerFactory.getLogger(PayMapService.class);
    @Autowired
    private PayMapDao payMapMapper;

    @Override
    public BaseDao<PayMap> getBaseDao() {
        return payMapMapper;
    }

    @Override
    public PayMap updatePayMapByPayCode(String tempPayCode, String msg, String msg2, PlatformType platformType, String ssn, String remark2) {
        PayMap param = new PayMap();
        param.setTempPayCode(tempPayCode);
        param.setPlatform(platformType.value());
        List<PayMap> payMaps = payMapMapper.select(param);
        Assert.notNull(payMaps);
        if (payMaps != null && !payMaps.isEmpty()) {
            PayMap payMap = payMaps.get(0);
            payMap.setRetMsg(msg);
            payMap.setRetMsg2(msg2);
            payMap.setSwiftNumber(ssn);
            payMap.setIsPaid("1");
            payMap.setNotifyTime(new Date().getTime() / 1000);
            if (StringUtils.isNotBlank(remark2)) {
                payMap.setRemark2(remark2);
            }
            payMapMapper.updateByPrimaryKeySelective(payMap);
            return payMap;
        } else {
            throw new BusinessException("数据库异常，交易记录查询为Null");
        }
    }

}
