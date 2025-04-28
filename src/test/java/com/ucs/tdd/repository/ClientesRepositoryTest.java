package com.ucs.tdd.repository;

import com.ucs.tdd.model.ClientesEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
@Sql(statements = {
        "DELETE FROM pagamentos",
        "DELETE FROM clientes"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("default")
class ClientesRepositoryTest {

    @Autowired
    private ClientesRepository repository;

    @Test
    void testSaveAndFindAll() {
        ClientesEntity c = new ClientesEntity();
        c.setId(100);
        c.setNome("TestUser");
        c.setDataNascimento("01012000");
        c.setTelefone("9999-9999");

        repository.save(c);
        List<ClientesEntity> todos = repository.findAll();
        assertThat(todos).hasSize(1);
        ClientesEntity encontrado = todos.getFirst();
        assertThat(encontrado.getNome()).isEqualTo("TestUser");
    }

    @Test
    void testDeleteAll() {
        ClientesEntity c = new ClientesEntity();
        c.setId(200);
        c.setNome("Outro");
        repository.save(c);
        repository.deleteAll();
        List<ClientesEntity> vazios = repository.findAll();
        assertThat(vazios).isEmpty();
    }
}
