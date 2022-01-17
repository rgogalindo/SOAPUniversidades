package com.soap.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soap.entity.Universidad;
import com.soap.repository.UniversidadRepositorio;

@Service
public class UniversidadServiceImp implements UniversidadService{

	@Autowired
	private UniversidadRepositorio repo;
	
	Logger logger = LoggerFactory.getLogger(UniversidadServiceImp.class);
	
	@Override
	@Transactional(readOnly=true)
	public List<Universidad> findAllUniversidades() {
		logger.trace("Se accedio al servicio para obtener un listado de todas las universidades");
		List<Universidad> unis = new ArrayList<>();
		repo.findAll().forEach(e -> unis.add(e));
		return unis;
	}

	@Override
	@Transactional
	public Universidad saveUniversidad(Universidad universidad) {
		logger.trace("Se accedio al servicio para guardar una nueva universidad");
		Universidad uni = new Universidad();
		uni.setNombre(universidad.getNombre());
		uni.setAnho(universidad.getAnho());
		uni.setDistrito(universidad.getDistrito());
		return repo.save(uni);
	}

	@Override
	@Transactional(readOnly=true)
	public Universidad findByCodigo(int codigo) {
		return repo.findByCodigo(codigo);
	}

}
