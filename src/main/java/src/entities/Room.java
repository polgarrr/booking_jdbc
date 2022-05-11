package src.entities;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Room {
    private String id;
    private String roomNumber;
    private Integer floor;
    private String roomType;
    private String description;
    private Integer price;
    private List<Booking> bookings;
}
