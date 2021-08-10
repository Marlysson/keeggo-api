package br.com.keeggo.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.keeggo.api.repositories.ClientRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientTests {
	
	@Autowired
	public MockMvc mock;
	
	@Autowired
	public ObjectMapper mapper;
	
	@Autowired
	public ClientRepository repository;
	
	@BeforeEach
	public void init() {
		repository.deleteAll();
	}
	
	@Test
	public void testShouldReturn404NotFoundWhenRetrieveAInexistentClient() throws Exception {
		mock.perform(get("/clients/{id}", 1)).andExpect(status().isNotFound());
	}
	
	@Test
	public void testShouldShowErrorWhenClientFieldsIsEmpty() throws Exception {
		
		String payload = "{}";
		
		List<String> errors = Arrays.asList("The name field should'nt be empty.", 
											"The cpf field should'nt be empty.", 
											"The address shouldn't be empty.");
		
		String responseResult = mapper.writeValueAsString(errors);
				
		MockHttpServletRequestBuilder request = post("/clients/").contentType(MediaType.APPLICATION_JSON).content(payload);
		
		mock.perform(request)
			.andExpect(status().is(400))
			.andExpect(content().json(responseResult));

	}
	
	@Test
	public void testShouldReturnErrorsWhenNameIsFilled() throws Exception {
		
		String payload = "{\"name\": \"Marlysson\"}";
		
		List<String> errors = Arrays.asList("The cpf field should'nt be empty.", 
											"The address shouldn't be empty.");
		
		String responseResult = mapper.writeValueAsString(errors);
				
		MockHttpServletRequestBuilder request = post("/clients/").contentType(MediaType.APPLICATION_JSON).content(payload);
		
		mock.perform(request)
			.andExpect(status().is(400))
			.andExpect(content().json(responseResult));
		
	}
	
	@Test
	public void testShouldReturnErrorsWhenNameAndCPFIsFilled() throws Exception {
		
		String payload = "{\"name\": \"Marlysson\", \"cpf\": \"11657925080\"}";
		
		List<String> errors = Arrays.asList("The address shouldn't be empty.");
		
		String responseResult = mapper.writeValueAsString(errors);
		
		MockHttpServletRequestBuilder request = post("/clients/").contentType(MediaType.APPLICATION_JSON).content(payload);
		
		mock.perform(request)
			.andExpect(status().is(400))
			.andExpect(content().json(responseResult));
		
	}
	
	@Test
	public void testShouldReturnErrorsWhenCPFIsntRightFormat() throws Exception {
		
		String payload = "{\"name\": \"Marlysson\",\"cpf\": \"abc\", " + 
						 "\"address\": {\"uf\" : \"Piaui\", \"city\": \"Teresina\", \"street\": \"Rua 12\", \"number\": 22}}";
		
		List<String> errors = Arrays.asList("The cpf isn't a valid. Should have 11 characters and only numbers.");
		
		String responseResult = mapper.writeValueAsString(errors);
		
		MockHttpServletRequestBuilder request = post("/clients/").contentType(MediaType.APPLICATION_JSON).content(payload);
		
		mock.perform(request)
			.andExpect(status().is(400))
			.andExpect(content().json(responseResult));
		
	}
	
	@Test
	public void testShouldReturnErrorsWhenCPFIsntRightFormatWithLetters() throws Exception {
		
		String payload = "{\"name\": \"Marlysson\",\"cpf\": \"abcdefghijk\", " + 
				"\"address\": {\"uf\" : \"Piaui\", \"city\": \"Teresina\", \"street\": \"Rua 12\", \"number\": 22}}";
		
		List<String> errors = Arrays.asList("The cpf isn't a valid. Should have 11 characters and only numbers.");
		
		String responseResult = mapper.writeValueAsString(errors);
		
		MockHttpServletRequestBuilder request = post("/clients/").contentType(MediaType.APPLICATION_JSON).content(payload);
		
		mock.perform(request)
		.andExpect(status().is(400))
		.andExpect(content().json(responseResult));
		
	}
	
	@Test
	public void testShouldReturnErrorsWhenJustSomeAddressFieldsIsntFilled() throws Exception {
		
		String payload = "{\"name\": \"Marlysson\", \"cpf\": \"11657925080\", \"address\": {\"uf\" : \"Piaui\", \"city\": \"Teresina\"}}";
		
		List<String> errors = Arrays.asList("The 'address.street' field shouldn't be empty.", 
											"The 'address.number' field shouldn't be empty.");
		
		String responseResult = mapper.writeValueAsString(errors);
		
		MockHttpServletRequestBuilder request = post("/clients/").contentType(MediaType.APPLICATION_JSON).content(payload);
		
		mock.perform(request)
			.andExpect(status().is(400))
			.andExpect(content().json(responseResult));
		
	}
	
	@Test
	public void testShouldReturnErrorsWhenClientWithSameCPFIsAttemptedCreated() throws Exception { 
		
		String payload = "{\"name\": \"Marlysson\",\"cpf\": \"11657925080\", " + 
				 "\"address\": {\"uf\" : \"Piaui\", \"city\": \"Teresina\", \"street\": \"Rua 12\", \"number\": 22}}";

		MockHttpServletRequestBuilder request = post("/clients/").contentType(MediaType.APPLICATION_JSON).content(payload);
		
		mock.perform(request).andExpect(status().isCreated());
		
		String result = mapper.writeValueAsString(Arrays.asList("This client already is created."));
		
		mock.perform(request)
			.andExpect(status().is4xxClientError())
			.andExpect(content().json(result));

	}
}
