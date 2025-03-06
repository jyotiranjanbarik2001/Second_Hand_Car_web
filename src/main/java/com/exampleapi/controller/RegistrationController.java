package com.exampleapi.controller;


import com.exampleapi.entity.Registration;
import com.exampleapi.payload.RegistrationDto;
import com.exampleapi.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    //http://localhost:8080/api/v1/registration

    private RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    //http://localhost:8080/api/v1/registration
    @PostMapping
    public ResponseEntity <RegistrationDto> addRegistration(@RequestBody RegistrationDto registrationDto){
        RegistrationDto r = registrationService.savaRegistration(registrationDto);
        return new ResponseEntity<>(r,HttpStatus.CREATED);
    }


    //http://localhost:8080/api/v1/registration?id=1
    @DeleteMapping
    public String deleteRegistration(@RequestParam Long id){
        registrationService.deleteRegistration(id);
        return "Registration deleted successfully";
    }



    @PutMapping("{id}")
    public String updateRegistration(
            @PathVariable long id,
            @RequestBody Registration registration)
    {
        registrationService.updateRegistration(id,registration);
        return "Registration updated successfully";
    }


    //http://localhost:8080/api/v1/registration?pageNo=0&pageSize=5&sortDir=asc
    @GetMapping
    public ResponseEntity<List<RegistrationDto>> getAllRegistration(
            @RequestParam(defaultValue ="0",required = false)int pageNo,
            @RequestParam(defaultValue="5",required = false) int pageSize,
            @RequestParam(defaultValue="id",required = false) String sortBy, // can be name, email etc.
            @RequestParam(defaultValue="asc",required = false) String sortDir  // can be asc or desc.
    ){
        List<RegistrationDto> dtos = registrationService.getAllRegistration(pageNo, pageSize,sortBy,sortDir);
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<?> getRegistrationById(@PathVariable Long id) throws FileNotFoundException {
        FileReader fr=new FileReader("E://test.txt");
        Registration registration= registrationService.getRegistrationById(id);
        if (registration!=null){
            return new ResponseEntity<>(registration, HttpStatus.OK);
        }
        return new ResponseEntity<>("No Record found for id:"+id, HttpStatus.OK);
    }
}