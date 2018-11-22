package com.fepa.meupet.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.control.auth.Login;
import com.fepa.meupet.model.environment.constants.LoginResultConfig;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.enums.LoginResult;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.etEmail = this.findViewById(R.id.etEmail);
        this.etPassword = this.findViewById(R.id.etPassword);

        this.btLogin = this.findViewById(R.id.btLogin);
    }

    /**
     * Starts the register activity
     * @param view
     */
    public void onRegisterClick(View view) {
        Intent registerIntent = new Intent(getApplicationContext(), RegisterUserActivity.class);
        startActivityForResult(registerIntent, GeneralConfig.REQUEST_CODE);
    }

    /**
     * Calls the login method from Login and generates proper
     * feedback messages based on the login result
     * @param view
     */
    public void onLoginClick(View view) {

        this.btLogin.setEnabled(false);

        // gets the response value from login
        LoginResult response = Login.login(
                this.etEmail.getText().toString(),
                this.etPassword.getText().toString()
        );

        // Deals with email error
        if (response == LoginResult.INVALID_EMAIL){
            this.etEmail.setError(getString(R.string.email_error));
            this.onLoginFailed();
        }
        else{
            // removes the error message if this was not the case
            this.etEmail.setError(null);
        }

        // Deals with password error
        if (response == LoginResult.INVALID_PASSWORD){
            this.etPassword.setError(getString(R.string.pwd_error));
            this.onLoginFailed();
        }
        else{
            // removes the error message if this was not the case
            this.etPassword.setError(null);
        }

        // Deals with login error
        if (response == LoginResult.LOGIN_FAILED){this.onLoginFailed();}

        // if the login was successful
        if (response == LoginResult.LOGIN_SUCCESS){
            this.onLoginSuccess();
        }
    }

    /**
     * Starts the home activity
     * and enable back the login button
     */
    private void onLoginSuccess(){
        this.btLogin.setEnabled(true);

        // starts the home activity
        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeIntent);
        this.finish();
    }

    /**
     * Generates a Toast login error message
     * and enable back the login button
     */
    private void onLoginFailed(){
        Toast.makeText(this, getText(R.string.login_error), Toast.LENGTH_SHORT).show();

        this.btLogin.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GeneralConfig.REQUEST_CODE){
            // if the registration went well
            if (resultCode == RESULT_OK){
                // starts the home activity
                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeIntent);
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back
        moveTaskToBack(true);
    }
}
