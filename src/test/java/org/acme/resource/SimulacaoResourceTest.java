package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.dto.SimulacaoRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class SimulacaoResourceTest {

    @Test
    public void testCriarSimulacaoComSucesso() {
        SimulacaoRequest request = new SimulacaoRequest(
                new BigDecimal("1000.00"),
                new BigDecimal("1.5"),
                12
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201) // Valida a criação
                .body("id", notNullValue())
                .body("valorTotalFinal", notNullValue());
    }

    @Test
    public void testFalhaValidacaoDadosNegativos() {
        // Enviar um valor inicial negativo para forçar o erro 400
        SimulacaoRequest request = new SimulacaoRequest(
                new BigDecimal("-500.00"),
                new BigDecimal("1.5"),
                12
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400); // Valida se o Bean Validation está a funcionar
    }

    @Test
    public void testSimulacaoNaoEncontrada() {
        given()
                .when()
                .get("/simulacoes/999999") // ID inexistente
                .then()
                .statusCode(404); // Valida o retorno correto
    }

    @Test
    public void testObterSimulacaoComSucesso() {
        // 1. Prepara o payload para criar uma simulação
        SimulacaoRequest request = new SimulacaoRequest(
                new BigDecimal("5000.00"),
                new BigDecimal("1.2"),
                24
        );

        // 2. Faz o POST para criar a simulação e guarda o ID gerado
        Integer idCriado = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(201)
                .extract().path("id"); // Extrai o ID do corpo da resposta (JSON)

        // 3. Faz o GET usando o ID extraído para cobrir o Response.ok()
        given()
                .when()
                .get("/simulacoes/" + idCriado)
                .then()
                .statusCode(200) // Valida que encontrou com sucesso
                .body("id", org.hamcrest.CoreMatchers.is(idCriado))
                .body("valorInicial", org.hamcrest.CoreMatchers.notNullValue());
    }
}