package com.dofun.shenglilei.framework.ilog;

import com.github.danielwegener.logback.kafka.delivery.DeliveryStrategy;
import com.github.danielwegener.logback.kafka.delivery.FailedDeliveryCallback;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步发kafka
 * @author coynn
 *
 */
public class IlogAsynchronousDeliveryStrategy implements DeliveryStrategy {
	/*static ExecutorService executor = new ThreadPoolExecutor(1, 100,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            new IlogThreadFactory("ilog"));*/
	static ExecutorService executor = new ThreadPoolExecutor(3, 3,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(20000),
            new IlogThreadFactory("ilog"));
	
	/**
	 * KAFKA发送错误计数
	 */
	final AtomicInteger kafkaErrors = new AtomicInteger(0);
	
	
	
	/**
	 * 失败备选计时
	 */
	volatile long failedCallStart = 0l;
	
	volatile Throwable lastThrow = null;

	
	@Override
    public <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, final E event,
                                  final FailedDeliveryCallback<E> failedDeliveryCallback) {
    	
    	//本地落盘开关打开时，直接走备选方案，不走kafka
    	if(ILogConfig.appconfig.getPrintlocal())
    	{
    		failedDeliveryCallback.onFailedDelivery(event, new RuntimeException("Printlocal Configed On!"));
    		return true;
    	}
    	
    	//最近有连续超过五次出错时，直接放弃kafka通道。
    	if(kafkaErrors.get()>5)
    	{
    		failedDeliveryCallback.onFailedDelivery(event, new RuntimeException("Printlocal Changed On Because Errors!"));
    		//每300秒再试一下kafka
    		if(System.currentTimeMillis() - failedCallStart > 300000l)
    		{
    			failedCallStart = System.currentTimeMillis();
    			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " "+Thread.currentThread().getName()+" ::::>ilog ReTry Kafka Producer " );
    		}
    		else
    		{
    			Throwable curThrow = lastThrow;
    			if(curThrow != null)
    			{
    				curThrow.printStackTrace();
    				lastThrow = null;
    			}
    			return true;
    		}
    	}
    	
    	try {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
			            producer.send(record, new Callback() {
			                @Override
			                public void onCompletion(RecordMetadata metadata, Exception exception) {
			                    if (exception != null) {
			                    	//出错时走备选方案
			                        failedDeliveryCallback.onFailedDelivery(event, exception);
			                        //错误计数
			                        kafkaErrors.getAndIncrement();
			                        lastThrow = exception;
			                        if(kafkaErrors.get() == 1)
			                        {
			                        	System.out.println("::>kafka excpetion:");
			                        	System.out.println("::>see https://wiki.jiatuiyun.net/pages/viewpage.action?pageId=3179223");
			                        	exception.printStackTrace();
			                        }
			                    }
			                    else
			                    {
			                    	//未出错时清0计数。
			                    	kafkaErrors.set(0);
			                    }
			                }
			            });
			        } catch (Throwable e) {
			            failedDeliveryCallback.onFailedDelivery(event, e);
			            kafkaErrors.getAndIncrement();
			            lastThrow = e;
			        }
				}
			});
		} catch (Throwable e) {
			failedDeliveryCallback.onFailedDelivery(event, e);
			lastThrow = e;
			return false;
		}
        return true;
    }

    
    /**
     * The thread factory
     */
    static class IlogThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        IlogThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            namePrefix = name+"-p-" +
                          poolNumber.getAndIncrement() +
                         "-t-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

}