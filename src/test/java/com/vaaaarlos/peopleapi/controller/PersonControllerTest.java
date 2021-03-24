package com.vaaaarlos.peopleapi.controller;

import static com.vaaaarlos.peopleapi.utils.PersonUtils.asJsonString;
import static com.vaaaarlos.peopleapi.utils.PersonUtils.createFakeDTO;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vaaaarlos.peopleapi.dto.MessageResponseDTO;
import com.vaaaarlos.peopleapi.dto.PersonDTO;
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

  private MessageResponseDTO createMessageResponse(long id, String message) {
    return MessageResponseDTO.builder().message(message + id).build();
  }

}
