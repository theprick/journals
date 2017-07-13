package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class JournalRepositoryTest extends IntegrationTestBase {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByPublicationDate() {
        DateTime from = new DateTime(2017, 7, 11, 21, 59, 59);
        DateTime to = new DateTime(2017, 7, 12, 21, 59, 59);

        Collection<Journal> journals = journalRepository.findByPublicationDate(from.toDate(), to.toDate());
        Assert.assertNotNull(journals);
        Assert.assertEquals(1, journals.size());
    }

    @Test
    public void testFindByCategoryIds() {
        List<Journal> journals =
                journalRepository.findByCategoryIdIn(Collections.singletonList(CATEGORY_ID_ENDOCRINOLOGY));
        Assert.assertEquals(1, journals.size());
        Assert.assertEquals("Medicine", journals.get(0).getName());
        Assert.assertEquals("8305d848-88d2-4cbd-a33b-5c3dcc548056", journals.get(0).getUuid());
        Assert.assertEquals("Test Publisher1", journals.get(0).getPublisher().getName());
        Assert.assertEquals(CATEGORY_ID_ENDOCRINOLOGY, journals.get(0).getCategory().getId());
    }

    @Test
    public void testFindByPublisher() {
        Optional<Publisher> publisher = publisherRepository
                .findByUser(userRepository.findByLoginName(PUBLISHER_LOGIN_WITH_PUBLICATIONS1));
        Assert.assertTrue(publisher.isPresent());
        List<Journal> journals = journalRepository.findByPublisher(publisher.get());
        Assert.assertEquals(2, journals.size());
        Assert.assertEquals("Medicine", journals.get(0).getName());
        Assert.assertEquals("8305d848-88d2-4cbd-a33b-5c3dcc548056", journals.get(0).getUuid());
        Assert.assertEquals("Test Publisher1", journals.get(0).getPublisher().getName());
        Assert.assertEquals(CATEGORY_ID_ENDOCRINOLOGY, journals.get(0).getCategory().getId());
        Assert.assertEquals("Test Journal", journals.get(1).getName());
        Assert.assertEquals("09628d25-ea42-490e-965d-cd4ffb6d4e9d", journals.get(1).getUuid());
        Assert.assertEquals("Test Publisher1", journals.get(1).getPublisher().getName());
        Assert.assertEquals(CATEGORY_ID_MICROBIOLOGY, journals.get(1).getCategory().getId());
    }
}
