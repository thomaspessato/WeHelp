package com.wehelp.wehelp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wehelp.wehelp.classes.ServiceContainer;
import com.wehelp.wehelp.classes.WeHelpApp;
import com.wehelp.wehelp.controllers.UserController;
import com.wehelp.wehelp.services.IExecuteCallback;
import com.wehelp.wehelp.services.IServiceResponseCallback;
import com.wehelp.wehelp.services.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    public UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((WeHelpApp) getApplication()).getNetComponent().inject(this);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        Button loginBtn = (Button)findViewById(R.id.btn_login);
        Button forgotBtn = (Button)findViewById(R.id.btn_forgot);
        final EditText emailTxt = (EditText)findViewById(R.id.editText);
        final EditText passwordTxt = (EditText)findViewById(R.id.editText2);
        final RelativeLayout loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        assert loadingPanel != null;
        loadingPanel.setVisibility(View.GONE);

        if (loginBtn != null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailTxt.getText().toString();// emailTxt.getText().toString();
                    String password = passwordTxt.getText().toString(); //passwordTxt.getText().toString();
                    loadingPanel.setVisibility(View.VISIBLE);
                    userController.login(email, password,
                            new IServiceResponseCallback() {
                                @Override
                                public void execute(JSONObject response) {
                                    Intent intent = new Intent(LoginActivity.this, TabbedActivity.class);
                                    try {
                                        System.out.println("RESPONSE FULL: "+response);
                                        JSONObject pessoa = response.getJSONObject("pessoa");
                                        intent.putExtra("nome",pessoa.getString("nome"));
                                        intent.putExtra("email",response.getString("email"));
                                        intent.putExtra("pessoa_id",pessoa.getString("pessoa_id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

//                                    intent.putExtra("EXTRA_SESSION_ID", sessionId);
                                    startActivity(intent);
                                    loadingPanel.setVisibility(View.GONE);
                                }
                            },
                            new IExecuteCallback() {
                                @Override
                                public void execute() {
                                    Toast.makeText(getApplicationContext(), "Erro ao logar", Toast.LENGTH_LONG).show();
                                    loadingPanel.setVisibility(View.GONE);
                                }
                            }
                    );
                }
            });
        }


        if (forgotBtn != null) {
            forgotBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                    startActivity(intent);
                }
            });
        }


    }
}
