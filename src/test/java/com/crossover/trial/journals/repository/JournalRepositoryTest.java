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
        //FIXME make test better
        DateTime now = new DateTime();
        //FIXME implement method journalRepository.findByPublicationDate
//        Collection<Journal> journals = journalRepository.findByPublicationDate(
//                now.minusHours(24).toDate(), now.toDate());

//        assertNotNull(journals);
//        assertEquals(3, journals.size());
    }

    public void testFindByCategoryIds() {
        List<Journal> journals =
                journalRepository.findByCategoryIdIn(Collections.singletonList(CATEGORY_ID_ENDOCRINOLOGY));
        Assert.assertEquals(1, journals.size());
        Assert.assertEquals("Medicine", journals.get(0).getName());
        Assert.assertEquals("8305d848-88d2-4cbd-a33b-5c3dcc548056", journals.get(0).getUuid());
        Assert.assertEquals("Test Publisher1", journals.get(0).getPublisher().getName());
        Assert.assertEquals(CATEGORY_ID_ENDOCRINOLOGY, journals.get(0).getCategory().getId());
    }

    public void testFindByPublisher() {
        Optional<Publisher> publisher = publisherRepository
                .findByUser(userRepository.findByLoginName(PUBLISHER_LOGIN_WITH_PUBLICATIONS1));
        Assert.assertTrue(publisher.isPresent());
        Collection<Journal> journals = journalRepository.findByPublisher(publisher.get());
        Assert.assertEquals(2, journals.size());
        Assert.assertEquals("Medicine", journals.get(0).getName());
        Assert.assertEquals("8305d848-88d2-4cbd-a33b-5c3dcc548056", journals.get(0).getUuid());
        Assert.assertEquals("Test Publisher1", journals.get(0).getPublisher().getName());
        Assert.assertEquals(CATEGORY_ID_ENDOCRINOLOGY, journals.get(0).getCategory().getId());
        Assert.assertEquals("Medicine", journals.get(1).getName());
        Assert.assertEquals("8305d848-88d2-4cbd-a33b-5c3dcc548056", journals.get(0).getUuid());
        Assert.assertEquals("Test Publisher1", journals.get(0).getPublisher().getName());
        Assert.assertEquals(CATEGORY_ID_ENDOCRINOLOGY, journals.get(0).getCategory().getId());

        //FIXME Implement test
    }

}
