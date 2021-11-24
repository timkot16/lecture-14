package by.itacademy.javaenterprise.kotkovski.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("by.itacademy.javaenterprise.kotkovski")
public class SpringConfig {

    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String userName;
    @Value("${dp.password}")
    private String password;
    @Value("${db.driver}")
    private String driver;
    @Value("${db.max.pool.size}")
    private Integer maxPoolSize;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setMaximumPoolSize(maxPoolSize);
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
