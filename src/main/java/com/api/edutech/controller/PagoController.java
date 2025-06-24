package com.api.edutech.controller;

import com.api.edutech.model.Pago;
import com.api.edutech.service.PagoService;
import com.api.edutech.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Pagos", description = "Operaciones relacionadas con la gestión de pagos")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private RestTemplate restTemplate;

    @Operation(summary = "Obtener todos los pagos", description = "Devuelve una lista de todos los pagos registrados.")
    @GetMapping
    public ResponseEntity<?> getAllPagos() {
        List<Pago> pagos = pagoService.getAllPagos();

        if (pagos.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "No hay pagos registrados",
                    "data", List.of()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", pagos
        ));
    }

    @Operation(summary = "Obtener un pago por ID", description = "Retorna los detalles de un pago específico por su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPagoById(@PathVariable Long id) {
        return pagoService.getPagoById(id)
                .map(pago -> ResponseEntity.ok(Map.of("success", true, "data", pago)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "El pago con ID " + id + " no existe"
                )));
    }

    @Operation(summary = "Crear un nuevo pago", description = "Registra un nuevo pago en el sistema. El ID del curso debe ser válido.")
    @PostMapping
    public ResponseEntity<?> crearPago(@Valid @RequestBody Pago pago, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.mapearErrores(result));
        }

        // Verificación de ID de curso con otro microservicio
        String idCurso = pago.getIdCurso();
        String urlVerificacion = "http://localhost:8080/api/cursos/" + idCurso;

        try {
            ResponseEntity<Void> respuesta = restTemplate.getForEntity(urlVerificacion, Void.class);
            if (!respuesta.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "success", false,
                        "message", "El ID de curso no es válido"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El curso con ID " + idCurso + " no existe"
            ));
        }

        pagoService.createPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true));
    }

    @Operation(summary = "Eliminar un pago", description = "Elimina un pago existente por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePago(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.getPagoById(id);

        if (pago.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El pago con ID " + id + " no existe"
            ));
        }

        pagoService.deletePago(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Pago eliminado exitosamente"
        ));
    }

    @Operation(summary = "Actualizar un pago", description = "Modifica los datos de un pago existente por su ID.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePago(@PathVariable Long id, @Valid @RequestBody Pago pago, BindingResult result) {
        Optional<Pago> pagoExistente = pagoService.getPagoById(id);

        if (pagoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El pago con ID " + id + " no existe"
            ));
        }

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errors", ValidationUtils.mapearErrores(result)
            ));
        }

        Pago pagoToUpdate = pagoExistente.get();
        pagoToUpdate.setFechaPago(pago.getFechaPago());
        pagoToUpdate.setMonto(pago.getMonto());
        pagoToUpdate.setEstadoPago(pago.getEstadoPago());
        pagoToUpdate.setMetodoPago(pago.getMetodoPago());
        pagoToUpdate.setCupon(pago.getCupon());

        pagoService.createPago(pagoToUpdate);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Pago actualizado correctamente"
        ));
    }
}