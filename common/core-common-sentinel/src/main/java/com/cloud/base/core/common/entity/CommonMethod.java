package com.cloud.base.core.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ResponseCode;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommonMethod {

    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static void checkParam(String className, String methodName, Object param) throws Exception {
        log.info("开始执行{}.{},参数:{}", className, methodName, JSONObject.toJSONString(param));
        if (param == null) {
            return;
        }
        Map<String, String> err = BeanValidator.validate(param);
        if (!CollectionUtils.isEmpty(err)) {
            log.info("{}.{},参数不合法:{}", className, methodName, JSONObject.toJSONString(err));
            ServerResponse serverResponse = ServerResponse.createByError(ResponseCode.ERROR.getCode(), "参数不合法");
            throw CommonException.create(serverResponse);
        }
    }

    public static JSONArray listToTree(List objList, String rootKey, String pidKey, String idKey,String childKey) {
        JSONArray treeList = new JSONArray();
        JSONArray sourceList = JSONArray.parseArray(JSON.toJSONString(objList));
        for (Object ele : sourceList) {
            if (((JSONObject) ele).getString(pidKey) == null || ((JSONObject) ele).getString(pidKey).equals(rootKey)) {
                treeList.add(ele);
            }
        }
        findChildren(treeList, sourceList, pidKey, idKey,childKey);
        return treeList;
    }

    private static void findChildren(JSONArray treeList, JSONArray sourceList, String pidKey, String idKey,String childKey) {
        for (Object treeEle : treeList) {
            JSONArray children = new JSONArray();
            for (Object ele : sourceList) {
                if (StringUtils.isNotEmpty(((JSONObject) treeEle).getString(idKey)) && ((JSONObject) treeEle).getString(idKey).equals(((JSONObject) ele).getString(pidKey))) {
                    children.add(ele);
                }
            }
            ((JSONObject) treeEle).put(childKey,children);
            findChildren(children, sourceList, pidKey, idKey,childKey);
        }
    }


}
