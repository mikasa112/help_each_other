package com.help.each.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description application基础配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {
    private IgnoreConfig ignores;
    private JwtConfig jwt;
    /*
     * 默认分页大小
     */
//    private Long pageSize = 10L;
}
