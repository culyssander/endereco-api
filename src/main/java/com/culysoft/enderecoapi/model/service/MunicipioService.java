package com.culysoft.enderecoapi.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culysoft.enderecoapi.api.model.MunicipioInput;
import com.culysoft.enderecoapi.api.model.MunicipioModel;
import com.culysoft.enderecoapi.api.model.MunicipioOutput;
import com.culysoft.enderecoapi.model.domain.Comuna;
import com.culysoft.enderecoapi.model.domain.Municipio;
import com.culysoft.enderecoapi.model.domain.Pais;
import com.culysoft.enderecoapi.model.domain.Provincia;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.repository.MunicipioRepository;

@Service
public class MunicipioService {
	
	@Autowired
	private MunicipioRepository municipioRepository;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private ProvinciaService provinciaService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public MunicipioModel findAll(MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		Provincia provincia = getProvincia(municipioInput);
		List<MunicipioOutput> municipioOutputs = new ArrayList<MunicipioOutput>();
		
		for(MunicipioOutput municipioOutput : toCollectionModel(provincia.getMunicipios())) {
			municipioOutputs.add(municipioOutputLink(municipioInput, municipioOutput));
		}
		
		MunicipioModel municipioModel = new MunicipioModel(municipioInput.getPais(), provincia.getNome(), municipioOutputs);
		municipioModel.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + municipioInput.getPais() + "/provincias/provincia/" + municipioInput.getProvincia() + "/municipios"));
		return municipioModel;
	}
	
	private List<MunicipioOutput> toCollectionModel(List<Municipio> municipios) {
		return municipios.stream().map(m -> toModel(m)).collect(Collectors.toList());
	}
	
	private MunicipioOutput toModel(Municipio municipio) {
		return modelMapper.map(municipio, MunicipioOutput.class);
	}

	public MunicipioOutput findById(MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		Provincia provincia = getProvincia(municipioInput);
		return municipioOutputLink(municipioInput, toModel(findMunicipioPeloIdNaProvincia(provincia, municipioInput.getId())));
	}
	
	private Municipio findMunicipioPeloIdNaProvincia(Provincia provincia, Long municipioId) throws EntidadeNaoEncontradaException {
		return provincia.getMunicipios().stream()
			.filter(m -> m.getId().equals(municipioId))
			.findFirst()
			.orElseThrow(() -> new EntidadeNaoEncontradaException("Municipio de id: " + municipioId + " não encontrado."));
	}

	public MunicipioOutput findByName(MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		Provincia provincia = getProvincia(municipioInput);
		return municipioOutputLink(municipioInput, toModel(findMunicipioPeloNomeNaProvincia(provincia, municipioInput.getMunicipioNome())));
	}

	public Municipio findMunicipioPeloNomeNaProvincia(Provincia provincia, String municipioNome) throws EntidadeNaoEncontradaException  {
		return provincia.getMunicipios().stream()
				.filter(m -> m.getNome().equalsIgnoreCase(municipioNome))
				.findFirst()
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Municipio de nome: " + municipioNome + " não encontrado."));
	}

	public MunicipioOutput save(MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		Municipio municipio = toEntity(municipioInput);
		
		Pais pais = paisService.findByNomeWithoutToModel(municipioInput.getPais());
		Provincia provincia = provinciaService.findProvinciaPeloNomeNoPais(pais, municipioInput.getProvincia());
		validacaoDoNovoMunicipio(provincia, municipioInput);
		
		municipio = municipioRepository.findById(municipioInput.getId()).orElse(new Municipio(municipioInput.getMunicipioNome()));
		municipio.setNome(municipioInput.getMunicipioNome());
		municipio = municipioRepository.save(municipio);
		
		provinciaService.adicionaMunicipioNaListaProvincia(provincia, municipio);	
		
		return toModel(municipio);
	}
	
	private void validacaoDoNovoMunicipio(Provincia provincia, MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		if(municipioInput.getId().equals(0L)) {
			for(Municipio municipio : provincia.getMunicipios()) {
				if(municipio.getNome().equalsIgnoreCase(municipioInput.getMunicipioNome())) {
					throw new EntidadeNaoEncontradaException("Municipio " + municipioInput.getMunicipioNome() + " já está registado na provincia " + provincia.getNome());
				}
			}
		}
	}

	private Municipio toEntity(MunicipioInput municipioInput) {
		return modelMapper.map(municipioInput, Municipio.class);
	}

	public void deleteById(MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		Provincia provincia = getProvincia(municipioInput);
		Municipio municipio = findMunicipioPeloIdNaProvincia(provincia, municipioInput.getId());
		provinciaService.remocaoDoMunicipioNaListaProvincia(provincia, municipio);
		municipioRepository.deleteById(municipio.getId());
	}
	
	@Transactional(readOnly = false)
	public void remocaoDaComunaNaListaMunicipio(Municipio municipio, Comuna comuna) {
		municipio.getComunas().removeIf(c -> c.equals(comuna));
		municipioRepository.save(municipio);
	}
	
	@Transactional(readOnly = false)
	public void adicionaComunaNaListaMunicipio(Municipio municipio, Comuna comuna) {
		if(municipio.getComunas() == null) {
			List<Comuna> comunas = new ArrayList<Comuna>();
			comunas.add(comuna);
			municipio.setComunas(comunas);
		}else {
			municipio.getComunas().removeIf(c -> c.equals(comuna));
			municipio.getComunas().add(comuna);
		}
		municipioRepository.save(municipio);
	}
	
	private Provincia getProvincia(MunicipioInput municipioInput) throws EntidadeNaoEncontradaException {
		Pais pais = paisService.findByNomeWithoutToModel(municipioInput.getPais());
		return provinciaService.findProvinciaPeloNomeNoPais(pais, municipioInput.getProvincia());
	}
	
	private MunicipioOutput municipioOutputLink(MunicipioInput municipioInput, MunicipioOutput municipioOutput) {
		municipioOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + municipioInput.getPais() + "/provincias/provincia/" + municipioInput.getProvincia() + "/municipios/" + municipioOutput.getId()));
		municipioOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + municipioInput.getPais() + "/provincias/provincia/" + municipioInput.getProvincia() + "/municipios/municipio/" + municipioOutput.getNome()));
		municipioOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + municipioInput.getPais() + "/provincias/provincia/" + municipioInput.getProvincia()));
		return municipioOutput;
	}
}
