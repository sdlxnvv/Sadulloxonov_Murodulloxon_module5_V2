package uz.murod.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.murod.bot.UserState;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;
}
