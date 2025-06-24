package com.api.edutech.controller;

import com.api.edutech.assembler.PagoModelAssembler;
import com.api.edutech.model.Pago;
import com.api.edutech.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pagos")
public class PagoControllerV2 {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Pago>> getAllPagos() {
        List<EntityModel<Pago>> pagos = pagoService.getAllPagos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pagos,
                linkTo(methodOn(PagoControllerV2.class).getAllPagos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Pago> getPagoById(@PathVariable Long id) {
        Pago pago = pagoService.getPagoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));

        return assembler.toModel(pago);
    }
}
