package com.example.patel_000.cardemuation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class CardLock {
    private static final String PREF_CARD_NUMBER = "account_number";
    private static final String DEFAULT_CARD_NUMBER = "00000000";
    private static final String TAG = "AccountStorage";
    private static String sCard = null;
    private static  Card card1 = null;
    private static final Object sCardLock = new Object();
     static boolean flag=false;

    public static void SetCardNumber(Context c, Card card) {
        synchronized(sCardLock) {
            Log.e(TAG, "Setting card number: " + card.cardID);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
            prefs.edit().putString(PREF_CARD_NUMBER, card.cardID).commit();
            sCard = card.cardID;
            card1=card;
            WebTask w= new WebTask();
            w.execute();
        }
    }

    public static Card GetCardNumber(Context c) {
        synchronized (sCardLock) {
            Card card=new Card();
            if (sCard == null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                String cid = prefs.getString(PREF_CARD_NUMBER, DEFAULT_CARD_NUMBER);
                sCard = cid;
            }

            return card1;
        }
    }
    private static class WebTask extends AsyncTask<String, String, String> {

        ProgressDialog Dialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestPackage request = new RequestPackage();
            request.setUri("http://cardemulation.x10host.com/idwallet/slim/public/idcard.php/getidcard/"+sCard);
            request.setMethod("GET");
            String ans = HttpManager.getData(request);
            Log.e("Response card look: ",ans);
            try {
                JSONArray arr=new JSONArray(ans);
                JSONObject obj1 = arr.getJSONObject(0);
                String name = obj1.getString("cardname");
                String num=obj1.getString("cardNum");
                String pass=obj1.getString("password");
                String cid=obj1.getString("idcardID");
               //// card=new Card(cid,name,num,pass);
              //  Log.e("CardLock card info: ",card.toString());
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
                flag=true;
            } catch (Exception e) {

            }
        }
    }
}
