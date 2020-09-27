package com.culysoft.enderecoapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.culysoft.enderecoapi.api.model.MunicipioInput;
import com.culysoft.enderecoapi.api.model.MunicipioModel;
import com.culysoft.enderecoapi.api.model.MunicipioOutput;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.service.MunicipioService;

@RestController
@RequestMapping("api/v1/paises/pais/{paisNome}/provincias/provincia/{provinciaNome}/municipios")
public class MunicipioController {

	@Autowired
	private MunicipioService municipioService;
	
	@GetMapping
	public MunicipioModel findAll(@PathVariable String paisNome, @PathVariable String provinciaNome) throws EntidadeNaoEncontradaException {
		return municipioService.findAll(getMunicipioInput(null, paisNome, provinciaNome, null));
	}
	
	
	@GetMapping("/{municipioId}")
	public MunicipioOutput findById(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable Long municipioId) throws EntidadeNaoEncontradaException {
		return municipioService.findById(getMunicipioInput(municipioId, paisNome, provinciaNome, null));
	}
	
	@GetMapping("/municipio/{municipioNome}")
	public MunicipioOutput findByNome(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable String municipioNome) throws EntidadeNaoEncontradaException {
		return municipioService.findByName(getMunicipioInput(null, paisNome, provinciaNome, municipioNome));
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public MunicipioOutput save(@PathVariable String paisNome, @PathVariable String provinciaNome, @Validated @RequestBody MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		return municipioService.save(getMunicipioInput(0L, paisNome, provinciaNome, municipioInput.getMunicipioNome()));
	}
	
	@PutMapping("/{municipioId}")
	public MunicipioOutput update(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable Long municipioId, @Validated @RequestBody MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		return municipioService.save(getMunicipioInput(municipioId, paisNome, provinciaNome, municipioInput.getMunicipioNome()));
	}
	
	@DeleteMapping("/{municipioId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable Long municipioId) throws EntidadeNaoEncontradaException {
		municipioService.deleteById(getMunicipioInput(municipioId, paisNome, provinciaNome, null));
	}
	
	private MunicipioInput getMunicipioInput(Long municipioId, String paisNome, String provinciaNome, String municipioNome) {
		return new MunicipioInput(municipioId, paisNome, provinciaNome, municipioNome);
	}
}
