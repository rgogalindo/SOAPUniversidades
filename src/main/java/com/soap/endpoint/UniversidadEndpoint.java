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
import com.universidades.jaxb.DeleteUniversidadByIdRequest;
import com.universidades.jaxb.DeleteUniversidadByIdResponse;
import com.universidades.jaxb.GetAllUniversidadesResponse;
import com.universidades.jaxb.GetUniversidadByIdRequest;
import com.universidades.jaxb.GetUniversidadByIdResponse;
import com.universidades.jaxb.ServicioStatus;
import com.universidades.jaxb.UniversidadInfo;
import com.universidades.jaxb.UpdateUniversidadRequest;
import com.universidades.jaxb.UpdateUniversidadResponse;

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
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="getUniversidadByIdRequest")
	@ResponsePayload
	/**
	 * Obtener la entrada de una Universidad dado su ID/codigo
	 * @param request Una peticion SOAP con el ID tipo número entero
	 * @return Una respuesta SOAP con la Universidad tipo UniversidadInfo
	 */
	public GetUniversidadByIdResponse getUniversidadById(@RequestPayload GetUniversidadByIdRequest request) {
		logger.trace("Se accedio al metodo para obtener una universidad dado su ID.");
		GetUniversidadByIdResponse respuesta = new GetUniversidadByIdResponse();
		Universidad uni = service.findByCodigo(request.getCodigo());
		if (uni != null) {
			UniversidadInfo info = new UniversidadInfo();
			BeanUtils.copyProperties(uni, info);
			respuesta.setUniversidadInfo(info);
		}
		return respuesta;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="updateUniversidadRequest")
	@ResponsePayload
	/**
	 * Actualizar la entrada de una Universidad
	 * @param request Una peticion SOAP con la informacion de la universidad tipo UniversidadInfo
	 * @return Una respuesta SOAP con la informacion de la universidad actualizada tipo UniversidadInfo y el estado del servicio.
	 */
	public UpdateUniversidadResponse updateUniversidad(@RequestPayload UpdateUniversidadRequest request) {
		logger.trace("Se accedio al metodo para actualizar la entrada de una universidad.");
		UpdateUniversidadResponse respuesta = new UpdateUniversidadResponse();
		ServicioStatus status = new ServicioStatus();
		UniversidadInfo info = request.getUniversidadInfo();
		Universidad check = service.findByCodigo(info.getCodigo());
		if(check == null) {
			status.setStatusCode("404");
			status.setMensaje("La universidad no existe.");
		} else {
			Universidad uni = new Universidad();
			BeanUtils.copyProperties(info, uni);
			Universidad universidad = service.updateUniversidad(uni);
			BeanUtils.copyProperties(universidad, info);
			respuesta.setUniversidadInfo(info);
			status.setMensaje("200");
			status.setStatusCode("Universidad actualizada.");
		}
		respuesta.setServicioStatus(status);
		return respuesta;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="deleteUniversidadByIdRequest")
	@ResponsePayload
	/**
	 * Eliminar una entrada de la base de datos dado un ID.
	 * @param request El ID tipo numero entero.
	 * @return El estado de servicio.
	 */
	public DeleteUniversidadByIdResponse deleteUniversidadById(@RequestPayload DeleteUniversidadByIdRequest request) {
		logger.trace("Se accedio al metodo para eliminar una universidad dado su ID.");
		DeleteUniversidadByIdResponse respuesta = new DeleteUniversidadByIdResponse();
		ServicioStatus status = new ServicioStatus();
		Universidad check = service.findByCodigo(request.getCodigo());
		if(check == null) {
			status.setStatusCode("404");
			status.setMensaje("La universidad no existe");
		} else {
			service.deleteUniversidadByCodigo(request.getCodigo());
			status.setStatusCode("200");
			status.setMensaje("Universidad eliminada");
		}
		respuesta.setServicioStatus(status);
		return respuesta;
	}
}
