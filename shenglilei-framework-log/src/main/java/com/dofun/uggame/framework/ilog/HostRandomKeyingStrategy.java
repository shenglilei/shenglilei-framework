package com.dofun.uggame.framework.ilog;

import com.github.danielwegener.logback.kafka.keying.KeyingStrategy;

import java.nio.ByteBuffer;
import java.util.UUID;

public class HostRandomKeyingStrategy implements KeyingStrategy<Object> {

	private static final String KEY_STR = UUID.randomUUID().toString();
    private byte[] hostnameHash = null ;

    public HostRandomKeyingStrategy() {
		super();
		String keyStr = KEY_STR;
		int nameKeyUse = keyStr.hashCode();
		hostnameHash = ByteBuffer.allocate(4).putInt(nameKeyUse).array();
    	System.out.println(String.format("::> HostRandomKeyingStrategy.java:18 ilog name key %s hashcode use %s", keyStr,nameKeyUse));
	}




    @Override
    public byte[] createKey(Object e) {
        return hostnameHash;
    }
}