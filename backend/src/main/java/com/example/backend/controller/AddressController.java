package com.example.backend.controller;

import com.example.backend.dto.address.AddressDto;
import com.example.backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddresses() {
        return ResponseEntity.ok(addressService.getAddresses());
    }

    @PostMapping
    public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto dto) {
        return ResponseEntity.ok(addressService.addAddress(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable UUID id, @RequestBody AddressDto dto) {
        return ResponseEntity.ok(addressService.updateAddress(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<AddressDto> setDefault(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.setDefault(id));
    }
}
