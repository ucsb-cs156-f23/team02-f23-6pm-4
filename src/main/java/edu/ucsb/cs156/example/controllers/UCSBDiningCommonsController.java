package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBDiningCommons;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Tag(name = "UCSBDiningCommons")
@RequestMapping("/api/ucsbdiningcommons")
@RestController
@Slf4j
public class UCSBDiningCommonsController extends ApiController {

    @Autowired
    UCSBDiningCommonsRepository ucsbDiningCommonsRepository;

    @Operation(summary= "List all ucsb dining commons")
    // @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBDiningCommons> allCommonss() {
        Iterable<UCSBDiningCommons> commons = ucsbDiningCommonsRepository.findAll();
        return commons;
    }

    @Operation(summary= "Get a single commons")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public UCSBDiningCommons getById(
            @Parameter(name="code") @RequestParam String code) {
        UCSBDiningCommons commons = ucsbDiningCommonsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommons.class, code));

        return commons;
    }

    @Operation(summary= "Create a new commons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBDiningCommons postCommons(
        @Parameter(name="code") @RequestParam String code,
        @Parameter(name="name") @RequestParam String name,
        @Parameter(name="hasSackMeal") @RequestParam boolean hasSackMeal,
        @Parameter(name="hasTakeOutMeal") @RequestParam boolean hasTakeOutMeal,
        @Parameter(name="hasDiningCam") @RequestParam boolean hasDiningCam,
        @Parameter(name="latitude") @RequestParam double latitude,
        @Parameter(name="longitude") @RequestParam double longitude
        )
        {

        UCSBDiningCommons commons = new UCSBDiningCommons();
        commons.setCode(code);
        commons.setName(name);
        commons.setHasSackMeal(hasSackMeal);
        commons.setHasTakeOutMeal(hasTakeOutMeal);
        commons.setHasDiningCam(hasDiningCam);
        commons.setLatitude(latitude);
        commons.setLongitude(longitude);

        UCSBDiningCommons savedCommons = ucsbDiningCommonsRepository.save(commons);

        return savedCommons;
    }

    @Operation(summary= "Delete a UCSBDiningCommons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteCommons(
            @Parameter(name="code") @RequestParam String code) {
        UCSBDiningCommons commons = ucsbDiningCommonsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommons.class, code));

        ucsbDiningCommonsRepository.delete(commons);
        return genericMessage("UCSBDiningCommons with id %s deleted".formatted(code));
    }

    @Operation(summary= "Update a single commons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public UCSBDiningCommons updateCommons(
            @Parameter(name="code") @RequestParam String code,
            @RequestBody @Valid UCSBDiningCommons incoming) {

        UCSBDiningCommons commons = ucsbDiningCommonsRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommons.class, code));


        commons.setName(incoming.getName());  
        commons.setHasSackMeal(incoming.getHasSackMeal());
        commons.setHasTakeOutMeal(incoming.getHasTakeOutMeal());
        commons.setHasDiningCam(incoming.getHasDiningCam());
        commons.setLatitude(incoming.getLatitude());
        commons.setLongitude(incoming.getLongitude());

        ucsbDiningCommonsRepository.save(commons);

        return commons;
    }
}
