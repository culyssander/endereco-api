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

import com.culysoft.enderecoapi.api.model.ProvinciaInput;
import com.culysoft.enderecoapi.api.model.ProvinciaModel;
import com.culysoft.enderecoapi.api.model.ProvinciaOutput;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.service.ProvinciaService;

@RestController
@RequestMapping("api/v1/paises/pais/{paisNome}/provincias")
public class ProvinciaController {
	
	@Autowired
	private ProvinciaService provinciaService;
	
	@GetMapping
	public ProvinciaModel findAll(@PathVariable String paisNome) throws EntidadeNaoEncontradaException { 
		return provinciaService.findAll(paisNome);
	}
	
	@GetMapping("/{provinciaId}")
	public ProvinciaOutput findById(@PathVariable String paisNome, @PathVariable Long provinciaId) throws EntidadeNaoEncontradaException {
		return provinciaService.findById(getProvinciaInput(provinciaId, paisNome, null));
	}
	
	@GetMapping("/provincia/{provinciaNome}")
	public ProvinciaOutput findByNome(@PathVariable String paisNome, @PathVariable String provinciaNome) throws EntidadeNaoEncontradaException {
		return provinciaService.findByNome(getProvinciaInput(null, paisNome, provinciaNome));
	}
	
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ProvinciaOutput save(@PathVariable String paisNome, @Validated @RequestBody ProvinciaInput provinciaInput) throws EntidadeNaoEncontradaException {
		return provinciaService.save(getProvinciaInput(0L, paisNome, provinciaInput.getProvinciaNome()));
	}
	
	
	@PutMapping("/{provinciaId}")
	public ProvinciaOutput update(@PathVariable String paisNome, @PathVariable Long provinciaId, @Validated @RequestBody ProvinciaInput provinciaInput) throws EntidadeNaoEncontradaException {
		return provinciaService.save(getProvinciaInput(provinciaId, paisNome, provinciaInput.getProvinciaNome()));
	}
	
	@DeleteMapping("/{provinciaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable String paisNome, @PathVariable Long provinciaId) throws EntidadeNaoEncontradaException {
		provinciaService.deleteById(getProvinciaInput(provinciaId, paisNome, null));
	}
	
	private ProvinciaInput getProvinciaInput(Long provinciaId, String paisNome, String provinciaNome) {
		return new ProvinciaInput(provinciaId, paisNome, provinciaNome);
	}

}
