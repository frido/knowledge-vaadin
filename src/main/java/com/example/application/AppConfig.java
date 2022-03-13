package com.example.application;

import java.sql.Connection;
import java.util.Properties;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;
import com.example.application.knowledge.CustomIntegratorProvider;
import com.example.application.knowledge.CustomInterceptorImpl;
import com.example.application.knowledge.CustomStatisticsImpl;
import com.example.application.knowledge.InlineQueryLogEntryCreator;
import com.example.application.knowledge.MessageQueue;
import com.example.application.views.knowledge.LogType;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.stat.spi.StatisticsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Configuration
@PropertySource(value = { "classpath:jdbc.properties", "classpath:application.properties" })
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public CustomInterceptorImpl customInterceptorImpl() {
        return new CustomInterceptorImpl();
    }

    @Bean
    public DataSource dataSource(@Autowired Environment env) {
        SLF4JQueryLoggingListener listener = new SLF4JQueryLoggingListener();
        listener.setLogLevel(SLF4JLogLevel.INFO);
        listener.setQueryLogEntryCreator(new InlineQueryLogEntryCreator());
        return ProxyDataSourceBuilder.create(realDataSource())
        .name("xxx")
        .listener(listener)
        .build();
    }

    public DataSource realDataSource() {
        return DataSourceBuilder.create()
        .driverClassName("com.mysql.cj.jdbc.Driver")
        .url("jdbc:mysql://localhost:3306/world?autoreconnect=true&serverTimezone=UTC&useLegacyDatetimeCode=false")
        .username("root")
        .password("root")
        .build();
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager jpaTransactionManager(@Autowired LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }


    private HibernateJpaVendorAdapter vendorAdaptor() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name="entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
        @Autowired DataSource dataSource, 
        @Autowired Properties properties,
        @Autowired StatisticsFactory statisticsFactory,
        @Autowired CustomInterceptorImpl interceptor) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan("com.example");
        properties.put("hibernate.session_factory.interceptor", interceptor);
        properties.put("hibernate.stats.factory", statisticsFactory);
        properties.put("hibernate.jdbc.batch_size", "2");
        properties.put("hibernate.order_inserts", "true");
        properties.put("hibernate.order_updates", "true");
        properties.put("hibernate.generate_statistics", "true");
        properties.put("hibernate.integrator_provider", new CustomIntegratorProvider());
        entityManagerFactoryBean.setJpaProperties(properties);

        return entityManagerFactoryBean;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.jdbc.batch_size", "2");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        return properties;
    }

    @Bean
    public StatisticsFactory statisticsFactory() {
        return CustomStatisticsImpl::new;
    }

    @Bean
    public HttpSessionListener httpSessionListener() {

        MessageQueue messageQueue = MessageQueue.getInstance();
        return new HttpSessionListener() {
    
            @Override
            public void sessionCreated(HttpSessionEvent hse) {
                messageQueue.add(LogType.HTTP_SESSION_LISTENER, "HttpSessionListener", "sessionCreated", String.valueOf(hse));
            }
        
            @Override
            public void sessionDestroyed(HttpSessionEvent hse) {
                messageQueue.add(LogType.HTTP_SESSION_LISTENER, "HttpSessionListener", "sessionDestroyed", String.valueOf(hse));
            }
            
        };
    }
}
