package com.api.edutech.controller;

import com.api.edutech.model.Cupon;
import com.api.edutech.service.CuponService;
import com.api.edutech.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Cupones", description = "Operaciones CRUD para la gestión de cupones")
@RestController
@RequestMapping("/api/cupones")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @Operation(summary = "Obtener todos los cupones", description = "Devuelve una lista de todos los cupones disponibles.")
    @GetMapping
    public ResponseEntity<?> getAllCupones() {
        List<Cupon> cupones = cuponService.getAllCupones();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", cupones
        ));
    }

    @Operation(summary = "Obtener un cupón por ID", description = "Retorna los detalles de un cupón específico.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCuponById(@PathVariable Long id) {
        return cuponService.getCuponById(id)
                .map(cupon -> ResponseEntity.ok(Map.of("success", true, "data", cupon)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "El cupón con ID " + id + " no existe"
                )));
    }

    @Operation(summary = "Crear un nuevo cupón", description = "Registra un nuevo cupón en el sistema.")
    @PostMapping
    public ResponseEntity<?> createCupon(@Valid @RequestBody Cupon cupon, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errors", ValidationUtils.mapearErrores(result)
            ));
        }

        cuponService.createCupon(cupon);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true));
    }

    @Operation(summary = "Eliminar un cupón", description = "Elimina un cupón existente por su ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCupon(@PathVariable Long id) {
        Optional<Cupon> cupon = cuponService.getCuponById(id);
        if (cupon.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El cupón con ID " + id + " no existe"
            ));
        }

        cuponService.deleteCupon(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cupón eliminado exitosamente"
        ));
    }

    @Operation(summary = "Actualizar un cupón", description = "Modifica los datos de un cupón existente.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCupon(@PathVariable Long id, @Valid @RequestBody Cupon cupon, BindingResult result) {
        Optional<Cupon> cuponExistente = cuponService.getCuponById(id);

        if (cuponExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "El cupón con ID " + id + " no existe"
            ));
        }

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "errors", ValidationUtils.mapearErrores(result)
            ));
        }

        Cupon cuponToUpdate = cuponExistente.get();
        cuponToUpdate.setCodigo(cupon.getCodigo());
        cuponToUpdate.setDescuento(cupon.getDescuento());
        cuponToUpdate.setFechaExpiracion(cupon.getFechaExpiracion());
        cuponToUpdate.setEstado(cupon.getEstado());

        cuponService.createCupon(cuponToUpdate);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Cupón actualizado correctamente"
        ));
    }
}