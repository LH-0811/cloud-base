package com.cloud.base.core.modules.zk.distributed.function.subscribe;

import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.zk.distributed.client.ZkDistributedClient;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 故事板集合
 *
 * @author lh0811
 * @date 2021/4/6
 */
@Slf4j
@Component
public class ZkStoryboardCollection implements ApplicationListener<ContextRefreshedEvent> {

    // 故事板集合 key为故事板主题
    @Getter
    private HashMap<String, ZkStoryboard> storyboardMap = new HashMap<>();

    // 故事板订阅者集合 key为故事板主题
    @Getter
    private HashMap<String, List<ZkSubscriber>> subscriberMap = new HashMap<>();

    @Autowired
    private ZkDistributedClient zkClient;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.storyboardMap = new HashMap<>();
        this.subscriberMap = new HashMap<>();
        Map<String, ZkStoryboard> storyboardBeans = event.getApplicationContext().getBeansOfType(ZkStoryboard.class);
        Map<String, ZkSubscriber> subscriberBeans = event.getApplicationContext().getBeansOfType(ZkSubscriber.class);
        // 提取故事板信息
        for (Map.Entry<String, ZkStoryboard> entry : storyboardBeans.entrySet()) {
            storyboardMap.put(entry.getValue().getSubjectName(), entry.getValue());
        }
        // 提取订阅者
        for (Map.Entry<String, ZkSubscriber> entry : subscriberBeans.entrySet()) {
            if (subscriberMap.containsKey(entry.getValue().getSubjectName())) {
                subscriberMap.get(entry.getValue().getSubjectName()).add(entry.getValue());
            } else {
                subscriberMap.put(entry.getValue().getSubjectName(), Lists.newArrayList(entry.getValue()));
            }
        }


        // 为故事板创建zknode节点
        for (Map.Entry<String, ZkStoryboard> entry : storyboardMap.entrySet()) {
            // 创建节点
            Stat stat = zkClient.getClient().checkExists().forPath(entry.getKey());
            if (stat == null){
                zkClient.getClient().create().creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(entry.getKey());
            }
        }

        // 为订阅者开启监听
        for (Map.Entry<String, List<ZkSubscriber>> entry : subscriberMap.entrySet()) {
            NodeCache cache = new NodeCache(zkClient.getClient(), entry.getKey());
            // 默认false 设置为true cache第一次启动时 会立即冲zookeeper上获取节点数据内容并保存到cache中
            cache.start(true);
            // 增加节点监听器
            cache.getListenable().addListener(new NodeCacheListener() {
                public void nodeChanged() throws Exception {
                    String info = new String(zkClient.getClient().getData().forPath(entry.getKey()));
                    for (ZkSubscriber zkSubscriber : entry.getValue()) {
                        zkSubscriber.getInfo(info);
                    }
                }
            });
        }

        log.info("zk 故事板 订阅者--完成加载");
    }


}
