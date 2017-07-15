package com.crossover.trial.journals.rest;

import com.crossover.trial.journals.MvcIntegrationTestBase;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.repository.JournalRepository;
import com.crossover.trial.journals.repository.SubscriptionRepository;
import com.crossover.trial.journals.service.CurrentUser;
import com.crossover.trial.journals.service.ServiceException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JournalRestServiceTest extends MvcIntegrationTestBase {

    @Autowired
    protected SubscriptionRepository subscriptionRepository;

    @Autowired
    protected JournalRepository journalRepository;

    @Test
    public void testBrowse() throws Exception {
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        mockMvc.perform(get("/rest/journals")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Medicine")))
                //TODO check why this doesn't match
//                .andExpect(jsonPath("$[0].publishDate", is("2017-07-11 9:59 PM")))
                .andExpect(jsonPath("$[0].publisher.id", is(1)))
                .andExpect(jsonPath("$[0].publisher.name", is("Test Publisher1")))
                .andExpect(jsonPath("$[0].category.id", is(3)))
                .andExpect(jsonPath("$[0].category.name", is("endocrinology")));
    }

    @Test
    public void testPublishedList() throws Exception {
        CurrentUser currentUser = getCurrentUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
        mockMvc.perform(get("/rest/journals/published")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Medicine")))
                .andExpect(jsonPath("$[0].publisher.id", is(1)))
                .andExpect(jsonPath("$[0].publisher.name", is("Test Publisher1")))
                .andExpect(jsonPath("$[0].category.id", is(3)))
                .andExpect(jsonPath("$[0].category.name", is("endocrinology")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Journal")))
                .andExpect(jsonPath("$[1].publisher.id", is(1)))
                .andExpect(jsonPath("$[1].publisher.name", is("Test Publisher1")))
                .andExpect(jsonPath("$[1].category.id", is(4)))
                .andExpect(jsonPath("$[1].category.name", is("microbiology")));
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

    @Test
    public void testUnPublish() throws Exception {
        CurrentUser currentUser = getCurrentUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);

        Journal journal = journalRepository.findOne(JOURNAL_ID_TEST_JOURNAL);
        Assert.assertNotNull(journal);
        Assert.assertEquals(PUBLISHER_LOGIN_WITH_PUBLICATIONS1, journal.getPublisher().getUser().getLoginName());

        mockMvc.perform(delete("/rest/journals/unPublish/" + JOURNAL_ID_TEST_JOURNAL)
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()));

        Assert.assertNull(journalRepository.findOne(JOURNAL_ID_TEST_JOURNAL));
    }

    @Test
    public void testUnPublishOtherPublisher() throws Exception {
        CurrentUser currentUser = getCurrentUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS2);

        Journal journal = journalRepository.findOne(JOURNAL_ID_MEDICINE);
        Assert.assertNotNull(journal);
        Assert.assertEquals(PUBLISHER_LOGIN_WITH_PUBLICATIONS1, journal.getPublisher().getUser().getLoginName());

        try {
            mockMvc.perform(delete("/rest/journals/unPublish/" + JOURNAL_ID_MEDICINE)
                    .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")));
            Assert.fail("A ServiceException should have been thrown");
        } catch(Exception ex) {
            checkServiceException(ex, "Journal cannot be removed");
        }
        Assert.assertNotNull(journalRepository.findOne(JOURNAL_ID_MEDICINE));
    }

    //TODO this should work @WithUserDetails("login") instead of manually creating the CurrentUser object
    @Test
    public void testSubscribeInvalidCategory() throws Exception {
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITHOUT_SUBSCRIPTIONS);
        try {
            mockMvc.perform(post("/rest/journals/subscribe/" + INVALID_CATEGORY_ID)
                    .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")));
        } catch (Exception ex) {
            checkServiceException(ex, "Category not found");
        }
    }

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

    @Test
    public void testGetUserSubscriptions() throws Exception{
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITH_SUBSCRIPTIONS);
        mockMvc.perform(get("/rest/journals/subscriptions")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(3)))
                .andExpect(jsonPath("$[0].name", is("endocrinology")));
                //TODO check active field
    }

    @Test
    public void testGetUserSubscriptionsEmpty() throws Exception{
        CurrentUser currentUser = getCurrentUser(USER_LOGIN_WITHOUT_SUBSCRIPTIONS);
        mockMvc.perform(get("/rest/journals/subscriptions")
                .principal(new UsernamePasswordAuthenticationToken(currentUser, "test")))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(contentType))
                .andExpect(content().string("[]"));
    }
}
