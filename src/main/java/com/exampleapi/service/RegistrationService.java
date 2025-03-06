package com.exampleapi.service;

import com.exampleapi.entity.Registration;
import com.exampleapi.exception.ResourceNotFound;
import com.exampleapi.payload.RegistrationDto;
import com.exampleapi.repository.RegistrationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RegistrationService(RegistrationRepository registrationRepository, ModelMapper modelMapper, ModelMapper modelMapper1) {
        this.registrationRepository = registrationRepository;
        this.modelMapper = modelMapper;
    }

    public RegistrationDto savaRegistration(RegistrationDto registrationDto) {
//       Registration registration = convertDtoToEntity(registrationDto);
//        Registration registration = new Registration();
//        registration.setName(registrationDto.getName());
//        registration.setEmailId(registrationDto.getEmailId());
//        registration.setMobile(registrationDto.getMobile());
//        Registration saveReg = registrationRepository.save(registration);
        //convert Dto to entity
        Registration registration = convertDtoToEntity(registrationDto);
        Registration saveReg = registrationRepository.save(registration);

        //convert entity to Dto
        RegistrationDto saveRegDto = convertEntityToDto(registration);
//        RegistrationDto saveRegDto = new RegistrationDto();
//        saveRegDto.setName(saveReg.getName());
//        saveRegDto.setEmailId(saveReg.getEmailId());
//        saveRegDto.setMobile(saveReg.getMobile());
        return saveRegDto;

    }


//

    public void deleteRegistration(Long id) {
        registrationRepository.deleteById(id);
    }

    public void updateRegistration(Long id ,Registration registration) {
        //Actual Record fetched from database

        Optional<Registration> opReg = registrationRepository.findById(id);
        Registration reg = opReg.get();
        reg.setName(registration.getName());
        reg.setEmailid(registration.getEmailid());
        reg.setMobile(registration.getMobile());
        registrationRepository.save(reg);

    }


    public List<RegistrationDto> getAllRegistration(int pageNo, int pageSize, String sortBy, String sortDir) {
        //use ternary operator for creating sort object
        Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        //String conditon?val1:val2
       Pageable page = PageRequest.of(  pageNo,  pageSize, sort);
        Page<Registration> records = registrationRepository.findAll(page);
        List<Registration> registrations = records.getContent();
        List<RegistrationDto> registrationDtos = registrations.stream().map(r -> convertEntityToDto(r)).collect(Collectors.toList());
        System.out.println(page.getPageNumber());
        System.out.println(page.getPageSize());
        System.out.println(records.getTotalPages());
        System.out.println(records.getTotalElements());
        System.out.println(records.isFirst());
        System.out.println(records.isLast());
        System.out.println(records.getNumber());
        return registrationDtos;
    }

    public Registration getRegistrationById(Long id) {
//        Registration reg = null;
//        try {
//            Optional<Registration> opReg = registrationRepository.findById(id);
//            reg = opReg.get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return reg;
        Registration registration = registrationRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("Record not found with id :"+id)
        );
        return registration;
    }

    public Registration convertDtoToEntity(
            RegistrationDto registrationDto
    ){

        Registration registration = modelMapper.map(registrationDto,Registration.class);
        return registration;
    }

    public RegistrationDto convertEntityToDto(Registration registration){
        RegistrationDto registrationDto = modelMapper.map(registration, RegistrationDto.class);
        return registrationDto;
    }


}