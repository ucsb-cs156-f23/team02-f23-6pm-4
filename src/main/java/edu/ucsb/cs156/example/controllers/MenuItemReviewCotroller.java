package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.MenuItemReview;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.MenuItemReviewRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;

@Tag(name = "MenuItemReviews")
@RequestMapping("/api/menuitemreviews")
@RestController
@Slf4j
public class MenuItemReviewCotroller extends ApiController{
    
    @Autowired
    MenuItemReviewRepository menuItemReviewRepository;

    @Operation(summary= "List all menu item reviews")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<MenuItemReview> allMenuItemReviews() {
        Iterable<MenuItemReview> reviews = menuItemReviewRepository.findAll();
        return reviews;
    }

    @Operation(summary= "Create a new review")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public MenuItemReview postMenuItemReview(
            @Parameter(name="itemID") @RequestParam long itemID,
            @Parameter(name="reviewerEmail") @RequestParam String reviewerEmail,
            @Parameter(name="stars") @RequestParam int stars,
            @Parameter(name="comments") @RequestParam String comments,
            @Parameter(name="dateReviewed", description="in iso format, e.g. YYYY-mm-ddTHH:MM:SS; see https://en.wikipedia.org/wiki/ISO_8601") @RequestParam("dateReviewed") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateReviewed)
            throws JsonProcessingException {

        // For an explanation of @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        // See: https://www.baeldung.com/spring-date-parameters

        log.info("dateReviewed={}", dateReviewed);

        MenuItemReview menuItemReview = new MenuItemReview();
        menuItemReview.setItemID(itemID);
        menuItemReview.setReviewerEmail(reviewerEmail);
        menuItemReview.setStars(stars);
        menuItemReview.setComments(comments);
        menuItemReview.setDateReviewed(dateReviewed);

        MenuItemReview savedMenuItemReview = menuItemReviewRepository.save(menuItemReview);

        return savedMenuItemReview;
    }

    @Operation(summary= "Get a single review")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public MenuItemReview getById(
            @Parameter(name="id") @RequestParam Long id) {
        MenuItemReview menuItemReview = menuItemReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MenuItemReview.class, id));

        return menuItemReview;
    }

    @Operation(summary= "Delete a MenuItemReview")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteMenuItemReview(
            @Parameter(name="id") @RequestParam Long id) {
        MenuItemReview menuItemReview = menuItemReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MenuItemReview.class, id));

        menuItemReviewRepository.delete(menuItemReview);
        return genericMessage("MenuItemReview with id %s deleted".formatted(id));
    }

    @Operation(summary= "Update a single review")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public MenuItemReview updateMenuItemReview(
            @Parameter(name="id") @RequestParam Long id,
            @RequestBody @Valid MenuItemReview incoming) {

        MenuItemReview menuItemReview = menuItemReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MenuItemReview.class, id));

        menuItemReview.setItemID(incoming.getItemID());
        menuItemReview.setReviewerEmail(incoming.getReviewerEmail());
        menuItemReview.setStars(incoming.getStars());
        menuItemReview.setComments(incoming.getComments());
        menuItemReview.setDateReviewed(incoming.getDateReviewed());

        menuItemReviewRepository.save(menuItemReview);

        return menuItemReview;
    }
}
