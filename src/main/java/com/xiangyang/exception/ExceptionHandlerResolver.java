package com.xiangyang.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.xiangyang.util.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 *
 * 异常处理配置
 *
 */
@RestControllerAdvice
public class ExceptionHandlerResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerResolver.class);
//	@Data
//	public static class ErrorInfo<T>{
//		private String code;
//		private T msg;
//		private long timestamp;
//		private T data;
//
//		public ErrorInfo() {
//			super();
//		}
//
//		public ErrorInfo(String code, T msg) {
//			super();
//			this.code = code;
//			this.msg = msg;
//		}
//
//		public ErrorInfo(String code, T msg, long timestamp) {
//			super();
//			this.code = code;
//			this.msg = msg;
//			this.timestamp = timestamp;
//			this.data = (T) "aa";
//		}
//	}
	
	/**
	 * 应用层面的业务代码级别异常处理
	 * @param codeException
	 * @return
	 */
	@ExceptionHandler(CodeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResponse<?> handleCodeException(CodeException codeException) {
		LOGGER.error("CodeException:{}", codeException);
//		ErrorInfo<String> errorInfo = new ErrorInfo<>(codeException.getCode(), codeException.getMsg(), codeException.getTimestamp());
		return CommonResponse.fail(codeException.getCode(),codeException.getMsg());
	}
	
	/**
	 * 系统异常处理
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResponse<?> handleException(Exception ex) {
		LOGGER.error("ExceptionException:{}", ex);
		return CommonResponse.fail("SERVICE_ERROR","服务异常");
	}
	
	/**
	 * 业务数据验证异常处理
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResponse<?> handleConstraintException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> cvSet = ex.getConstraintViolations();
		ConstraintViolation<?> cv = (ConstraintViolation<?>) cvSet.toArray()[0];
		String message = cv.getMessage();
		LOGGER.error("Exception:message={}, {}", message, ex);
		if (message == null) {
			return CommonResponse.fail("SERVICE_ERROR","服务异常");
		}
		String[] codeMsg = message.split("[:]");
		return CommonResponse.fail(codeMsg[0],codeMsg[1]);
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
