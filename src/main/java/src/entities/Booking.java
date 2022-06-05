package src.entities;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Booking {
    private String id;
    private Date checkIn;
    private Date checkOut;
    private User user;
    private Room room;
}
