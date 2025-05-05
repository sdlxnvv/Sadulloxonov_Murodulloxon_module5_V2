package uz.murod.entity;

import lombok.*;

import java.sql.Time;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Homework {
    private UUID id;
    private Long chatId;
    private String themeOrDescription;
    private String zipFileId;
    private Integer ball;
    private String teacherFeedback;
    private Time sendTime;
    private Time checkTime;
    private User user;
}
