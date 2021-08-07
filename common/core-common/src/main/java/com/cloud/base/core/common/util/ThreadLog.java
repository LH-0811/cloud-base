package com.cloud.base.core.common.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 当前线程日志 打印在一起
 *
 * @author lh0811
 * @date 2021/8/7
 */
@Slf4j
public class ThreadLog {

    private static HashMap<String, LinkedList<String>> DebugMap = new HashMap<>();
    private static HashMap<String, LinkedList<String>> InfoMap = new HashMap<>();
    private static HashMap<String, LinkedList<String>> ErrorMap = new HashMap<>();

    public static void debug(String log) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        LinkedList<String> debugList = DebugMap.get(threadId);
        if (CollectionUtils.isNotEmpty(debugList)) {
            debugList.add(log);
        } else {
            debugList = Lists.newLinkedList();
            debugList.add(log);
            DebugMap.put(threadId, debugList);
        }
    }

    public static void info(String log) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        LinkedList<String> infoList = InfoMap.get(threadId);
        if (CollectionUtils.isNotEmpty(infoList)) {
            infoList.add(log);
        } else {
            infoList = Lists.newLinkedList();
            infoList.add(log);
            InfoMap.put(threadId, infoList);
        }
    }

    public static void err(String log) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        LinkedList<String> errList = ErrorMap.get(threadId);
        if (CollectionUtils.isNotEmpty(errList)) {
            errList.add(log);
        } else {
            errList = Lists.newLinkedList();
            errList.add(log);
            ErrorMap.put(threadId, errList);
        }
    }

    public static void output() {
        String threadId = String.valueOf(Thread.currentThread().getId());

        LinkedList<String> debugList = DebugMap.get(threadId);
        LinkedList<String> infoList = InfoMap.get(threadId);
        LinkedList<String> errorList = ErrorMap.get(threadId);


        if (CollectionUtils.isNotEmpty(debugList)) {
            for (String logMsg : debugList) {
                log.debug("[Thread:Id:{}]   {}", threadId, logMsg);
            }
            DebugMap.remove(threadId);
        }

        if (CollectionUtils.isNotEmpty(infoList)) {
            for (String logMsg : infoList) {
                log.info("[Thread:Id:{}]   {}", threadId, logMsg);
            }
            InfoMap.remove(threadId);
        }

        if (CollectionUtils.isNotEmpty(errorList)) {
            for (String logMsg : errorList) {
                log.error("[Thread:Id:{}]   {}", threadId, logMsg);
            }
            ErrorMap.remove(threadId);
        }
    }


}
