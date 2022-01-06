package com.cloud.base.user.repository.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.base.user.repository.dao.SysSerialNumberDao;
import com.cloud.base.user.repository.dao.mapper.SysSerialNumberMapper;
import com.cloud.base.user.repository.entity.SysSerialNumber;
import org.springframework.stereotype.Service;

/**
 * 系统管理-序号表 前缀+批次+序号
 *
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
@Service
public class SysSerialNumberDaoImpl extends ServiceImpl<SysSerialNumberMapper, SysSerialNumber> implements SysSerialNumberDao {

}
