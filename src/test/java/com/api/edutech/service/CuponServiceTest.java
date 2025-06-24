package com.api.edutech.service;

import com.api.edutech.model.Cupon;
import com.api.edutech.repository.CuponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuponServiceTest{

    @Mock
    private CuponRepository cuponRepository;

    @InjectMocks
    private CuponService cuponService;

    private Cupon cupon;

    @BeforeEach
    void setUp() {
        cupon = new Cupon();
        cupon.setId(1L);
        cupon.setCodigo("DESC10");
        cupon.setDescuento(10.0);
    }

    @Test
    void testGetAllCupones() {
        when(cuponRepository.findAll()).thenReturn(Arrays.asList(cupon));
        assertEquals(1, cuponService.getAllCupones().size());
    }

    @Test
    void testGetCuponById_found() {
        when(cuponRepository.findById(1L)).thenReturn(Optional.of(cupon));
        Optional<Cupon> resultado = cuponService.getCuponById(1L);
        assertTrue(resultado.isPresent());
        assertEquals("DESC10", resultado.get().getCodigo());
    }

    @Test
    void testGetCuponById_notFound() {
        when(cuponRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(cuponService.getCuponById(99L).isEmpty());
    }

    @Test
    void testCreateCupon() {
        when(cuponRepository.save(cupon)).thenReturn(cupon);
        Cupon nuevo = cuponService.createCupon(cupon);
        assertEquals("DESC10", nuevo.getCodigo());
    }

    @Test
    void testDeleteCupon() {
        doNothing().when(cuponRepository).deleteById(1L);
        cuponService.deleteCupon(1L);
        verify(cuponRepository, times(1)).deleteById(1L);
    }
}