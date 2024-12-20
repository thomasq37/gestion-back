package fr.qui.gestion.v2.config;

import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof ObjectMapper) {
            ObjectMapper mapper = (ObjectMapper) bean;
            mapper.getFactory().setStreamReadConstraints(
                    StreamReadConstraints.builder().maxStringLength(10_000_000).build()
            );
        }
        return bean;
    }
}
