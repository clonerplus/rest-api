package ir.sobhan.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ir.sobhan.restapi.dao")
public class JpaConfig {
}
