package org.duckdns.ahamike.rollbook.process.project.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.BusinessException;
import org.duckdns.ahamike.rollbook.config.security.user.UserRepository;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.process.project.tag.TagRepository;
import org.duckdns.ahamike.rollbook.process.project.terminal.TerminalRepository;
import org.duckdns.ahamike.rollbook.table.AttendanceEntity;
import org.duckdns.ahamike.rollbook.table.TagEntity;
import org.duckdns.ahamike.rollbook.table.TerminalEntity;
import org.duckdns.ahamike.rollbook.table.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final TerminalRepository terminalRepository;
    private final TagRepository tagRepository;

    public GlobalResponse<AttendanceEntity> saveAttender(RequestAttend request) {
        List<UserEntity> userList = userRepository.findByTag_Name(request.getTag());
        int userListSize = userList.size();
        if (userListSize == 0) {
            throw new BusinessException(ReturnCode.NO_SUCH_DATA, "User of tag does not exist");
        } else if (userListSize > 1) {
            throw new BusinessException(ReturnCode.TOO_MANY_DATA, "Two or more user of tag exist");
        }
        List<TerminalEntity> terminalList = terminalRepository.findByName(request.getTerminal());
        int terminalListSize = terminalList.size();
        if (terminalListSize == 0) {
            throw new BusinessException(ReturnCode.NO_SUCH_DATA, "Terminal does not exist");
        } else if (terminalListSize > 1) {
            throw new BusinessException(ReturnCode.TOO_MANY_DATA, "Two or more terminal exist");
        }
        List<TagEntity> tagList = tagRepository.findByName(request.getTag());
        int tagListSize = tagList.size();
        if (tagListSize == 0) {
            throw new BusinessException(ReturnCode.NO_SUCH_DATA, "Tag does not exist");
        } else if (tagListSize > 1) {
            throw new BusinessException(ReturnCode.TOO_MANY_DATA, "Two or more tag exist");
        }

        UserEntity user = userList.get(0);
        TerminalEntity terminal = terminalList.get(0);
        TagEntity tag = tagList.get(0);
        AttendanceEntity entity = new AttendanceEntity(user.getId(), terminal.getId(), tag.getId());

        AttendanceEntity response = attendanceRepository.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<List<ResponseGetAttendance>> getAttenders(Long idUser, LocalDate from, LocalDate to) {
        LocalDateTime start = getStartOfDay(from);
        LocalDateTime end = getStartOfDay(to).plusDays(1);

        List<ResponseGetAttendance> response = attendanceRepository.findDailyTagAttendance(idUser, start, end);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    private LocalDateTime getStartOfDay(LocalDate date) {
        if (date != null) {
            return date.atStartOfDay();
        }
        else {
            return LocalDateTime.now().toLocalDate().atStartOfDay();
        }
    }
}
