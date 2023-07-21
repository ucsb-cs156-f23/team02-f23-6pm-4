package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.models.SystemInfo;
import edu.ucsb.cs156.example.services.SystemInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System Information")
@RequestMapping("/api/systemInfo")
@RestController
public class SystemInfoController extends ApiController {

    @Autowired
    private SystemInfoService systemInfoService;

    @Operation(summary= "Get global information about the application")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public SystemInfo getSystemInfo() {
        return systemInfoService.getSystemInfo();
    }

}