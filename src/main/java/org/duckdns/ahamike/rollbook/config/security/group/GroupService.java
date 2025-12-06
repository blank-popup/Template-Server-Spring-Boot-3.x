package org.duckdns.ahamike.rollbook.config.security.group;

import org.duckdns.ahamike.rollbook.config.response.BusinessException;
import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.response.ReturnCode;
import org.duckdns.ahamike.rollbook.table.GroupEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GlobalResponse<GroupDomain> registerGroup(GroupDomain request) {
        if (groupRepository.existsByName(request.getName()) == true) {
            throw new BusinessException(ReturnCode.DATA_ALREADY_EXISTS, "Group already exists");
        }

        GroupEntity requestEntity = groupMapper.toEntity(request);
        GroupEntity responseEntity = groupRepository.save(requestEntity);
        GroupDomain response = groupMapper.toDomain(responseEntity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<GroupDomain> removeGroup(Long groupId) {
        GroupEntity entity = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Group does not exist"));

        entity.setIsEnabled(false);
        GroupEntity responseEntity = groupRepository.save(entity);
        GroupDomain response = groupMapper.toDomain(responseEntity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }
}
