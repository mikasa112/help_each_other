package com.help.each.config;

import lombok.Data;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description jwt生成参数配置
 */
@Data
public class JwtConfig {
    private String tokenHeader = "Authorization";
    private Long ttl = 600000L;
    private Long remember = 604800000L;
}
