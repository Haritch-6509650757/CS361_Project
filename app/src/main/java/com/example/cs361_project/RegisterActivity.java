package com.example.cs361_project;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        final Button register_btn = findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveData()){
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        });


    }//end onCreate
    public boolean saveData(){
        final EditText username = findViewById(R.id.username_field);
        final EditText password = findViewById(R.id.password_field);
        final EditText repassword = findViewById(R.id.repassword_field);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.err_title);
        dialog.setPositiveButton(R.string.noti_close, null);

        if(username.getText().length() == 0){
            dialog.setMessage(R.string.noti_input_username);
            dialog.show();
            username.requestFocus();
            return false;
        }

        if(password.getText().length() == 0){
            dialog.setMessage(R.string.noti_input_password);
            dialog.show();
            password.requestFocus();
            return false;
        }

        if(repassword.getText().length() == 0){
            dialog.setMessage(R.string.noti_input_repassword);
            dialog.show();
            repassword.requestFocus();
            return false;
        }

        if (!password.getText().toString().equals(repassword.getText().toString())) {
            dialog.setMessage(R.string.noti_password_mismatch);
            dialog.show();
            repassword.requestFocus();
            return false;
        }

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            dialog.setMessage(R.string.noti_select_role);
            dialog.show();
            radioGroup.requestFocus();
            return false;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("DbVolley", response);
                try {
                    String strStatusID = "0";
                    String strError = "Unknown Status!";
                    JSONObject c;
                    JSONArray data = new JSONArray("["+response.toString()+"]");

                    Log.i("Response : ", data.toString());

                    for(int i = 0; i < data.length(); i++){
                        c = data.getJSONObject(i);
                        Log.i("JSONOBJECT", c.toString());
                        strStatusID = c.getString("id");
                        strError = c.getString("status");
                    }

                    if(strStatusID.equals("0")){
                        dialog.setMessage(strError);
                        dialog.show();
                    } else {
                        dialog.setTitle(R.string.noti_register_success);
                        dialog.setMessage(R.string.noti_register_save);
                        dialog.show();
                        username.setText("");
                        password.setText("");
                        repassword.setText("");
                        radioGroup.clearCheck();
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Submission Error!" ,Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DbVolley", "Volley::onErrorResponse():"+error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("username",username.getText().toString());
                params.put("password",password.getText().toString());
                params.put("job",selectedRadioButton.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        return true;
    }

}
