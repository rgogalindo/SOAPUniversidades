package com.soap.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.soap.entity.Universidad;

@Repository
public interface UniversidadRepositorio extends CrudRepository<Universidad, Integer> {

	Universidad findByCodigo(int codigo);
	
}
