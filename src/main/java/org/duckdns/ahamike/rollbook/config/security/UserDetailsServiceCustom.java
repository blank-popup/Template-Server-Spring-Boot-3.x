package org.duckdns.ahamike.rollbook.config.security;

import org.duckdns.ahamike.rollbook.config.redis.RedisService;
import org.duckdns.ahamike.rollbook.table.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserDetailsServiceCustom implements UserDetailsService {
    private final RedisService redisService;
    private final ObjectMapper mapper;

    @Override
    public UserDetailsCustom loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username: {}", username);
        Object userObject = redisService.getOValue(username);
        UserEntity user = mapper.convertValue(userObject, UserEntity.class);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new UserDetailsCustom(user);
    }
}
