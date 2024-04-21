package com.AntonioBarrios.ventas.ventas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.AntonioBarrios.ventas.ventas.model.Ventas;
import com.AntonioBarrios.ventas.ventas.service.VentasService;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentasController {
    private final VentasService ventasService;

    @Autowired
    public VentasController(VentasService ventasService) {
        this.ventasService = ventasService;
    }

    @GetMapping
    public ResponseEntity<List<Ventas>> findAllVentas() {
        List<Ventas> ventas = ventasService.findAllVentas();
        return new ResponseEntity<>(ventas, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Ventas> createVentaAdd(@RequestBody Ventas venta) {
        Ventas createdVenta = ventasService.addVenta(venta, true);
        return ResponseEntity.ok(createdVenta);
    }

    @PostMapping("/subtract")
    public ResponseEntity<Ventas> createVentaSubtract(@RequestBody Ventas venta) {
        Ventas createdVenta = ventasService.addVenta(venta, false);
        return ResponseEntity.ok(createdVenta);
    }

}
