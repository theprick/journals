package com.crossover.trial.journals.service.email;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;

import java.util.List;

public interface EmailService {

    void sendNewJurnalPublishedEmail(User user, Journal journal);

    void sendDailyDigestEmail(User user, List<Journal> journals);
}
