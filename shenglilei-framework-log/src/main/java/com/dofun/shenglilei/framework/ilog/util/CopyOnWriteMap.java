package com.dofun.shenglilei.framework.ilog.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 适用于读多写少的免锁Map，多线程环境性能更高。
 * @author jcoynn
 *
 * @param <K>
 * @param <V>
 */
public class CopyOnWriteMap<K, V> implements Cloneable{

    private volatile Map<K, V> internalMap;
    
    public CopyOnWriteMap() {
        internalMap = new HashMap<K, V>(100);//初始大小应根据实际应用来指定
    }
    
    public synchronized V put(K key, V value) {
        Map<K, V> newMap = new HashMap<K, V>(internalMap);//复制出一个新HashMap
        V val = newMap.put(key, value);//在新HashMap中执行写操作
        internalMap = newMap;//将原来的Map引用指向新Map
        return val;
    }
  
    public synchronized void clearKeys(List<K> keys) {
    	 Map<K, V> newMap = new HashMap<K, V>(internalMap);//复制出一个新HashMap
    	 for(K k:keys) {
    		 newMap.remove(k); 
    	 }
         internalMap = newMap;//将原来的Map引用指向新Map
    }
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        Map<K, V> newMap = new HashMap<K, V>(internalMap);
        newMap.putAll(m);
        internalMap = newMap;
    }
    
    public List<K> getKeys(){
    	return new ArrayList<>(internalMap.keySet());
    }
    
    public V get(Object key) {
        V result = internalMap.get(key);
        return result;
    }
    @Override
    public String toString() {
    	return internalMap.toString();
    }
}