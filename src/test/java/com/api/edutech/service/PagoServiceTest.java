import com.api.edutech.model.Pago;
import com.api.edutech.repository.PagoRepository;
import com.api.edutech.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pago;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setId(1L);
        pago.setMonto(5000.0);
    }

    @Test
    void testGetAllPagos() {
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago));
        assertEquals(1, pagoService.getAllPagos().size());
    }

    @Test
    void testGetPagoById_found() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        Optional<Pago> resultado = pagoService.getPagoById(1L);
        assertTrue(resultado.isPresent());
        assertEquals(5000.0, resultado.get().getMonto());
    }

    @Test
    void testGetPagoById_notFound() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(pagoService.getPagoById(99L).isEmpty());
    }

    @Test
    void testCreatePago() {
        when(pagoRepository.save(pago)).thenReturn(pago);
        Pago nuevo = pagoService.createPago(pago);
        assertEquals(5000.0, nuevo.getMonto());
    }

    @Test
    void testDeletePago() {
        doNothing().when(pagoRepository).deleteById(1L);
        pagoService.deletePago(1L);
        verify(pagoRepository, times(1)).deleteById(1L);
    }
}