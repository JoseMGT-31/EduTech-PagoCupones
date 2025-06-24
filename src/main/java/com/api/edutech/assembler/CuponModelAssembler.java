package com.api.edutech.assembler;

import com.api.edutech.controller.CuponControllerV2;
import com.api.edutech.model.Cupon;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CuponModelAssembler implements RepresentationModelAssembler<Cupon, EntityModel<Cupon>> {

    @Override
    public EntityModel<Cupon> toModel(Cupon cupon) {
        return EntityModel.of(cupon,
                linkTo(methodOn(CuponControllerV2.class).getCuponById(cupon.getId())).withRel("detalle"),
                linkTo(methodOn(CuponControllerV2.class).getAllCupones()).withRel("ver_todos"));
    }
}
