/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.recruitment.builder;

import org.springframework.stereotype.Component;

import com.raybiztech.recruitment.business.Address;
import com.raybiztech.recruitment.dto.AddressDTO;

/**
 *
 * @author hari
 */
@Component("addressBuilder")
public class AddressBuilder {
    
    public Address createAddressEntity(AddressDTO addressDTO)
    {
        Address address = null;
        if(addressDTO!=null)
        {
            address = new Address();
            address.setAddress1(addressDTO.getAddress1());
            address.setAddress2(addressDTO.getAddress2());
            address.setAddressId(addressDTO.getAddressId());
            address.setCity(addressDTO.getCity());
            address.setCounty(addressDTO.getCounty());
            address.setState(addressDTO.getState());
            address.setZip(addressDTO.getZip());
           
        }
        return address;
    }
    
    
    
     public AddressDTO createAddressDTO(Address address)
    {
        AddressDTO addressDTO = null;
        if(address!=null)
        {
            addressDTO = new AddressDTO();
            addressDTO.setAddress1(address.getAddress1());
            addressDTO.setAddress2(address.getAddress2());
            addressDTO.setAddressId(address.getAddressId());
            addressDTO.setCity(address.getCity());
            addressDTO.setCounty(address.getCounty());
            addressDTO.setState(address.getState());
            addressDTO.setZip(address.getZip());
           
        }
        return addressDTO;
    }
}
