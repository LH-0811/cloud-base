package com.cloud.base.example.cloud.order.service.impl;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.example.cloud.account.api.ExampleAccountApi;
import com.cloud.base.example.cloud.account.param.AccountSubtractionParam;
import com.cloud.base.example.cloud.order.param.OrderCreateParam;
import com.cloud.base.example.cloud.order.repository.dao.TOrderDao;
import com.cloud.base.example.cloud.order.repository.entity.TOrder;
import com.cloud.base.example.cloud.order.service.ExampleStorageService;
import com.cloud.base.example.cloud.order.service.OrderService;
import com.cloud.base.example.cloud.order.vo.OrderVo;
import com.cloud.base.example.cloud.storage.param.SubtractionStorageParam;
import com.cloud.base.example.cloud.storage.vo.SubtractionStorageVo;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TOrderDao orderDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ExampleStorageService storageService;

    @Autowired
    private ExampleAccountApi accountService;

    // 创建订单
    @Override
    // 添加seata 全局事务注解 name 唯一
    @GlobalTransactional(name = "create_order",rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public OrderVo createOrder(OrderCreateParam param) throws Exception {

        // 拼接实体类
        TOrder order = new TOrder();
        BeanUtils.copyProperties(param, order);
        // 补充属性
        order.setId(idWorker.nextId());
        order.setStatus(0);

        log.info("----> 创建订单");
        orderDao.insertSelective(order);
        log.info("----> 调用 库存服务 扣减库存");
        ServerResponse<SubtractionStorageVo> subtractionStorageResponse = null;
        try {
            SubtractionStorageParam subtractionStorageParam = new SubtractionStorageParam();
            subtractionStorageParam.setProductId(param.getProductId());
            subtractionStorageParam.setCount(param.getCount());
            subtractionStorageResponse = storageService.subtractionStorage(subtractionStorageParam);
        } catch (Exception e) {
            throw CommonException.create(e,ServerResponse.createByError("扣减库存失败"));
        }
        if (!subtractionStorageResponse.isSuccess()) {
            throw CommonException.create(subtractionStorageResponse);
        }
        log.info("----> 完成 库存服务 扣减库存");

        log.info("----> 调用 账户服务 扣减余额");
        // 获取到库存扣减后的信息
        SubtractionStorageVo subtractionStorageVo = subtractionStorageResponse.getData();
        ServerResponse<AccountSubtractionParam> accountSubtractionResponse = null;
        try {
            AccountSubtractionParam accountSubtractionParam = new AccountSubtractionParam();
            accountSubtractionParam.setAmt(subtractionStorageVo.getTotalPrice());
            accountSubtractionParam.setUserId(param.getUserId());
            accountSubtractionResponse = accountService.accountSubtraction(accountSubtractionParam);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("扣减账户余额失败"));
        }
        if (!accountSubtractionResponse.isSuccess()) {
            throw CommonException.create(accountSubtractionResponse);
        }
        log.info("----> 完成 账户服务 扣减余额");

        log.info("----> 修改订单状态 开始");
        // 拼接修改参数
        try {
            order.setStatus(1);
            order.setMoney(subtractionStorageVo.getTotalPrice());
            orderDao.updateByPrimaryKeySelective(order);
        } catch (Exception e) {
            throw CommonException.create(e,ServerResponse.createByError("修改订单状态失败"));
        }
        log.info("----> 修改订单状态 完成");

        log.info("完成 订单创建");
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);
        return orderVo;
    }


}
