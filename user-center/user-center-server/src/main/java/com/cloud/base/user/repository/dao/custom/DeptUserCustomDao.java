package com.cloud.base.user.repository.dao.custom;

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
            "\tsys_dept.`id` AS deptId,\n" +
            "\tsys_dept.`no` AS deptNo,\n" +
            "\tsys_dept.`name` AS deptName,\n" +
            "\tsys_user.`id` AS userId,\n" +
            "\tsys_user.`username` AS username,\n" +
            "\tsys_user.`nick_name` AS nickName,\n" +
            "\tsys_user.phone AS phone,\n" +
            "\tsys_user.e_mail AS eMail,\n" +
            "\tsys_user.active_flag AS activeFlag,\n" +
            "\tsys_user.last_login AS lastLogin,\n" +
            "\tsys_user.create_time AS createTime,\n" +
            "\tsys_user.update_time AS updateTime \n" +
            "FROM\n" +
            "\tsys_dept,\n" +
            "\tsys_user_dept_rel,\n" +
            "\tsys_user \n" +
            "WHERE\n" +
            "\tsys_dept.id = sys_user_dept_rel.dept_id \n" +
            "\tAND sys_user_dept_rel.user_id = sys_user.id \n" +
            "\tAND sys_user.del_flag = 0 \n" +
            "<if test='param.deptId != null'>" +
            "\tAND sys_dept.id = #{param.deptId} \n" +
            "</if>" +
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
            "</script>")
    List<DeptUserDto> selectDeptUser(@Param("param") SysDeptUserQueryParam param);

}
















