package com.crossover.trial.journals;

import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import com.crossover.trial.journals.repository.UserRepository;
import com.crossover.trial.journals.service.CurrentUser;
import com.crossover.trial.journals.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.nio.charset.Charset;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class MvcIntegrationTestBase extends IntegrationTestBase implements TestConstants{

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected UserService userService;

    @Autowired
    protected SubscriptionRepository subscriptionRepository;

    @BeforeClass
    public static void init() {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "java.io,java.lang,java.util,com.crossover.trial.journals.dto");
    }


    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

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
