package com.artest.chatapp;

import static com.artest.chatapp.Register.MD5;
import static com.artest.chatapp.Register.SHA256;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {
    TextView register, guestStart;
    EditText username, password;
    Button loginButton;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fill in
        register = (TextView)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);
        guestStart = (TextView)findViewById(R.id.guestStart);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        guestStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Map.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                // Fill in to encode pass

                if (user.equals("")) {
                    username.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else {
                    String url = "https://networkinglab9-3f213-default-rtdb.asia-southeast1.firebasedatabase.app/" + "users.json"; // Change it to your address
                    final ProgressDialog pd = new ProgressDialog(Login.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            // Fill in
                            if(s.equals("null")){
                                Toast.makeText(Login.this,"user not found",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try{
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(obj.getJSONObject(user).getString("password").equals(MD5(pass))){
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        //login success, into map diagram
                                        startActivity(new Intent(Login.this, Users.class));
                                    }
                                    else{
                                        Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e){
                                    e.printStackTrace();
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });
    }
}