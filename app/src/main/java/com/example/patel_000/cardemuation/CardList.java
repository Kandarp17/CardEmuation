package com.example.patel_000.cardemuation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CardList extends AppCompatActivity {
    ListView list;
    ArrayList<Card> clist=clist = new ArrayList<>();;
    int UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        UID=getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getInt("Uid", 0);
        boolean l=getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("Login", false);
        if(l==false || UID==0){
            Intent in =new Intent(this,MainActivity.class);
            startActivity(in);
        }
        list=(ListView)findViewById(R.id.cardlist);
        callAsynchronousTask();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Intent intent = new Intent(CardList.this, CardActivity.class);
                Toast.makeText(parent.getContext(),"cid: "+ clist.get(position).cardID,Toast.LENGTH_LONG).show();
                CardLock.SetCardNumber(CardList.this,clist.get(position));
                 startActivity(intent);

            }});
    }
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            WebTask wt = new WebTask();
                            wt.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000); //execute in every 30000 ms
    }
    void setList() {
       /*Card c1=new Card("3","hello","123234","hellopass");
        clist.add(c1);
        c1=new Card("4","kandarp","123456789","hellopass");
        clist.add(c1);*/

        String[] setinllist= new String[clist.size()];
        String[] listname = new String[clist.size()];
        String[] listnumber = new String[clist.size()];

        for (int i = 0; i < clist.size(); i++) {
            listname[i] = clist.get(i).name;
            listnumber[i] = clist.get(i).number;
            setinllist[i]= listname[i]+ "\n "+ listnumber[i];
        }

       // setinllist[0]="cjhsfjh";
        Log.e("adapter set",setinllist.toString());
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,setinllist);
        list.setAdapter(adapter);
        Log.e("adapter set","step2");
      //  registerForContextMenu(list);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public void onAddCardAction(){
        Intent i=new Intent(CardList.this,NewCard.class);
        startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addcard:
                onAddCardAction();
                // User chose the "Settings" item, show the app settings UI...
                return true;
            case R.id.logout:
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putBoolean("Login",false).commit();
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putInt("Uid",0).commit();
                finish();
                moveTaskToBack(true);
                // User chose the "Settings" item, show the app settings UI...
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
            clist.clear();
            Dialog = new ProgressDialog(CardList.this);
            Dialog.setIndeterminate(true);
            Dialog.setCancelable(false);
            Dialog.setTitle("Loading.....");
            Dialog.setMessage(".....Please Wait.....");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            RequestPackage request = new RequestPackage();
            request.setUri(getString(R.string.connectionstr)+"idcard.php/getidcardforuser/"+UID);
            request.setMethod("GET");
            String ans = HttpManager.getData(request);
            Log.e("response idbyuser: ",ans);
            try {
                JSONArray arr2 = new JSONArray(ans);
                for (int i = 0; i < arr2.length(); i++) {
                    JSONObject obj12 = arr2.getJSONObject(i);
                    String name = obj12.getString("cardname");
                    String num=obj12.getString("cardNum");
                    String pass=obj12.getString("password");
                    String cid=obj12.getString("idcardID");
                    Card c = new Card(cid, name, num, pass);
                        clist.add(c);
                        Log.e("card in list: ",clist.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return ans;
        }

        @Override
        protected void onPostExecute(String ans) {
            super.onPostExecute(ans);
            Dialog.dismiss();
            setList();
        }
    }

}
