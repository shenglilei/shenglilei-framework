package com.dofun.shenglilei.framework.core.conf;

/**
 * 本地缓存变更事件通知的接口
 */
public interface CacheChangeEventListener {
    void onReceive(final String content);
}
