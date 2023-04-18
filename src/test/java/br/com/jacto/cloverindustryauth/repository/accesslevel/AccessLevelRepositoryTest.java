package br.com.jacto.cloverindustryauth.repository.accesslevel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccessLevelRepositoryTest {

    @Test
    @DisplayName("Deveria retornar o número de usuários associados a um determinado nível de acesso")
    void countUsersByAccessLevelIdScenario1() {
        // Cria um objeto AccessLevelRepository falso
        AccessLevelRepository fakeRepository = Mockito.mock(AccessLevelRepository.class);

        // Configura o comportamento do método countUsersByAccessLevelId() para retornar 5 quando a entrada for um UUID específico
        UUID testUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
        Mockito.when(fakeRepository.countUsersByAccessLevelId(testUUID)).thenReturn(5);

        // Chama o método countUsersByAccessLevelId() do objeto falso com um argumento de teste
        long result = fakeRepository.countUsersByAccessLevelId(testUUID);

        // Verifica se o resultado é o esperado
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Deveria retornar 0 quando não houver usuários associados a um determinado nível de acesso")
    void countUsersByAccessLevelIdScenario2() {
        // Cria um objeto AccessLevelRepository falso
        AccessLevelRepository fakeRepository = Mockito.mock(AccessLevelRepository.class);

        // Configura o comportamento do método countUsersByAccessLevelId() para retornar 0 quando a entrada for um UUID específico
        UUID testUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
        Mockito.when(fakeRepository.countUsersByAccessLevelId(testUUID)).thenReturn(0);

        // Chama o método countUsersByAccessLevelId() do objeto falso com um argumento de teste
        long result = fakeRepository.countUsersByAccessLevelId(testUUID);

        // Verifica se o resultado é o esperado
        assertEquals(0, result);
    }

}