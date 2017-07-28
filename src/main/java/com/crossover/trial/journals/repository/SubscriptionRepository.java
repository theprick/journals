package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Popescu Adrian on 19.03.2017
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByCategory(Category category);

    List<Subscription> findByUserAndCategoryId(User user, long categoryId);

    List<Subscription> findByCategoryId(Long categoryId);
}
