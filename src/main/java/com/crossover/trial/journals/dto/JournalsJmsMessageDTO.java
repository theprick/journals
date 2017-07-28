package com.crossover.trial.journals.dto;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JournalsJmsMessageDTO implements Serializable {

    private static final long serialVersionUID = -4584116792829775850L;

    private UserDTO user;

    private List<JournalDTO> journals;

    public JournalsJmsMessageDTO(User user, Journal journal) {
        this.user = new UserDTO(user.getLoginName());
        this.journals = new ArrayList<>(Collections.singletonList(
                new JournalDTO(journal.getName(),
                        journal.getPublishDate(),
                        journal.getPublisher().getName(),
                        journal.getCategory().getName())));
    }

    public JournalsJmsMessageDTO(User user, List<Journal> journals) {
        this.user = new UserDTO(user.getLoginName());
        this.journals = journals.stream().map(
                journal -> new JournalDTO(journal.getName(),
                        journal.getPublishDate(),
                        journal.getPublisher().getName(),
                        journal.getCategory().getName())
        ).collect(Collectors.toList());
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<JournalDTO> getJournals() {
        return journals;
    }

    public void setJournals(List<JournalDTO> journals) {
        this.journals = journals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JournalsJmsMessageDTO that = (JournalsJmsMessageDTO) o;

        if (!user.equals(that.user)) return false;
        return journals.equals(that.journals);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + journals.hashCode();
        return result;
    }
}
