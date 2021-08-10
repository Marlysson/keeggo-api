package br.com.keeggo.api.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("")
	public ResponseEntity<?> all(){
		return ResponseEntity.ok(service.findAllClients());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> retrieve(@PathVariable Long id){
		
		Client client = service.findClientBy(id);
		
		if ( client == null ) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(client);
		
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		
		Client client = service.findClientBy(id);
		
		if ( client == null ) {
			return ResponseEntity.notFound().build();
		}
		
		service.delete(client);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/")
	public ResponseEntity<?> create(@RequestBody Client clientPayload) {
		
		List<String> errors = service.validate(clientPayload);
		
		if ( !errors.isEmpty() ) {
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
		
		Client client = service.create(clientPayload);
		
		return new ResponseEntity<>(client, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Client newClientPayload){
		
		Client client = service.findClientBy(id);
		
		if ( client == null ) {
			return ResponseEntity.notFound().build();
		}

		client = service.update(client, newClientPayload);
		
		return new ResponseEntity<>(client, HttpStatus.OK);
	}
}
