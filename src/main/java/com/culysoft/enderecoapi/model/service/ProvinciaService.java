package com.culysoft.enderecoapi.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culysoft.enderecoapi.api.model.ProvinciaInput;
import com.culysoft.enderecoapi.api.model.ProvinciaModel;
import com.culysoft.enderecoapi.api.model.ProvinciaOutput;
import com.culysoft.enderecoapi.model.domain.Municipio;
import com.culysoft.enderecoapi.model.domain.Pais;
import com.culysoft.enderecoapi.model.domain.Provincia;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.repository.ProvinciaRepository;

@Service
public class ProvinciaService {

	@Autowired
	private ProvinciaRepository provinciaRepository;
	
	@Autowired
	PaisService paisService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Transactional(readOnly = true)
	public ProvinciaModel findAll(String paisNome) throws EntidadeNaoEncontradaException {
		Pais pais = getPais(paisNome);
		List<ProvinciaOutput> provinciaOutputs = new ArrayList<ProvinciaOutput>();
		for(ProvinciaOutput provinciaOutput : toCollectionModel(pais.getProvincias())) {
			provinciaOutputs.add(provinciaOutputLink(pais, provinciaOutput));
		}
		ProvinciaModel provinciaModel = new ProvinciaModel(pais.getNome(), provinciaOutputs);
		provinciaModel.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + pais.getNome()));
		return provinciaModel;
	}
	
	private ProvinciaOutput toModel(Provincia provincia) {
		return modelMapper.map(provincia, ProvinciaOutput.class);
	}
	
	private List<ProvinciaOutput> toCollectionModel(List<Provincia> provincias) {
		return provincias.stream().map(provincia -> toModel(provincia)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ProvinciaOutput findById(ProvinciaInput provinciaInput) throws EntidadeNaoEncontradaException {
		Pais pais = getPais(provinciaInput.getPais());
		return  provinciaOutputLink(pais, toModel(findProvinciaPeloIdNoPais(pais, provinciaInput.getId())));
	}
	
	@Transactional(readOnly = true)
	public ProvinciaOutput findByNome(ProvinciaInput provinciaInput) throws EntidadeNaoEncontradaException {
		Pais pais = getPais(provinciaInput.getPais());
		return  provinciaOutputLink(pais, toModel(findProvinciaPeloNomeNoPais(pais, provinciaInput.getProvinciaNome())));
	}
	
	@Transactional(readOnly = false)
	public ProvinciaOutput save(ProvinciaInput provinciaInput) throws EntidadeNaoEncontradaException {
		Provincia provincia = toEntity(provinciaInput);
		
		Pais pais = getPais(provinciaInput.getPais());
		validacaoDaNovaProvincia(pais, provinciaInput);
		provincia = provinciaRepository.findById(provinciaInput.getId()).orElse(new Provincia(provinciaInput.getProvinciaNome()));
		provincia.setNome(provinciaInput.getProvinciaNome());
		provincia = provinciaRepository.save(provincia);
		
		paisService.adicionaProvinciaNaListaPais(pais, provincia);
		
		return provinciaOutputLink(pais, toModel(provincia));
	}
	
	private Provincia toEntity(ProvinciaInput provinciaInput) {
		return modelMapper.map(provinciaInput, Provincia.class);
	}
	
	@Transactional(readOnly = false)
	public void deleteById(ProvinciaInput provinciaInput) throws EntidadeNaoEncontradaException {
		Pais pais = getPais(provinciaInput.getPais());
		Provincia provincia = findProvinciaPeloIdNoPais(pais, provinciaInput.getId());
		paisService.remocaoDaProvinciaNaListaPais(pais, provincia);
		provinciaRepository.deleteById(provincia.getId());
	}
	
	public Provincia findProvinciaPeloNomeNoPais(Pais pais, String provinciaNome) throws EntidadeNaoEncontradaException {
		return pais.getProvincias().stream()
				.filter(p -> p.getNome().equalsIgnoreCase(provinciaNome))
				.findFirst()
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Provincia de nome: " + provinciaNome + " no País " + pais.getNome() + " não encontrado."));
	}
	
	private Provincia findProvinciaPeloIdNoPais(Pais pais, Long provinciaId) throws EntidadeNaoEncontradaException {
		return pais.getProvincias().stream()
				.filter(p -> p.getId().equals(provinciaId))
				.findFirst()
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Provincia de id: " + provinciaId + " no País " + pais.getNome() + " não encontrado."));
	}
	
	@Transactional(readOnly = false)
	public void remocaoDoMunicipioNaListaProvincia(Provincia provincia, Municipio municipio) {
		provincia.getMunicipios().removeIf(m -> m.equals(municipio));
		provinciaRepository.save(provincia);
	}
	
	@Transactional(readOnly = false)
	public void adicionaMunicipioNaListaProvincia(Provincia provincia, Municipio municipio) {
		if(provincia.getMunicipios() == null) {
			List<Municipio> municipios = new ArrayList<Municipio>();
			municipios.add(municipio);
			provincia.setMunicipios(municipios);
		}else {
			provincia.getMunicipios().removeIf(m -> m.equals(municipio));
			provincia.getMunicipios().add(municipio);
		}
		provinciaRepository.save(provincia);
	}
	
	private void validacaoDaNovaProvincia(Pais pais, ProvinciaInput provinciaInput) throws EntidadeNaoEncontradaException {
		if(provinciaInput.getId().equals(0L)) {
			for(Provincia provincia : pais.getProvincias()) {
				if(provincia.getNome().equalsIgnoreCase(provinciaInput.getProvinciaNome())) {
					throw new EntidadeNaoEncontradaException("Provincia " + provinciaInput.getProvinciaNome() + " já está registado na País " + pais.getNome());
				}
			}
		}
	}
	
	private Pais getPais(String paisNome) throws EntidadeNaoEncontradaException {
		return paisService.findByNomeWithoutToModel(paisNome);
	}
	
	private ProvinciaOutput provinciaOutputLink(Pais pais, ProvinciaOutput provinciaOutput) {
		provinciaOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + pais.getNome() + "/provincias/" + provinciaOutput.getId()));
		provinciaOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + pais.getNome() + "/provincias/provincia/" + provinciaOutput.getNome()));
		provinciaOutput.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + pais.getNome()));
		return provinciaOutput;
	}
}
