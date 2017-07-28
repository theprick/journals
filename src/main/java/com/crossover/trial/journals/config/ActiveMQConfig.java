package com.crossover.trial.journals.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
public class ActiveMQConfig {

    @Value("${journals.activemq.broker.url}")
    String brokerUrl;

    @Value("${journals.activemq.broker.username}")
    String userName;

    @Value("${journals.activemq.broker.password}")
    String password;

    @Bean
    public JmsListenerContainerFactory<?> journalsJmsConFactory(ConnectionFactory connectionFactory,
                                                                DefaultJmsListenerContainerFactoryConfigurer config) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        config.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        org.apache.qpid.jms.JmsConnectionFactory connectionFactory = new JmsConnectionFactory();
        connectionFactory.setRemoteURI("amqp://localhost:5672");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");

//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//        connectionFactory.setUserName(userName);
//        connectionFactory.setPassword(password);
//        connectionFactory.setBrokerURL(brokerUrl);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }
}
