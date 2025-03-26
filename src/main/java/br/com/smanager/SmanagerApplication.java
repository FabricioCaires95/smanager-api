package br.com.smanager;

import br.com.smanager.infrastructure.config.AppConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfigProperties.class)
public class SmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmanagerApplication.class, args);
    }

}
