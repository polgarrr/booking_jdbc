package src.entities;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Booking {
    private String id;
    private Date checkIn;
    private Date checkOut;
    private User user;
    private Room room;
}
