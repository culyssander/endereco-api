package com.culysoft.enderecoapi.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.culysoft.enderecoapi.api.model.PaisInput;
import com.culysoft.enderecoapi.api.model.PaisModel;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.exception.NegocioException;
import com.culysoft.enderecoapi.model.service.PaisService;

@RestController
@RequestMapping("api/v1/paises")
public class PaisController {

	@Autowired
	private PaisService paisService;

	@GetMapping
	public List<PaisModel> findAll() {
		return paisService.findAll();
	}

	@GetMapping("/{id}")
	public PaisModel findById(@PathVariable Long id) throws EntidadeNaoEncontradaException {
		return paisService.findById(id);
	}

	@GetMapping("pais/{nome}")
	public PaisModel findByNome(@PathVariable String nome) throws EntidadeNaoEncontradaException {
		return paisService.findByNome(nome);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PaisModel save(@Validated @RequestBody PaisInput paisInput) throws NegocioException, EntidadeNaoEncontradaException {
		paisInput.setId(null);
		return paisService.save(paisInput);
	}
	
	@PostMapping("all")
	@ResponseStatus(code = HttpStatus.CREATED)
	public List<PaisModel> saveAll(@Validated @RequestBody List<PaisInput> paisesInput) throws NegocioException, EntidadeNaoEncontradaException {
		return paisService.saveAll(paisesInput);
	}

	@PutMapping("/{id}")
	public PaisModel update(@PathVariable Long id, @Validated @RequestBody PaisInput paisInput) throws NegocioException, EntidadeNaoEncontradaException {
		paisInput.setId(id);
		return paisService.save(paisInput);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) throws NegocioException {
		if (paisService.deleteById(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

}
