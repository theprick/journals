package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PublisherRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    public void testFindByUser() {
        User user = userRepository.findByLoginName(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);

        Optional<Publisher> publisher = publisherRepository.findByUser(user);
        Assert.assertTrue(publisher.isPresent());
        //noinspection OptionalGetWithoutIsPresent
        Assert.assertEquals(Long.valueOf(1), publisher.get().getUser().getId());
        //noinspection OptionalGetWithoutIsPresent
        Assert.assertEquals("Test Publisher1", publisher.get().getName());
    }
}
