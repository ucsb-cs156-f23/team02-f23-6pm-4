package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBDiningCommonsMenuItem;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBDiningCommonsMenuItemController.class)
@Import(TestConfig.class)
public class UCSBDiningCommonsMenuItemControllerTests extends ControllerTestCase {

        @MockBean
        UCSBDiningCommonsMenuItemRepository ucsbDiningCommonsMenuItemRepository;

        @MockBean
        UserRepository userRepository;

        // Tests for GET /api/ucsbdiningcommonsmenuitems/all
        
        @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsbdiningcommonsmenuitems/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/ucsbdiningcommonsmenuitems/all"))
                                .andExpect(status().is(200)); // logged
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_ucsbdiningcommonsmenuitems() throws Exception {

                // arrange
                UCSBDiningCommonsMenuItem item1 = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("1")
                                .name("taco")
                                .station("Mexican")
                                .build();

                UCSBDiningCommonsMenuItem item2 = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("2")
                                .name("waffle")
                                .station("Breakfast")
                                .build();

                ArrayList<UCSBDiningCommonsMenuItem> expectedItems = new ArrayList<>();
                expectedItems.addAll(Arrays.asList(item1, item2));

                when(ucsbDiningCommonsMenuItemRepository.findAll()).thenReturn(expectedItems);

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitems/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbDiningCommonsMenuItemRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedItems);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        // Tests for POST /api/ucsbdiningcommonsmenuitems/post...

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsbdiningcommonsmenuitems/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/ucsbdiningcommonsmenuitems/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_ucsbdiningcommonsmenuitem() throws Exception {
                // arrange
                UCSBDiningCommonsMenuItem item = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("1")
                                .name("taco")
                                .station("Mexican")
                                .build();

                when(ucsbDiningCommonsMenuItemRepository.save(eq(item))).thenReturn(item);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/ucsbdiningcommonsmenuitems/post?diningCommonsCode=1&name=taco&station=Mexican")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemRepository, times(1)).save(item);
                String expectedJson = mapper.writeValueAsString(item);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

       // Tests for GET /api/ucsbdiningcommonsmenuitems?id=...

        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/ucsbdiningcommonsmenuitems?id=7"))
                                .andExpect(status().is(403)); // logged out users can't get by id
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange
                UCSBDiningCommonsMenuItem item = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("1")
                                .name("taco")
                                .station("Mexican")
                                .build();

                when(ucsbDiningCommonsMenuItemRepository.findById(eq(123L))).thenReturn(Optional.of(item));

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitems?id=123"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(ucsbDiningCommonsMenuItemRepository, times(1)).findById(eq(123L));
                String expectedJson = mapper.writeValueAsString(item);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(ucsbDiningCommonsMenuItemRepository.findById(eq(123L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/ucsbdiningcommonsmenuitems?id=123"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(ucsbDiningCommonsMenuItemRepository, times(1)).findById(eq(123L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("UCSBDiningCommonsMenuItem with id 123 not found", json.get("message"));
        }

        // Tests for DELETE /api/ucsbdiningcommonsmenuitems?id=... 

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_delete_a_menu_item() throws Exception {
                // arrange

                UCSBDiningCommonsMenuItem item = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("1")
                                .name("taco")
                                .station("Mexican")
                                .build();


                when(ucsbDiningCommonsMenuItemRepository.findById(eq(15L))).thenReturn(Optional.of(item));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbdiningcommonsmenuitems?id=15")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemRepository, times(1)).findById(15L);
                verify(ucsbDiningCommonsMenuItemRepository, times(1)).delete(any());

                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBDiningCommonsMenuItem with id 15 deleted", json.get("message"));
        }
        
        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_tries_to_delete_non_existant_ucsbdiningcommonsmenuitem_and_gets_right_error_message()
                        throws Exception {
                // arrange

                when(ucsbDiningCommonsMenuItemRepository.findById(eq(15L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/ucsbdiningcommonsmenuitems?id=15")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(ucsbDiningCommonsMenuItemRepository, times(1)).findById(15L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("UCSBDiningCommonsMenuItem with id 15 not found", json.get("message"));
        }

       // Tests for PUT /api/ucsbdiningcommonsmenuitems?id=... 

       @WithMockUser(roles = { "ADMIN", "USER" })
       @Test
       public void admin_can_edit_an_existing_ucsbdiningcommonsmenuitem() throws Exception {
               // arrange

                UCSBDiningCommonsMenuItem itemOrig = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("1")
                                .name("taco")
                                .station("Mexican")
                                .build();

                UCSBDiningCommonsMenuItem itemEdited = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("1")
                                .name("taco")
                                .station("Entrees")
                                .build();

               String requestBody = mapper.writeValueAsString(itemEdited);

               when(ucsbDiningCommonsMenuItemRepository.findById(eq(67L))).thenReturn(Optional.of(itemOrig));

               // act
               MvcResult response = mockMvc.perform(
                               put("/api/ucsbdiningcommonsmenuitems?id=67")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .characterEncoding("utf-8")
                                               .content(requestBody)
                                               .with(csrf()))
                               .andExpect(status().isOk()).andReturn();

               // assert
               verify(ucsbDiningCommonsMenuItemRepository, times(1)).findById(67L);
               verify(ucsbDiningCommonsMenuItemRepository, times(1)).save(itemEdited); // should be saved with correct user
               String responseString = response.getResponse().getContentAsString();
               assertEquals(requestBody, responseString);
       }

       
       @WithMockUser(roles = { "ADMIN", "USER" })
       @Test
       public void admin_cannot_edit_ucsbdiningcommonsmenuitem_that_does_not_exist() throws Exception {
               // arrange

                UCSBDiningCommonsMenuItem itemEdited = UCSBDiningCommonsMenuItem.builder()
                                .diningCommonsCode("1")
                                .name("taco")
                                .station("Entrees")
                                .build();

               String requestBody = mapper.writeValueAsString(itemEdited);

               when(ucsbDiningCommonsMenuItemRepository.findById(eq(67L))).thenReturn(Optional.empty());

               // act
               MvcResult response = mockMvc.perform(
                               put("/api/ucsbdiningcommonsmenuitems?id=67")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .characterEncoding("utf-8")
                                               .content(requestBody)
                                               .with(csrf()))
                               .andExpect(status().isNotFound()).andReturn();

               // assert
               verify(ucsbDiningCommonsMenuItemRepository, times(1)).findById(67L);
               Map<String, Object> json = responseToJson(response);
               assertEquals("UCSBDiningCommonsMenuItem with id 67 not found", json.get("message"));

       }
}
