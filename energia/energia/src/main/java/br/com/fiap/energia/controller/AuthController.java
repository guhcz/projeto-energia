package br.com.fiap.energia.controller;

import br.com.fiap.energia.config.security.TokenService;
import br.com.fiap.energia.dto.LoginDto;
import br.com.fiap.energia.dto.TokenDto;
import br.com.fiap.energia.dto.UsuarioCadastroDto;
import br.com.fiap.energia.dto.UsuarioExibicaoDto;
import br.com.fiap.energia.model.UsuarioModel;
import br.com.fiap.energia.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(
                        loginDto.email(),
                        loginDto.senha());

        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.gerarToken((UsuarioModel) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDto(token));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioExibicaoDto register(@RequestBody @Valid UsuarioCadastroDto usuarioCadastroDto) {
        UsuarioExibicaoDto usuarioSalvar;
        usuarioSalvar = usuarioService.cadastrar(usuarioCadastroDto);
        return usuarioSalvar;
    }


}
