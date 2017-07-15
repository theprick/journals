package com.crossover.trial.journals;

import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.CurrentUser;
import com.crossover.trial.journals.service.UserService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.Optional;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestBase implements TestConstants {

    @Autowired
    protected UserService userService;

    protected User getUser(String userLogin) {
        Optional<User> user = userService.getUserByLoginName(userLogin);
        if(!user.isPresent()) {
            Assert.fail(userLogin + " doesn't exist");
        }
        return user.get();
    }

    protected CurrentUser getCurrentUser(String userLogin) {
        return new CurrentUser(getUser(userLogin));
    }
}
