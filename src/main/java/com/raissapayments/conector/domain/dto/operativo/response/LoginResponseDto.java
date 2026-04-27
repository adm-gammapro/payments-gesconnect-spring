package com.raissapayments.conector.domain.dto.operativo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto {
    @JsonProperty("key")
    String key;

    @JsonProperty("status")
    String status;
}
