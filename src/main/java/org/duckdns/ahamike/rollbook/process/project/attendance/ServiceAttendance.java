package org.duckdns.ahamike.rollbook.process.project.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.config.security.user.RepositoryUser;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.process.project.tag.RepositoryTag;
import org.duckdns.ahamike.rollbook.process.project.terminal.RepositoryTerminal;
import org.duckdns.ahamike.rollbook.table.EntityAttendance;
import org.duckdns.ahamike.rollbook.table.EntityTag;
import org.duckdns.ahamike.rollbook.table.EntityTerminal;
import org.duckdns.ahamike.rollbook.table.EntityUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceAttendance {
    private final RepositoryAttendance repositoryAttendance;
    private final RepositoryUser repositoryUser;
    private final RepositoryTerminal repositoryTerminal;
    private final RepositoryTag repositoryTag;

    public GlobalResponse<EntityAttendance> saveAttender(RequestAttend request) {
        List<EntityUser> listUser = repositoryUser.findByTag_Name(request.getTag());
        int sizeListUser = listUser.size();
        if (sizeListUser == 0) {
            throw new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "User of tag does not exist");
        } else if (sizeListUser > 1) {
            throw new ExceptionBusiness(ReturnCode.TOO_MANY_DATA, "Two or more user of tag exist");
        }
        List<EntityTerminal> listTerminal = repositoryTerminal.findByName(request.getTerminal());
        int sizeListTerminal = listTerminal.size();
        if (sizeListTerminal == 0) {
            throw new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Terminal does not exist");
        } else if (sizeListTerminal > 1) {
            throw new ExceptionBusiness(ReturnCode.TOO_MANY_DATA, "Two or more terminal exist");
        }
        List<EntityTag> listTag = repositoryTag.findByName(request.getTag());
        int sizeListTag = listTag.size();
        if (sizeListTag == 0) {
            throw new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Tag does not exist");
        } else if (sizeListTag > 1) {
            throw new ExceptionBusiness(ReturnCode.TOO_MANY_DATA, "Two or more tag exist");
        }

        EntityUser user = listUser.get(0);
        EntityTerminal terminal = listTerminal.get(0);
        EntityTag tag = listTag.get(0);
        EntityAttendance entity = new EntityAttendance(user.getId(), terminal.getId(), tag.getId());

        EntityAttendance response = repositoryAttendance.save(entity);

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

        List<ResponseGetAttendance> response = repositoryAttendance.findDailyTagAttendance(idUser, start, end);

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
