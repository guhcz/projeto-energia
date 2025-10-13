package br.com.fiap.energia.dto;

import br.com.fiap.energia.model.UsuarioRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UsuarioCadastroDto(
        Long id,

        @NotBlank(message = "O nome do usuário é obrigatório!")
        String nome,

        @NotBlank(message = "A senha é obrigatória!")
        @Size(min = 6, max = 10, message = "A senha deve conter entre 6 e 10 caracteres!")
        String senha,

        @NotBlank(message = "O e-mail é obrigatório!")
        @Email(message = "O e-mail está com o formato inválido!")
        String email,

        @NotNull(message = "A data de cadastro é obrigatória!")
        LocalDate dataCadastro,

        UsuarioRole role
) {
}
