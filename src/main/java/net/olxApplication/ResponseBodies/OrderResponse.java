package net.olxApplication.ResponseBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderResponse {

    @JsonProperty("Order_Status")
    private String status;

    @JsonProperty("product_Name")
    private String productName;

    private Double price;
}
