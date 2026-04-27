package com.raissapayments.conector.domain.dto.operativo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaldoResponseDto {
	@JsonProperty("status")
	String status;

	@JsonProperty("accounts")
	List<AccountsResponseDto> accounts;
}
