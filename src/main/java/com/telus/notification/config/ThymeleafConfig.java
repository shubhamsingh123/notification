package com.telus.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

// @Configuration
// public class ThymeleafConfig {

//     @Bean
//     public TemplateEngine emailTemplateEngine() {
//         SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//         templateEngine.addTemplateResolver(stringTemplateResolver());
//         return templateEngine;
//     }

//     private StringTemplateResolver stringTemplateResolver() {
//         StringTemplateResolver resolver = new StringTemplateResolver();
//         resolver.setTemplateMode(TemplateMode.HTML);
//         resolver.setCacheable(false);
//         return resolver;
//     }
// }

@Configuration
public class ThymeleafConfig {

    @Bean
    public TemplateEngine stringTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(stringTemplateResolver());
        return templateEngine;
    }

    private StringTemplateResolver stringTemplateResolver() {
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        resolver.setCheckExistence(false); // Optional, avoids unnecessary checks
        return resolver;
    }
}