package com.sluka.taras.common.mapper;

import com.sluka.taras.common.dto.DescriptionDto;
import com.sluka.taras.common.model.Description;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DescriptionMapper {

    public DescriptionDto toDto(Description description) {
        DescriptionDto descriptionDto = new DescriptionDto();
        descriptionDto.setId(description.getId());
        descriptionDto.setType(description.getType());
        descriptionDto.setValue(description.getValue());
        return descriptionDto;
    }

    public List<DescriptionDto> toDto(List<Description> descriptions) {
        List<DescriptionDto> descriptionDtoList = new ArrayList<>();
        for (Description description : descriptions)
            descriptionDtoList.add(toDto(description));
        return descriptionDtoList;
    }

    public Description toEntity(DescriptionDto descriptionDto) {
        Description description = new Description();
        description.setId(descriptionDto.getId());
        description.setType(descriptionDto.getType());
        description.setValue(descriptionDto.getValue());
        return description;
    }

    public List<Description> toEntity(List<DescriptionDto> descriptionDtoList) {
        List<Description> descriptionList = new ArrayList<>();
        for (DescriptionDto descriptionDto : descriptionDtoList) {
            descriptionList.add(toEntity(descriptionDto));
        }
        return descriptionList;
    }
}
