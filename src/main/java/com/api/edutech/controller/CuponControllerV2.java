package com.api.edutech.controller;

import com.api.edutech.assembler.CuponModelAssembler;
import com.api.edutech.model.Cupon;
import com.api.edutech.service.CuponService;
import com.api.edutech.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/cupones")
public class CuponControllerV2 {

    @Autowired
    private CuponService cuponService;

    @Autowired
    private CuponModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Cupon>> getAllCupones() {
        List<EntityModel<Cupon>> cupones = cuponService.getAllCupones().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(cupones,
                linkTo(methodOn(CuponControllerV2.class).getAllCupones()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Cupon> getCuponById(@PathVariable Long id) {
        Cupon cupon = cuponService.getCuponById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cupón no encontrado"));
        return assembler.toModel(cupon);
    }

    @PostMapping
    public ResponseEntity<?> createCupon(@Valid @RequestBody Cupon cupon, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = ValidationUtils.mapearErrores(result);
            return ResponseEntity.badRequest().body(Map.of("success", false, "errors", errores));
        }

        Cupon nuevoCupon = cuponService.createCupon(cupon);
        EntityModel<Cupon> cuponConLinks = assembler.toModel(nuevoCupon);

        return ResponseEntity
                .created(linkTo(methodOn(CuponControllerV2.class).getCuponById(nuevoCupon.getId())).toUri())
                .body(cuponConLinks);
    }
}