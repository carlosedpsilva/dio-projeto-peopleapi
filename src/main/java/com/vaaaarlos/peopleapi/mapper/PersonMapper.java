package com.vaaaarlos.peopleapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
  
  PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

}
