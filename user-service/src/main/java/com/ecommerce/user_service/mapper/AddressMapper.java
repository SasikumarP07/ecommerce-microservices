package com.ecommerce.user_service.mapper;

import com.ecommerce.common_dto.dto.address.AddressRequestDTO;
import com.ecommerce.common_dto.dto.address.AddressResponseDTO;
import com.ecommerce.user_service.entity.Address;
import com.ecommerce.user_service.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Address entities and DTOs.
 */
@Slf4j
public class AddressMapper {

    /**
     * Converts an AddressRequestDTO to an Address entity.
     *
     * @param dto the DTO containing address request data
     * @return the Address entity
     */
    public static Address toEntity(AddressRequestDTO dto) {
        log.debug("Mapping AddressRequestDTO to Address entity: {}", dto);
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPinCode(dto.getPinCode());
        address.setCountry(dto.getCountry());
        log.debug("Mapped Address entity: {}", address);
        return address;
    }

    /**
     * Converts a list of Address entities (from a User) to a list of AddressResponseDTOs.
     *
     * @param user the User entity containing addresses
     * @return list of AddressResponseDTOs
     */
    public static List<AddressResponseDTO> toDTO(User user) {
        log.debug("Mapping User entity's addresses to list of AddressResponseDTOs. User ID: {}", user.getId());
        List<AddressResponseDTO> addressDTOList = user.getAddresses().stream().map(address -> {
            AddressResponseDTO addressDTO = new AddressResponseDTO();
            addressDTO.setId(address.getId());
            addressDTO.setStreet(address.getStreet());
            addressDTO.setCity(address.getCity());
            addressDTO.setState(address.getState());
            addressDTO.setPinCode(address.getPinCode());
            addressDTO.setCountry(address.getCountry());
            return addressDTO;
        }).collect(Collectors.toList());
        log.debug("Mapped AddressResponseDTO list: {}", addressDTOList);
        return addressDTOList;
    }

    /**
     * Converts a single Address entity to an AddressResponseDTO.
     *
     * @param address the Address entity
     * @return the corresponding AddressResponseDTO
     */
    public static AddressResponseDTO toDTO(Address address) {
        log.debug("Mapping Address entity to AddressResponseDTO: {}", address);
        AddressResponseDTO addressDTO = new AddressResponseDTO();
        addressDTO.setId(address.getId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setPinCode(address.getPinCode());
        addressDTO.setCountry(address.getCountry());
        log.debug("Mapped AddressResponseDTO: {}", addressDTO);
        return addressDTO;
    }

    /**
     * Updates an existing Address entity using values from an AddressRequestDTO.
     *
     * @param address the existing Address entity
     * @param dto     the DTO containing updated address data
     * @return the updated Address entity
     */
    public static Address toEntity(Address address, AddressRequestDTO dto) {
        log.debug("Updating existing Address entity with new values from AddressRequestDTO. Old Address: {}, New DTO: {}", address, dto);
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPinCode(dto.getPinCode());
        address.setCountry(dto.getCountry());
        log.debug("Updated Address entity: {}", address);
        return address;
    }
}
