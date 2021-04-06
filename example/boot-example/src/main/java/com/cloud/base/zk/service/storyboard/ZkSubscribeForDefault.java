package com.cloud.base.zk.service.storyboard;

import com.cloud.base.core.modules.zk.distributed.function.subscribe.ZkSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/4/6
 */
@Slf4j
@Component
public class ZkSubscribeForDefault implements ZkSubscriber {

    @Override
    public String getSubjectName() {
        return "/cloud_base/zk_storyboard_default";
    }

    @Override
    public void getInfo(String info) {
        log.info("获取到订阅信息:{}", info);
    }


}
