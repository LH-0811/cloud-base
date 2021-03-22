package com.cloud.base.datasource.service;

import com.cloud.base.datasource.param.PeopleCreateParam;
import com.cloud.base.datasource.param.PeopleQueryParam;
import com.cloud.base.datasource.pojo.People;
import com.github.pagehelper.PageInfo;

/**
 * @author lh0811
 * @date 2020/12/9
 */
public interface PeopleService {
    /**
     * 创建人员信息
     * @param param
     * @throws Exception
     */
    void peopleCreate(PeopleCreateParam param) throws Exception;

    /**
     * 查询人员信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    PageInfo<People> peopleQuery(PeopleQueryParam param) throws Exception;
}
