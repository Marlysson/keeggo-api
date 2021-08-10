package br.com.keeggo.api.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.keeggo.api.models.Client;
import br.com.keeggo.api.services.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {
		
	@Autowired
	private ClientService service;
	
	@PostMapping("/")
	public ResponseEntity create(@RequestBody Client clientPayload) {
		
		List<String> errors = service.validate(clientPayload);
		
		if ( !errors.isEmpty() ) {
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
		
		Client client = service.create(clientPayload);
		
		return new ResponseEntity<>(client, HttpStatus.CREATED);
	}
}
