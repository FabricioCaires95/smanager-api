package br.com.smanager.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppConfigProperties {

    private final int pageSize;

}
