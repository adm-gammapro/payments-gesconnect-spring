package com.raissapayments.conector.domain.dto.operativo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountsResponseDto {
    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;

    @JsonProperty("number")
    String number;

    @JsonProperty("branch")
    String branch;

    @JsonProperty("currency")
    String currency;

    @JsonProperty("balance")
    Double balance;

    @JsonProperty("contable")
    Double contable;
}
