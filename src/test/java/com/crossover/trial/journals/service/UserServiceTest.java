package com.crossover.trial.journals.service;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Role;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import com.crossover.trial.journals.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;

public class UserServiceTest extends IntegrationTestBase {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSubscribe() {
        User user = getUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        Assert.assertEquals(1, user.getSubscriptions().size());
        List<Subscription> subscriptions = subscriptionRepository.findByUserAndCategoryId(user, CATEGORY_ID_MICROBIOLOGY);
        Assert.assertTrue(subscriptions == null || subscriptions.isEmpty());

        userService.subscribe(user, CATEGORY_ID_MICROBIOLOGY);
        Assert.assertEquals(2, user.getSubscriptions().size());

        subscriptions = subscriptionRepository.findByUserAndCategoryId(user, CATEGORY_ID_MICROBIOLOGY);
        Assert.assertNotNull(subscriptions);
        Assert.assertEquals(1, subscriptions.size());
        Assert.assertEquals(USER_LOGIN_WITH_SUBSCRIPTIONS, subscriptions.get(0).getUser().getLoginName());
        Assert.assertEquals(CATEGORY_ID_MICROBIOLOGY, subscriptions.get(0).getCategory().getId());
        Assert.assertNotNull(subscriptions.get(0).getDate());
    }

    @Test(expected = ServiceException.class)
    public void testSubscribeInvalidCategory() {
        User user = getUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        userService.subscribe(user, INVALID_CATEGORY_ID);
    }

    @Test
    public void testGetUserByLoginName() {
        Optional<User> user = userService.getUserByLoginName(USER_LOGIN_WITH_SUBSCRIPTIONS);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(USER_LOGIN_WITH_SUBSCRIPTIONS, user.get().getLoginName());
        Assert.assertEquals("$2a$10$WcgRF8VQ8DKt4h4Hz9pWv.6MXnIRmcPr0j9jqsseprsBwTD4w8WSm", user.get().getPwd());
        Assert.assertEquals(true, user.get().getEnabled());
        Assert.assertEquals(Role.USER, user.get().getRole());
    }

    @Test
    public void testGetUserByLoginNameNotFound() {
        Optional<User> user = userService.getUserByLoginName(INVALID_USER_LOGIN);
        Assert.assertFalse(user.isPresent());
    }

    @Test
    public void testFindById() {
        User expectedUser = userRepository.findByLoginName(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
        User user = userService.findById(expectedUser.getId());

        Assert.assertEquals(expectedUser, user);
    }

    @Test
    public void testFindByIdNotFound() {
        User user = userService.findById(INVALID_USER_ID);
        assertNull(user);
    }
}
