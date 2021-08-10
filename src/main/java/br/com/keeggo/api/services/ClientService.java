package br.com.keeggo.api.services;

import java.util.ArrayList;
import java.util.List;

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
}
