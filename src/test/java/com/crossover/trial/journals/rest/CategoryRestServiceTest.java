package com.crossover.trial.journals.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.crossover.trial.journals.MvcIntegrationTestBase;
import org.junit.Test;

public class CategoryRestServiceTest extends MvcIntegrationTestBase {

	@Test
	public void getCategories() throws Exception {
		mockMvc.perform(get("/public/rest/category")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("immunology")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("pathology")))
				.andExpect(jsonPath("$[2].id", is(3)))
				.andExpect(jsonPath("$[2].name", is("endocrinology")))
				.andExpect(jsonPath("$[3].id", is(4)))
				.andExpect(jsonPath("$[3].name", is("microbiology")))
				.andExpect(jsonPath("$[4].id", is(5)))
				.andExpect(jsonPath("$[4].name", is("neurology")));
	}
}
