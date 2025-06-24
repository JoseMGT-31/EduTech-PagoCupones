package com.api.edutech.assembler;

import com.api.edutech.controller.PagoControllerV2;
import com.api.edutech.model.Pago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<Pago>> {

    @Override
    public EntityModel<Pago> toModel(Pago pago) {
        return EntityModel.of(pago,
                linkTo(methodOn(PagoControllerV2.class).getPagoById(pago.getId())).withRel("detalle_pago"),
                linkTo(methodOn(PagoControllerV2.class).getAllPagos()).withRel("lista_pagos"));
    }
}