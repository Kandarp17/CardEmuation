package com.example.patel_000.cardemuation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {
    Button signup;
    EditText name, email;
    int uid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findAllViews();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i= new Intent(SignUp.this,CardList.class);
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putInt("Uid",uid).commit();
                startActivity(i);*/
               new WebTask().execute();
            }
        });
    }

    private void findAllViews() {
        name=(EditText)findViewById(R.id.nameEditText);
        email=(EditText)findViewById(R.id.emailSignUpEditText);
        signup=(Button)findViewById(R.id.signUpButton);

    }

    private class WebTask extends AsyncTask<String, String, String> {

        ProgressDialog Dialog = null;
        String id=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog = new ProgressDialog(SignUp.this);
            Dialog.setIndeterminate(true);
            Dialog.setCancelable(false);
            Dialog.setTitle("Loading.....");
            Dialog.setMessage(".....Please Wait.....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("ans fro add user: ","Step 1 in");
            RequestPackage request = new RequestPackage();
            request.setUri(getString(R.string.connectionstr)+"user.php/adduser");
            request.setMethod("POST");
            request.setParam("name",name.getText().toString());
            request.setParam("email",email.getText().toString());
            String ans = HttpManager.getData(request);
            Log.e("ans fro add user: ",ans);
            if(ans.equals("error")){
                return ans;
            }else{
                JSONArray arr2= null;

                try {
                    arr2 = new JSONArray(ans);
                    JSONObject obj12 = arr2.getJSONObject(0);
                    id= obj12.getString("LAST_INSERT_ID()");
                    Log.e("response id: ",""+id);
                } catch (JSONException e) {
                    //Toast.makeText(SignUp.this,"Something is Wrong",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                return id;

            }
        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);
            try {
                Dialog.dismiss();
                if(!ans.equals("error")) {
                    Intent i= new Intent(SignUp.this,CardList.class);
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putInt("Uid",Integer.parseInt(id)).commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putBoolean("Login",true).commit();
                    startActivity(i);
                }
                else{
                    Toast.makeText(SignUp.this,"Something is Wrong",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }
}
