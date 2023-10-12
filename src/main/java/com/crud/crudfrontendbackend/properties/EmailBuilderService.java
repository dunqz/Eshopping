package com.crud.crudfrontendbackend.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailBuilderService {

    private final TemplateEngine templateEngine;

    @Autowired
    public EmailBuilderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildEmail(String name, String link) {
        // Create a Thymeleaf context and set variables for the template
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("link", link);

        // Process the template with the context
        return templateEngine.process("template", context);
    }
}