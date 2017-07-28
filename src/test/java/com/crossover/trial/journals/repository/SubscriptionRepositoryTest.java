package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SubscriptionRepositoryTest extends IntegrationTestBase {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testFindByCategory() {
        Category category = categoryRepository.findOne(CATEGORY_ID_ENDOCRINOLOGY);
        List<Subscription> subscriptions = subscriptionRepository.findByCategory(category);
        Assert.assertEquals(1, subscriptions.size());
        Assert.assertEquals(Long.valueOf(3), subscriptions.get(0).getUser().getId());
    }

    @Test
    public void testFindByCategoryIdNoSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findByCategoryId(CATEGORY_ID_MICROBIOLOGY);
        Assert.assertNotNull(subscriptions);
        Assert.assertTrue(subscriptions.isEmpty());
    }

    @Test
    public void testFindByCategoryId() {
        List<Subscription> subscriptions = subscriptionRepository.findByCategoryId(CATEGORY_ID_ENDOCRINOLOGY);
        Assert.assertNotNull(subscriptions);
        Assert.assertEquals(1, subscriptions.size());
        Subscription subscription = subscriptions.get(0);
        Assert.assertEquals(categoryRepository.getOne(CATEGORY_ID_ENDOCRINOLOGY), subscription.getCategory());
        Assert.assertEquals(getUser(USER_LOGIN_WITH_SUBSCRIPTIONS), subscription.getUser());
    }
}
