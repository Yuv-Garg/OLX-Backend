package net.olxApplication.ResponseBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.olxApplication.Entity.User;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponse {


    private String name;
    private String email;

    @JsonProperty("wallet_Balance")
    private Double walletBalance;

    @JsonProperty("number_of_transactions")
    private long orderNumber;

    @JsonProperty("products_Added")
    private long productsAdded;

    @JsonProperty("User_Status")
    private String status;


}
