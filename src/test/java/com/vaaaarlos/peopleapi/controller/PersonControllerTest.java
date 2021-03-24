package com.vaaaarlos.peopleapi.controller;

import static com.vaaaarlos.peopleapi.utils.PersonUtils.asJsonString;
import static com.vaaaarlos.peopleapi.utils.PersonUtils.createFakeDTO;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import com.vaaaarlos.peopleapi.dto.MessageResponseDTO;
import com.vaaaarlos.peopleapi.dto.PersonDTO;
import com.vaaaarlos.peopleapi.exception.PersonNotFoundException;
import com.vaaaarlos.peopleapi.service.PersonService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

  private static final String PEOPLE_API_URL_PATH = "/api/v1/people";

  private MockMvc mockMvc;

  private PersonController personController;

  @Mock
  private PersonService personService;

  @BeforeEach
  void setUp() {
    personController = new PersonController(personService);
    mockMvc = MockMvcBuilders.standaloneSetup(personController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
        .build();
  }

  @Test
  void testWhenPOSTIsCalledThenAPersonShouldBeCreated() throws Exception {
    PersonDTO expectedPersonDTO = createFakeDTO();
    MessageResponseDTO expectedResponseMessage = createMessageResponse(1L, "Created person with ID ");

    when(personService.createPerson(expectedPersonDTO)).thenReturn(expectedResponseMessage);

    mockMvc.perform(post(PEOPLE_API_URL_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(expectedPersonDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
  }

  @Test
  void testWhenGETIsCalledThenReturnAllRegisteredPeopleListShouldBeReturned() throws Exception {
    var expectedValidId = 1L;
    PersonDTO expectedPersonDTO = createFakeDTO();
    expectedPersonDTO.setId(expectedValidId);
    List<PersonDTO> expectedPersonDTOList = Collections.singletonList(expectedPersonDTO);

    when(personService.listAll()).thenReturn(expectedPersonDTOList);

    mockMvc.perform(get(PEOPLE_API_URL_PATH)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].firstName", is("João")))
        .andExpect(jsonPath("$[0].lastName", is("Carlos")));
  }
  
  @Test
  void testWhenGETIsCalledWithValidIdThenAPersonShouldBeReturned() throws Exception {
    var expectedValidId = 1L;
    PersonDTO expectedPersonDTO = createFakeDTO();
    expectedPersonDTO.setId(expectedValidId);

    when(personService.findById(expectedValidId)).thenReturn(expectedPersonDTO);

    mockMvc.perform(get(PEOPLE_API_URL_PATH + "/" + expectedValidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("João")))
        .andExpect(jsonPath("$.lastName", is("Carlos")));
  }
  
  @Test
  void testWhenGETIsCalledWithInvalidIdThenAnErrorMessageShouldBeReturned() throws Exception {
    var expectedInvalidId = 1L;
    PersonDTO expectedPersonDTO = createFakeDTO();
    expectedPersonDTO.setId(expectedInvalidId);

    when(personService.findById(expectedInvalidId)).thenThrow(PersonNotFoundException.class);

    mockMvc.perform(get(PEOPLE_API_URL_PATH + "/" + expectedInvalidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testWhenDELETEIsCalledWithValidIdThenAPersonShouldBeDeleted() throws Exception {
    var expectedValidId = 1L;

    mockMvc.perform(delete(PEOPLE_API_URL_PATH + "/" + expectedValidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
  
  @Test
  void testWhenDELETEIsCalledWithInvalidIdThenAnErrorMessageShouldBeReturned() throws Exception {
    var expectedInvalidId = 1L;

    doThrow(PersonNotFoundException.class).when(personService).deleteById(expectedInvalidId);;

    mockMvc.perform(delete(PEOPLE_API_URL_PATH + "/" + expectedInvalidId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void testWhenPUTIsCalledWithValidIdThenAPersonShouldBeUpdated() throws Exception {
    var expectedValidId = 1L;
    PersonDTO updatePersonDTO = createFakeDTO();
    MessageResponseDTO expectedResponseMessage = createMessageResponse(expectedValidId, "Updated person with ID");
    
    when(personService.updateById(expectedValidId, updatePersonDTO)).thenReturn(expectedResponseMessage);

    mockMvc.perform(put(PEOPLE_API_URL_PATH + "/" + expectedValidId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(updatePersonDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
  }

  @Test
  void testWhenPUTIsCalledWithInvalidIdThenAnErrorMessageShouldBeReturned() throws Exception {
    var expectedInvalidId = 1L;
    PersonDTO updatePersonDTO = createFakeDTO();
    
    when(personService.updateById(expectedInvalidId, updatePersonDTO)).thenThrow(PersonNotFoundException.class);

    mockMvc.perform(put(PEOPLE_API_URL_PATH + "/" + expectedInvalidId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(updatePersonDTO)))
        .andExpect(status().isNotFound());
  }

  private MessageResponseDTO createMessageResponse(long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }

}
