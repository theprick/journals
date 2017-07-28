package com.crossover.trial.journals.service.email;

import com.crossover.trial.journals.dto.JournalDTO;
import com.crossover.trial.journals.dto.UserDTO;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import org.apache.log4j.Logger;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

public final class EmailComposer {
    private static final Logger LOG = Logger.getLogger(EmailComposer.class);

    public static final String NEW_JOURNAL_PUBLISHED_EMAIL_TEMPLATE = "new-journal-published-email";
    private static final String DAILY_DIGEST_EMAIL_TEMPLATE = "daily-digest-email";

    public static String createNewJournaPublishedNotificationMail(UserDTO user, JournalDTO journal) {
        LOG.debug("Create new publication notification mail for " +  user.getLoginName());
        SpringTemplateEngine templateEngine = setupTemplateEngine();

        StringWriter writer = new StringWriter();
        Context context = new Context();
        context.setLocale(Locale.US);
        context.setVariable("user", user);
        context.setVariable("journal", journal);
        templateEngine.process(NEW_JOURNAL_PUBLISHED_EMAIL_TEMPLATE, context, writer);

        return writer.toString();
    }

    public static String createDailyDigestMail(UserDTO user, List<JournalDTO> journals) {
        LOG.debug("Create daily digest mail for " +  user.getLoginName());
        SpringTemplateEngine templateEngine = setupTemplateEngine();

        StringWriter writer = new StringWriter();
        Context context = new Context();
        context.setLocale(Locale.US);
        context.setVariable("user", user);
        context.setVariable("journals", journals);
        templateEngine.process(DAILY_DIGEST_EMAIL_TEMPLATE, context, writer);

        return writer.toString();
    }

    private static SpringTemplateEngine setupTemplateEngine() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.clearTemplateAliases();
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);

        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(templateResolver);

        return templateEngine;
    }
}
