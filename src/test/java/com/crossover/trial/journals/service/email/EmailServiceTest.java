package com.crossover.trial.journals.service.email;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.JournalRepository;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.subethamail.wiser.Wiser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class EmailServiceTest extends IntegrationTestBase {

    public static final String FROM = "noreply@journals.com";

    @Autowired
    private EmailService emailService;

    private Wiser wiser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private JournalRepository journalRepository;

    @Before
    public void setup() {
        wiser = new Wiser();
        wiser.start();
    }

    @After
    public void tearDown() {
        wiser.stop();
    }

    @Test
    public void testSendNewJurnalPublishedEmail() throws Exception {
        User user = getUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        Journal journal = journalRepository.findOne(JOURNAL_ID_MEDICINE);

        emailService.sendNewJurnalPublishedEmail(user, journal);

        WiserAssertions.assertReceivedMessage(wiser).assertMessage(
                FROM,
                USER_LOGIN_WITH_SUBSCRIPTIONS,
                "New journal has been published in " + journal.getCategory(),
                getExpectedEmailContent("expected-new-journal-published-email.html"),
                "text/html; charset=utf-8");
    }

    private String getExpectedEmailContent(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(classLoader.getResourceAsStream(fileName)));

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine())!=null) {
            sb.append(line);
            sb.append('\n');
        }
        br.close();

        return sb.toString();
    }

    @Test
    public void testSendDailyDigestEmail() throws Exception {
        User user = getUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        User publisher = getUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
        Optional<Publisher> publisher1 = publisherRepository.findByUser(publisher);
        Assert.assertTrue(publisher1.isPresent());

        List<Journal> journals = journalRepository.findByPublisher(publisher1.get());
        emailService.sendDailyDigestEmail(user, journals);

        WiserAssertions.assertReceivedMessage(wiser).assertMessage(
                FROM,
                USER_LOGIN_WITH_SUBSCRIPTIONS,
                "Daily digest",
                getExpectedEmailContent("expected-daily-digest-email.html"),
                "text/html; charset=utf-8");
    }
}