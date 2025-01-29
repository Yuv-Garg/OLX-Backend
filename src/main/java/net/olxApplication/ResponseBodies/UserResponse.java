package net.olxApplication.ResponseBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.olxApplication.Entity.User;
import net.olxApplication.Enums.UserStatus;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {


    private String name;
    private String email;

    private Double walletBalance;

    private long orderNumber;

    private long productsAdded;

    @Enumerated
    private UserStatus userStatus;


}
