package org.duckdns.ahamike.rollbook.config.security.endpoint;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.config.security.role.RoleRepository;
import org.duckdns.ahamike.rollbook.config.security.tenant.TenantRepository;
import org.duckdns.ahamike.rollbook.config.security.tenant.setting.TenantContext;
import org.duckdns.ahamike.rollbook.config.security.user.UserRepository;
import org.duckdns.ahamike.rollbook.table.EndpointEntity;
import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.duckdns.ahamike.rollbook.table.TenantEntity;
import org.duckdns.ahamike.rollbook.table.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EndpointCache {

    private Map<String, EndpointInfo> endpointMap = new ConcurrentHashMap<>();

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private final RequestMappingHandlerMapping handlerMapping;
    
    @Autowired
    private PathPatternParser pathPatternParser;

    public EndpointCache(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void loadAll() {
        createDefaultData();
        scanEndpoints();
        refreshCache();
    }

    public List<EndpointEntity> scanEndpoints() {
        List<EndpointEntity> endpointList = endpointRepository.findAll();
        List<EndpointEntity> newEndpointList = new ArrayList<>();
        List<TenantEntity> tenantList = tenantRepository.findByIsEnabled(true);

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        List<Map.Entry<RequestMappingInfo, HandlerMethod>> sorted = handlerMethods
                .entrySet()
                .stream()
                .sorted(
                    Comparator.comparingInt(
                        (Map.Entry<RequestMappingInfo, HandlerMethod> e) -> Optional.ofNullable(
                                e.getValue()
                                        .getMethod()
                                        .getAnnotation(EndpointOrder.class)
                        )
                        .map(EndpointOrder::value0)
                        .orElse(Integer.MAX_VALUE)
                    ).thenComparingInt(
                        e -> Optional.ofNullable(
                                e.getValue()
                                        .getMethod()
                                        .getAnnotation(EndpointOrder.class)
                        )
                        .map(EndpointOrder::value1)
                        .orElse(Integer.MAX_VALUE)
                    )
                )
                .toList();

        Map<RequestMappingInfo, HandlerMethod> sortedMap = sorted
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
           
        sortedMap.forEach((mapping, handlerMethod) -> {
            Set<String> patterns = extractPaths(mapping);
            Set<RequestMethod> methods = mapping.getMethodsCondition().getMethods();
                                                                          
            for (String path : patterns) {
                for (RequestMethod method : methods) {
                    String[] partPath = ("/contextPath" + path).split("/");
                    if (TenantContext.isBoardPath(partPath) == false) {
                        String endpointName = buildEndpointName(handlerMethod)
                                                .replace("Controller", "");
                        String parameter = buildEndpointParameter(handlerMethod);
                        endpointList.stream()
                                .filter(ep -> endpointName.equals(ep.getName()))
                                .findFirst()
                                .orElseGet(() -> {
                                    EndpointEntity newEndpoint = new EndpointEntity(
                                            endpointName,
                                            method.name(),
                                            path,
                                            parameter
                                    );
                                    newEndpointList.add(newEndpoint);
                                    return newEndpoint;
                                });
                    }
                    else {
                        String tenantBelong = partPath[4];
                        String tenantName = partPath[5];
                        for (TenantEntity tenant : tenantList.stream()
                                                .filter(t -> t.getBelong().equals(tenantBelong))
                                                .collect(Collectors.toList())) {
                            String endpointName = tenant.getName()
                                                + "_"
                                                + buildEndpointName(handlerMethod)
                                                    .replace("Controller", "");
                            String parameter = buildEndpointParameter(handlerMethod);
                            endpointList.stream()
                                    .filter(ep -> endpointName.equals(ep.getName()))
                                    .findFirst()
                                    .orElseGet(() -> {
                                        EndpointEntity newEndpoint = new EndpointEntity(
                                                endpointName,
                                                method.name(),
                                                path.replace(tenantName, tenant.getName()),
                                                parameter,
                                                tenantBelong,
                                                tenant.getName()
                                        );
                                        newEndpointList.add(newEndpoint);
                                        return newEndpoint;
                                    });
                        }
                    }
                }
            }
        });

        if (newEndpointList.size() > 0) {
            log.info("New Endpoints: {}", newEndpointList);
            for (EndpointEntity endpoint : newEndpointList) {
                String roleName = "ROLE_" + endpoint.getName().toUpperCase();
                RoleEntity role = roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            RoleEntity newRole = roleRepository.save(new RoleEntity(roleName));
                            return newRole;
                        });
                endpoint.addRole(role);
            }
            endpointRepository.saveAll(newEndpointList);
            return endpointRepository.findAll();
        }
        else {
            log.info("No New Endpoint");
            return endpointList;
        }
    }

    public Map<String, EndpointInfo> refreshCache() {
        endpointMap.clear();

        List<EndpointEntity> endpoints = endpointRepository.findAll();
        endpointMap = endpoints.stream()
                .collect(Collectors.toMap(
                        EndpointEntity::getName,
                        endpoint -> new EndpointInfo(
                            endpoint.getName(),
                            endpoint.getMethod(),
                            endpoint.getPath(),
                            pathPatternParser.parse(endpoint.getPath()),
                            endpoint.getRoles().stream()
                                    .map(RoleEntity::getName)
                                    .collect(Collectors.toSet())
                        )
                ));
        log.info("CacheEndpoint roleMap refreshed: {}", endpointMap);

        return endpointMap;
    }

    public Set<String> getRoles(String endpointName) {
        EndpointInfo infoEndpoint = endpointMap.get(endpointName);
        return infoEndpoint.getRoles();
    }

    public Map<String, EndpointInfo> getEndpointMap() {
        return endpointMap;
    }

    public void createDefaultData() {
        RoleEntity roleAdmin = createRoleIfNotExists("ROLE_ADMIN");
        RoleEntity roleManager = createRoleIfNotExists("ROLE_MANAGER");
        RoleEntity roleUser = createRoleIfNotExists("ROLE_USER");
        RoleEntity roleCommon = createRoleIfNotExists("ROLE_COMMON");
        RoleEntity roleGuest = createRoleIfNotExists("ROLE_GUEST");
        Set<RoleEntity> adminRoles = Set.of(roleAdmin, roleCommon);
        Set<RoleEntity> managerRoles = Set.of(roleManager, roleCommon);
        Set<RoleEntity> userRoles = Set.of(roleUser, roleCommon);
        Set<RoleEntity> guestRoles = Set.of(roleGuest);
        createUserIfNotExists("admin", "$2a$10$V64Lebxd3FX2D9tE0EVCh.5lZDwCrwJpIlNiv0pMdkZdCZug/pgtq", "admin", adminRoles);
        createUserIfNotExists("manager", "$2a$10$JeqkXaNM9XW.DCYjTPifJ.tp5nb6B9Owx2EZAhiqShWoqvQAqOOhu", "manager", managerRoles);
        createUserIfNotExists("user00", "$2a$10$sHMKXwqnD3wz/MGlS9eBPuWlQGmW8AKAWSuAZddsnOvi/WRH1sF4C", "user00", userRoles);
        createUserIfNotExists("user01", "$2a$10$rbvshCSjJbNtUpAV9I98LulCbEg2Vs2ARgMZWqHF7iw70mnVYvg7G", "user01", userRoles);
        createUserIfNotExists("guest", "$2a$10$inDwapcFo0b.kU/mKM5E2ulsYCgjA9VPEdRaqBN2teShV4RRTuLlm", "guest", guestRoles);
    }

    private RoleEntity createRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity(roleName);
                    return roleRepository.save(newRole);
                });
    }

    private UserEntity createUserIfNotExists(String username, String password, String name, Set<RoleEntity> roles) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity(username, password, null, name, null, null, roles);
                    roles.forEach(role -> newUser.addRole(role));
                    return userRepository.save(newUser);
                });
    }

    private Set<String> extractPaths(RequestMappingInfo mapping) {
        return mapping.getPathPatternsCondition()
                .getPatterns()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    private String buildEndpointName(HandlerMethod handlerMethod) {
        StringBuilder sb = new StringBuilder();

        sb.append(handlerMethod.getBeanType().getSimpleName())
                .append("_")
                .append(handlerMethod.getMethod().getName());

        return sb.toString();
    }

    private String buildEndpointParameter(HandlerMethod handlerMethod) {
        StringBuilder sb = new StringBuilder();

        sb.append("(")
                .append(handlerMethod.getBeanType().getSimpleName())
                .append("#")
                .append(handlerMethod.getMethod().getName())
                .append("(");

        Parameter[] params = handlerMethod.getMethod().getParameters();
        for (int ii = 0; ii < params.length; ++ii) {
            Parameter param = params[ii];
            if (param.isAnnotationPresent(PathVariable.class)) {
                PathVariable pv = param.getAnnotation(PathVariable.class);
                sb.append("path:").append(!pv.value().isEmpty() ? pv.value() : param.getName());
            } else if (param.isAnnotationPresent(RequestParam.class)) {
                RequestParam rp = param.getAnnotation(RequestParam.class);
                sb.append("param:").append(!rp.value().isEmpty() ? rp.value() : param.getName());
            } else if (param.isAnnotationPresent(RequestBody.class)) {
                sb.append("body:").append(param.getType().getSimpleName());
            } else {
                sb.append("arg:").append(param.getType().getSimpleName());
            }

            if (ii < params.length - 1) sb.append(", ");
        }

        sb.append("))");
        return sb.toString();
    }
}
