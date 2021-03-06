package com.crossover.trial.journals.service;

import com.crossover.trial.journals.IntegrationTestBase;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.PublisherRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class JournalServiceTest extends IntegrationTestBase {

	@Autowired
	private JournalService journalService;

	@Autowired
	private UserService userService;

	@Autowired
	private PublisherRepository publisherRepository;

	@Test
	public void browseSubscribedUser() {
		List<Journal> journals = journalService.listAll(getUser(USER_LOGIN_WITH_SUBSCRIPTIONS));
		assertNotNull(journals);
		assertEquals(1, journals.size());

		assertEquals(new Long(1), journals.get(0).getId());
		assertEquals("Medicine", journals.get(0).getName());
		assertEquals(new Long(1), journals.get(0).getPublisher().getId());
		assertNotNull(journals.get(0).getPublishDate());
	}

	@Test
	public void browseUnSubscribedUser() {
		List<Journal> journals = journalService.listAll(getUser(USER_LOGIN_WITHOUT_SUBSCRIPTIONS));
		assertEquals(0, journals.size());
	}

	@Test
	public void listPublisher() {
		User user = getUser("publisher1");
		Optional<Publisher> p = publisherRepository.findByUser(user);
		List<Journal> journals = journalService.publisherList(p.get());
		assertEquals(2, journals.size());

		assertEquals(new Long(1), journals.get(0).getId());
		assertEquals(new Long(2), journals.get(1).getId());

		assertEquals("Medicine", journals.get(0).getName());
		assertEquals("Test Journal", journals.get(1).getName());
		journals.forEach(j -> assertNotNull(j.getPublishDate()));
		journals.forEach(j -> assertEquals(new Long(1), j.getPublisher().getId()));

	}

	@Test(expected = ServiceException.class)
	public void publishFailWithInconpleteJournal() throws ServiceException {
		User user = getUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS2);
		Optional<Publisher> p = publisherRepository.findByUser(user);

		Journal journal = new Journal();
		journal.setName(NEW_JOURNAL_NAME);

		journalService.publish(p.get(), journal, CATEGORY_ID_ENDOCRINOLOGY);
	}

	@Test(expected = ServiceException.class)
	public void publishFailInNonExistentCategory() throws ServiceException {
		User user = getUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS2);
		Optional<Publisher> p = publisherRepository.findByUser(user);

		Journal journal = new Journal();
		journal.setName("New Journal");

		journalService.publish(p.get(), journal, INVALID_CATEGORY_ID);
	}

	@Test()
	public void publishSuccess() {
		User user = getUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS2);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		Assert.assertTrue(p.isPresent());

		List<Journal> userJournals = journalService.listAll(getUser(USER_LOGIN_WITH_SUBSCRIPTIONS));
		assertEquals(1, userJournals.size());
		List<Journal> publisherJournals = journalService.publisherList(p.get());
		assertEquals(1, publisherJournals.size());

		Journal journal = new Journal();
		journal.setName(NEW_JOURNAL_NAME);
		journal.setUuid("SOME_EXTERNAL_ID");
		try {
			journalService.publish(p.get(), journal, CATEGORY_ID_ENDOCRINOLOGY);
		} catch (ServiceException e) {
			fail(e.getMessage());
		}

		userJournals = journalService.listAll(getUser(USER_LOGIN_WITH_SUBSCRIPTIONS));
		assertEquals(2, userJournals.size());
		assertTrue(userJournals.stream().anyMatch(j -> j.getUuid().equals("SOME_EXTERNAL_ID") && j.getName().equals(NEW_JOURNAL_NAME)));

		publisherJournals = journalService.publisherList(p.get());
		assertEquals(2, publisherJournals.size());
		assertTrue(publisherJournals.stream().anyMatch(j ->
				j.getPublishDate() != null &&
				j.getUuid().equals("SOME_EXTERNAL_ID") &&
				j.getName().equals(NEW_JOURNAL_NAME) &&
				j.getPublisher().getUser().getLoginName().equals(PUBLISHER_LOGIN_WITH_PUBLICATIONS2) &&
				j.getCategory().getId().equals(CATEGORY_ID_ENDOCRINOLOGY)));
	}

	@Test(expected = ServiceException.class)
	public void unPublishFail() {
		User user = getUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		journalService.unPublish(p.get(), INVALID_JOURNAL_ID);
	}

	@Test(expected = ServiceException.class)
	public void unPublishFail2() {
		User user = getUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		journalService.unPublish(p.get(), INVALID_JOURNAL_ID);
	}

	@Test
	public void unPublishSuccess() {
		User user = getUser(PUBLISHER_LOGIN_WITH_PUBLICATIONS1);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		Assert.assertTrue(p.isPresent());

		List<Journal> journals = journalService.publisherList(p.get());
		assertEquals(2, journals.size());

		journalService.unPublish(p.get(), JOURNAL_ID_TEST_JOURNAL);

		journals = journalService.publisherList(p.get());
		assertEquals(1, journals.size());
		assertFalse(journals.stream().anyMatch(j -> j.getId().equals(JOURNAL_ID_TEST_JOURNAL) && j.getName().equals("Medicine")));
	}
}
