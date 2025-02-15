package com.example.xeroxapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText email_id, pass_id;
    private Button sign_button_id, reg_button_id;
    private TextView forgot_pass;
    private String Email, password;
    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        setViews();

        sign_button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViews();
                if (validateInputs()) {
                    login();
                }
                //Intent i =new Intent(MainActivity.this,Dashboard_page.class);
                //startActivity(i);
                //validateUser(Email,password);
            }
        });

        reg_button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register_page.class);
                startActivity(intent);
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Dashboard_page.class);
                startActivity(i);
            }
        });

    }


    public void setViews() {
        email_id = findViewById(R.id.main_user_email);
        Email = email_id.getText().toString().trim();

        pass_id = findViewById(R.id.main_password);
        password = pass_id.getText().toString().trim();

        sign_button_id = findViewById(R.id.main_sign_in_button);

        reg_button_id = findViewById(R.id.main_register_button);

        forgot_pass = findViewById(R.id.main_forgot_pass);
    }


    private boolean validateInputs() {
        if (KEY_EMPTY.equals(Email)) {
            email_id.setError("Email cannot be empty");
            email_id.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            pass_id.setError("Password cannot be empty");
            pass_id.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Logging In.. Please wait...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();

    }
        private void login() {
            displayLoader();
            JSONObject request = new JSONObject();
            try {
                //Populate the request parameters
                request.put(KEY_EMAIL, Email);
                request.put(KEY_PASSWORD, password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest stringRequest = new JsonObjectRequest
                    (Request.Method.POST, Constants.LOGIN_URL, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            try {
                                //Check if user got logged in successfully

                                if (response.getInt(KEY_STATUS) == 1) {
                                    //session.loginUser(Email, response.getString(KEY_FULL_NAME));
                                    Intent i = new Intent(MainActivity.this,Dashboard_page.class);
                                    i.putExtra("EMAIL",Email);
                                    startActivity(i);
                                   // overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadeout);

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();

                            //Display error message whenever an error occurs
                            Toast.makeText(getApplicationContext(),
                                    "Error connecting", Toast.LENGTH_SHORT).show();

                        }
                    });
            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
            // Access the RequestQueue through your singleton class.
            //MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        }
    }


