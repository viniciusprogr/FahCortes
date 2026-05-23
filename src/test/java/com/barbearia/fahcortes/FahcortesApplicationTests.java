package com.barbearia.fahcortes;

import com.barbearia.fahcortes.domain.entities.usuario.UsuarioEnum;
import com.barbearia.fahcortes.infra.entities.UsuarioEntity;
import com.barbearia.fahcortes.infra.persistence.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FahcortesApplicationTests {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CommandLineRunner commandLineRunner;

	@Test
	void contextLoads() {
	}

	@Test
	void adminDeveSerCriadoNaInicializacao() {
		assertTrue(usuarioRepository.findByEmail("admin@fahcortes.com").isPresent());
	}

	@Test
	void adminDeveTerRoleAdmin() {
		UsuarioEntity admin = usuarioRepository.findByEmail("admin@fahcortes.com").orElseThrow();
		assertEquals(UsuarioEnum.ADMIN, admin.getRole());
	}

	@Test
	void adminDeveTerNomeCorreto() {
		UsuarioEntity admin = usuarioRepository.findByEmail("admin@fahcortes.com").orElseThrow();
		assertEquals("Admin", admin.getNome());
	}

	@Test
	void initNaoCriaDuplicataDeAdmin() throws Exception {
		commandLineRunner.run();
		long count = usuarioRepository.findAll().stream()
				.filter(u -> "admin@fahcortes.com".equals(u.getEmail()))
				.count();
		assertEquals(1, count);
	}
}
