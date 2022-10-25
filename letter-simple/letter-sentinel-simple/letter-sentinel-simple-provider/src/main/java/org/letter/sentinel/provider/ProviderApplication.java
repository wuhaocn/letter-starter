package org.letter.sentinel.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author wuhao
 */
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) throws IOException {
        Properties configProperties = new Properties();
        InputStream config = ProviderApplication.class.getClassLoader().getResourceAsStream("application-provider.properties");
        configProperties.load(config);
        SpringApplication springApplication = new SpringApplication(ProviderApplication.class);
        springApplication.setDefaultProperties(configProperties);
        springApplication.run(args);
    }
}