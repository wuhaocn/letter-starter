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
		SpringApplication.run(ProviderApplication.class, args);
    }
}