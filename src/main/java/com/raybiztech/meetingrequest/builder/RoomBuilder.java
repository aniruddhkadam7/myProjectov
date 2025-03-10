/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.meetingrequest.builder;

import com.raybiztech.meetingrequest.business.Location;
import com.raybiztech.meetingrequest.business.Room;
import com.raybiztech.meetingrequest.dao.MeetingRequestDao;
import com.raybiztech.meetingrequest.dto.RoomDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sravani
 */
@Component("roomBuilder")
public class RoomBuilder {

    @Autowired
    LocationBuilder locationBuilder;
    @Autowired
    MeetingRequestDao meetingRequestDaoImpl;

    public Room toEntity(RoomDto roomDto) {
        Room room = null;
        if (roomDto != null) {
            room = new Room();
            room.setId(roomDto.getId());
            room.setLocation(meetingRequestDaoImpl.findBy(Location.class, roomDto.getLocationId()));
            room.setRoomName(roomDto.getRoomName());
            room.setRoomStatus(Boolean.TRUE);


	

        }
        return room;
    }

    public RoomDto toDto(Room room) {
        RoomDto roomDto = null;
        if (room != null) {
            roomDto = new RoomDto();
            roomDto.setId(room.getId());
            roomDto.setLocationId(room.getLocation().getId());
            roomDto.setRoomName(room.getRoomName());
            roomDto.setRoomStatus(room.getRoomStatus());
            roomDto.setLocationName(room.getLocation().getLocationName());
        }
        return roomDto;


    }

    public List<RoomDto> convertEntityListToDtoList(List<Room> roomList) {

        List<RoomDto> roomDtos = null;

        if (roomList != null) {
            roomDtos = new ArrayList<RoomDto>();
            for (Room room : roomList) {
                roomDtos.add(toDto(room));
            }
        }

        return roomDtos;
    }

}
