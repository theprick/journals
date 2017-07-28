package com.crossover.trial.journals.jms;

import com.crossover.trial.journals.dto.JournalsJmsMessageDTO;
import com.crossover.trial.journals.service.email.EmailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageConsumer {

    private final static Logger log = Logger.getLogger(JmsMessageConsumer.class);

    @Autowired
    private EmailService emailService;

    @JmsListener(destination = "${journals.activemq.queue.new-publication}",
            containerFactory = "journalsJmsConFactory")
    public void sendNewJournalPublishedNotification(JournalsJmsMessageDTO message) {
        log.debug("Send notification email to " +  message.getUser().getLoginName());
        emailService.sendNewJurnalPublishedEmail(message.getUser(), message.getJournals().get(0));
    }

    @JmsListener(destination = "${journals.activemq.queue.daily-notification}",
            containerFactory = "journalsJmsConFactory")
    public void sendDailyDigest(JournalsJmsMessageDTO message) {
        log.debug("Send daily digest mail to " +  message.getUser().getLoginName());
        emailService.sendDailyDigestEmail(message.getUser(), message.getJournals());
    }
}
