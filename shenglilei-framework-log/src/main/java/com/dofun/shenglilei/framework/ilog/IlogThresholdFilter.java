package com.dofun.shenglilei.framework.ilog;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;
import com.dofun.shenglilei.framework.ilog.util.IlogUtil;

import java.util.ArrayList;
import java.util.List;

public class IlogThresholdFilter extends ThresholdFilter {
    public static List<IlogThresholdFilter> thresholdFilters = new ArrayList<>();

    public static final String LOGGER_LEVEL_PROPERTIES = "ilog.level";


    @Override
    public void start() {
        setLevel(ILogConfig.appconfig.getLevel());
        super.start();
        thresholdFilters.add(this);
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String level = IlogUtil.getLogProperty(event.getLoggerName(), LOGGER_LEVEL_PROPERTIES);
        if (level != null) {
            if (event.getLevel().isGreaterOrEqual(Level.toLevel(level))) {
                return FilterReply.NEUTRAL;
            }
        }

        return super.decide(event);
    }
}
