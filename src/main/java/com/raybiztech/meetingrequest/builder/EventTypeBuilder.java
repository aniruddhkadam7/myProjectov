package com.raybiztech.meetingrequest.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.meetingrequest.business.EventType;
import com.raybiztech.meetingrequest.dto.EventTypeDto;

@Component("eventTypeBuilder")
public class EventTypeBuilder {

	public EventType toEntity(EventTypeDto dto) {
		EventType eventType = null;
		if (dto != null) {
			eventType = new EventType();
			eventType.setId(dto.getId());
			eventType.setName(dto.getName());
		}
		return eventType;
	}

	public EventTypeDto toDto(EventType eventType) {
		EventTypeDto eventTypeDto = null;
		if (eventType != null) {
			eventTypeDto = new EventTypeDto();
			eventTypeDto.setId(eventType.getId());
			eventTypeDto.setName(eventType.getName());
		}
		return eventTypeDto;
	}

	public List<EventType> toEntityList(List<EventTypeDto> dtos) {
		List<EventType> eventTypes = null;
		if (dtos != null) {
			eventTypes = new ArrayList<EventType>();
			for (EventTypeDto dto : dtos) {
				eventTypes.add(toEntity(dto));
			}
		}
		return eventTypes;
	}

	public List<EventTypeDto> toDtoList(List<EventType> eventType) {
		List<EventTypeDto> eventTypeDtos = null;
		if (eventType != null) {
			eventTypeDtos = new ArrayList<EventTypeDto>();
			for (EventType type : eventType) {
				eventTypeDtos.add(toDto(type));
			}
		}
		return eventTypeDtos;
	}

}
