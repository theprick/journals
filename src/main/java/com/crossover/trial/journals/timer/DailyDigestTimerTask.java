package com.crossover.trial.journals.timer;

import com.crossover.trial.journals.dto.JournalsJmsMessageDTO;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.UserRepository;
import com.crossover.trial.journals.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyDigestTimerTask {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalService journalService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${journals.activemq.queue.daily-notification}")
    private String newPublicationQueue;

//    @Scheduled( fixedRate = 86400000 )
    @Scheduled( cron = "0 0 1 * * ?" )
    public void sendDailyDigest() {
        List<Journal> journals = journalService.getJournalsPublishedInTheLast24Hours();
        if(journals != null && !journals.isEmpty()) {
            List<User> allUsers = userRepository.findAll();
            allUsers.forEach( user -> jmsTemplate.convertAndSend(newPublicationQueue,
                    new JournalsJmsMessageDTO(user, journals)));
        }
    }
}
