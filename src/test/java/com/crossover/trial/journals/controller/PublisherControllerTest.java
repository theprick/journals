package com.crossover.trial.journals.controller;

import com.crossover.trial.journals.MvcIntegrationTestBase;
import com.crossover.trial.journals.service.CurrentUser;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PublisherControllerTest extends MvcIntegrationTestBase {

    public static final String FILENAME = "test.txt";

    @Test
    public void testHandleFileUpload() throws Exception {
        CurrentUser currentUser = getCurrentUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test.txt", FILENAME,
                "text/plain", this.getClass().getClassLoader().getResourceAsStream(FILENAME));

        mockMvc.perform(fileUpload("/publisher/publish")
                .file(mockMultipartFile)
                .param("name", "test")
                .param("category", String.valueOf(CATEGORY_ID_ENDOCRINOLOGY))
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.MOVED_TEMPORARILY.value()))
                .andExpect(redirectedUrl("/publisher/browse"));
    }
}
