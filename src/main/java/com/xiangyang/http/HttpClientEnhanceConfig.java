package com.xiangyang.http;

import java.io.Serializable;
import java.util.HashMap;

public class HttpClientEnhanceConfig implements Serializable {
    private Integer maxTotal;
    private Integer defaultMaxPerRoute;
    private Integer connectTimeout;
    private Integer readTimeout;
    private Boolean isUsePool;
    private HashMap<String, Integer> hostMaxPerRoute;
    private String requestContentEncoding;
    private Integer retryRequestCount;
    private Integer logChannel;

    public HttpClientEnhanceConfig() {
    }

    public Integer getMaxTotal() {
        return null == this.maxTotal ? 500 : this.maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getDefaultMaxPerRoute() {
        return null == this.defaultMaxPerRoute ? 50 : this.defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(Integer defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public Integer getConnectTimeout() {
        return null == this.connectTimeout ? 2000 : this.connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return null == this.readTimeout ? 3000 : this.readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public HashMap<String, Integer> getHostMaxPerRoute() {
        return this.hostMaxPerRoute;
    }

    public void setHostMaxPerRoute(HashMap<String, Integer> hostMaxPerRoute) {
        this.hostMaxPerRoute = hostMaxPerRoute;
    }

    public Boolean getIsUsePool() {
        return null == this.isUsePool ? true : this.isUsePool;
    }

    public void setIsUsePool(Boolean isUsePool) {
        this.isUsePool = isUsePool;
    }

    public String getRequestContentEncoding() {
        return this.requestContentEncoding != null && this.requestContentEncoding.length() != 0 ? this.requestContentEncoding : "UTF-8";
    }

    public void setRequestContentEncoding(String requestContentEncoding) {
        this.requestContentEncoding = requestContentEncoding;
    }

    public Integer getRetryRequestCount() {
        return null == this.retryRequestCount ? 3 : this.retryRequestCount;
    }

    public void setRetryRequestCount(Integer retryRequestCount) {
        this.retryRequestCount = retryRequestCount;
    }

    public Integer getLogChannel() {
        return this.logChannel == null ? 1 : this.logChannel;
    }

    public void setLogChannel(Integer logChannel) {
        this.logChannel = logChannel;
    }
}
