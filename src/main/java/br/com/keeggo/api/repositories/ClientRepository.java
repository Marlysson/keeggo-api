package br.com.keeggo.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.keeggo.api.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
		
	Client findByCpf(String cpf);
}
