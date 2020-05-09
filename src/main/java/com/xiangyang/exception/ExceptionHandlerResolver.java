package com.xiangyang.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//import feign.FeignException;

/**
 *
 * 异常处理配置
 *
 */
@RestControllerAdvice
public class ExceptionHandlerResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerResolver.class);
	@Data
	public static class ErrorInfo<T>{
		private String code;
		private T msg;
		private long timestamp;
		private T data;
		
		public ErrorInfo() {
			super();
		}

		public ErrorInfo(String code, T msg) {
			super();
			this.code = code;
			this.msg = msg;
		}
		
		public ErrorInfo(String code, T msg, long timestamp) {
			super();
			this.code = code;
			this.msg = msg;
			this.timestamp = timestamp;
			this.data = (T) "aa";
		}
	}
	
	/**
	 * 应用层面的业务代码级别异常处理
	 * @param codeException
	 * @return
	 */
	@ExceptionHandler(CodeException.class) 
	public ResponseEntity<ErrorInfo<?>> handleCodeException(CodeException codeException) {
		LOGGER.error("Exception:{}", codeException);
		ErrorInfo<String> errorInfo = new ErrorInfo<>(codeException.getCode(), codeException.getMsg(), codeException.getTimestamp());
		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 系统异常处理
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo<?>> handleException(Exception ex) {
		LOGGER.error("Exception:{}", ex);
		ErrorInfo<String> errorInfo = new ErrorInfo<>("SERVICE_ERROR", "服务异常", System.currentTimeMillis());
		return  new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 业务数据验证异常处理
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorInfo<?>> handleConstraintException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> cvSet = ex.getConstraintViolations();
		ConstraintViolation<?> cv = (ConstraintViolation<?>) cvSet.toArray()[0];
		String message = cv.getMessage();
		LOGGER.error("Exception:message={}, {}", message, ex);
		if (message == null) {
			ErrorInfo<String> errorInfo = new ErrorInfo<>("SERVICE_ERROR", "服务异常", System.currentTimeMillis());
			return  new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String[] codeMsg = message.split("[:]");
		ErrorInfo<String> errorInfo = new ErrorInfo<>(codeMsg[0], codeMsg[1], System.currentTimeMillis());
		return  new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * 处理Feign接口调用异常
	 * @return
	 */
//	@ExceptionHandler(FeignException.class)
//	public ResponseEntity<ErrorInfo<?>> handleFeignException(FeignException ex) {
//		LOGGER.error("Exception:{}", ex);
//		String message = ex.getMessage().trim();
//		if(message.indexOf("{") == -1) {
//			ErrorInfo<String> errorInfo = new ErrorInfo<>("SERVICE_ERROR", "服务异常", System.currentTimeMillis());
//			return  new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
//		}  
//		message = message.substring(message.indexOf("{"));
//		ErrorInfo<?> errorInfo = JSON.parseObject(message, ErrorInfo.class);
//		return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
}
