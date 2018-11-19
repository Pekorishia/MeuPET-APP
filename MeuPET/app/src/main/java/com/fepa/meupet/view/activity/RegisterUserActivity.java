package com.fepa.meupet.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.control.auth.RegisterUser;
import com.fepa.meupet.model.environment.constants.LoginResultConfig;
import com.fepa.meupet.model.environment.constants.RegisterUserResultConfig;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPwd;

    private Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        this.etName = this.findViewById(R.id.etUserName);
        this.etEmail = this.findViewById(R.id.etRegisterEmail);
        this.etPassword = this.findViewById(R.id.etRegisterPwd);
        this.etConfirmPwd = this.findViewById(R.id.etConfirmRegisterPwd);

        this.btRegister = this.findViewById(R.id.btRegisterUser);
    }

    /**
     * Calls the regiter method from RegisterUser and generates proper
     * feedback messages based on the register result
     * @param view
     */
    public void onRegisterClick(View view) {
        this.btRegister.setEnabled(false);

        // gets the response value from register
        int response = RegisterUser.register(
                this.etName.getText().toString(),
                this.etEmail.getText().toString(),
                this.etPassword.getText().toString(),
                this.etConfirmPwd.getText().toString()
        );

        // Deals with email error
        if (response == RegisterUserResultConfig.INVALID_EMAIL){
            this.etEmail.setError(getString(R.string.email_error));
            this.onRegisterFailed();
        }
        else{
            // removes the error message if this was not the case
            this.etEmail.setError(null);
        }

        // Deals with password error
        if (response == RegisterUserResultConfig.INVALID_PASSWORD){
            this.etPassword.setError(getString(R.string.pwd_error));
            this.onRegisterFailed();
        }
        else{
            // removes the error message if this was not the case
            this.etPassword.setError(null);
        }

        // Deals with password mismatch error
        if (response == RegisterUserResultConfig.PASSWORD_MISMATCH){
            this.etConfirmPwd.setError(getString(R.string.password_mismatch_error));
            this.onRegisterFailed();
        }
        else{
            // removes the error message if this was not the case
            this.etConfirmPwd.setError(null);
        }

        // Deals with login error
        if (response == RegisterUserResultConfig.REGISTER_FAILED){this.onRegisterFailed();}

        // if the registration was successful
        if (response == RegisterUserResultConfig.REGISTER_SUCCESS){
           this.onRegisterSuccess();
        }
    }

    /**
     * Goes back to the login activity
     * @param view
     */
    public void onGoToLoginClick(View view) {
        this.finish();
    }

    /**
     * Enables back the registration button
     * and finish the activity sending back RESULT_OK
     */
    private void onRegisterSuccess(){
        this.btRegister.setEnabled(true);
        setResult(RESULT_OK);
        this.finish();
    }

    /**
     * Generates a Toast register error message
     * and enable back the register button
     */
    private void onRegisterFailed(){
        Toast.makeText(this, getText(R.string.register_error), Toast.LENGTH_SHORT).show();

        this.btRegister.setEnabled(true);
    }
}
