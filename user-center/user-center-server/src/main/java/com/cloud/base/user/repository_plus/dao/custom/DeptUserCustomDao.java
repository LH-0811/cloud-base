package com.cloud.base.user.repository_plus.dao.custom;

import com.cloud.base.user.dto.DeptUserDto;
import com.cloud.base.user.param.SysDeptUserQueryParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户自定义数据库访问
 *
 * @author lh0811
 * @date 2021/8/7
 */
public interface DeptUserCustomDao {


    @Select("<script>" +
            "SELECT\n" +
            "\td.`id` AS deptId,\n" +
            "\td.`no` AS deptNo,\n" +
            "\td.`name` AS deptName,\n" +
            "\tu.`id` AS userId,\n" +
            "\tu.`username` AS username,\n" +
            "\tu.`gender` AS gender,\n" +
            "\tu.`nick_name` AS nickName,\n" +
            "\tu.phone AS phone,\n" +
            "\tu.email AS email,\n" +
            "\tu.active_flag AS activeFlag,\n" +
            "\tu.last_login AS lastLogin,\n" +
            "\tu.create_time AS createTime,\n" +
            "\tu.update_time AS updateTime \n" +
            "FROM\n" +
            "\t( SELECT * FROM \n" +
            "sys_user \n" +
            "WHERE del_flag = 0 \n" +
            "<if test='param.username != null'>" +
            "\tAND sys_user.username like CONCAT(CONCAT('%',#{param.username}),'%') \n" +
            "</if>" +
            "<if test='param.phone != null'>" +
            "\tAND sys_user.phone = #{param.phone} \n" +
            "</if>" +
            "<if test='param.activeFlag != null'>" +
            "\tAND sys_user.active_flag = #{param.activeFlag} \n" +
            "</if>" +
            "<if test='param.createTimeLow != null'>" +
            "\tAND sys_user.create_time &gt;=  #{param.createTimeLow} \n" +
            "</if>" +
            "<if test='param.createTimeUp != null'>" +
            "\tAND sys_user.create_time &lt;=  #{param.createTimeUp} \n" +
            "</if>" +
            ") u\n" +
            "\tLEFT JOIN sys_user_dept_rel r ON u.id = r.user_id\n" +
            "\tLEFT JOIN sys_dept d ON r.dept_id = d.id\n" +
            "<if test='param.deptId != null'>" +
            "\tWHERE d.id = #{param.deptId} \n" +
            "</if>" +
            "</script>")
    List<DeptUserDto> selectDeptUser(@Param("param") SysDeptUserQueryParam param);

}
















