package com.example.taco_cloud_resource_server.app.model.taco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.example.taco_cloud_resource_server.app.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class TacoOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;
    
    private Date placedAt = new Date();

    @NotBlank(message = "Cannot be empty!")
    private String deliveryName;

    @NotBlank(message = "Cannot be empty!")
    private String deliveryStreet;

    @NotBlank(message = "Cannot be empty!")
    private String deliveryCity;

    @NotBlank(message = "Cannot be empty!")
    private String deliveryState;

    @NotBlank(message = "Cannot be empty!")
    private String deliveryZip;

    @CreditCardNumber(message = "Invalid number!")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Invalid format!")
    private String ccExpiration;
    
    @Digits(integer = 3, fraction = 0, message = "Invalid number!")
    @Column(name = "cc_cvv")
    private String ccCVV;

    @ManyToMany
    @JoinTable(
        name = "taco_order_tacos",
        joinColumns = @JoinColumn(name = "taco_order_id"),
        inverseJoinColumns = @JoinColumn(name = "tacos_id")
    )
    private List<Taco> tacos = new ArrayList<>();

    @ManyToOne
    private User user;

    @PrePersist
    public void generateIdIfNull() {
        if (this.id == null) id = UUID.randomUUID().toString();
    }

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
