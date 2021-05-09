package com.cloud.base.core.modules.zk.distributed.function.subscribe;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/4/6
 */
@Component
public class ZkStoryboardEngine {

    @Autowired
    private ZkStoryboardCollection storyboardCollection;

    @Autowired
    private ZkDistributedClient zkClient;

    /**
     * 向指定主题的故事板发送消息。
     * 消息发送成功后，订阅者可以获取到该消息。
     * @param subject
     * @param msg
     * @throws Exception
     */
    public void sendMsy(String subject, String msg) throws Exception {
        ZkStoryboard zkStoryboard = storyboardCollection.getStoryboardMap().get(subject);
        if (zkStoryboard == null){
            throw CommonException.create(ServerResponse.createByError("故事板不存在"));
        }
        zkClient.getClient().setData().forPath(subject,msg.getBytes());
    }

}
