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

import com.culysoft.enderecoapi.api.model.ComunaInput;
import com.culysoft.enderecoapi.api.model.ComunaModel;
import com.culysoft.enderecoapi.api.model.ComunaOutput;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.service.ComunaService;

@RestController
@RequestMapping("api/v1/paises/pais/{paisNome}/provincias/provincia/{provinciaNome}/municipios/municipio/{municipioNome}/comunas")
public class ComunaController {

	@Autowired
	private ComunaService comunaService;
	
	@GetMapping
	public ComunaModel findAll(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable String municipioNome) throws EntidadeNaoEncontradaException {
		ComunaInput comunaInput = new ComunaInput(null, paisNome, provinciaNome, municipioNome, null);
		return comunaService.findAll(comunaInput);
	}
	
	@GetMapping("/{comunaId}")
	public ComunaOutput findById(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable String municipioNome, @PathVariable Long comunaId) throws EntidadeNaoEncontradaException {
		ComunaInput comunaInput = new ComunaInput(comunaId, paisNome, provinciaNome, municipioNome, null);
		return comunaService.findById(comunaInput);
	}
	
	@GetMapping("/comuna/{comunaNome}")
	public ComunaOutput findByNome(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable String municipioNome, @PathVariable String comunaNome) throws EntidadeNaoEncontradaException {
		ComunaInput comunaInput = new ComunaInput(null, paisNome, provinciaNome, municipioNome, comunaNome);
		return comunaService.findByNome(comunaInput);
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ComunaOutput save(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable String municipioNome, @Validated @RequestBody ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		return comunaService.save(getComunaInput(0L, paisNome, provinciaNome, municipioNome, comunaInput.getComunaNome()));
	}
	
	@PutMapping("/{comunaId}")
	public ComunaOutput update(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable String municipioNome, @PathVariable Long comunaId, @Validated @RequestBody ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		return comunaService.save(getComunaInput(comunaId, paisNome, provinciaNome, municipioNome, comunaInput.getComunaNome()));
	}
	
	@DeleteMapping("/{comunaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable String paisNome, @PathVariable String provinciaNome, @PathVariable String municipioNome, @PathVariable Long comunaId) throws EntidadeNaoEncontradaException {
		ComunaInput comunaInput = new ComunaInput(comunaId, paisNome, provinciaNome, municipioNome, null);
		comunaService.deleteById(comunaInput);
	}
	
	private ComunaInput getComunaInput(Long comunaId, String paisNome, String provinciaNome, String municipioNome, String comunaNome) {
		return new ComunaInput(comunaId, paisNome, provinciaNome, municipioNome, comunaNome);
	}
}
