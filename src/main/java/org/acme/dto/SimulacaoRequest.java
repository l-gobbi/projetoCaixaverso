package org.acme.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SimulacaoRequest(
        @NotNull(message = "O valor inicial é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor inicial deve ser maior que zero")
        BigDecimal valorInicial,

        @NotNull(message = "A taxa de juros é obrigatória")
        @DecimalMin(value = "0.01", message = "A taxa de juros deve ser maior que zero")
        BigDecimal taxaJurosMensal,

        @NotNull(message = "O prazo em meses é obrigatório")
        @Min(value = 1, message = "O prazo mínimo é de 1 mês")
        Integer prazoMeses
) {}