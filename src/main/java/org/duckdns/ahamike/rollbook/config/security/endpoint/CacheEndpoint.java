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

import org.duckdns.ahamike.rollbook.config.security.role.RepositoryRole;
import org.duckdns.ahamike.rollbook.config.security.tenant.RepositoryTenant;
import org.duckdns.ahamike.rollbook.config.security.tenant.setting.TenantContext;
import org.duckdns.ahamike.rollbook.config.security.user.RepositoryUser;
import org.duckdns.ahamike.rollbook.table.EntityEndpoint;
import org.duckdns.ahamike.rollbook.table.EntityRole;
import org.duckdns.ahamike.rollbook.table.EntityTenant;
import org.duckdns.ahamike.rollbook.table.EntityUser;
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
public class CacheEndpoint {

    private Map<String, InfoEndpoint> mapEndpointRoles = new ConcurrentHashMap<>();

    @Autowired
    private RepositoryEndpoint repositoryEndpoint;

    @Autowired
    private RepositoryTenant repositoryTenant;

    @Autowired
    private RepositoryRole repositoryRole;

    @Autowired
    private RepositoryUser repositoryUser;

    private final RequestMappingHandlerMapping handlerMapping;
    
    @Autowired
    private PathPatternParser pathPatternParser;

    public CacheEndpoint(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void loadAll() {
        createDefaultData();
        scanEndpoints();
        refreshCache();
    }

    public List<EntityEndpoint> scanEndpoints() {
        List<EntityEndpoint> listEndpoint = repositoryEndpoint.findAll();
        List<EntityEndpoint> newListEndpoint = new ArrayList<>();
        List<EntityTenant> listTenant = repositoryTenant.findByIsEnabled(true);

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
                        listEndpoint.stream()
                                .filter(ep -> endpointName.equals(ep.getName()))
                                .findFirst()
                                .orElseGet(() -> {
                                    EntityEndpoint newEndpoint = new EntityEndpoint(
                                            endpointName,
                                            method.name(),
                                            path,
                                            parameter
                                    );
                                    newListEndpoint.add(newEndpoint);
                                    return newEndpoint;
                                });
                    }
                    else {
                        String tenantBelong = partPath[4];
                        String tenantName = partPath[5];
                        for (EntityTenant tenant : listTenant.stream()
                                                .filter(t -> t.getBelong().equals(tenantBelong))
                                                .collect(Collectors.toList())) {
                            String endpointName = tenant.getName()
                                                + "_"
                                                + buildEndpointName(handlerMethod)
                                                    .replace("Controller", "");
                            String parameter = buildEndpointParameter(handlerMethod);
                            listEndpoint.stream()
                                    .filter(ep -> endpointName.equals(ep.getName()))
                                    .findFirst()
                                    .orElseGet(() -> {
                                        EntityEndpoint newEndpoint = new EntityEndpoint(
                                                endpointName,
                                                method.name(),
                                                path.replace(tenantName, tenant.getName()),
                                                parameter,
                                                tenantBelong,
                                                tenant.getName()
                                        );
                                        newListEndpoint.add(newEndpoint);
                                        return newEndpoint;
                                    });
                        }
                    }
                }
            }
        });

        if (newListEndpoint.size() > 0) {
            log.info("New Endpoints: {}", newListEndpoint);
            for (EntityEndpoint endpoint : newListEndpoint) {
                String roleName = "ROLE_" + endpoint.getName().toUpperCase();
                EntityRole role = repositoryRole.findByName(roleName)
                        .orElseGet(() -> {
                            EntityRole newRole = repositoryRole.save(new EntityRole(roleName));
                            return newRole;
                        });
                endpoint.addRole(role);
            }
            repositoryEndpoint.saveAll(newListEndpoint);
            return repositoryEndpoint.findAll();
        }
        else {
            log.info("No New Endpoint");
            return listEndpoint;
        }
    }

    public Map<String, InfoEndpoint> refreshCache() {
        mapEndpointRoles.clear();

        List<EntityEndpoint> endpoints = repositoryEndpoint.findAll();
        mapEndpointRoles = endpoints.stream()
                .collect(Collectors.toMap(
                        EntityEndpoint::getName,
                        endpoint -> new InfoEndpoint(
                            endpoint.getName(),
                            endpoint.getMethod(),
                            endpoint.getPath(),
                            pathPatternParser.parse(endpoint.getPath()),
                            endpoint.getRoles().stream()
                                    .map(EntityRole::getName)
                                    .collect(Collectors.toSet())
                        )
                ));
        log.info("CacheEndpoint roleMap refreshed: {}", mapEndpointRoles);

        return mapEndpointRoles;
    }

    public Set<String> getRoles(String endpointName) {
        InfoEndpoint infoEndpoint = mapEndpointRoles.get(endpointName);
        return infoEndpoint.getRoles();
    }

    public Map<String, InfoEndpoint> getMapEndpointRoles() {
        return mapEndpointRoles;
    }

    public void createDefaultData() {
        EntityRole roleAdmin = createRoleIfNotExists("ROLE_ADMIN");
        EntityRole roleManager = createRoleIfNotExists("ROLE_MANAGER");
        EntityRole roleUser = createRoleIfNotExists("ROLE_USER");
        EntityRole roleCommon = createRoleIfNotExists("ROLE_COMMON");
        EntityRole roleGuest = createRoleIfNotExists("ROLE_GUEST");
        Set<EntityRole> adminRoles = Set.of(roleAdmin, roleCommon);
        Set<EntityRole> managerRoles = Set.of(roleManager, roleCommon);
        Set<EntityRole> userRoles = Set.of(roleUser, roleCommon);
        Set<EntityRole> guestRoles = Set.of(roleGuest);
        createUserIfNotExists("admin", "admin", "admin", adminRoles);
        createUserIfNotExists("manager", "manager", "manager", managerRoles);
        createUserIfNotExists("user00", "user00", "user00", userRoles);
        createUserIfNotExists("user01", "user01", "user01", userRoles);
        createUserIfNotExists("guest", "guest", "guest", guestRoles);
    }

    private EntityRole createRoleIfNotExists(String roleName) {
        return repositoryRole.findByName(roleName)
                .orElseGet(() -> {
                    EntityRole newRole = new EntityRole(roleName);
                    return repositoryRole.save(newRole);
                });
    }

    private EntityUser createUserIfNotExists(String username, String password, String name, Set<EntityRole> roles) {
        return repositoryUser.findByUsername(username)
                .orElseGet(() -> {
                    EntityUser newUser = new EntityUser(username, password, null, name, null, null, roles);
                    roles.forEach(role -> newUser.addRole(role));
                    return repositoryUser.save(newUser);
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
