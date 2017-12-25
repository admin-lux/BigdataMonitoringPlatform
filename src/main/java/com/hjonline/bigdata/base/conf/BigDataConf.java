package com.hjonline.bigdata.base.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "bigdata")
@PropertySource("classpath:bigdata.properties")
@Component
@Primary
public class BigDataConf {

}