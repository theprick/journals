package com.crossover.trial.journals.service;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Role;
import com.crossover.trial.journals.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

//FIXME modify to integration
@RunWith(MockitoJUnitRunner.class)
public class CurrentUserDetailsServiceTest extends IntegrationTestBase{

    public static final String USER_EMAIL = "emailaddress";

    @Mock
    private UserService userService;

    @InjectMocks
    private CurrentUserDetailsService currentUserDetailsService;

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setId(1L);
        user.setEnabled(true);
        user.setLoginName("email");
        user.setPwd("passwd");
        user.setRole(Role.PUBLISHER);
        when(userService.getUserByLoginName(USER_EMAIL)).thenReturn(Optional.of(user));
        CurrentUser currentUser = currentUserDetailsService.loadUserByUsername(USER_EMAIL);
        assertNotNull(currentUser);
    }

    @Test(expected = org.springframework.security.core.userdetails.UsernameNotFoundException.class)
    public void testLoadUserByUsernameNotFound() {
        when(userService.getUserByLoginName(anyString())).thenReturn(Optional.empty());
        CurrentUser currentUser = currentUserDetailsService.loadUserByUsername(USER_EMAIL);
        assertNotNull(currentUser);
    }
}
