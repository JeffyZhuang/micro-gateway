package com.zzh.filter;

import com.zzh.config.CommonConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * @Author: zzh
 * @Description:
 * @Date: 2019/8/19
 */
@Component
@Slf4j
public class LoggerFilter implements GlobalFilter, Ordered {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");

    private static final String REQ_TIME = "request_time";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(REQ_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQ_TIME);
                    if (Objects.nonNull(startTime)) {
                        log.info("{\"host\":\"{}\",\"uri\":\"{}\",\"method\":\"{}\",\"requestTime\":\"{}\"," +
                                "\"responseTime\":\"{}\",\"params\":\"{}\"}", exchange.getRequest().getURI().getHost
                                (), exchange.getRequest().getPath(), exchange.getRequest().getMethod(),
                                reqTimeFormatStr(startTime), consumerTime(startTime), exchange.getRequest()
                                        .getQueryParams());
                    }
                }

        ));
    }

    /**
     * @return
     * @Description 消耗时间计算
     * @Date 2019/8/19 15:47
     * @Param
     * @author zzh
     **/
    private double consumerTime(Long startTime) {
        try {
            return (System.currentTimeMillis() - startTime) / 1000.0;
        } catch (Exception e) {
            log.error("route consumerTime error startTime={}, error={}", startTime, e);
        }
        return 0;
    }

    /**
     * @return
     * @Description 时间格式化
     * @Date 2019/8/19 15:27
     * @Param
     * @author zzh
     **/
    private String reqTimeFormatStr(Long startTime) {
        try {
            return dateTimeFormatter.format(Instant.ofEpochMilli(startTime).atZone(ZoneId.systemDefault()));
        } catch (Exception e) {
            log.error("route reqTimeFormatStr error startTime={}, error={}", startTime, e);
        }
        return "";
    }

    @Override
    public int getOrder() {
        return CommonConst.Order.LOGER_FILTER_ORDER;
    }
}
