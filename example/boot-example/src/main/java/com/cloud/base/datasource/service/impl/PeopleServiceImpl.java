package com.cloud.base.datasource.service.impl;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.util.IdWorker;
import com.cloud.base.core.modules.datasource.annotation.DataSource;
import com.cloud.base.datasource.dao.PeopleDao;
import com.cloud.base.datasource.param.PeopleCreateParam;
import com.cloud.base.datasource.param.PeopleQueryParam;
import com.cloud.base.datasource.pojo.People;
import com.cloud.base.datasource.service.PeopleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author lh0811
 * @date 2020/12/9
 */
@Slf4j
@Service("peopleService")
public class PeopleServiceImpl implements PeopleService {

    @Autowired
    private PeopleDao peopleDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 创建人员信息
     *
     * @param param
     * @throws Exception
     */
    @DataSource
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void peopleCreate(PeopleCreateParam param) throws Exception {
        log.info(">>>开始 创建人员信息");

        try {
            People people = new People();
            // 设置参数中未传递 并且必要的属性
            people.setId(idWorker.nextId());
            // 属性对拷
            BeanUtils.copyProperties(param, people);
            // 入库
            peopleDao.insertSelective(people);
            log.info(">>>完成 创建人员信息");
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("创建人员信息失败，请联系管理员"));
        }
    }



    /**
     * 查询人员信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @DataSource("slave2")
    @Override
    public PageInfo<People> peopleQuery(PeopleQueryParam param) throws Exception {
        log.info(">>>开始 查询人员信息");
        try {
            Example example = new Example(People.class);
            // 设置排序
            example.setOrderByClause("id desc");
            // 查询条件
            Example.Criteria criteria = example.createCriteria();

            if (StringUtils.isNotEmpty(param.getName())) {
                criteria.andLike("name", "%" + param.getName() + "%");
            }
            if (param.getAge() != null) {
                criteria.andEqualTo("age", param.getAge());
            }
            // 分页查询助手
            PageHelper.startPage(param.getPageNum(),param.getPageSize());
            List<People> people = peopleDao.selectByExample(example);
            PageInfo<People> pageInfo = new PageInfo<>(people);
            // 清除分页信息
            PageHelper.clearPage();
            log.info(">>>完成 查询人员信息");
            return pageInfo;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("查询人员信息失败，请联系管理员"));
        }
    }

}
