package com.project.logitrack.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.Entity.Shipment;
import com.project.logitrack.Entity.UserPrinciple;
import com.project.logitrack.Mappers.ShipmentMapper;
import com.project.logitrack.dto.ShipmentDto;
import com.project.logitrack.dto.UpdateShipmentRequestDto;
import com.project.logitrack.dto.UpdateStatusDto;
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
    public ResponseEntity<ShipmentDto> getShipmentByTrackingId(@PathVariable("trackingId") String trackingId) {
        Optional<Shipment> shipmentOpt = shipmentService.getShipmentByTrackingId(trackingId);
        return shipmentOpt.map(ShipmentMapper::toDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{shipmentId}/status")
    @PreAuthorize("hasAuthority('sub_admin')")
    public ResponseEntity<ShipmentDto> updateShipmentForSubAdmin(
            @PathVariable Long shipmentId,
            @RequestBody UpdateShipmentRequestDto request,
            @AuthenticationPrincipal UserPrinciple currentUser) {
        
        // Note: The 'request' object now only needs to contain the 'status'.
        ShipmentDto updatedShipment = shipmentService.updateShipmentBySubAdmin(shipmentId, request, currentUser);
        return ResponseEntity.ok(updatedShipment);
    }
    
    @GetMapping("/my-center")
    @PreAuthorize("hasAuthority('sub_admin')") // This endpoint is only for sub-admins
    public ResponseEntity<List<ShipmentDto>> getMyCenterShipments(@AuthenticationPrincipal UserPrinciple currentUser) {
        
        Long centerId = currentUser.getUser().getLogisticCenterId().getId();

        List<Shipment> shipmentsResult = shipmentService.getShipmentsByCurrentCenter(centerId);
        
        List<ShipmentDto> shipments = ShipmentMapper.toDtoList(shipmentsResult);
        
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/center/{centerId}") //working
    public ResponseEntity<List<ShipmentDto>> getShipmentsByCenter(@PathVariable Long centerId) {
        List<Shipment> shipmentsResult = shipmentService.getShipmentsByCurrentCenter(centerId);
        List<ShipmentDto> shipments= ShipmentMapper.toDtoList(shipmentsResult);
        return ResponseEntity.ok(shipments);
    }
}
