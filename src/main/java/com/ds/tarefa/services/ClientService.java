package com.ds.tarefa.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ds.tarefa.dto.ClientDTO;
import com.ds.tarefa.entities.Client;
import com.ds.tarefa.exceptions.DatabaseException;
import com.ds.tarefa.exceptions.ResourceNotFoundException;
import com.ds.tarefa.repositories.ClientRepository;

@Service
public class ClientService {

	@Autowired 
	private ClientRepository repo;
	
	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		List<Client> ents = repo.findAll();
		List<ClientDTO> dtos = new ArrayList<>();
		dtos = ents.stream().map(cli -> new ClientDTO(cli)).collect(Collectors.toList());
		return dtos;
	}

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		
		Page<Client> list = repo.findAll(pageRequest);
		
		Page<ClientDTO> dtos = 
				list.map(cli -> new ClientDTO(cli));
		
		return dtos;
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {		
		try {
			System.out.println("MAs bahhh 11");
			Client cli = null;
			cli = repo.getById(id);
			return new ClientDTO(cli);
		}
		catch(javax.persistence.EntityNotFoundException enf) {
			System.out.println("MAs bahhh");
			throw new ResourceNotFoundException("Cliente não localizado com o id "+id);
		}		
	}
	
	
	@Transactional
	public ClientDTO save(ClientDTO dto) {
		
		Client cli = new Client(dto);
		cli.setId(null);
		cli = repo.save(cli);
		
		return new ClientDTO(cli);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client cli = repo.getById(id);
			//dtoToEntity(dto, cli);
			cli.setDTO(dto);
			cli = repo.save(cli);
			return new ClientDTO(cli);
		}
		catch(EntityNotFoundException ex) {
			throw new ResourceNotFoundException("Id not found "+ id);
		}
	}
	
	@Transactional
	public void delete(Long id) {
		try {
			repo.deleteById(id);
		}
		catch(EntityNotFoundException enf) {
			throw new ResourceNotFoundException("Não encontrado cliente com id: "+ id + " para deletar");
		}
		catch(EmptyResultDataAccessException ex) {
			throw new ResourceNotFoundException("Id not found "+ id);
		}
		catch(DataIntegrityViolationException dataEx) {
			throw new DatabaseException("Recurso não pode ser excluído pois possui registros dependentes");
		}
	}
}
