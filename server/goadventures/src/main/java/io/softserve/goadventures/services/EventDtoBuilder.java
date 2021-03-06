package io.softserve.goadventures.services;

import io.softserve.goadventures.dto.EventDTO;
import io.softserve.goadventures.models.Event;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service //TODO it is not really a DTO object if it is marked as a service. Should be moved..
public class EventDtoBuilder {
    @Autowired
    private ModelMapper modelMapper;

    public Page<EventDTO> convertToDto(Page<Event> event) {
        return modelMapper.map(event, new TypeToken<Page<EventDTO>>() {}.getType());
    }
}
