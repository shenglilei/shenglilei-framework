/*
package com.dofun.shenglilei.framework.core.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.common.utils.Objects;
import com.alibaba.nacos.common.utils.StringUtils;
import com.dofun.shenglilei.framework.common.error.impl.CommonError;
import com.dofun.shenglilei.framework.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

*/
/**
 * @ClassName ConfigChangeConsumer
 * @Description nacos自定义配置变更通知的消费者，籍此构建一个通过nacos配置变更通知机制来对同服务实例做消息通知和内容传达的通道，使用场景有进程内的缓存更新，需要通知到所有同服务的实例进行该缓存的更新
 * @Author zouchuanyi
 * @Date 2022/5/6 17:38
 * @Version 1.0
 **//*

@Service
@Slf4j
public class ConfigChangeConsumer {
    private ConfigService configService;

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.config.username}")
    private String username;
    @Value("${spring.cloud.nacos.config.password}")
    private String password;
    @Value("${spring.application.name}")
    private String dataId;
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;
    private String group="DEFAULT_GROUP";

    private Map<String, CacheChangeEventListener> cacheChangeEventListenerMap;


    @PostConstruct
    private void postConstruct(){
        //加-1以便和服务的正常配置区分出来，否则会覆盖服务的原配置文件的内容
        dataId+="-1"+".yml";
        initConfigClient();
        cacheChangeEventListenerMap=new HashMap<>(10);
    }

    private void initConfigClient(){
        if (Objects.isNull(this.configService)) {
            Properties properties = new Properties();
            properties.put("namespace", namespace);
            properties.put("username", username);
            properties.put("password", password);
            properties.put("name", dataId);
            properties.put("serverAddr", serverAddr);
            properties.put("dataId", dataId);
            properties.put("group", group);

            try {
                this.configService = NacosFactory.createConfigService(properties);
                this.configService.addListener(dataId, group, new Listener() {
                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        log.debug("receive:" + configInfo);
                        JSONObject jsonObject= JSON.parseObject(configInfo);
                        if (Objects.isNull(jsonObject)) {
                            log.error("实例化成json对象失败");
                            return;
                        }
                        String key=jsonObject.getString("key");
                        if (StringUtils.isBlank(key)) {
                            log.error("获取不到key");
                            return;
                        }
                       CacheChangeEventListener listener=cacheChangeEventListenerMap.get(key);
                        if (Objects.isNull(listener)) {
                            log.error("获取不到listener");
                            return;
                        }
                        listener.onReceive(jsonObject.getString("value"));
                    }

                    @Override
                    public Executor getExecutor() {
                        return null;
                    }
                });
            } catch (NacosException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String key,String value){
        //测试修改已有的配置，看看是否更新成功
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("key",key);
            jsonObject.put("value",value);
            boolean isPublishOk = this.configService.publishConfig(dataId, group, jsonObject.toJSONString(), "yaml");
            log.debug("发布配置成功情况："+ isPublishOk);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public String getAllConfig(){
        String content = null;
        try {
            content = this.configService.getConfig(dataId, group, 5000);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        log.debug("获取到的配置全部为：\n"+content);
        return content;
    }

    public void registerCacheChangeEventListener(String key, CacheChangeEventListener listener){
        if (StringUtils.isBlank(key)||Objects.isNull(listener)) {
            throw new BusinessException(CommonError.ILLEGAL_PARAMETER);
        }
        cacheChangeEventListenerMap.put(key,listener);
    }
}
*/
