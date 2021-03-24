package com.vaaaarlos.peopleapi.mapper;

import com.vaaaarlos.peopleapi.dto.PersonDTO;
import com.vaaaarlos.peopleapi.entity.Person;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {

  @Mapping(target = "birthDate", source = "birthDate", dateFormat = "dd/MM/yyyy")
  Person toModel(PersonDTO personDTO);

  @Mapping(target = "birthDate", source = "birthDate", dateFormat = "dd/MM/yyyy")
  PersonDTO toDTO(Person person);

}
