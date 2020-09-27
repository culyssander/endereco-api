package com.culysoft.enderecoapi.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.culysoft.enderecoapi.api.model.PaisInput;
import com.culysoft.enderecoapi.api.model.PaisModel;
import com.culysoft.enderecoapi.model.domain.Comuna;
import com.culysoft.enderecoapi.model.domain.Municipio;
import com.culysoft.enderecoapi.model.domain.Pais;
import com.culysoft.enderecoapi.model.domain.Provincia;
import com.culysoft.enderecoapi.model.exception.EntidadeNaoEncontradaException;
import com.culysoft.enderecoapi.model.exception.NegocioException;
import com.culysoft.enderecoapi.model.repository.PaisRepository;
import com.culysoft.enderecoapi.model.repository.ProvinciaRepository;

@Service
public class PaisService {

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private ProvinciaRepository provinciaRepository;

	@Autowired
	ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public List<PaisModel> findAll() {
		return toCollectionModel(paisRepository.findAll());
	}

	@Transactional(readOnly = true)
	public PaisModel findById(Long id) throws EntidadeNaoEncontradaException {
		Pais pais = paisRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("País de id: " + id + " não encontrado."));
		return paisModelLink(toModel(pais));
	}

	@Transactional(readOnly = true)
	public PaisModel findByNome(String nome) throws EntidadeNaoEncontradaException {
		Pais pais = paisRepository.findByNome(nome).orElseThrow(() -> new EntidadeNaoEncontradaException("País de nome: " + nome + " não encontrado."));
		return paisModelLink(toModel(pais));
	}
	
	@Transactional(readOnly = true)
	public Pais findByNomeWithoutToModel(String nome) throws EntidadeNaoEncontradaException {
		 return paisRepository.findByNome(nome).orElseThrow(() -> new EntidadeNaoEncontradaException("País de nome: " + nome + " não encontrado."));
	}

	@Transactional(readOnly = false)
	public PaisModel save(PaisInput paisInput) throws NegocioException, EntidadeNaoEncontradaException {
		Pais pais = toEntity(paisInput);

		pais = pais.getId() == null ? add(pais) : edit(pais);

		return toModel(paisRepository.save(pais));
	}

	private Pais toEntity(PaisInput paisInput) {
		return modelMapper.map(paisInput, Pais.class);
	}

	private Pais add(Pais pais) throws NegocioException {
		validacaoDoPAisExistente(pais);

		pais.setCapital(cadastrarCapital(pais.getCapital()));

		List<Provincia> provincias = new ArrayList<>();
		provincias.add(pais.getCapital());

		pais.setProvincias(provincias);

		return pais;
	}

	private Pais edit(Pais pais) throws EntidadeNaoEncontradaException {
		if (paisRepository.existsById(pais.getId())) {
			Pais paisTemp = paisRepository.findById(pais.getId()).get();

			pais.setCapital(cadastrarCapital(findProvinciaInPais(paisTemp, pais.getCapital().getNome())));

			paisTemp.getProvincias().removeIf(p -> p.getNome().equalsIgnoreCase(pais.getCapital().getNome()));

			pais.setProvincias(paisTemp.getProvincias());
			pais.getProvincias().add(pais.getCapital());

			return pais;
		}
		throw new EntidadeNaoEncontradaException("País de id: " + pais.getId() + " não encontrado.");
	}

	@Transactional(readOnly = false)
	public List<PaisModel> saveAll(List<PaisInput> paisesInput) throws NegocioException, EntidadeNaoEncontradaException {
		if (paisesInput != null) {
			
			List<PaisModel> paisModels = new ArrayList<PaisModel>();
			
			for (PaisInput paisInput : paisesInput) {
				Pais pais = toEntity(paisInput);

				pais = pais.getId() == null ? add(pais) : edit(pais);
				
				paisModels.add(toModel(paisRepository.save(pais)));
			}
			
			return paisModels;
		}
		throw new NegocioException("Lista de paises não pode ser vazia.");
	}

	private void validacaoDoPAisExistente(Pais pais) throws NegocioException {
		Pais paisExistente = paisRepository.findByNome(pais.getNome()).orElse(null);
		if (paisExistente != null) {
			throw new NegocioException(pais.getNome() + " já está registado. País já encontra-se no nosso sistema.");
		}
	}

	@Transactional(readOnly = false)
	private Provincia cadastrarCapital(Provincia provincia) {
		return provinciaRepository.save(provincia);
	}

	@Transactional(readOnly = false)
	public boolean deleteById(Long id) {
		if (paisRepository.existsById(id)) {
			paisRepository.deleteById(id);
			return true;
		}
		return false;
	}

	private Provincia findProvinciaInPais(Pais pais, String provinciaNome) {
		return pais.getProvincias().stream()
				.filter(p -> p.getNome().equalsIgnoreCase(provinciaNome))
				.findFirst()
				.orElse(new Provincia(provinciaNome));
	}

	private PaisModel toModel(Pais pais) {
		return modelMapper.map(pais, PaisModel.class);
	}
	
	private List<PaisModel> toCollectionModel(List<Pais> paises) {
		return paises.stream().map(pais -> paisModelLink(toModel(pais))).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = false)
	public void  remocaoDaProvinciaNaListaPais(Pais pais, Provincia provincia) {
		pais.getProvincias().removeIf(p -> p.equals(provincia));
		paisRepository.save(pais);
	}
	
	@Transactional(readOnly = false)
	public void adicionaProvinciaNaListaPais(Pais pais, Provincia provincia) {
		if (pais.getProvincias() == null) {
			List<Provincia> provincias = new ArrayList<Provincia>();
			provincias.add(provincia);
			pais.setProvincias(provincias);
		}else {
			pais.getProvincias().removeIf(p -> p.equals(provincia));
			pais.getProvincias().add(provincia);
		}
		paisRepository.save(pais);
	}
	
	private PaisModel paisModelLink(PaisModel paisModel) {
		List<Provincia> provincias = new ArrayList<Provincia>();
		List<Comuna> comunas = new ArrayList<Comuna>();
		
			paisModel.add(Link.of("http://localhost:8080/api/v1/paises/" + paisModel.getId()));
			paisModel.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome()));
			paisModel.add(Link.of("http://localhost:8080/api/v1/paises"));
			
			for(Provincia provincia : paisModel.getProvincias()) {
				provincia.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/" + provincia.getId()));
				provincia.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/provincia/" + provincia.getNome()));
				provincia.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome()));
				
				for(Municipio municipio: provincia.getMunicipios()) {
					municipio.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/provincia/"+ provincia.getNome() +"/municipios/" + municipio.getId()));
					municipio.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/provincia/"+ provincia.getNome() +"/municipios/municipio/" + municipio.getNome()));
					municipio.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/provincia/" + provincia.getNome()));
					
					for(Comuna comuna : municipio.getComunas()) {
						comuna.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/provincia/"+ provincia.getNome() +"/municipios/municipio/" + municipio.getNome() + "/comunas/" + comuna.getId()));
						comuna.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/provincia/"+ provincia.getNome() +"/municipios/municipio/" + municipio.getNome() + "/comunas/comuna/" + comuna.getNome()));
						comuna.add(Link.of("http://localhost:8080/api/v1/paises/pais/" + paisModel.getNome() + "/provincias/provincia/"+ provincia.getNome() +"/municipios/municipio/" + municipio.getNome()));
						comunas.add(comuna);
					}
					
				}
				
				provincias.add(provincia);
		}
		paisModel.setProvincias(provincias);
		return paisModel;
	}
	
}
