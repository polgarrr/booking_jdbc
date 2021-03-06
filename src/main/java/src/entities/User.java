package src.entities;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String id;
    private String phone;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private List<Booking> bookings;
}

