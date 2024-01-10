package szybiakg.loginPage.messages;

import lombok.Data;

@Data
public class MessageDto {
    private Integer id;
    private String content;
    private Integer recipientId;
    private Integer senderId;
    private String date;
}
