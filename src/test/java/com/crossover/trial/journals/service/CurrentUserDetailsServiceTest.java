package com.crossover.trial.journals.service;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Role;
import com.crossover.trial.journals.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class CurrentUserDetailsServiceTest extends IntegrationTestBase {

    @Autowired
    private CurrentUserDetailsService currentUserDetailsService;

    @Test
    public void testLoadUserByUsername() {
        CurrentUser currentUser = currentUserDetailsService.loadUserByUsername(USER_LOGIN_WITH_SUBSCRIPTIONS);
        User user = currentUser.getUser();
        Assert.assertEquals(USER_LOGIN_WITH_SUBSCRIPTIONS, user.getLoginName());
        Assert.assertEquals("$2a$10$WcgRF8VQ8DKt4h4Hz9pWv.6MXnIRmcPr0j9jqsseprsBwTD4w8WSm", user.getPwd());
        Assert.assertEquals(Role.USER, user.getRole());
        Assert.assertEquals(true, user.getEnabled());
    }

    @Test(expected = org.springframework.security.core.userdetails.UsernameNotFoundException.class)
    public void testLoadUserByUsernameNotFound() {
        currentUserDetailsService.loadUserByUsername(INVALID_USER_LOGIN);
    }
}
