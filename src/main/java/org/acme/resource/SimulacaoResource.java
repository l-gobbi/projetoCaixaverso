package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Simulacao;
import org.acme.dto.SimulacaoRequest;
import org.acme.service.SimulacaoService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Simulação de Financiamento", description = "API para simular financiamentos com juros compostos")
public class SimulacaoResource {

    @Inject
    SimulacaoService service;

    @POST
    @Operation(summary = "Criar nova simulação", description = "Recebe os parâmetros, calcula os juros e persiste na base de dados.")
    @APIResponse(responseCode = "201", description = "Simulação criada com sucesso")
    @APIResponse(responseCode = "400", description = "Erro de validação nos dados de entrada (Payload inválido)")
    public Response criarSimulacao(@Valid SimulacaoRequest request) {
        Simulacao simulacao = service.gerarSimulacao(request);

        return Response.created(URI.create("/simulacoes/" + simulacao.id))
                .entity(simulacao)
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Consultar simulação", description = "Retorna uma simulação existente através do ID, incluindo a memória de cálculo.")
    @APIResponse(responseCode = "200", description = "Simulação encontrada")
    @APIResponse(responseCode = "404", description = "Simulação não encontrada")
    public Response obterSimulacao(@PathParam("id") Long id) {
        Simulacao simulacao = service.obterSimulacao(id);

        if (simulacao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(simulacao).build();
    }
}