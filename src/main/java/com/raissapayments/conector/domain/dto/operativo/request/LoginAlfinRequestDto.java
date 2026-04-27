package com.raissapayments.conector.domain.dto.operativo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginAlfinRequestDto {
    @JsonProperty("key_access")
    private String provider;

    @JsonProperty("secret_access")
    private String username;

    @JsonProperty("codigoEmpresa")
    private String company_code;

    @JsonProperty("contasena")
    private String password;

    @JsonProperty("apiKey")
    private String apiKey;

    @JsonProperty("indicador_valor_adicional")
    private boolean indicadorValorAdicional;
}
