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
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/clients")
@EnableSwagger2
public class ClientController {
		
	@Autowired
	private ClientService service;
	
	@ApiOperation(value="Retrieve all clients.")
	@GetMapping("")
	public ResponseEntity<?> all(){
		return ResponseEntity.ok(service.findAllClients());
	}
	
	@ApiOperation(value="Retrive one client by id.")
	@GetMapping("/{id}")
	public ResponseEntity<?> retrieve(@PathVariable Long id){
		
		Client client = service.findClientBy(id);
		
		if ( client == null ) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(client);
		
	}
	
	@ApiOperation(value="Delete one clients.")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		
		Client client = service.findClientBy(id);
		
		if ( client == null ) {
			return ResponseEntity.notFound().build();
		}
		
		service.delete(client);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Create a client.")
	@PostMapping("/")
	public ResponseEntity<?> create(@RequestBody Client clientPayload) {
		
		List<String> errors = service.validate(clientPayload);
		
		if ( !errors.isEmpty() ) {
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
		
		Client client = service.create(clientPayload);
		
		return new ResponseEntity<>(client, HttpStatus.CREATED);
	}
	
	@ApiOperation(value="Update a client.")
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
