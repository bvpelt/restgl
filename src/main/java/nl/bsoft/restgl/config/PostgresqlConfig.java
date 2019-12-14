package nl.bsoft.restgl.config;

import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)

public class PostgresqlConfig extends DatabaseConfig {

    private PrometheusMeterRegistry prometheusMeterRegistry;

    public PostgresqlConfig(final PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @Bean
    @Primary
    public DataSourceProperties DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.configuration")
    public HikariDataSource dataSource() {
        log.info("datasourcepg config: {}", DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build().toString());
        return DataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @PostConstruct
    public void setUpHikariWithMetrics() {
        if (dataSource() instanceof HikariDataSource) {
            dataSource().setMetricRegistry(prometheusMeterRegistry);
        }
    }

    @Bean
    public SessionFactory sfPostgres(final LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return Objects.requireNonNull(entityManagerFactory.getObject()).unwrap(SessionFactory.class);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean(name = "liquibase")
    @DependsOn("dataSource")
    @Primary
    public SpringLiquibase liquibase(@Qualifier("dataSource") final DataSource dataSource,
                                     @Qualifier("liquibaseProperties") LiquibaseProperties liquibaseProperties) {
        return springLiquibase(dataSource, liquibaseProperties);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") final DataSource dataSource) {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());
        factoryBean.setPackagesToScan("nl.bsoft.restql.config.repo", "nl.bsoft.restgl.model");
        factoryBean.setPersistenceUnitName("postgres-unit");

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return hibernateProperties;
    }

}
