package com.cloud.base.zk.service.storyboard;

import com.cloud.base.core.modules.zk.distributed.function.subscribe.ZkStoryboard;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/4/6
 */
@Component
public class ZkStoryboardForDefault  implements ZkStoryboard{
    @Override
    public String getSubjectName() {
        return "/cloud_base/zk_storyboard_default";
    }
}
