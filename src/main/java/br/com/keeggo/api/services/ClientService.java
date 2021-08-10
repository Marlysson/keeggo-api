package br.com.keeggo.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.keeggo.api.models.Address;
import br.com.keeggo.api.models.Client;
import br.com.keeggo.api.repositories.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Autowired
	private AddressService addressService;
	
	private static final Integer CPF_LENGTH = 11;
	
	public List<String> validate(Client client){
		
		List<String> errors = new ArrayList<>();

		if ( client.getName() == null || client.getName().isEmpty() ) {
			errors.add("The name field should'nt be empty.");
		}
		
		if ( client.getCpf() == null || client.getCpf().isEmpty() ) {
			errors.add("The cpf field should'nt be empty.");
			
		}else if ( client.getCpf().length() != CPF_LENGTH ){
			errors.add(String.format("The cpf isn't a valid. Should have %d characters.", CPF_LENGTH));
		}
		
		Client clientByCpf = repository.findByCpf(client.getCpf());
		
		if ( client.getId() == null && clientByCpf != null ) {
			errors.add("This client already is created.");
		}
		
		List<String> addressErrors = addressService.validate(client.getAddress());
		errors.addAll(addressErrors);

		return errors;
		
	}
	
	public Client create(Client client) {
		
		Address address = client.getAddress();
		address = addressService.create(address);
		
		client.setAddress(address);
		return repository.save(client);
	}
	
	public void delete(Client client) {
		repository.delete(client);
	}
	
	public Client findClientBy(Long id) {
		Optional<Client> result = repository.findById(id);
		return result.orElse(null);
	}

	public List<Client> findAllClients() {
		return repository.findAll();
	}
	
	public Client update(Client client, Client clientUpdated) {
		
		if ( clientUpdated.getName() != null && !clientUpdated.getName().isEmpty() ) {
			client.setName(clientUpdated.getName());
		}
		
		if ( clientUpdated.getAddress() != null ) {
			
			if ( clientUpdated.getAddress().getUf() != null && !clientUpdated.getAddress().getUf().isEmpty() ) {
				client.getAddress().setUf(clientUpdated.getAddress().getUf());
			}
			
			if ( clientUpdated.getAddress().getUf() != null && !clientUpdated.getAddress().getCity().isEmpty() ) {
				client.getAddress().setCity(clientUpdated.getAddress().getCity());
			}
			
			if ( clientUpdated.getAddress().getStreet() != null && !clientUpdated.getAddress().getStreet().isEmpty() ) {
				client.getAddress().setStreet(clientUpdated.getAddress().getStreet());
			}
			
			if ( clientUpdated.getAddress().getNumber() != null && clientUpdated.getAddress().getNumber() != null ) {
				client.getAddress().setNumber(clientUpdated.getAddress().getNumber());
			}
			
		}
		
		return repository.save(client);
		
	}
	
}
