package com.crossover.trial.journals.service;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest extends IntegrationTestBase {

    @Test
    public void testSubscribeWhenAlredySubscribed() {
        Subscription subscription = new Subscription();
        Category category = new Category(CATEGORY_ID_ENDOCRINOLOGY, "Test Category");
        subscription.setCategory(category);
        when(mockedUser.getSubscriptions()).thenReturn(Collections.singletonList(subscription));

        userService.subscribe(mockedUser, CATEGORY_ID_ENDOCRINOLOGY);

        verifyNoMoreInteractions(userRepository, categoryRepository);
    }

    @Test
    public void testSubscribe() {
        when(mockedUser.getSubscriptions()).thenReturn(null);
        when(categoryRepository.findOne(CATEGORY_ID_ENDOCRINOLOGY))
                .thenReturn(new Category(CATEGORY_ID_ENDOCRINOLOGY, "Test Category"));

        userService.subscribe(mockedUser, CATEGORY_ID_ENDOCRINOLOGY);

        verify(userRepository, times(1)).save(mockedUser);
    }

    @Test(expected = ServiceException.class)
    public void testSubscribeInvalidCategory() {
        when(mockedUser.getSubscriptions()).thenReturn(null);
        when(categoryRepository.findOne(CATEGORY_ID_ENDOCRINOLOGY)).thenReturn(null);

        userService.subscribe(mockedUser, CATEGORY_ID_ENDOCRINOLOGY);

        verify(userRepository, times(1)).save(mockedUser);
    }

    @Test
    public void testGetUserByLoginName() {
        when(userRepository.findByLoginName(USER_NAME)).thenReturn(mockedUser);

        Optional<User> user = userService.getUserByLoginName(USER_NAME);

        assertTrue(user.isPresent());
        assertEquals(user.get(), mockedUser);
    }

    @Test
    public void testGetUserByLoginNameNotFound() {
        when(userRepository.findByLoginName(USER_NAME)).thenReturn(null);

        Optional<User> user = userService.getUserByLoginName(USER_NAME);

        assertFalse(user.isPresent());
    }

    @Test
    public void testFindById() {
        when(userRepository.findOne(USER_ID)).thenReturn(mockedUser);

        User user = userService.findById(USER_ID);

        assertNotNull(user);
        assertEquals(user, mockedUser);
    }

    @Test
    public void testFindByIdNotFound() {
        userRepository.findOne(USER_ID)).thenReturn(null);

        User user = userService.findById(USER_ID);

        assertNull(user);
    }
}
