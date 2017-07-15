package com.crossover.trial.journals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class MvcIntegrationTestBase extends IntegrationTestBase implements TestConstants{

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @BeforeClass
    public static void init() {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "java.io,java.lang,java.util,com.crossover.trial.journals.dto");
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
}
