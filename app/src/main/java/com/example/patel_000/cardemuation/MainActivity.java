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
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
 EditText email;
 Button login, signup;
    private int uid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findAllViews();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i= new Intent(MainActivity.this,CardList.class);
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putInt("Uid",uid).commit();
                startActivity(i);*/

                WebTask w=new WebTask();
                w.execute();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });
    }

    private void findAllViews() {
        email=(EditText)findViewById(R.id.loginEmailEditText);
        login=(Button)findViewById(R.id.signInButton);
        signup=(Button)findViewById(R.id.createAccountButton);
    }
    private class WebTask extends AsyncTask<String, String, String> {

        ProgressDialog Dialog = null;
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog = new ProgressDialog(MainActivity.this);
            Dialog.setIndeterminate(true);
            Dialog.setCancelable(false);
            Dialog.setTitle("Loading.....");
            Dialog.setMessage(".....Please Wait.....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestPackage request = new RequestPackage();
            request.setUri(getString(R.string.connectionstr)+"user.php/getUsers");
            request.setMethod("GET");
            String ans = HttpManager.getData(request);
            try {
                Log.e("Response:",ans);
                JSONArray arr2 = new JSONArray(ans);
                int i=0,flag=1;
                while (i < arr2.length()) {
                    JSONObject obj12 = arr2.getJSONObject(i);
                    String em = obj12.getString("email");
                    Log.e("email: ",""+em);
                    if(em.equals(email.getText().toString())){
                        flag=1;
                        uid= obj12.getInt("id");
                        break;

                    }
                    i++;
                }
                Log.e("uid: ",""+uid);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return ans;
        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);
            try {
                Dialog.dismiss();
                if(uid!=0) {
                    Intent i= new Intent(MainActivity.this,CardList.class);
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putInt("Uid",uid).commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putBoolean("Login",true).commit();
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this,"Enter Valid Email",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }
}
