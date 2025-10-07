package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdmin;
import com.mju.groupware.dto.User;
import com.mju.groupware.util.UserInfoMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

//@ControllerAdvice(basePackages = "com.mju.groupware.controller")
@RequiredArgsConstructor
public class GlobalUserModelAdvice {
    @Autowired(required = false)
    private ConstantAdmin constantAdmin;
    private final UserInfoMethod userInfoMethod;

    @ModelAttribute
    public void injectCurrentUser(Model model, Principal principal) {
        if (principal == null || constantAdmin == null) {
            return;
        }
        User user = new User();
        userInfoMethod.getUserInformation(
                principal,
                user,
                model,
                this.constantAdmin.getSTUDENT(),
                this.constantAdmin.getPROFESSOR(),
                this.constantAdmin.getADMINISTRATOR()
        );
    }
}


