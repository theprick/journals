package com.crossover.trial.journals.controller;

import com.crossover.trial.journals.MvcIntegrationTestBase;
import com.crossover.trial.journals.service.CurrentUser;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PublisherControllerTest extends MvcIntegrationTestBase {

    private static final String FILENAME = "test.txt";
    private static final String FILE_PARAM_NAME = "file";

    @Test
    public void testHandleFileUpload() throws Exception {
        CurrentUser currentUser = getCurrentUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(FILE_PARAM_NAME, FILENAME,
                "text/plain", this.getClass().getClassLoader().getResourceAsStream(FILENAME));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/publisher/publish")
                .file(mockMultipartFile)
                .param("name", "Test Journal")
                .param("category", String.valueOf(1))
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.MOVED_TEMPORARILY.value()))
                .andExpect(redirectedUrl("/publisher/browse"));
    }
}
