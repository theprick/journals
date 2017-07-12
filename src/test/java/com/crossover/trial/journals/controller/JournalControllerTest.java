package com.crossover.trial.journals.controller;

import com.crossover.trial.journals.MvcIntegrationTestBase;
import com.crossover.trial.journals.service.CurrentUser;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JournalControllerTest extends MvcIntegrationTestBase {

    @Test
    public void testRenderDocument() throws Exception{
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        mockMvc.perform(get("/view/" + JOURNAL_ID_MEDICINE)
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testRenderDocumentNotFound() throws  Exception{
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        mockMvc.perform(get("/view/" + INVALID_JOURNAL_ID)
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void testRenderDocumentUserNotSubscribed() throws  Exception{
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITHOUT_SUBSCRIPTIONS);
        mockMvc.perform(get("/view/" + JOURNAL_ID_MEDICINE)
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}
