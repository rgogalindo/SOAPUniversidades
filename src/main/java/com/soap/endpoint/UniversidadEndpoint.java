package com.soap.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.soap.entity.Universidad;
import com.soap.service.UniversidadService;
import com.universidades.jaxb.AddUniversidadRequest;
import com.universidades.jaxb.AddUniversidadResponse;
import com.universidades.jaxb.GetAllUniversidadesResponse;
import com.universidades.jaxb.ServicioStatus;
import com.universidades.jaxb.UniversidadInfo;

@Endpoint
public class UniversidadEndpoint {

	private static final String NAMESPACE_URI = "http://www.universidades.com/universidad-ws";
	
	@Autowired
	private UniversidadService service;
	
	Logger logger = LoggerFactory.getLogger(UniversidadEndpoint.class);
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="getAllUniversidadesRequest")
	@ResponsePayload
	/**
	 * Método para acceder a todas las universidades.
	 * @return Una respuesta SOAP para obtener un listado de todas las universidad
	 */
	public GetAllUniversidadesResponse getAllUniversidades() {
		logger.trace("Se accedio al metodo para obtener todas las universidades");
		GetAllUniversidadesResponse respuesta = new GetAllUniversidadesResponse();
		List<Universidad> unis = service.findAllUniversidades();
		List<UniversidadInfo> infos = new ArrayList<>();
		
		for(int i=0; i<unis.size();i++) {
			UniversidadInfo objeto = new UniversidadInfo();
			BeanUtils.copyProperties(unis.get(i), objeto);
			infos.add(objeto);
		}
		respuesta.getUniversidadInfo().addAll(infos);
		return respuesta;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="addUniversidadRequest")
	@ResponsePayload
	/**
	 * Método para guardar una nueva Universidad en la base de datos.
	 * @param request Una peticion SOAP con un objeto tipo UniversidadInfo
	 * @return Una respuesta SOAP con un objeto tipo UniversidadInfo y otro tipo ServicioStatus
	 */
	public AddUniversidadResponse addUniversidad(@RequestPayload AddUniversidadRequest request) {
		logger.trace("Se accedio al metodo para agregar una nueva Universidad");
		AddUniversidadResponse respuesta = new AddUniversidadResponse();
		ServicioStatus status = new ServicioStatus();
		Universidad uni = new Universidad();
		UniversidadInfo info = request.getUniversidadInfo();
		BeanUtils.copyProperties(info, uni);
		Universidad universidad = service.saveUniversidad(uni);
		BeanUtils.copyProperties(universidad, info);
		respuesta.setUniversidadInfo(info);
		status.setStatusCode("200");
		status.setMensaje("Universidad agregada");
		respuesta.setServicioStatus(status);
		return respuesta;
	}
}
