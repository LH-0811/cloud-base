package com.cloud.base.core.common.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 当前线程日志 打印在一起
 *
 * @author lh0811
 * @date 2021/8/7
 */
@Slf4j
public class ThreadLog {

    private static DebugLevelLog debugLevelLog = new DebugLevelLog();
    private static InfoLevelLog infoLevelLog = new InfoLevelLog();
    private static ErrorLevelLog errorLevelLog = new ErrorLevelLog();


    public static DebugLevelLog debug() {

        return debugLevelLog;
    }

    public static InfoLevelLog info() {
        return infoLevelLog;
    }

    public static ErrorLevelLog err() {
        return errorLevelLog;
    }



    public interface LevelLog {

        void input(String log);

        void output();

    }


    public static class DebugLevelLog implements LevelLog {
        private static HashMap<String, LinkedList<String>> DebugMap = new HashMap<>();

        @Override
        public void input(String log) {
            String threadId = String.valueOf(Thread.currentThread().getId());
            LinkedList<String> debugList = DebugMap.get(threadId);
            if (CollectionUtils.isNotEmpty(debugList)) {
                debugList.add(log);
            }else {
                debugList = Lists.newLinkedList();
                debugList.add(log);
                DebugMap.put(threadId,debugList);
            }
        }

        @Override
        public void output() {
            LinkedList<String> debugList = DebugMap.get(String.valueOf(Thread.currentThread().getId()));
            if (CollectionUtils.isNotEmpty(debugList)) {
                for (String logMsg : debugList) {
                    log.debug(logMsg);
                }
            }
        }
    }
    public static class InfoLevelLog implements LevelLog {
        private static HashMap<String, LinkedList<String>> InfoMap = new HashMap<>();


        @Override
        public void input(String log) {
            String threadId = String.valueOf(Thread.currentThread().getId());
            LinkedList<String> infoList = InfoMap.get(threadId);
            if (CollectionUtils.isNotEmpty(infoList)) {
                infoList.add(log);
            }else {
                infoList = Lists.newLinkedList();
                infoList.add(log);
                InfoMap.put(threadId,infoList);
            }
        }

        @Override
        public void output() {
            String threadId = String.valueOf(Thread.currentThread().getId());
            LinkedList<String> infoList = InfoMap.get(threadId);
            if (CollectionUtils.isNotEmpty(infoList)) {
                for (String logMsg : infoList) {
                    log.info("[ThreadId:{}] {}",threadId,logMsg);
                }
            }
        }
    }
    public static class ErrorLevelLog implements LevelLog {
        private static HashMap<String, LinkedList<String>> ErrorMap = new HashMap<>();

        @Override
        public void input(String log) {
            String threadId = String.valueOf(Thread.currentThread().getId());
            LinkedList<String> errList = ErrorMap.get(threadId);
            if (CollectionUtils.isNotEmpty(errList)) {
                errList.add(log);
            }else {
                errList = Lists.newLinkedList();
                errList.add(log);
                ErrorMap.put(threadId,errList);
            }
        }

        @Override
        public void output() {
            LinkedList<String> errList = ErrorMap.get(String.valueOf(Thread.currentThread().getId()));
            if (CollectionUtils.isNotEmpty(errList)) {
                for (String logMsg : errList) {
                    log.error(logMsg);
                }
            }
        }
    }



}
