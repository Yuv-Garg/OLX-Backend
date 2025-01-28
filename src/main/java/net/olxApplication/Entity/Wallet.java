package net.olxApplication.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double balance;


    @OneToOne(mappedBy = "wallet")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "wallet")
//    @JsonIgnore
    private List<Transaction> transactions;

}
