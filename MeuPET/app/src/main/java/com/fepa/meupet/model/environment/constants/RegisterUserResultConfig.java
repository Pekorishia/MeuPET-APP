package com.fepa.meupet.model.environment.constants;

public final class RegisterUserResultConfig {

    // disable instantiation
    private RegisterUserResultConfig(){}

    public static final int REGISTER_SUCCESS = 1;
    public static final int REGISTER_FAILED = 0;
    public static final int INVALID_EMAIL = -1;
    public static final int INVALID_PASSWORD = -2;
    public static final int PASSWORD_MISMATCH = -3;
}
