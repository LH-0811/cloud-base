package com.cloud.base.core.modules.zk.distributed.function.subscribe;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
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

    public void sendMsy(String subject, String msg) throws Exception {
        ZkStoryboard zkStoryboard = storyboardCollection.getStoryboardMap().get(subject);
        if (zkStoryboard == null){
            throw CommonException.create(ServerResponse.createByError("故事板不存在"));
        }
        zkClient.getClient().setData().forPath(subject,msg.getBytes());
    }

}
