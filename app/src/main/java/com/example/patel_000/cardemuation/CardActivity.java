package com.example.patel_000.cardemuation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class CardActivity extends AppCompatActivity {
    String cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Card c=CardLock.GetCardNumber(this);
        cid=c.cardID;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                // User chose the "Settings" item, show the app settings UI...
                new WebTask().execute();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private class WebTask extends AsyncTask<String, String, String> {

        ProgressDialog Dialog = null;
        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog = new ProgressDialog(CardActivity.this);
            Dialog.setIndeterminate(true);
            Dialog.setCancelable(false);
            Dialog.setTitle("Loading.....");
            Dialog.setMessage(".....Please Wait.....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestPackage request = new RequestPackage();

            Log.e("id to remove card",""+cid);
            request.setUri(getString(R.string.connectionstr)+"idcard.php/removeidcard/"+cid);
            request.setMethod("GET");
            String ans = HttpManager.getData(request);

            Log.e("Response remove card",ans);
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
                    Toast.makeText(CardActivity.this,"Something is Wrong",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }
}
