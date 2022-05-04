package com.cloud.base.common.youji.cronjob.mgr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cloud.base.common.core.util.ip.StringUtils;
import com.cloud.base.common.youji.cronjob.core.repository.dao.Youji2ConfigDao;
import com.cloud.base.common.youji.cronjob.core.repository.entity.Youji2Config;
import com.cloud.base.common.youji.cronjob.mgr.service.Youji2ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lh0811
 * @date 2022/4/15
 */
@Slf4j
@Service
public class Youji2ConfigServiceImpl implements Youji2ConfigService {

    @Autowired
    private Youji2ConfigDao youji2ConfigDao;


    /**
     * 根据key获取锁
     *
     * @param key
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public Boolean getLockForKey(String key) {
        if (StringUtils.isBlank(key)) return Boolean.FALSE;
        QueryWrapper<Youji2Config> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Youji2Config::getCfgKey, key);
        Youji2Config config = youji2ConfigDao.getOne(queryWrapper);
        if (config != null && config.getCfgValue().equals("0")) {
            // 获取锁
            config.setCfgValue("1");
            UpdateWrapper<Youji2Config> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(Youji2Config::getId, config.getId()).eq(Youji2Config::getCfgValue, "0");
            boolean isOk = youji2ConfigDao.update(config, updateWrapper);
            if (isOk) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    /**
     * 根据key释放锁
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void releaseLockForKey(String key) {
        if (StringUtils.isBlank(key)) return;
        QueryWrapper<Youji2Config> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Youji2Config::getCfgKey, key);
        Youji2Config config = youji2ConfigDao.getOne(queryWrapper);
        if (config != null && config.getCfgValue().equals("1")) {
            // 获取锁
            config.setCfgValue("0");
            UpdateWrapper<Youji2Config> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(Youji2Config::getId, config.getId()).eq(Youji2Config::getCfgValue, "1");
            youji2ConfigDao.update(config, updateWrapper);
        }
    }


}
