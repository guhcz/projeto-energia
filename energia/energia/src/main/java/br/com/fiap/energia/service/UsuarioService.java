package br.com.fiap.energia.service;

import br.com.fiap.energia.dto.EquipamentoExibicaoDto;
import br.com.fiap.energia.dto.UsuarioCadastroDto;
import br.com.fiap.energia.dto.UsuarioExibicaoDto;
import br.com.fiap.energia.exception.UsuarioNaoEncontradoException;
import br.com.fiap.energia.model.UsuarioModel;
import br.com.fiap.energia.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioExibicaoDto cadastrar(UsuarioCadastroDto usuarioCadastroDto) {

        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuarioCadastroDto.senha());

        UsuarioModel usuario = new UsuarioModel();
        BeanUtils.copyProperties(usuarioCadastroDto, usuario);
        usuario.setSenha(senhaCriptografada);
        return new UsuarioExibicaoDto(usuarioRepository.save(usuario));
    }

    public UsuarioExibicaoDto atualizar(UsuarioCadastroDto usuarioCadastroDto) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findById(usuarioCadastroDto.id());

        if (usuarioOptional.isPresent()) {
            UsuarioModel usuarioModel = new UsuarioModel();
            BeanUtils.copyProperties(usuarioCadastroDto, usuarioModel);
            return new UsuarioExibicaoDto(usuarioRepository.save(usuarioModel));
        } else {
            throw new UsuarioNaoEncontradoException("Usuário nao encontrado!");
        }
    }

    public void excluir(Long id) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            usuarioRepository.delete(usuarioOptional.get());
        } else {
            throw new UsuarioNaoEncontradoException("Usuário nao encontrado!");
        }
    }

    public UsuarioExibicaoDto buscarPeloId(Long id) {

        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            return new UsuarioExibicaoDto(usuarioOptional.get());
        } else {
            throw new UsuarioNaoEncontradoException("Equipamento nao encontrado!");
        }
    }

    public List<UsuarioExibicaoDto> listarTodosOsUsuarios() {
        return usuarioRepository
                .findAll()
                .stream()
                .map(UsuarioExibicaoDto::new)
                .toList();
    }

    public UsuarioExibicaoDto buscarPeloNome(String nome) {
        Optional<UsuarioExibicaoDto> usuarioExibicaoDtoOptional = usuarioRepository.findByNome(nome);

        if (usuarioExibicaoDtoOptional.isPresent()) {
            UsuarioModel usuario = new UsuarioModel();
            BeanUtils.copyProperties(nome, usuario);
            return new UsuarioExibicaoDto(usuario);
        } else {
            throw new UsuarioNaoEncontradoException("Nome do usuário nao encontrado!");
        }
    }

//    public UsuarioExibicaoDto buscarPeloEmail(String email) {
//        Optional<UsuarioExibicaoDto> usuarioExibicaoDtoOptional = usuarioRepository.findByEmail(email);
//
//        if (usuarioExibicaoDtoOptional.isPresent()) {
//            UsuarioModel usuario = new UsuarioModel();
//            BeanUtils.copyProperties(email, usuario);
//            return new UsuarioExibicaoDto(usuario);
//        } else {
//            throw new UsuarioNaoEncontradoException("Nome do usuário nao encontrado!");
//        }
//    }

    public List<UsuarioExibicaoDto> listarPelaDataCadastro(LocalDate dataInicial, LocalDate dataFinal) {
        return usuarioRepository.findByDataCadastroBetween(dataInicial, dataFinal);
    }

}
