package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBDiningCommons;
import edu.ucsb.cs156.example.entities.UCSBOrganization;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsRepository;
import edu.ucsb.cs156.example.repositories.UCSBOrganizationRepository;

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

import javax.validation.Valid;

@Tag(name = "UCSBOrganization")
@RequestMapping("/api/ucsborganization")
@RestController
@Slf4j

public class UCSBOrganizationController extends ApiController {
    @Autowired
    UCSBOrganizationRepository ucsbOrganizationRepository;

    @Operation(summary= "List all ucsb organizations")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBOrganization> allOrganizations() {
        Iterable<UCSBOrganization> orgs = ucsbOrganizationRepository.findAll();
        return orgs;
    }


    @Operation(summary= "Create a new organization")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBOrganization postOrganization(
        @Parameter(name="orgCode") @RequestParam String orgCode,
        @Parameter(name="orgTranslationShort") @RequestParam String orgTranslationShort,
        @Parameter(name="orgTranslation") @RequestParam String orgTranslation,
        @Parameter(name="inactive") @RequestParam boolean inactive
        )
        {

        UCSBOrganization organizations = new UCSBOrganization();
        organizations.setOrgCode(orgCode);
        organizations.setOrgTranslationShort(orgTranslationShort);
        organizations.setOrgTranslation(orgTranslation);
        organizations.setInactive(inactive);

        UCSBOrganization savedOrganizations = ucsbOrganizationRepository.save(organizations);

        return savedOrganizations;
    }

    @Operation(summary= "Get a single organization")
            @PreAuthorize("hasRole('ROLE_USER')")
            @GetMapping("")
            public UCSBOrganization getById(
                    @Parameter(name="orgCode") @RequestParam String orgCode) {
                UCSBOrganization org = ucsbOrganizationRepository.findById(orgCode)
                        .orElseThrow(() -> new EntityNotFoundException(UCSBOrganization.class, orgCode));
        
                return org;
            }

    @Operation(summary= "Update a single organization")
            @PreAuthorize("hasRole('ROLE_ADMIN')")
            @PutMapping("")
            public UCSBOrganization updateOrg(
                @Parameter(name="orgCode") @RequestParam String orgCode,
                @RequestBody @Valid UCSBOrganization incoming) {
        
            UCSBOrganization org = ucsbOrganizationRepository.findById(orgCode)
                    .orElseThrow(() -> new EntityNotFoundException(UCSBOrganization.class, orgCode));
        
        
            org.setOrgCode(incoming.getOrgCode());  
            org.setOrgTranslationShort(incoming.getOrgTranslationShort());
            org.setOrgTranslation(incoming.getOrgTranslation());
            org.setInactive(incoming.getInactive());
        
            ucsbOrganizationRepository.save(org);
        
            return org;
            } 


}
