package com.raissapayments.conector.domain.dto.operativo.response.ejecucion.detallada;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaTransGetBffResponseDto {
    @JsonProperty("TipoDocBeneficiario")
    private Integer tipoDocBeneficiario;

    @JsonProperty("DocumentoBeneficiario")
    private String documentoBeneficiario;

    @JsonProperty("NombreBeneficiario")
    private String nombreBeneficiario;

    @JsonProperty("DireccionBeneficiario")
    private String direccionBeneficiario;

    @JsonProperty("TelefonoBeneficiario")
    private String telefonoBeneficiario;

    @JsonProperty("MovilBeneficiario")
    private String movilBeneficiario;

    @JsonProperty("MismoTitularOut")
    private String mismoTitularOut;

    @JsonProperty("TransferenciaId")
    private String transferenciaId;

    @JsonProperty("ITF")
    private BigDecimal itf;

    @JsonProperty("ComisionOrigen")
    private BigDecimal comisionOrigen;

    @JsonProperty("ComisionDestino")
    private BigDecimal comisionDestino;

    @JsonProperty("MPE001IDL")
    private String mpe001idl;

    @JsonProperty("CodRespuesta")
    private String codRespuesta;

    @JsonProperty("DscRespuesta")
    private String dscRespuesta;

    @JsonProperty("Erroresnegocio")
    private ErroresNegocioDto erroresNegocio;

    @JsonProperty("Btoutreq")
    private BtoutreqDto btoutreq;

    // ✅ Método para verificar si hay errores
    public boolean hasErroresNegocio() {
        return erroresNegocio != null
                && erroresNegocio.getBtErrorNegocio() != null
                && !erroresNegocio.getBtErrorNegocio().isEmpty();
    }

    // ✅ Método para obtener todos los mensajes de error
    public List<String> getMensajesError() {
        List<String> mensajes = new ArrayList<>();

        if (hasErroresNegocio()) {
            for (BTErrorNegocioDto error : erroresNegocio.getBtErrorNegocio()) {
                String mensaje = String.format("[%s] Código: %d - %s",
                        error.getSeveridad(),
                        error.getCodigo(),
                        error.getDescripcion()
                );
                mensajes.add(mensaje);
            }
        }

        return mensajes;
    }

    // ✅ Método para obtener solo las descripciones
    public List<String> getDescripcionesError() {
        List<String> descripciones = new ArrayList<>();

        if (hasErroresNegocio()) {
            for (BTErrorNegocioDto error : erroresNegocio.getBtErrorNegocio()) {
                descripciones.add(error.getDescripcion());
            }
        }

        return descripciones;
    }

    // ✅ Método para obtener las descripciones como una sola cadena separada por "|"
    public String getDescripcionesErrorAsString() {
        if (!hasErroresNegocio()) {
            return "";
        }

        return erroresNegocio.getBtErrorNegocio().stream()
                .map(BTErrorNegocioDto::getDescripcion)
                .collect(Collectors.joining(" - "));
    }

    public String getCodigosErrorAsString() {
        if (!hasErroresNegocio()) {
            return "";
        }

        return erroresNegocio.getBtErrorNegocio().stream()
                .map(error -> String.valueOf(error.getCodigo()))
                .collect(Collectors.joining(" - "));
    }

    // ✅ Método para obtener el primer error (el más relevante)
    public String getPrimerError() {
        if (hasErroresNegocio() && !erroresNegocio.getBtErrorNegocio().isEmpty()) {
            BTErrorNegocioDto error = erroresNegocio.getBtErrorNegocio().get(0);
            return String.format("Código: %d - %s", error.getCodigo(), error.getDescripcion());
        }
        return null;
    }

    // ✅ Método para obtener un resumen del error
    public String getResumenError() {
        if (!hasErroresNegocio()) {
            return null;
        }

        if (erroresNegocio.getBtErrorNegocio().size() == 1) {
            BTErrorNegocioDto error = erroresNegocio.getBtErrorNegocio().get(0);
            return error.getDescripcion();
        } else {
            return String.format("Se encontraron %d errores", erroresNegocio.getBtErrorNegocio().size());
        }
    }
}
