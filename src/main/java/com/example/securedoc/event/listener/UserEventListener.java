package com.example.securedoc.event.listener;

import com.example.securedoc.event.UserEvent;
import com.example.securedoc.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.example.securedoc.enumeration.EventType.REGISTRATION;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent user){

        switch (user.getType()) {

            case REGISTRATION -> {
                emailService.sendNewAccountEmail(user.getUserEntity().getFirstName(),
                        user.getUserEntity().getEmail(), (String) user.getData().get("key"));
                break;
            }
            case RESETPASSWORD -> {
                emailService.sendPasswordResetEmail(user.getUserEntity().getFirstName(),
                        user.getUserEntity().getEmail(),(String)user.getData().get("key") );
                break;
            }
            default -> {}
        }
    }
}
