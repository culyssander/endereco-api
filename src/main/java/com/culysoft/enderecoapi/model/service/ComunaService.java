package com.culysoft.enderecoapi.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culysoft.enderecoapi.api.model.ComunaInput;
import com.culysoft.enderecoapi.api.model.ComunaModel;
import com.culysoft.enderecoapi.api.model.ComunaOutput;
import com.culysoft.enderecoapi.model.domain.Comuna;
import com.culysoft.enderecoapi.model.domain.Municipio;
import com.culysoft.enderecoapi.model.domain.Pais;
import com.culysoft.enderecoapi.model.domain.Provincia;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.repository.ComunaRepository;

@Service
public class ComunaService {

	@Autowired
	private PaisService paisService;
	
	@Autowired
	private ProvinciaService provinciaService;
	
	@Autowired
	private MunicipioService municipioService;
	
	@Autowired
	private ComunaRepository comunaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ComunaModel findAll(ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		Municipio municipio = getMunicipio(comunaInput);
		List<ComunaOutput> comunaOutputs = new ArrayList<ComunaOutput>();
		
		for(ComunaOutput comunaOutput: toCollectionModel(municipio.getComunas())) {
			comunaOutputs.add(comunaOutputLink(comunaInput, comunaOutput));
		}
		
		ComunaModel comunaModel =  new ComunaModel(comunaInput.getPais(), comunaInput.getProvincia(), municipio.getNome(), comunaOutputs);
		
		return comunaModel;
	}
	
	private List<ComunaOutput> toCollectionModel(List<Comuna> comunas) {
		return comunas.stream().map(m -> toModel(m)).collect(Collectors.toList());
	}
	
	private ComunaOutput toModel(Comuna comuna) {
		return modelMapper.map(comuna, ComunaOutput.class);
	}

	public ComunaOutput findById(ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		Municipio municipio = getMunicipio(comunaInput);
		return comunaOutputLink(comunaInput, toModel(findComunaPeloIdNoMunicipio(municipio, comunaInput.getComunaId())));
	}
	
	private Comuna findComunaPeloIdNoMunicipio(Municipio municipio, Long comunaId) throws EntidadeNaoEncontradaException {
		return municipio.getComunas().stream()
			.filter(c -> c.getId().equals(comunaId))
			.findFirst()
			.orElseThrow(() -> new EntidadeNaoEncontradaException("Comuna de id: " + comunaId + " não encontrado."));
	}
	
	public ComunaOutput findByNome(ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		Municipio municipio = getMunicipio(comunaInput);
		return comunaOutputLink(comunaInput, toModel(findComunaPeloNomeNoMunicipio(municipio, comunaInput.getComunaNome())));
	}
	
	private Comuna findComunaPeloNomeNoMunicipio(Municipio municipio, String comunaNome) throws EntidadeNaoEncontradaException {
		return municipio.getComunas().stream()
			.filter(c -> c.getNome().equalsIgnoreCase(comunaNome))
			.findFirst()
			.orElseThrow(() -> new EntidadeNaoEncontradaException("Comuna de nome: " + comunaNome + " não encontrado."));
	}
	
	@Transactional(readOnly = false)
	public ComunaOutput save(ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		Comuna comuna = toEntity(comunaInput);
		
		Municipio municipio = getMunicipio(comunaInput);
		validacaoDaNovaComuna(municipio, comunaInput);
		
		comuna = comunaRepository.findById(comunaInput.getComunaId()).orElse(new Comuna(comunaInput.getComunaNome()));
		comuna.setNome(comunaInput.getComunaNome());
		comuna = comunaRepository.save(comuna);
		
		municipioService.adicionaComunaNaListaMunicipio(municipio, comuna);
		
		return toModel(comuna);
	}
	
	private void validacaoDaNovaComuna(Municipio municipio, ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		if(comunaInput.getComunaId().equals(0L)) {
			for(Comuna comuna : municipio.getComunas()) {
				if(comuna.getNome().equalsIgnoreCase(comunaInput.getComunaNome())) {
					throw new EntidadeNaoEncontradaException("Comuna " + comunaInput.getComunaNome() + " já está registado no Municipio " + municipio.getNome());
				}
			}
		}
	}

	private Comuna toEntity(ComunaInput comunaInput) {
		return modelMapper.map(comunaInput, Comuna.class);
	}

	@Transactional(readOnly = false)
	public void deleteById(ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		Municipio municipio = getMunicipio(comunaInput);
		Comuna comuna = findComunaPeloIdNoMunicipio(municipio, comunaInput.getComunaId());
		municipioService.remocaoDaComunaNaListaMunicipio(municipio, comuna);
		comunaRepository.deleteById(comunaInput.getComunaId());
	}
	
	private Municipio getMunicipio(ComunaInput comunaInput) throws EntidadeNaoEncontradaException {
		Pais pais = paisService.findByNomeWithoutToModel(comunaInput.getPais());
		Provincia provincia = provinciaService.findProvinciaPeloNomeNoPais(pais, comunaInput.getProvincia());
		return municipioService.findMunicipioPeloNomeNaProvincia(provincia, comunaInput.getMunicipio());
	}
	
	private ComunaOutput comunaOutputLink(ComunaInput comunaInput, ComunaOutput comunaOutput) {
		comunaOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + comunaInput.getPais() + "/provincias/provincia/" + comunaInput.getProvincia() + "/municipios/municipio/"+ comunaInput.getMunicipio() + "/comunas/" + comunaOutput.getId()));
		comunaOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + comunaInput.getPais() + "/provincias/provincia/" + comunaInput.getProvincia() + "/municipios/municipio/"+ comunaInput.getMunicipio() + "/comunas/comuna/" + comunaOutput.getNome()));
		comunaOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + comunaInput.getPais() + "/provincias/provincia/" + comunaInput.getProvincia() + "/municipios/municipio/"+ comunaInput.getMunicipio()));
		return comunaOutput;
	}
}
