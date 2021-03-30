package com.cloud.base.example.cloud.storage.service.impl;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.example.cloud.storage.param.SubtractionStorageParam;
import com.cloud.base.example.cloud.storage.repository.dao.TStorageDao;
import com.cloud.base.example.cloud.storage.repository.entity.TStorage;
import com.cloud.base.example.cloud.storage.service.StorageService;
import com.cloud.base.example.cloud.storage.vo.SubtractionStorageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@Slf4j
@Service("storageService")
public class StorageServiceImpl implements StorageService {

    @Autowired
    private TStorageDao storageDao;



    // 减库存
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubtractionStorageVo subtractionStorageVo(SubtractionStorageParam param) throws Exception {

        // 查询数据合法性
        TStorage storage = null;
        try {
            TStorage selectStorage = new TStorage();
            selectStorage.setProductId(param.getProductId());
            storage = storageDao.selectOne(selectStorage);
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("系统错误,没有找打库存信息"));
        }
        if (storage == null){
            throw CommonException.create( ServerResponse.createByError("没有找打库存信息"));
        }


        if (param.getCount().compareTo(storage.getResidue())>0){
            throw CommonException.create( ServerResponse.createByError("库存不足"));
        }

        // 扣减库存
        try {
            storage.setId(storage.getId());
            storage.setUsed(storage.getUsed()+param.getCount());
            storage.setResidue(storage.getResidue() - param.getCount());
            storageDao.updateByPrimaryKeySelective(storage);
        } catch (Exception e) {
            throw CommonException.create(e,ServerResponse.createByError("系统错误,扣减库存失败"));
        }
        log.info("扣减库存完成");
        return new SubtractionStorageVo(param.getProductId(), param.getCount(), storage.getPrice().multiply(new BigDecimal(param.getCount())));
    }




}
