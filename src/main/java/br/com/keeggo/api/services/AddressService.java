package br.com.keeggo.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.keeggo.api.models.Address;
import br.com.keeggo.api.repositories.AddressRepository;

@Service
public class AddressService {
		
	@Autowired
	private AddressRepository repository;
	
	public List<String> validate(Address address){
		
		List<String> errors = new ArrayList<>();
		
		if ( address == null ) {
			errors.add("The address shouldn't be empty.");
			
		}else {
			
			if ( address.getUf() == null || address.getUf().isEmpty()) {
				errors.add("The 'address.uf' field shouldn't be empty.");
			}
			
			if ( address.getCity() == null || address.getCity().isEmpty()) {
				errors.add("The 'address.city' field shouldn't be empty.");
			}
			
			if ( address.getStreet() == null || address.getStreet().isEmpty()) {
				errors.add("The 'address.street' field shouldn't be empty.");
			}
			
			if ( address.getNumber() == null || address.getNumber() == null ) {
				errors.add("The 'address.number' field shouldn't be empty.");
			}
			
		}
		
		return errors;
		
	}
	
	public Address create(Address address) {
		return repository.save(address);
	}
}
