package com.fepa.meupet.control.auth;

import android.util.Patterns;

import com.fepa.meupet.model.environment.constants.LoginResultConfig;
import com.fepa.meupet.model.environment.constants.RegisterUserResultConfig;

public final class RegisterUser {

    // disable instantiation
    private RegisterUser(){}

    public static int register (String name, String email, String password, String pwdConfirmation){

        if (!validateEmail(email)){
            return RegisterUserResultConfig.INVALID_EMAIL;
        }

        if (!validatePassword(password)){
            return RegisterUserResultConfig.INVALID_PASSWORD;
        }

        if (!validatePasswordConfirmation(password,pwdConfirmation)){
            return RegisterUserResultConfig.PASSWORD_MISMATCH;
        }

        // TODO: Call a DAO to register
        // RegisterUserResultConfig.REGISTER_FAILED

        return RegisterUserResultConfig.REGISTER_SUCCESS;
    }

    private static boolean validateEmail(String email){
        if (email == ""){return false;}
        if (email == null) {return false;}
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){return false;}

        return true;
    }

    private static boolean validatePassword(String password){
        if (password == null) {return false;}
        if (password.length() < 4) {return false;}

        return true;
    }

    private static boolean validatePasswordConfirmation(String password, String pwdConfirmation){
        if (!pwdConfirmation.equals(password)) {return false;}

        return true;
    }
}
