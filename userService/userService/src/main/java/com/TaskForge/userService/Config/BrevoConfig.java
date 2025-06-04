package com.TaskForge.userService.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.xdev.brevo.api.TransactionalEmailsApi;

@Configuration
public class BrevoConfig {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Bean
    public TransactionalEmailsApi transactionalEmailsApi() {
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
        apiInstance.getApiClient().setApiKey(brevoApiKey);
        return apiInstance;
    }
}
