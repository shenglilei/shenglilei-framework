package com.dofun.shenglilei.framework.ilog;

import ch.qos.logback.classic.spi.ILoggingEvent;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import static com.dofun.shenglilei.framework.ilog.IlogFactory.LOGGER_PROPERTY_CONFIG_TOPIC;
import static com.dofun.shenglilei.framework.ilog.IlogFactory.LOGGER_PROPERTY_TOPIC;
import static com.dofun.shenglilei.framework.ilog.util.IlogUtil.getLogProperty;


public class IlogKafkaAppender extends KafkaAppender<ILoggingEvent> {
	
	private static final String METADATA_FETCH_TIMEOUT_MS = "metadata.fetch.timeout.ms";
	private static final String MAX_BLOCK_MS = "max.block.ms";
	private static final String REQUEST_TIMEOUT_MS = "request.timeout.ms";
	private String topic = null;
	@Override
	public void start() {
		//根据hostName作为key
		if(keyingStrategy == null)
		{
			setKeyingStrategy(new HostRandomKeyingStrategy());
		}
		
		//默认使用异步方式发kafka
		if(deliveryStrategy == null)
		{
			setDeliveryStrategy(new IlogAsynchronousDeliveryStrategy());
		}
		System.out.println("111111===="+producerConfig);
		//超时时间设定
		//默认：<producerConfig>request.timeout.ms=10000</producerConfig>
		if(!producerConfig.containsKey(REQUEST_TIMEOUT_MS))
		{
			addProducerConfigValue(REQUEST_TIMEOUT_MS, 5000);
		}
		
		//默认：<producerConfig>max.block.ms=10000</producerConfig>
		if(!producerConfig.containsKey(MAX_BLOCK_MS))
		{
			addProducerConfigValue(MAX_BLOCK_MS, 5000);
		}
		
		//默认：<producerConfig>metadata.fetch.timeout.ms=10000</producerConfig>
		if(!producerConfig.containsKey(METADATA_FETCH_TIMEOUT_MS))
		{
			addProducerConfigValue(METADATA_FETCH_TIMEOUT_MS, 5000);
		}
		//kafka地址 <producerConfig>bootstrap.servers=127.0.0.1:9092</producerConfig>
		if(!producerConfig.containsKey("bootstrap.servers"))
		{
			String bootstrapServers = System.getenv("ILOG_BOOTSTRAP_SERVERS");
			if(bootstrapServers != null && bootstrapServers.trim().length()>0)
			{
				addProducerConfigValue("bootstrap.servers",bootstrapServers);
			}
			else
			{
				addProducerConfigValue("bootstrap.servers","bootstrap.servers=8.129.41.26:9092");
				System.err.println("::>警告：你没有配置kafka的地址，使用默认配置 ； Wran: Kafka Bootstrap not Configed See:https://wiki.jiatuiyun.net/pages/viewpage.action?pageId=3179216");
			}
		}
		/*addProducerConfigValue("batch.size", 16384*100);
		addProducerConfigValue("compression.type", "gzip");*/
		addProducerConfigValue("compression.type", "gzip");
		
		//检查devtool使用
		checkDevTool();
		
		//topic环境变量设定
		topic = System.getenv("ILOG_DEFAULT_TOPIC");
		super.start();
	}
	
	/**
	 * spring-boot-devtools 依赖如果引用，则可能导致类被加载两次，有许多坑。<br/>
	 * 例如：单例失效、类型转换不成功<br/>
	 * 要么去除 spring-boot-devtools 依赖，<br/>
	 * 要么在 SpringApplication.run 之前调用  System.setProperty("spring.devtools.restart.enabled", "false")
	 */
	private void checkDevTool()
	{
		try {
			Class.forName("org.springframework.boot.devtools.restart.Restarter");
			String restart = System.getProperty("spring.devtools.restart.enabled");
			if(!"false".equals(restart))
			{
				System.out.println(String.format("::>WARN:If use devtools,you shoud call System.setProperty(\"spring.devtools.restart.enabled\", \"false\") before SpringApplication.run"));
				System.out.println(String.format("::>有坑警告:使用了 devtools ,需要在SpringApplication.run 之前调用  System.setProperty(\"spring.devtools.restart.enabled\", \"false\")"));
			}
		} catch (ClassNotFoundException e) {
			
		}
	}
	
	@Override
	protected String topic(ILoggingEvent e) {
		String topicFromConfigedLogger = getLogProperty(e.getLoggerName(), LOGGER_PROPERTY_CONFIG_TOPIC);
		if(topicFromConfigedLogger != null)
		{
			return topicFromConfigedLogger;
		}
		String topicFromLogger = getLogProperty(e.getLoggerName(), LOGGER_PROPERTY_TOPIC);
		if(topicFromLogger != null)
		{
			return topicFromLogger;
		}
		if(StringUtils.isNotBlank(topic)) {
			return topic;
		}
		
		return super.topic(e);
	}
	
}
