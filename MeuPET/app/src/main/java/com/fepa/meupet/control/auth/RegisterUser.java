package com.fepa.meupet.control.auth;

import android.util.Patterns;

import com.fepa.meupet.model.environment.enums.RegisterResult;

public final class RegisterUser {

    // disable instantiation
    private RegisterUser(){}

    public static RegisterResult register (String email, String password, String pwdConfirmation){

        if (!validateEmail(email)){
            return RegisterResult.INVALID_EMAIL;
        }

        if (!validatePassword(password)){
            return RegisterResult.INVALID_PASSWORD;
        }

        if (!validatePasswordConfirmation(password,pwdConfirmation)){
            return RegisterResult.PASSWORD_MISMATCH;
        }

        // TODO: Call a DAO to register
        // RegisterUserResultConfig.REGISTER_FAILED

        return RegisterResult.REGISTER_SUCCESS;
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
