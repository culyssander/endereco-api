package com.culysoft.enderecoapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culysoft.enderecoapi.api.model.Home;
import com.culysoft.enderecoapi.model.domain.Login;
import com.culysoft.enderecoapi.model.exception.NegocioException;
import com.culysoft.enderecoapi.model.util.JwtUtil;

@RestController
@RequestMapping
public class HomeController {
	
	@Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @GetMapping
	public Home home() {
		Home home = new Home();
		home.setMensagem("Bem-vindo a API de endereço (Entidade {Pais-Provincia-Municipio-Comuna}) ");
		home.setDescricao("API criado com o objectivo de estudar consume de API usado o RestTemplate, JavaHttp e Feing(cloud) - queria usar a minha propria API.");
		home.add(Link.of("http://localhost:8080/api/v1/paises"));
		home.add(Link.of("https://github.com/culyssander"));
		return home;
	}
    
    @PostMapping("/login")
    public String generateToken(@Validated @RequestBody Login authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new NegocioException("Utilizador/Palavra-Passe invalida.");
        }
        return jwtUtil.generateToken(authRequest.getUsername());
    }

	
}
