package za.co.ebank.bank.model;

import lombok.*;

/**
 *
 * @author cliff
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse {
    private Object data;
    private String message;
    private boolean error = true;

    public ApiResponse(Object data, String message){
        this.data = data;
        this.message = message;
    }
}