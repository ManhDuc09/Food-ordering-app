package com.example.backend.service;

import com.example.backend.dto.address.AddressDto;
import com.example.backend.model.Address;
import com.example.backend.model.User;
import com.example.backend.repository.AddressRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressDto> getAddresses() {
        return addressRepository.findByUser(currentUser()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressDto addAddress(AddressDto dto) {
        Address address = new Address();
        address.setUser(currentUser());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setIsDefault(false);
        return toDto(addressRepository.save(address));
    }

    @Transactional
    public AddressDto updateAddress(UUID id, AddressDto dto) {
        User user = currentUser();
        Address address = findOwnedAddress(id, user);
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        return toDto(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(UUID id) {
        addressRepository.delete(findOwnedAddress(id, currentUser()));
    }

    @Transactional
    public AddressDto setDefault(UUID id) {
        User user = currentUser();
        Address address = findOwnedAddress(id, user);
        addressRepository.clearDefaultForUser(user);
        address.setIsDefault(true);
        return toDto(addressRepository.save(address));
    }

    private Address findOwnedAddress(UUID id, User user) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Forbidden");
        }
        return address;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private AddressDto toDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setIsDefault(address.getIsDefault());
        return dto;
    }
}
