package com.governance.visaagent.servicevisaagent.dal;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface VisaRequestMapper {
    VisaRequest toEntity(VisaRequestDto visaRequestDto);

    VisaRequestDto toDto(VisaRequest visaRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    VisaRequest partialUpdate(VisaRequestDto visaRequestDto,
                              @MappingTarget VisaRequest visaRequest);
}