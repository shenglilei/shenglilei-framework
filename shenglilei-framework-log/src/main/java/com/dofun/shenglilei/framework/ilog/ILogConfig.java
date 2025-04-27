package com.dofun.shenglilei.framework.ilog;

import lombok.Getter;
import lombok.Setter;

public class ILogConfig {
	
	public static volatile ILogAppConfig appconfig = new ILogAppConfig();
	
	@Getter
	@Setter
	public static class ILogAppConfig {
		
		protected ILogAppConfig() {
			super();
		}

		/**
		 * 控制台日志开关
		 */
		private volatile Boolean printconsole = true;
		/**
		 * 日志本地文件落盘开关
		 */
		private volatile Boolean printlocal = false;
		
		/**
		 * 日志级别
		 */
		private volatile String level = "INFO";
		
		/**
		 * 控制台和文件系统的日志格式
		 */
		private volatile String pattern = "%d{HH:mm:ss.SSS} [%thread] %-1level %logger{100}:%L - %msg%n";
		
	
	}

}
