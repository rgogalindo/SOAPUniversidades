package com.soap.service;

import java.util.List;

import com.soap.entity.Universidad;

public interface UniversidadService {

	List<Universidad> findAllUniversidades();
	
	Universidad findByCodigo(int codigo);
	
	Universidad saveUniversidad(Universidad universidad);
	
	Universidad updateUniversidad(Universidad universidad);
	
	void deleteUniversidadByCodigo(int codigo);
}
