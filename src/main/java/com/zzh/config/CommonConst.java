package com.zzh.config;

import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;

/**
 * @Author: zzh
 * @Description: 常量
 * @Date: 2019/8/19
 */
public class CommonConst {
    public static class Order {
        public static final Integer LOGIN_FILTER_ORDER = NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        public static final Integer AUTH_FILTER_ORDER = LOGIN_FILTER_ORDER - 1;
        public static final Integer RESPONSE_DATA_FILTER_ORDER = LOGIN_FILTER_ORDER - 1;
        public static final Integer LOGER_FILTER_ORDER = RESPONSE_DATA_FILTER_ORDER - 1;
    }

}
