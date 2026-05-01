package com.hezong.pubcar;

import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.core.client.RestTemplate;
import com.java110.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.java110.service.init.ServiceStartInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;

/**
 * @author hh
 * @datetime 2021/7/14 15:18
 */
@SpringBootApplication(scanBasePackages = {"com.java110.service","com.java110.core", "com.hezong.pubcar",
                "com.java110.config.properties.code", "com.java110.db"})
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
            basePackages = {"com.hezong.pubcar.listener"})
@EnableFeignClients(basePackages = {"com.java110.intf.user", "com.java110.intf.common", "com.java110.intf.fee",
        "com.java110.intf.order"})
public class PubCarServiceApplicationStart{

    private static final Logger logger = LoggerFactory.getLogger(PubCarServiceApplicationStart.class);

    public static void main(String[] args) {
        try {
            ApplicationContext context = SpringApplication.run(PubCarServiceApplicationStart.class, args);
            ServiceStartInit.initSystemConfig(context);
        } catch (Throwable e) {
            logger.error("系统启动失败", e);
        }
    }

    /**
     * 实例化RestTemplate，通过@LoadBalanced注解开启均衡负载能力.
     *
     * @return restTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        return new RestTemplateBuilder().additionalMessageConverters(m).build(RestTemplate.class);
    }


}
