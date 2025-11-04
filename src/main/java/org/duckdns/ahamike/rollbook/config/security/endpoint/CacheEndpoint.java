package org.duckdns.ahamike.rollbook.config.security.endpoint;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.table.EntityEndpoint;
import org.duckdns.ahamike.rollbook.table.EntityRole;
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
    private final RequestMappingHandlerMapping handlerMapping;
    @Autowired
    private PathPatternParser pathPatternParser;

    public CacheEndpoint(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void loadAll() {
        scanEndpoints();
        refreshCache();
    }

    public List<EntityEndpoint> scanEndpoints() {
        List<EntityEndpoint> endpoints = repositoryEndpoint.findAll();
        List<EntityEndpoint> newEndpoints = new ArrayList<>();

        handlerMapping.getHandlerMethods().forEach((mapping, handlerMethod) -> {
            Set<String> patterns = extractPaths(mapping);
            Set<RequestMethod> methods = mapping.getMethodsCondition().getMethods();

            for (String path : patterns) {
                for (RequestMethod method : methods) {
                    String parameter = buildEndpointParameter(handlerMethod);
                    String endpointName = method.name() + " " + path + " " + parameter;
                    endpoints.stream()
                            .filter(ep -> endpointName.equals(ep.getName()))
                            .findFirst()
                            .orElseGet(() -> {
                                EntityEndpoint newEndpoint = new EntityEndpoint(endpointName, method.name(), path, parameter);
                                newEndpoints.add(newEndpoint);
                                return newEndpoint;
                            });
                }
            }
        });

        if (newEndpoints.size() > 0) {
            log.info("New Endpoints: {}", newEndpoints);
            repositoryEndpoint.saveAll(newEndpoints);
            return repositoryEndpoint.findAll();
        }
        else {
            log.info("No New Endpoint");
            return endpoints;
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
                                    .collect(Collectors.toList())
                                    // ,(list1, list2) -> {
                                    //     list1.addAll(list2);
                                    //     return list1;
                                    // }
                        )
                ));
        log.info("CacheEndpoint roleMap refreshed: {}", mapEndpointRoles);

        return mapEndpointRoles;
    }

    public List<String> getRoles(String endpointName) {
        InfoEndpoint infoEndpoint = mapEndpointRoles.get(endpointName);
        return infoEndpoint.getRoles();
    }

    public void addRole(String endpointName, String role) {
        List<String> roles = getRoles(endpointName);
        if (roles.stream().anyMatch(r -> r.equals(role)) == false) {
            roles.add(role);
        }
    }

    public void removeRole(String endpointName, String role) {
        List<String> roles = getRoles(endpointName);
        if (roles.stream().anyMatch(r -> r.equals(role)) == true) {
            roles.remove(role);
        }
    }

    public Map<String, InfoEndpoint> getMapEndpointRoles() {
        return mapEndpointRoles;
    }


    private Set<String> extractPaths(RequestMappingInfo mapping) {
        return mapping.getPathPatternsCondition()
                .getPatterns()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
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
