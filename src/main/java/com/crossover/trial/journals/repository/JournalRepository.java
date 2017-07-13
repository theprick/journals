package com.crossover.trial.journals.repository;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.NamedQuery;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface JournalRepository extends CrudRepository<Journal, Long> {

    List<Journal> findByPublisher(Publisher publisher);

    List<Journal> findByCategoryIdIn(List<Long> ids);

    @Query("from Journal where publishDate > :from and publishDate <= :to")
    List<Journal> findByPublicationDate(@Param("from") Date from, @Param("to")Date to);

}
