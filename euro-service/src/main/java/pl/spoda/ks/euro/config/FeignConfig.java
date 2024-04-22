package pl.spoda.ks.euro.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "pl.spoda.ks.euro")
public class FeignConfig {
}
