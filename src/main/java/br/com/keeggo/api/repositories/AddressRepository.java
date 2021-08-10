package br.com.keeggo.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.keeggo.api.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
	
}
