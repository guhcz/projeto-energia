package br.com.fiap.energia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(
        @NotBlank(message = "O e-mail do usuário é obrigatório!")
        @Email(message = "O e-mail do usuário nao é válido!")
        String email,

        @NotBlank(message = "A senha do usuário é obrigatória!")
        @Size(min = 6, max = 10, message = "A sdewnha deve conter entre 6 e 10 caracteres!")
        String senha
) {
}
