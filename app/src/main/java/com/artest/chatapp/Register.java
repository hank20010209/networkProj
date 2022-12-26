package com.artest.chatapp;

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
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import static com.artest.chatapp.Login.yourDatabaseURL;

public class Register extends AppCompatActivity {

    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);

        Firebase.setAndroidContext(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                try {
                    pass = MD5(pass);
                    //pass = SHA256(pass); Example 3.
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (user.equals("")) {
                    username.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else if (!user.matches("[A-Za-z0-9]+")) {
                    username.setError("only alphabet or number allowed");
                } else if (user.length() < 5) {
                    username.setError("at least 5 characters long");
                } else if (pass.length() < 5) {
                    password.setError("at least 5 characters long");
                } else {
                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://networkinglab9-3f213-default-rtdb.asia-southeast1.firebasedatabase.app/" + "users.json"; // Change it to your address

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://networkinglab9-3f213-default-rtdb.asia-southeast1.firebasedatabase.app/" + "users"); // Change it to your address

                            if (s.equals("null")) {
                                // Fill in
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();

                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if (!obj.has(user)) {
                                        // Fill in
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG)
                                                .show();
                                    }

                                } catch (JSONException e) {
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

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data){
            int halfbyte = (b >>> 4) & 0X0F;
            int two_halfs = 0;
            do{
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char)('a' + (halfbyte - 10)));
                halfbyte = b & 0X0F;
            }while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha256hash = md.digest();
        return convertToHex(sha256hash);
    }

    public static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] md5hash = md.digest();
        return convertToHex(md5hash);
    }
}