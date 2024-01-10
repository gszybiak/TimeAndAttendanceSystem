package szybiakg.loginPage.messages;

import org.springframework.stereotype.Service;
import szybiakg.loginPage.employee.Employee;
import szybiakg.loginPage.employee.EmployeeDto;
import szybiakg.loginPage.employee.EmployeeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final EmployeeService employeeService;

    public MessageService(MessageRepository messageRepository, EmployeeService employeeService) {
        this.messageRepository = messageRepository;
        this.employeeService = employeeService;
    }
    public List<MessageDto> findMessagesByEmployeeId(Integer senderId, Integer recipientId) {
        List<Message> messages = messageRepository.findMessages(senderId, recipientId);
        return messages.stream()
                .map(message -> {
                    MessageDto messageDto = new MessageDto();
                    messageDto.setId(message.getId());
                    messageDto.setContent(message.getContent());
                    messageDto.setRecipientId(message.getRecipient().getId());
                    messageDto.setSenderId(message.getSender().getId());
                    messageDto.setDate(formatLocalDateTime(message.getDate()));
                    return messageDto;
                }).collect(Collectors.toList());
    }

    public void addMessage(String messageContent, Integer recipientId){
        Optional<EmployeeDto> employee = employeeService.findAuthenticatedEmployee();
        Message message = new Message();
        message.setContent(messageContent);
        message.setRecipient(employeeService.findById(recipientId).get());
        message.setSender(employeeService.findById(employee.get().getId()).get());
        message.setDate(LocalDateTime.now());
        messageRepository.save(message);
    }

    private static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "";
    }
}
