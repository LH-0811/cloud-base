package com.cloud.base.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author lh0811
 * @date 2021/11/3
 */
public class UCConstant {

    //：0-静态资源 1-目录 2-菜单 3-按钮 4-接口
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum Type {
        GROUP(0, "菜单组"),
        MENU(1, "菜单"),
        INTERFACE(2, "接口"),
        PERMS_CODE(3, "权限码"),
        STATIC_RES(4, "静态资源");

        private Integer code;

        private String desc;


        public static String getDescByCode(Integer code) {
            Type[] typeEnums = values();
            for (Type type : typeEnums) {
                if (type.code.equals(code)) {
                    return type.desc;
                }
            }
            return null;
        }

        public static Integer getCodeByDesc(String desc) {
            Type[] typeEnums = values();
            for (Type type : typeEnums) {
                if (type.desc.equals(desc)) {
                    return type.code;
                }
            }
            return null;
        }
    }
}
