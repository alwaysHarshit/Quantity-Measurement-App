package com.app.quantity_measurement_app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API Response Wrapper")
public class ResponseDTO {

    @Schema(description = "Status code of the response", example = "200")
    private int status;

    @Schema(description = "Message describing the result", example = "Operation successful")
    private String message;

    @Schema(description = "Response data (token, user info, etc.)")
    private Object data;

    public static ResponseDTO success(String message, Object data) {
        return new ResponseDTO(200, message, data);
    }

    public static ResponseDTO error(int status, String message) {
        return new ResponseDTO(status, message, null);
    }
}
