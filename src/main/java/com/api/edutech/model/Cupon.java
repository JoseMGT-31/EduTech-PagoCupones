package com.api.edutech.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cupones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "El código es obligatorio")
    private String codigo;

    @DecimalMin(value = "0.1", inclusive = true, message = "El descuento debe ser mayor que 0")
    @NotNull(message = "El descuento no puede ser nulo")
    private Double descuento;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "La fecha de expiración es obligatoria")
    private Date fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado no puede ser nulo")
    private EstadoCupon estado;
}