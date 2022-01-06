package com.cloud.base.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.user.repository.dao.SysSerialNumberDao;
import com.cloud.base.user.repository.entity.SysSerialNumber;
import com.cloud.base.user.service.SysSerialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;

/**
 * 序号服务
 *
 * @author lh0811
 * @date 2021/12/8
 */
@Slf4j
@Service
public class SysSerialServiceImpl implements SysSerialService {

    @Autowired
    private SysSerialNumberDao sysSerialNumberDao;



    /**
     * 获取下一个业务序号
     * 为了避免并发获取到重复的序号，必须要求在数据库事务级别串行处理该事务。
     *
     * @param bizType 业务类型
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public String getNextNumByBizType(String bizType) throws Exception {
        // 按照bizType获取序号信息
        QueryWrapper<SysSerialNumber> serialNumberQueryWrapper = new QueryWrapper<>();
        serialNumberQueryWrapper.lambda().eq(SysSerialNumber::getBizType, bizType);
        SysSerialNumber serialNumber = sysSerialNumberDao.getOne(serialNumberQueryWrapper);
        if (serialNumber == null) {
            throw CommonException.create(ServerResponse.createByError("未获取到序号配置"));
        }

        // 拼接序号
        StringBuffer sb = new StringBuffer(serialNumber.getPrefix());

        // 当前要获取到的序号
        Integer currentNum = serialNumber.getCurrentNum() + 1;

        // 获取到当前批次号
        Integer batch = serialNumber.getBatch();
        if (currentNum > serialNumber.getEndNum()) {
            // 当前序号归到开始序号
            currentNum = serialNumber.getStartNum();
            // 批次号+1
            batch += 1;
        }

        // 组装序号
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置是否使用分组
        numberFormat.setGroupingUsed(false);
        // 设置最大整数位数
        numberFormat.setMaximumIntegerDigits(serialNumber.getBatchLen());
        // 设置最小整数位数
        numberFormat.setMinimumIntegerDigits(serialNumber.getBatchLen());

        // 拼接批次号
        sb.append(numberFormat.format(batch));

        // 设置最大整数位数
        numberFormat.setMaximumIntegerDigits(serialNumber.getNumLen());
        // 设置最小整数位数
        numberFormat.setMinimumIntegerDigits(serialNumber.getNumLen());

        // 拼接序号
        sb.append(numberFormat.format(currentNum));

        // 更新序号表数据
        serialNumber.setCurrentNum(currentNum);
        serialNumber.setBatch(batch);
        sysSerialNumberDao.updateById(serialNumber);

        // 返回序号
        return sb.toString();
    }

}
