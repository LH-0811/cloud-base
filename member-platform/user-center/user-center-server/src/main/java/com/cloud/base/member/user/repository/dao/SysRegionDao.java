package com.cloud.base.member.user.repository.dao;

import com.cloud.base.member.user.repository.entity.SysRegion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 系统-行政区域划分表(SysRegion)表数据库访问层
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
public interface SysRegionDao extends Mapper<SysRegion>, SelectByIdListMapper<SysRegion,String> {

    /**
     * 获取省列表
     *
     * @return
     */
    @Select("SELECT * FROM sys_region WHERE province_code is NULL")
    List<SysRegion> selectProvinceRegionList();

    /**
     * 更加省号码 获取市列表
     *
     * @param provinceCode
     * @return
     */
    @Select("SELECT * FROM sys_region WHERE province_code = #{provinceCode} and city_code is null")
    List<SysRegion> selectCityRegionList(@Param("provinceCode") String provinceCode);

    /**
     * 根据市号码 获取县区列表
     *
     * @param cityCode
     * @return
     */
    @Select("SELECT * FROM sys_region WHERE city_code = #{cityCode}  and area_code is null")
    List<SysRegion> selectAreaRegionList(@Param("cityCode") String cityCode);

    /**
     * 根据县区号码 获取街道列表
     *
     * @param areaCode
     * @return
     */
    @Select("SELECT * FROM sys_region WHERE area_code = #{areaCode}  and street_code is null")
    List<SysRegion> selectStreetRegionList(@Param("areaCode") String areaCode);


    /**
     * 根据县区号码 获取街道列表
     *
     * @param streetCode
     * @return
     */
    @Select("SELECT * FROM sys_region WHERE street_code = #{streetCode}")
    List<SysRegion> selectCommunityRegionList(@Param("streetCode") String streetCode);
}
