package com.dofun.shenglilei.framework.ilog;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import com.github.danielwegener.logback.kafka.KafkaAppenderConfig;
import com.github.danielwegener.logback.kafka.delivery.FailedDeliveryCallback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * logback-kafka-appender JAR包拷过来的代码，只是改了topic不写死。
 * @author coynn
 *
 * @param <E>
 */
public class KafkaAppender<E> extends KafkaAppenderConfig<E> {

    /**
     * Kafka clients uses this prefix for its slf4j logging.
     * This appender defers appends of any Kafka logs since it could cause harmful infinite recursion/self feeding effects.
     */
    private static final String KAFKA_LOGGER_PREFIX = "org.apache.kafka.clients";

    private static LazyProducer lazyProducer = null;
    private final AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<E>();
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();
    private final FailedDeliveryCallback<E> failedDeliveryCallback = new FailedDeliveryCallback<E>() {
        @Override
        public void onFailedDelivery(E evt, Throwable throwable) {
            aai.appendLoopOnAppenders(evt);
        }
    };

    public KafkaAppender() {
        // setting these as config values sidesteps an unnecessary warning (minor bug in KafkaProducer)
        addProducerConfigValue(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        addProducerConfigValue(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
    }
    static KafkaAppender me;
    @Override
    public void doAppend(E e) {
        ensureDeferredAppends();
        if (e instanceof ILoggingEvent && ((ILoggingEvent)e).getLoggerName().startsWith(KAFKA_LOGGER_PREFIX)) {
            deferAppend(e);
        } else {
            super.doAppend(e);
        }
    }

    @Override
    public void start() {
        // only error free appenders should be activated
        if (!checkPrerequisites()) return;

        if(lazyProducer == null)
        {
        	lazyProducer = new LazyProducer();
        }

        super.start();
        
        me = this;
        
        start = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        super.stop();
        /*if (lazyProducer != null && lazyProducer.isInitialized()) {
            try {
                lazyProducer.get().close();
            } catch (KafkaException e) {
                this.addWarn("Failed to shut down kafka producer: " + e.getMessage(), e);
            }
            lazyProducer = null;
        }*/
        
    }

    @Override
    public void addAppender(Appender<E> newAppender) {
        aai.addAppender(newAppender);
    }

    @Override
    public Iterator<Appender<E>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    @Override
    public Appender<E> getAppender(String name) {
        return aai.getAppender(name);
    }

    @Override
    public boolean isAttached(Appender<E> appender) {
        return aai.isAttached(appender);
    }

    @Override
    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    @Override
    public boolean detachAppender(Appender<E> appender) {
        return aai.detachAppender(appender);
    }

    @Override
    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }
    
    static ExecutorService executor = Executors.newFixedThreadPool(2, new IlogAsynchronousDeliveryStrategy.IlogThreadFactory("kpend"));
  
    
    volatile long start = System.currentTimeMillis();

    @Override
    protected void append(E e) {
        try {
			final byte[] payload = encoder.doEncode(e);
			final byte[] key = keyingStrategy.createKey(e);
			final ProducerRecord<byte[], byte[]> record = new ProducerRecord<byte[],byte[]>(topic(e), key, payload);
			
			//刚启动的60秒内，异步线程。因为启动时，要连KAFKA会要点时间，这个时间不要影响主业务。
			if(System.currentTimeMillis() - start > 60000)
			{
				deliveryStrategy.send(lazyProducer.get(), record, e, failedDeliveryCallback);
			}
			else
			{
				executor.execute(()->{
					try {
						deliveryStrategy.send(lazyProducer.get(), record, e, failedDeliveryCallback);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
			}
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
    
    /**
      * 提供合适的topic
     * @return
     */
    protected String topic(E e)
    {
    	return topic;
    }
    
    
    
    static Producer<byte[], byte[]> createProducer() {
        return new KafkaProducer<byte[], byte[]>(new HashMap<String, Object>(me.getProducerConfig()));
    }

    private void deferAppend(E event) {
        queue.add(event);
    }

    // drains queue events to super
    private void ensureDeferredAppends() {
        E event;

        while ((event = queue.poll()) != null) {
            super.doAppend(event);
        }
    }

    /**
     * Lazy initializer for producer, patterned after commons-lang.
     *
     * @see <a href="https://commons.apache.org/proper/commons-lang/javadocs/api-3.4/org/apache/commons/lang3/concurrent/LazyInitializer.html">LazyInitializer</a>
     */
    private static class LazyProducer {

        private volatile Producer<byte[], byte[]> producer;

        public Producer<byte[], byte[]> get() {
            Producer<byte[], byte[]> result = this.producer;
            if (result == null) {
                synchronized(this) {
                    result = this.producer;
                    if(result == null) {
                        this.producer = result = this.initialize();
                    }
                }
            }

            return result;
        }

        protected Producer<byte[], byte[]> initialize() {
            Producer<byte[], byte[]> producer = null;
            try {
                producer = createProducer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return producer;
        }

        public boolean isInitialized() { return producer != null; }
    }

}
