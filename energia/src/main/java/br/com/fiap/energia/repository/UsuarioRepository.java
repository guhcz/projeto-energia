package br.com.fiap.energia.repository;

import br.com.fiap.energia.dto.UsuarioExibicaoDto;
import br.com.fiap.energia.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioExibicaoDto> findByNome(String nome);

    UserDetails findByEmail(String email);

    List<UsuarioExibicaoDto> findByDataCadastroBetween(LocalDate dataInicial, LocalDate dataFinal);

}
