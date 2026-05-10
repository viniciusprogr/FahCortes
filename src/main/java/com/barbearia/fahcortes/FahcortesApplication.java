package com.barbearia.fahcortes;

import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FahcortesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FahcortesApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (usuarioRepository.findByEmail("admin@fahcortes.com").isEmpty()) {
				UsuarioEntity admin = new UsuarioEntity();
				admin.setNome("Admin");
				admin.setEmail("admin@fahcortes.com");
				admin.setSenha(passwordEncoder.encode("fah1234"));
				admin.setRole(UsuarioEnum.ADMIN);
				usuarioRepository.save(admin);
			}
		};
	}
}
