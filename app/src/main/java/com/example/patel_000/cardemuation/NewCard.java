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

public class NewCard extends AppCompatActivity {
    EditText viewnumber, viewname, viewpassword;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        findAllViews();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebTask().execute();
            }
        });
    }

    private void findAllViews() {
        viewname =(EditText)findViewById(R.id.nameEditText);
        viewnumber =(EditText)findViewById(R.id.numberedittext);
        viewpassword =(EditText)findViewById(R.id.loginPasswordEditText);
        add =(Button) findViewById(R.id.addcardbutton);

    }
    private class WebTask extends AsyncTask<String, String, String> {

        ProgressDialog Dialog = null;
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog = new ProgressDialog(NewCard.this);
            Dialog.setIndeterminate(true);
            Dialog.setCancelable(false);
            Dialog.setTitle("Loading.....");
            Dialog.setMessage(".....Please Wait.....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestPackage request = new RequestPackage();
            request.setUri(getString(R.string.connectionstr)+"idcard.php/addidcard");
            request.setMethod("POST");
            request.setParam("cardname",viewname.getText().toString());
            request.setParam("cardnum",viewnumber.getText().toString());
            request.setParam("password",viewpassword.getText().toString());
            request.setParam("userid",""+getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getInt("Uid",0));
            String ans = HttpManager.getData(request);
            Log.e("response addcard: ",ans);
            return ans;
        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);
            try {
                Dialog.dismiss();
                if(!ans.equals("error")) {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }
                else{
                    Toast.makeText(NewCard.this,"Something is Wrong",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }
}
