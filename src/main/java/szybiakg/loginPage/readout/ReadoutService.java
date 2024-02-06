package szybiakg.loginPage.readout;

import org.springframework.stereotype.Service;
import szybiakg.loginPage.employee.Employee;
import szybiakg.loginPage.employee.EmployeeService;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReadoutService {

    private final EmployeeService employeeService;
    private final ReadoutRepository readoutRepository;
    private final ReadoutTypeRepository readoutTypeRepository;

    public ReadoutService(EmployeeService employeeService, ReadoutRepository readoutRepository, ReadoutTypeRepository readoutTypeRepository) {
        this.employeeService = employeeService;
        this.readoutRepository = readoutRepository;
        this.readoutTypeRepository = readoutTypeRepository;
    }

    public boolean addPresence(Integer userId){
        boolean isPresence = false;
        Employee employee = employeeService.findEmployeeByUserId(userId).get();
        Optional<Readout> lastBreakReadoutOptional = getLastReadout(employee, PresenceType.B.name());
        Optional<Readout> lastPresenceReadoutOptional = getLastReadout(employee, PresenceType.P.name());

        if (lastPresenceReadoutOptional.isEmpty() || lastPresenceReadoutOptional.get().getEndTime() != null) {
            Readout newReadout = new Readout();
            newReadout.setEmployee(employee);
            newReadout.setStartTime(LocalDateTime.now());
            newReadout.setReadoutType(readoutTypeRepository.findBySymbol(PresenceType.P.name()).get());
            readoutRepository.save(newReadout);
            isPresence = true;
        } else {
            if(lastBreakReadoutOptional.get().getEndTime() == null)
                throw new RuntimeException("You can't end work when you didn't end brake.");
            Readout lastReadout = lastPresenceReadoutOptional.get();
            lastReadout.setEndTime(LocalDateTime.now());
            readoutRepository.save(lastReadout);
        }
        return isPresence;
    }

    private Optional<Readout> getLastReadout(Employee employee, String presenceType){
        return readoutRepository.findTopByEmployeeAndReadoutType_SymbolOrderByIdDesc(employee, presenceType);
    }

    public boolean addBreak(Integer userId){
        boolean isBreak = false;
        Employee employee = employeeService.findEmployeeByUserId(userId).get();
        Optional<Readout> lastBreakReadoutOptional = getLastReadout(employee, PresenceType.B.name());
        Optional<Readout> lastPresenceReadoutOptional = getLastReadout(employee, PresenceType.P.name());

        if (lastBreakReadoutOptional.isEmpty() || lastBreakReadoutOptional.get().getEndTime() != null) {
            if(lastPresenceReadoutOptional.isEmpty() || lastPresenceReadoutOptional.get().getEndTime() != null)
                throw new RuntimeException("You can't start brake when you didn't start work.");
            Readout newReadout = new Readout();
            newReadout.setEmployee(employee);
            newReadout.setStartTime(LocalDateTime.now());
            newReadout.setReadoutType(readoutTypeRepository.findBySymbol(PresenceType.B.name()).get());
            readoutRepository.save(newReadout);
            isBreak = true;
        } else {
            Readout lastReadout = lastBreakReadoutOptional.get();
            lastReadout.setEndTime(LocalDateTime.now());
            readoutRepository.save(lastReadout);
        }
        return isBreak;
    }

    public boolean getIsBreak(Integer userId){
        Optional<Employee> employee = employeeService.findAuthenticatedEmployeeByEntity();
        if(employee.isEmpty())
            return false;
        Optional<Readout> lastReadoutOptional = getLastReadout(employee.get(), PresenceType.B.name());
        return lastReadoutOptional.filter(readout -> readout.getEndTime() == null).isPresent();
    }

    public boolean getIsPresence(Integer userId){
        Optional<Employee> employee = employeeService.findAuthenticatedEmployeeByEntity();
        if(employee.isEmpty())
            return false;
        Optional<Readout> lastReadoutOptional = getLastReadout(employee.get(), PresenceType.P.name());
        return lastReadoutOptional.filter(readout -> readout.getEndTime() == null).isPresent();
    }

    public Integer getQuantityLates(List<ReadoutDto> presenceList) {
        return (int) presenceList.stream()
                .filter(ReadoutDto::getIsLate)
                .count();
    }


    public String getSumTime(List<ReadoutDto> readoutList) {
        int totalSeconds = readoutList.stream()
                .mapToInt(ReadoutDto::getDurationSecond)
                .sum();

        final int WORKING_HOURS_IN_DAY = 8;

        Duration duration = Duration.ofSeconds(totalSeconds);

        long days = duration.toHours() / WORKING_HOURS_IN_DAY;
        long remainingHours = duration.toHours() % WORKING_HOURS_IN_DAY;
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append(" days ");
        }

        if (remainingHours > 0) {
            result.append(remainingHours).append(" hours ");
        }

        if (minutes > 0) {
            result.append(minutes).append(" minutes ");
        }

        if (seconds > 0) {
            result.append(seconds).append(" seconds");
        }

        return result.toString();
    }

    public List<ReadoutDto> loadReadoutList(String presenceType, Integer employeeId){
        Employee employee;
        if(employeeId == null)
            employee = employeeService.findAuthenticatedEmployeeByEntity().get();
        else
            employee = employeeService.findById(employeeId).get();

        List<Readout> presenceList = readoutRepository.findByEmployeeAndReadoutType_SymbolOrderByIdDesc(employee, presenceType).get();

        return presenceList.stream().map(readout -> {
            ReadoutDto readoutDto = new ReadoutDto();
            readoutDto.setId(readout.getId());
            readoutDto.setStartTime(formatLocalDateTime(readout.getStartTime()));
            readoutDto.setEndTime(formatLocalDateTime(readout.getEndTime()));
            readoutDto.setDuration(formatDuration(readout.getDuration()));
            readoutDto.setDurationSecond((int) readout.getDuration().getSeconds());
            readoutDto.setIsLate(readout.getIsLate());

            return readoutDto;
        }).collect(Collectors.toList());
    }

    public List<ReadoutTypeDto> getListTypeHolidays(){
        List<ReadoutType> allTypes = (List<ReadoutType>) readoutTypeRepository.findAll();
        return allTypes.stream().filter(allType ->
                !allType.getSymbol().equalsIgnoreCase("P")
                        && !allType.getSymbol().equalsIgnoreCase("B"))
                .map(type ->{
            ReadoutTypeDto readoutTypeDto = new ReadoutTypeDto();
            readoutTypeDto.setId(type.getId());
            readoutTypeDto.setSymbol(type.getSymbol());
            readoutTypeDto.setName(type.getName());
            return readoutTypeDto;
                }).collect(Collectors.toList());
    }

    public List<ReadoutDto> loadHolidaysList(Integer employeeId){
        Employee employee;
        if(employeeId == null)
            employee = employeeService.findAuthenticatedEmployeeByEntity().get();
        else
            employee = employeeService.findById(employeeId).get();

        List<Readout> readoutList = readoutRepository.findAllByEmployeeOrderByIdDesc(employee).get();
        return readoutList.stream()
                .filter(readout -> !readout.getReadoutType().getSymbol().equals("P") && !readout.getReadoutType().getSymbol().equals("B"))
                .map(holiday -> {
            ReadoutDto readoutDto = new ReadoutDto();
            readoutDto.setId(holiday.getId());
            readoutDto.setEmployeeId(holiday.getEmployee().getId());
            readoutDto.setStartTime(formatLocalDateTimeHoliday(holiday.getStartTime()));
            readoutDto.setEndTime(formatLocalDateTimeHoliday(holiday.getEndTime()));
            readoutDto.setDuration(calculateWorkdays(holiday.getStartTime(), holiday.getEndTime()));
            readoutDto.setIsApproval(holiday.getIsApproval());
            readoutDto.setNameReadoutType(holiday.getReadoutType().getName());
            return readoutDto;
        }).collect(Collectors.toList());
    }

    public boolean approveHolidays(Integer employeeId){
        Employee employee = employeeService.findById(employeeId).get();
        List<Readout> readoutList = readoutRepository.findAllByEmployeeOrderByIdDesc(employee).get()
                .stream()
                .filter(readout -> !readout.getReadoutType().getSymbol().equals("P") && !readout.getReadoutType().getSymbol().equals("B"))
                .toList();

        for (Readout readout : readoutList) {
            readout.setIsApproval(true);
        }

        readoutRepository.saveAll(readoutList);
        return true;
    }

    public static String calculateWorkdays(LocalDateTime start, LocalDateTime end) {
        long workdays = 0;
        LocalDateTime current = start;

        while (current.compareTo(end) <= 0) {
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY && current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workdays++;
            }
            current = current.plusDays(1);
        }
        return String.valueOf(workdays);
    }

    public void addHoliday(Integer holidayTypeId, LocalDate startDate, LocalDate endDate, boolean isSupervisor){
        LocalDateTime start = startDate.atStartOfDay().withHour(7).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = endDate.atStartOfDay().withHour(15).withMinute(0).withSecond(0).withNano(0);

        if(start.getDayOfMonth() > end.getDayOfMonth())
            throw new RuntimeException("End date can't be earlier than start date.");

        Readout newReadout = new Readout();
        newReadout.setEmployee(employeeService.findAuthenticatedEmployeeByEntity().get());
        newReadout.setStartTime(start);
        newReadout.setEndTime(end);
        newReadout.setReadoutType(readoutTypeRepository.findById(holidayTypeId).get());
        newReadout.setIsApproval(isSupervisor);
        readoutRepository.save(newReadout);
    }

    public static String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    private static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "";
    }

    private static String formatLocalDateTimeHoliday(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }
}
