package com.cactus.guozy.core.service;

import com.cactus.guozy.common.service.BaseService;
import com.cactus.guozy.core.domain.PayMap;
import com.cactus.guozy.core.pay.PlatformType;

/**
 * 交易流水业务接口
 */
public interface PayMapService extends BaseService<PayMap> {

    /**
     * 支付通知更新交易记录
     * @param tempPayCode
     * @param msg
     * @param msg2
     * @param platformType
     * @param ssn
     * @param remark2
     * @return
     */
    PayMap updatePayMapByPayCode(String tempPayCode, String msg, String msg2, PlatformType platformType, String ssn, String remark2);

}
