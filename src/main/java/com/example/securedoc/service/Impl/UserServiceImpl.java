package com.example.securedoc.service.Impl;

import com.example.securedoc.cache.CacheStore;
import com.example.securedoc.domain.RequestContext;
import com.example.securedoc.entity.ConfirmationEntity;
import com.example.securedoc.entity.CredentialEntity;
import com.example.securedoc.entity.RoleEntity;
import com.example.securedoc.entity.UserEntity;
import com.example.securedoc.enumeration.Authority;
import com.example.securedoc.enumeration.EventType;
import com.example.securedoc.enumeration.LoginType;
import com.example.securedoc.event.UserEvent;
import com.example.securedoc.exception.APIException;
import com.example.securedoc.repository.ConfirmationRepository;
import com.example.securedoc.repository.CredentialRepository;
import com.example.securedoc.repository.RoleRepository;
import com.example.securedoc.repository.UserRespository;
import com.example.securedoc.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.example.securedoc.utils.UserUtils.createUserEntity;
import static java.time.LocalDateTime.now;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRespository userRespository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final ApplicationEventPublisher publisher;

    private final CacheStore<String, Integer> userCache;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {

        var userEntity = userRespository.save(createNewUser(firstName,lastName,email));

        var credentialEntity  = new CredentialEntity(userEntity, password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);

        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmationEntity.getKey())));

    }

    @Override
    public RoleEntity getRoleName(String name) {
        var roleEntity = roleRepository.findByNameIgnoreCase(name);
        return roleEntity.orElseThrow(()-> new APIException("Role not found"));
    }

    @Override
    public void verifyAccountToken(String token) {
        var confirmationEntity = getUserConfirmation(token);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRespository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {

        var user = getUserEntityByEmail(email);
        RequestContext.setUserId(user.getId());
        switch ( loginType){
            case LOGIN_ATTEMPT -> {

                if(userCache.get(user.getEmail())==null){
                    user.setLoginAttempts(0);
                    user.setAccountNonLocked(true);
                }
                user.setLoginAttempts(user.getLoginAttempts()+1);
                userCache.put(user.getEmail(), user.getLoginAttempts());

                if (userCache.get(user.getEmail())>5){
                    user.setAccountNonLocked(false);
                }

            }
            case LOGIN_SUCCESS -> {
                user.setLoginAttempts(0);
                user.setAccountNonLocked(true);
                user.setLastLogin(now());
                userCache.evict(user.getEmail());
            }
        }
        userRespository.save(user);
    }

    private UserEntity getUserEntityByEmail(String email) {

        var userEntity =  userRespository.findByEmailIgnoreCase(email);
        return userEntity.orElseThrow(()-> new APIException("User not found"));
    }

    private ConfirmationEntity getUserConfirmation(String token) {
        var confirmationEntity =  confirmationRepository.findByKey(token);
        return confirmationEntity.orElseThrow(()-> new APIException("Incorrect token"));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {

        var role = getRoleName(Authority.USER.name());

        return createUserEntity(firstName, lastName, email, role);
    }
}
