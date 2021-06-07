package com.cloud.base;

import com.alibaba.fastjson.JSON;
import com.cloud.base.member.property.repository.dao.PropCouponInfoDao;
import com.cloud.base.member.property.service.PropCouponUserService;
import com.cloud.base.member.property.vo.PropCouponDetailInfo;
import com.cloud.base.member.property.vo.PropCouponOfUserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/6/5
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMain {

    @Autowired
    private PropCouponUserService propCouponUserService;

    @Autowired
    private PropCouponInfoDao propCouponInfoDao;

    @Test
    public void test() {
        List<PropCouponDetailInfo> result = propCouponInfoDao.getPropCouponOfUserByUserId(1L);
        log.info("result:{}", JSON.toJSONString(result));
    }
}
