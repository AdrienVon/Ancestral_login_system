package cn.gzxy.gtxyrgzn.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppConfig {
    @Value("${app.storage}")
    String storage;
}
