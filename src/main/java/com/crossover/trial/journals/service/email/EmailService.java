package com.crossover.trial.journals.service.email;

import com.crossover.trial.journals.dto.JournalDTO;
import com.crossover.trial.journals.dto.UserDTO;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;

import java.util.List;

public interface EmailService {

    void sendNewJurnalPublishedEmail(UserDTO user, JournalDTO journal);

    void sendDailyDigestEmail(UserDTO user, List<JournalDTO> journals);
}
