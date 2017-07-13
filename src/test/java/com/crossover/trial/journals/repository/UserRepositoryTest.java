package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Role;
import com.crossover.trial.journals.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByLoginName() {
        User user = userRepository.findByLoginName(USER_LOGIN_WITH_SUBSCRIPTIONS);
        Assert.assertNotNull(user);
        Assert.assertEquals(USER_LOGIN_WITH_SUBSCRIPTIONS, user.getLoginName());
        Assert.assertEquals(Role.USER, user.getRole());
        Assert.assertEquals("$2a$10$WcgRF8VQ8DKt4h4Hz9pWv.6MXnIRmcPr0j9jqsseprsBwTD4w8WSm", user.getPwd());
        Assert.assertTrue(user.getEnabled());
    }

}
