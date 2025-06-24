package com.api.edutech;

import com.api.edutech.model.Cupon;
import com.api.edutech.model.EstadoCupon;
import com.api.edutech.model.EstadoPago;
import com.api.edutech.model.Pago;
import com.api.edutech.repository.CuponRepository;
import com.api.edutech.repository.PagoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CuponRepository cuponRepository;

    @Autowired
    private PagoRepository pagoRepository;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // Crear cupones
        for (int i = 0; i < 10; i++) {
            Cupon cupon = new Cupon();
            cupon.setCodigo(faker.code().ean8());
            cupon.setFechaExpiracion(faker.date().future(30, java.util.concurrent.TimeUnit.DAYS));
            double descuentoDouble = faker.number().randomDouble(2, 5, 50);
            cupon.setDescuento(BigDecimal.valueOf(descuentoDouble).setScale(2, RoundingMode.HALF_UP).doubleValue());
            cupon.setEstado(EstadoCupon.ACTIVO);
            cuponRepository.save(cupon);
        }

        // Crear pagos
        for (int i = 0; i < 10; i++) {
            Pago pago = new Pago();
            double montoDouble = faker.number().randomDouble(2, 1000, 5000);
            pago.setMonto(BigDecimal.valueOf(montoDouble).setScale(2, RoundingMode.HALF_UP).doubleValue());
            pago.setFechaPago(new Date());
            pago.setEstadoPago(random.nextBoolean() ? EstadoPago.PENDIENTE : EstadoPago.COMPLETADO);
            pago.setMetodoPago(faker.business().creditCardType());
            pago.setCupon(cuponRepository.findAll().get(random.nextInt((int) cuponRepository.count())));
            pago.setIdCurso("CURSO-" + faker.code().asin());
            pagoRepository.save(pago);
        }


        System.out.println("Datos Faker generados correctamente.");
    }
}