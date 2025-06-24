package com.api.edutech.service;

import com.api.edutech.model.Cupon;
import com.api.edutech.repository.CuponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuponService {

    @Autowired
    private CuponRepository cuponRepository;

    public List<Cupon> getAllCupones() {
        return cuponRepository.findAll();
    }

    public Optional<Cupon> getCuponById(Long id) {
        return cuponRepository.findById(id);
    }

    public Cupon createCupon(Cupon cupon) {
        return cuponRepository.save(cupon);
    }

    public void deleteCupon(Long id) {
        cuponRepository.deleteById(id);
    }
}