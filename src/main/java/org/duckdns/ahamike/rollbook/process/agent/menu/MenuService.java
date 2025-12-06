package org.duckdns.ahamike.rollbook.process.agent.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.BusinessException;
import org.duckdns.ahamike.rollbook.config.security.role.RoleRepository;
import org.duckdns.ahamike.rollbook.config.security.user.UserRepository;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.MenuEntity;
import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.duckdns.ahamike.rollbook.table.UserEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MenuMapper menuMapper;

    public GlobalResponse<MenuDomain> registerMenu(MenuDomain request) {
        menuRepository.findById(request.getParentId())
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Parent Menu does not exist"));

        Set<RoleEntity> setRole = roleRepository.findAllByNameIn(request.getRoles());
        if (setRole.size() != request.getRoles().size()) {
            throw new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist");
        }

        MenuEntity requestEntity = menuMapper.toEntity(request);
        MenuEntity responseEntity = menuRepository.save(requestEntity);
        MenuDomain response = menuMapper.toDomain(responseEntity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<List<ResponseGetRecursiveMenu>> getRecursiveMenu(Long userId) {

        // 1. Load user roles
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "User does not exist"));

        Set<String> roleNames = user.getRoles()
                                    .stream()
                                    .map(RoleEntity::getName)
                                    .collect(Collectors.toSet());

        // 2. Load filtered menus
        List<MenuEntity> menus = menuRepository.findMenusByRoles(roleNames);

        // 3. Build hierarchical tree
        List<ResponseGetRecursiveMenu> response = buildMenuTree(menus);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    private List<ResponseGetRecursiveMenu> buildMenuTree(List<MenuEntity> menus) {
        Map<Long, ResponseGetRecursiveMenu> map = new HashMap<>();
        List<ResponseGetRecursiveMenu> roots = new ArrayList<>();

        // Convert to DTOs
        for (MenuEntity m : menus) {
            ResponseGetRecursiveMenu dto = ResponseGetRecursiveMenu.from(m);
            map.put(m.getId(), dto);
        }

        // Build parent-child relations
        for (MenuEntity m : menus) {
            ResponseGetRecursiveMenu dto = map.get(m.getId());

            if (m.getParent() == null) {
                roots.add(dto);
            } else {
                ResponseGetRecursiveMenu parent = map.get(m.getParent().getId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        // sort children
        sortTree(roots);

        return roots;
    }

    private void sortTree(List<ResponseGetRecursiveMenu> nodes) {
        nodes.sort(Comparator.comparing(ResponseGetRecursiveMenu::getOrdering));
        for (ResponseGetRecursiveMenu node : nodes) {
            sortTree(node.getChildren());
        }
    }
}
