package com.project.logitrack.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Mappers.ShipmentMapper;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.service.ShipmentService;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {
	@Autowired
    private ShipmentService shipmentService;

    @PostMapping  //didn't try
    public ResponseEntity<ShipmentDto> createShipment(@RequestBody Shipment shipment) {
        Shipment createdShipment = shipmentService.createShipment(shipment);
        ShipmentDto created= ShipmentMapper.toDto(createdShipment);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{trackingId}")  //working
    public ResponseEntity<ShipmentDto> getShipmentByTrackingId(@PathVariable String trackingId) {
        Optional<Shipment> shipmentOpt = shipmentService.getShipmentByTrackingId(trackingId);
        return shipmentOpt.map(ShipmentMapper::toDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")  //working
    public ResponseEntity<ShipmentDto> updateStatus(@PathVariable Long id, @RequestBody String status) {
        Optional<Shipment> updatedShipment = shipmentService.updateShipmentStatus(id, status);
        return updatedShipment.map(ShipmentMapper::toDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/center/{centerId}") //working
    public ResponseEntity<List<ShipmentDto>> getShipmentsByCenter(@PathVariable Long centerId) {
        List<Shipment> shipmentsResult = shipmentService.getShipmentsByCurrentCenter(centerId);
        List<ShipmentDto> shipments= ShipmentMapper.toDtoList(shipmentsResult);
        return ResponseEntity.ok(shipments);
    }
}
