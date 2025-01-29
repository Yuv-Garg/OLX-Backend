package net.olxApplication.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.olxApplication.Enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private Double price;

    @Enumerated(EnumType.STRING) // Automatically int assign krta hai
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    @JsonIgnore
    private User user ;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Order> orders;


}
