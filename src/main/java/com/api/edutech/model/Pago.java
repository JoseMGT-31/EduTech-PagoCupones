package com.api.edutech.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El id del curso es obligatorio")
    private String idCurso;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "La fecha de pago es obligatoria")
    private Date fechaPago;

    @NotNull(message = "El monto es obligatorio")
    private Double monto;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del pago es obligatorio")
    private EstadoPago estadoPago;

    @NotEmpty(message = "El método de pago es obligatorio")
    private String metodoPago;

    @ManyToOne
    @JoinColumn(name = "cupon_id")
    private Cupon cupon;
}