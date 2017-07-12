package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.MvcIntegrationTestBase;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.service.CurrentUser;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JournalRestServiceTest extends MvcIntegrationTestBase {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    public void testBrowse() throws Exception {
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITHOUT_SUBSCRIPTIONS);
        mockMvc.perform(get("/rest/journals")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Journal1")))
                .andExpect(jsonPath("$[0].publisher.id", is(1)))
                .andExpect(jsonPath("$[0].publisher.name", is("Test Publisher1")))
                .andExpect(jsonPath("$[0].category.id", is(1)))
                .andExpect(jsonPath("$[0].category.name", is("immunology")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Journal2")))
                .andExpect(jsonPath("$[1].publisher.id", is(1)))
                .andExpect(jsonPath("$[1].publisher.name", is("Test Publisher1")))
                .andExpect(jsonPath("$[1].category.id", is(2)))
                .andExpect(jsonPath("$[1].category.name", is("pathology")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Test Journal3")))
                .andExpect(jsonPath("$[2].publisher.id", is(2)))
                .andExpect(jsonPath("$[2].publisher.name", is("Test Publisher2")))
                .andExpect(jsonPath("$[2].category.id", is(3)))
                .andExpect(jsonPath("$[2].category.name", is("endocrinology")));
    }

    @Test
    public void testPublishedList() throws Exception {
        CurrentUser currentUser = getCurrentUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
        mockMvc.perform(get("/rest/journals/published")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Journal1")))
                .andExpect(jsonPath("$[0].publisher.id", is(1)))
                .andExpect(jsonPath("$[0].publisher.name", is("Test Publisher1")))
                .andExpect(jsonPath("$[0].category.id", is(1)))
                .andExpect(jsonPath("$[0].category.name", is("immunology")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Journal2")))
                .andExpect(jsonPath("$[1].publisher.id", is(1)))
                .andExpect(jsonPath("$[1].publisher.name", is("Test Publisher1")))
                .andExpect(jsonPath("$[1].category.id", is(2)))
                .andExpect(jsonPath("$[1].category.name", is("pathology")));
    }

    @Test
    public void testPublishedListEmpty() throws Exception {
        CurrentUser currentUser = getCurrentUser(PUBLISHER_LOGIN_WITHOUT_PUBLICATIONS);
        mockMvc.perform(get("/rest/journals/published")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(content().string("[]"));
    }

    public void testUnPublish() {
        //FIXME: Implement test
    }

    //TODO this should work @WithUserDetails("user1") instead of manually creating the CurrentUser object
    @Test
    public void testSubscribe() throws Exception {
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITHOUT_SUBSCRIPTIONS);
        List<Subscription> subscriptions = subscriptionRepository.findByUserAndCategoryId(currentUser.getUser(), CATEGORY_ID_ENDOCRINOLOGY);
        Assert.assertTrue(
                String.format("No subscriptions expected for user %d in category %d", currentUser.getUser().getId(), CATEGORY_ID_ENDOCRINOLOGY),
                subscriptions == null || subscriptions.isEmpty());

        mockMvc.perform(post("/rest/journals/subscribe/" + CATEGORY_ID_ENDOCRINOLOGY)
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()));

        subscriptions = subscriptionRepository.findByUserAndCategoryId(currentUser.getUser(), CATEGORY_ID_ENDOCRINOLOGY);
        Assert.assertTrue(
                String.format("One subscriptions expected for user %d in category %d", currentUser.getUser().getId(), CATEGORY_ID_ENDOCRINOLOGY),
                subscriptions != null && subscriptions.size() == 1);
    }

    public void testGetUserSubscriptions() {
        //FIXME: Implement test
    }

    @Test
    public void testSubscriptionsEmpty() throws Exception{
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITHOUT_SUBSCRIPTIONS);
        mockMvc.perform(get("/rest/journals/subscriptions")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(content().string("[]"));
    }
}
