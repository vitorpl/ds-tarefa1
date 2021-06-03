package com.ds.tarefa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.tarefa.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
