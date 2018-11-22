package com.fepa.meupet.control.auth;

import android.util.Patterns;

import com.fepa.meupet.model.environment.enums.LoginResult;

public final class Login {

    // disable instantiation
    private Login(){}

    public static LoginResult login (String email, String password){

        if (!validateEmail(email)){
            return LoginResult.INVALID_EMAIL;
        }

        if (!validatePassword(password)){
            return LoginResult.INVALID_PASSWORD;
        }

        // TODO: Call a DAO to validade the login
        // LoginResultConfig.LOGIN_FAILED

        return LoginResult.LOGIN_SUCCESS;
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
}
