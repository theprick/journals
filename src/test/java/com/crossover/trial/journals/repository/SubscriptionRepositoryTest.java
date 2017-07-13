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
}
