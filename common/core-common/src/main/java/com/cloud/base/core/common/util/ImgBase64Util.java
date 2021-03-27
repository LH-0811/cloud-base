package com.cloud.base.core.common.util;

import org.apache.commons.codec.binary.Base64;

public class ImgBase64Util {

    /**
     * 将图片转换成Base64编码
     * @return
     */
    public static String getImgStr(byte[] imgData) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        return  "data:image/png;base64,"+new String(Base64.encodeBase64(imgData));
    }


}
