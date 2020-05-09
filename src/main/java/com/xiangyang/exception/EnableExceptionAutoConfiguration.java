package com.xiangyang.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ConditionalOnProperty(name={"exception.enabled"}, havingValue="true", matchIfMissing=true)
@Import(value={ExceptionHandlerResolver.class})
@Slf4j
public class EnableExceptionAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnableExceptionAutoConfiguration.class);
	
	public EnableExceptionAutoConfiguration() {
		log.info("加载了这个异常启动类");
		LOGGER.info("####EnableExceptionAutoConfiguration#### ####INIT SUCCESS");
	}
	
}
