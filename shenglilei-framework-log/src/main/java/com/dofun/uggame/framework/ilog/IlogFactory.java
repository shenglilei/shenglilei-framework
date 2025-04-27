package com.dofun.uggame.framework.ilog;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.dofun.uggame.framework.ilog.util.IlogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dofun.uggame.framework.ilog.IlogThresholdFilter.LOGGER_LEVEL_PROPERTIES;
import static com.dofun.uggame.framework.ilog.util.IlogUtil.putLogProperty;


public class IlogFactory {
    private static final String DEFAULT_LOG_TYPE = "daily_log";
    public static final String LOGGER_PROPERTY_LOGTYPE = "ilog.logtype";
    /**
     * 用于代码声明定义的topic
     */
    public static final String LOGGER_PROPERTY_TOPIC = "ilog.topic";
    /**
     * 用于配置文件定义logger的topic
     */
    public static final String LOGGER_PROPERTY_CONFIG_TOPIC = "ilog.config.topic";


    /**
     * 获取logger，允许定制topic和logtype
     *
     * @param loggerName
     * @param logType
     * @param topic
     * @return
     */
    public static Logger getLogger(String loggerName, String logType, String topic) {
        if (topic != null) {
            loggerName = String.format("%s.%s", loggerName, topic);
        }
        if (logType != null) {
            loggerName = String.format("%s.%s", loggerName, logType);
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(loggerName);
        if (topic != null) {
            putLogProperty(loggerName, LOGGER_PROPERTY_TOPIC, topic);
            //有topic的加日志级别设定为info
            IlogUtil.putLogProperty(loggerName, LOGGER_LEVEL_PROPERTIES, Level.INFO.levelStr);
            logger.setLevel(Level.INFO);
        }
        if (logType == null) {
            logType = DEFAULT_LOG_TYPE;
        }
        putLogProperty(loggerName, LOGGER_PROPERTY_LOGTYPE, logType);
        return logger;
    }

    public static Logger getLogger(String loggerName, String logType) {
        return getLogger(loggerName, logType, null);
    }

    public static Logger getLogger(String loggerName) {
        return getLogger(loggerName, null, null);
    }


}
