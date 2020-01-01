package com.example.qiang.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.example.qiang.R;
import com.example.qiang.entity.Mainlottery;
import com.example.qiang.fireWorks.Firework;
import com.example.qiang.fireWorks.FireworkView;
import com.example.qiang.gson.Note;
import com.example.qiang.gson.Winners;
import com.example.qiang.http.HttpUtil;
import com.example.qiang.tool.CustomDialog;
import com.example.qiang.util.ToastUtil;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LetteryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LetteryActivity";
    private List<String> datas = new ArrayList<>();
    private List<String> winnners = new ArrayList<>();
    private boolean running;
    private boolean wasrunning;
    private boolean flag =false;
    private int temp1 ;
    private int count = 0;
    private int level ;
    private TextView award1;
    private TextView award2;
    private TextView award3;
    private Button AddItem;
    private Button searchData;
  private Button ClearData;
  private Button Lettery;
    private Button chushihua;
  private EditText editText;
  private TextView WinningList;
  private TextView textView;
    private TextView textView1;
    private TextView textView2;
  private TextView Winner1;
  private TextView Winner2;
   private Winners win;
  private   String[] data;
  private   String temp[];
  private TextView Winner3;
  private FireworkView fv;
    protected ActionBar mActionBar;
   private List<Integer> tempnumber = new ArrayList<>();
    private Mainlottery note;
    final Handler handler = new Handler();
    Thread myThread;
    Boolean flagadd;
    private Long noteId;
    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lettery);
        initdata();
        Log.d(TAG, "onCreate: ");
////          data= new String[]{"Fans","Minard","Stephen zheng","蘇振輝","陳世豪","Ace Lai", "曾國清","Loki","Morgan","Henry","Henk", "Elva", "Jay", "Sophia", "Jeff", "Carrie"
////                  , "Rio", "Brian","Ann","Cila","Alice", "Tony", "Hardy",
////                  "Danny", "Andrew", "Rookie","George", "Alan", "May","Maxine", "Martin", "向佳佳"
////                  , "Janson wang", "Snow" , "总经办", "Andy zhang","Harvey chang","Lina huang"
//       };
//        datas.addAll(Arrays.asList(data));
//      startLettery();
        win = new Winners();
        searchData = findViewById(R.id.searchData);
        editText = findViewById(R.id.addData);
        textView = findViewById(R.id.DataView);
        textView1= findViewById(R.id.DataView1);
        textView2= findViewById(R.id.DataView2);
        chushihua = findViewById(R.id.chushihua);
        AddItem = findViewById(R.id.Add);
        ClearData = findViewById(R.id.clearData);
        Lettery = findViewById(R.id.Start);
        Winner1 = findViewById(R.id.Winner1);
        Winner2 = findViewById(R.id.Winner2);
        Winner3 = findViewById(R.id.Winner3);
        WinningList = findViewById(R.id.zhongjiang);
        award1 = findViewById(R.id.First);
        award2 = findViewById(R.id.Second);
        award3 = findViewById(R.id.Third);
        fv = findViewById(R.id.fv);
        running = false;
        AddItem.setOnClickListener(this);
        ClearData.setOnClickListener(this);
        chushihua.setOnClickListener(this);
        Lettery.setOnClickListener(this);
        searchData.setOnClickListener(this);
        WinningList.setOnClickListener(this);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1);
        }
        editText.setVisibility(View.GONE);
        findViewById(R.id.close).setVisibility(View.GONE);
        if (noteId!=null) {
            loadNote();
        }
        myThread = new Thread(){
            @Override
            public void run() {
                int[] random = new int[tempnumber.get(level)];
                Set<Integer> set=new LinkedHashSet();
                boolean panduan;
                while(true){
                    int z=(int)(Math.random()*datas.size());
                    panduan=set.add(z);
                    if(!panduan){
                        continue;
                    }
                    if(set.size()>=tempnumber.get(level)){
                        break;
                    }
                }
                Object[] temp = set.toArray();
                for(int i = 0;i<temp.length;i++) {
                    random[i] = (int)temp[i];
                }
                if (running && (count < 200)) {
                    count++;
                    Log.d(TAG, "run: "+random.length);
                    textView.setText(datas.get(random[0]));
                    if (random.length>1){
                    textView1.setText(datas.get(random[1]));
                        if (random.length>2){
                            textView2.setText(datas.get(random[2]));}
                    }

                    handler.postDelayed(this,10);
                }else {
                    wasrunning = false;
                }
                if (running && (count == 200)) {
                    wasrunning=false;
                    if (level==2){
                        if (tempnumber.get(2)==3){
                        Winner1.setText(textView.getText().toString()+"  "+textView1.getText().toString()+"  "+textView2.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            winnners.add(textView.getText().toString());
                            winnners.add(textView1.getText().toString());
                            winnners.add(textView2.getText().toString());

                        datas.remove(textView.getText().toString());
                        datas.remove(textView1.getText().toString());
                        datas.remove(textView2.getText().toString());}
                            if (tempnumber.get(2)==2) {
                                textView2.setVisibility(View.GONE);
                                Winner1.setText(textView.getText().toString() + "  " + textView1.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                                winnners.add(textView.getText().toString());
                                winnners.add(textView1.getText().toString());

                                datas.remove(textView.getText().toString());
                                datas.remove(textView1.getText().toString());
                            }
                            if (tempnumber.get(2)==1) {
                                Winner1.setText(textView.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                                winnners.add(textView.getText().toString());

                                datas.remove(textView.getText().toString());
                            }
                            fv.setFireworkCount(2);
                            fv.showFirework();
//                        if (tempnumber.get(1)==3){
//                            textView2.setVisibility(View.VISIBLE);
//                            textView1.setVisibility(View.VISIBLE);}
//                        if (tempnumber.get(1)==2) {
//                            textView1.setVisibility(View.VISIBLE);
//                            textView2.setVisibility(View.GONE);}
//                        if (tempnumber.get(1)==1) {
//                            textView1.setVisibility(View.GONE);
//                            textView2.setVisibility(View.GONE);}
                        level--;
                    }
                    else if (level==1){
                        fv.setFireworkCount(3);
                        fv.showFirework();
                        if (tempnumber.get(1)==3){
                            Winner2.setText(textView.getText().toString()+"  "+textView1.getText().toString()+"  "+textView2.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            winnners.add(textView.getText().toString());
                            winnners.add(textView1.getText().toString());
                            winnners.add(textView2.getText().toString());

                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                            datas.remove(textView2.getText().toString());}
                        if (tempnumber.get(1)==2) {
                            Winner2.setText(textView.getText().toString() + "  " + textView1.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            winnners.add(textView.getText().toString());
                            winnners.add(textView1.getText().toString());

                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                        }
                        if (tempnumber.get(1)==1) {
                            Winner2.setText(textView.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            winnners.add(textView.getText().toString());

                            datas.remove(textView.getText().toString());
                        }
                        level--;
                    }else if (level==0){
                        fv.setFireworkCount(5);
                        fv.showFirework();
                        if (tempnumber.get(0)==3){
                            Winner3.setText(textView.getText().toString()+"  "+textView1.getText().toString()+"  "+textView2.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            winnners.add(textView.getText().toString());
                            winnners.add(textView1.getText().toString());
                            winnners.add(textView2.getText().toString());

                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                            datas.remove(textView2.getText().toString());}
                        if (tempnumber.get(0)==2) {
                            Winner3.setText(textView.getText().toString() + "  " + textView1.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            winnners.add(textView.getText().toString());
                            winnners.add(textView1.getText().toString());
                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                        }
                        if (tempnumber.get(0)==1) {
                            Winner3.setText(textView.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            winnners.add(textView.getText().toString());
                            datas.remove(textView.getText().toString());
                        }
                        level=tempnumber.size()-1;
//                        textView1.setVisibility(View.VISIBLE);
//                        textView2.setVisibility(View.VISIBLE);
                        WinningList.setVisibility(View.VISIBLE);
                        win.setNoteid(Integer.valueOf(noteId.toString()));
                        win.setWinjson(Joiner.on(",").join(winnners));
                        note.setWinjson(win.getWinjson());
                        Gson gson = new Gson();
                        JSONObject jsonObject = JSONObject.parseObject(gson.toJson(note));
                        HttpUtil.sendOkHttpRequestJson(HttpUtil.BASE_URL + "lottery/mlotterysave.do",jsonObject, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                            }
                        });
//                        if (DataSupport.where("noteid=?",noteId.toString()).find(Winners.class).size()!=0){
//                            Winners winners1 = new Winners();
//                            winners1.setWinjson(win.getWinjson());
//                            winners1.updateAll("noteid=?",noteId.toString());
//                        }else {
//                            Winners winners1 = new Winners();
//                        winners1.setNoteid(win.getNoteid());
//                        winners1.setWinjson(win.getWinjson());
//                        winners1.save();
//                        }
                        if (!note.isEndless()){
                            datas.addAll(winnners);
                        }
                        Toast.makeText(LetteryActivity.this,"抽奖完毕", Toast.LENGTH_SHORT).show();

                    }
                    handler.removeCallbacks(this);
                    Lettery.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "一直在运行的可怜人");
            }
        };}


    private void setNote(Mainlottery note) {
        this.note = note;
        Log.d(TAG, "setNote: "+note.isEndless());
        if (note.getPeoplefirst()!=0){
            tempnumber.add(note.getPeoplefirst());
        }
        if (note.getPeoplesecond()!=0){
            tempnumber.add(note.getPeoplesecond());
        }else {
            findViewById(R.id.Second).setVisibility(View.INVISIBLE);
            Winner2.setVisibility(View.INVISIBLE);
        }
        if (note.getPeoplethird()!=0){
            tempnumber.add(note.getPeoplethird());
        }else {
            findViewById(R.id.First).setVisibility(View.INVISIBLE);
            Winner1.setVisibility(View.INVISIBLE);
        }
        level = tempnumber.size()-1;
        setTitle(note.getTitle());
        award1.setText(note.getAward3());
        award1.append(":");
        award2.setText(note.getAward2());
        award2.append(":");
        award3.setText(note.getAward1());
        award3.append(":");
        temp1=level;
    }

    private void loadNote(){
        Map<String,String> param = new HashMap<String,String>();
        param.put("id",noteId.toString());

        HttpUtil.sendOkHttpRequestText(HttpUtil.BASE_URL + "lottery/findmlotterybyid.do", param, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final Mainlottery note = new Gson().fromJson(response.body().string(),Mainlottery.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setNote(note);
                    }
                });

            }
        });
//        new AsyncTask<Void, Void, Note>() {
//            @Override
//            protected Note doInBackground(Void... longs) {
//                List<Note> noteList= DataSupport.where("id=?",noteId.toString()).find(Note.class);
//                Note note = noteList.get(0);
//                Log.d(TAG, "onPostExecute: "+note.toString());
//                return note;
//            }
//            @Override
//            protected void onPostExecute(Note note) {
//                setNote(note);
//            }
//        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lettery, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String,String> param = new HashMap<String, String>();
        param.put("id",noteId.toString());
        HttpUtil.sendOkHttpRequestText(HttpUtil.BASE_URL + "lottery/findmlotterybyid.do", param, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final Mainlottery note = new Gson().fromJson(response.body().string(),Mainlottery.class);
                if (!note.getWinjson().equals("")){
                temp = note.getWinjson().split(",");
                winnners.clear();
                winnners .addAll( Arrays.asList(temp));
                    datas.removeAll(winnners);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WinningList.setVisibility(View.VISIBLE);
                        }
                    });
            }

            }
        });
        }
//        DataSupport.deleteAll(Winners.class,"noteid=?",noteId.toString());




    /* startThread = new Thread(){
         @Override
         public void run() {

             handler.post(myThread);
             Log.d(TAG, "run: "+Thread.currentThread().getName());
         }
     };*/




   /* private void startLettery() {

        *//*new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
*//*
        myThread = new Thread(){
            @Override
            public void run() {{

                            Winners winners1 = new Winners();
                            winners1.setWinjson(win.getWinjson());
                            winners1.updateAll("noteid=?",noteId.toString());
                        }
                int randomInt = (int) (Math.random() * datas.size());
                if (running && (count < 200)) {
                    count++;
                    textView.setText(datas.get(randomInt));
                    wasrunning = true;
                    handler.postDelayed(this,10);
                }else {
                    wasrunning = false;
                }
                if (running && (count == 200)) {
                    if (level==0){
                    Winner1.setText(textView.getText().toString());
                    datas.remove(textView.getText().toString());
                    level++;
                    }
                    else if (level==1){
                        Winner2.setText(textView.getText().toString());
                        datas.remove(textView.getText().toString());
                        level++;
                    }else if (level==2){
                        Winner3.setText(textView.getText().toString());
                        level=0;
                        Toast.makeText(LetteryActivity.this,"抽奖完毕",Toast.LENGTH_SHORT).show();
                    }
                    handler.removeCallbacks(this);
                }
                Log.d(TAG, "一直在运行的可怜人");

            }
        };
    }*/

    public void initdata(){

//       String address = "http://10.132.212.167:8080/bird/lottery/findlotteryp.do";
       HttpUtil.sendOkHttpRequest(HttpUtil.BASE_URL+"lottery/findlotteryp.do",new Callback() {
           @Override
           public void onFailure(@NotNull Call call, @NotNull IOException e) {
           }

           @Override
           public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
               datas.clear();
                datas.addAll(Arrays.asList(response.body().string().trim().split(",")));
                if (winnners.size()!=0){
                    datas.removeAll(winnners);
                    WinningList.setVisibility(View.VISIBLE);
                }
           }
       });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
           if (flag){
               editText.setVisibility(View.GONE);
//               AddItem.setVisibility(View.INVISIBLE);
//               ClearData.setVisibility(View.INVISIBLE);
               findViewById(R.id.close).setVisibility(View.GONE);
               flag= false;
           }else {
               editText.setVisibility(View.VISIBLE);
               findViewById(R.id.close).setVisibility(View.VISIBLE);
               flag= true;
           }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chushihua:
                note.setWinjson(null);
                Gson gson = new Gson();
                JSONObject json = JSONObject.parseObject(gson.toJson(note));
                HttpUtil.sendOkHttpRequestJson(HttpUtil.BASE_URL + "lottery/mlotterysave.do", json, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                        if (jsonObject.getBoolean("errres")){
                            datas.addAll(winnners);
                            winnners.clear();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showMsg(LetteryActivity.this,"OK");
                                }
                            });

                        }
                    }
                });

//                if (DataSupport.deleteAll(Winners.class)!=0){
//                    ToastUtil.showMsg(LetteryActivity.this,"OK");
//                    datas.addAll(winnners);
//                    winnners.clear();
//                }

                break;
            case R.id.zhongjiang:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(note.getTitle());
                StringBuilder stringBuilder = new StringBuilder();
                    int i=0,j=0,k=0;
                for(String a :winnners){
                    if (note.getPeoplethird()!=0&&j<note.getPeoplethird()){

                            if (j % note.getPeoplethird() == 0 && j != 0) {
                                stringBuilder.append("\r\n");
                            }
                            if (j % note.getPeoplethird() == 0) {
                                stringBuilder.append(note.getAward3() + ":   ");
                            }
                            stringBuilder.append(a.toString()+"     ");
                            j++;
                        if(j==note.getPeoplethird()){
                            stringBuilder.append("\r\n");
                        }

                    }else if(note.getPeoplesecond()!=0&&k<note.getPeoplesecond()){

                            if (k%note.getPeoplesecond()==0&&k!=0){
                                stringBuilder.append("\r\n");
                            }
                            if (k%note.getPeoplesecond()==0){
                                stringBuilder.append(note.getAward2()+":   ");}
                            stringBuilder.append(a.toString()+"     ");
                            k++;
                        if(k==note.getPeoplesecond()){
                            stringBuilder.append("\r\n");
                        }

                    }else {
                    if (i%note.getPeoplefirst()==0&&i!=0){
                        stringBuilder.append("\r\n");
                    }
                    if (i%note.getPeoplefirst()==0){
                        stringBuilder.append(note.getAward1()+":   ");}

                        stringBuilder.append(a.toString()+"     ");
                    i++;
                }}
                builder1.setMessage(
                    stringBuilder.toString()
                );
                builder1.setCancelable(true);
                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder1.show();
                break;
            case R.id.searchData:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("名单");
                builder.setMessage(datas.toString());
                builder.setCancelable(true);
                builder.setPositiveButton("人数:" + datas.size(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                break;
            case R.id.Add:
                if(!wasrunning && !editText.getText().toString().matches("")){

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("lotteryname",editText.getText().toString().trim() );
                    HttpUtil.sendOkHttpRequestJson(HttpUtil.BASE_URL + "lottery/lotterysave.do", jsonObject, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                         String result = response.body().string();
                            JSONObject json = JSONObject.parseObject(result);
                            flagadd = (Boolean) json.get("errres");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run(){
                                    if (flagadd){
                                        datas.add(editText.getText().toString().trim());
                                        Toast.makeText(LetteryActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(LetteryActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });

                }
                break;
            case R.id.clearData:
                if(!wasrunning) {
                      Map<String, String> params = new HashMap<String, String>();
                      params.put("names", editText.getText().toString().trim());
                      HttpUtil.sendOkHttpRequestText(HttpUtil.BASE_URL + "lottery/lotterydelete.do", params, new Callback() {
                          @Override
                          public void onFailure(@NotNull Call call, @NotNull IOException e) {
                          }

                          @Override
                          public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                              String result = response.body().string();
                              JSONObject json = JSONObject.parseObject(result);
                              flagadd = (Boolean) json.get("errres");
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      if (flagadd){
                                          datas.remove(editText.getText().toString());
                                          Toast.makeText(LetteryActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                      }else {
                                          Toast.makeText(LetteryActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              });

                          }
                      });


                }
                break;
            case R.id.Start:
                if (!wasrunning){
                if(datas.size()>=note.getTotalpeople()){
                    count = 0;
                    running = true;
                    wasrunning = true;
                    Lettery.setVisibility(View.INVISIBLE);

                    if (temp1==level&&!note.isEndless()){

                        winnners.clear();
                    }
                    if (level==2){
                        if (tempnumber.get(2)==3){
                            textView2.setVisibility(View.VISIBLE);
                            textView1.setVisibility(View.VISIBLE);}
                        if (tempnumber.get(2)==2) {
                            textView1.setVisibility(View.VISIBLE);
                            textView2.setVisibility(View.GONE);}
                        if (tempnumber.get(2)==1) {
                            textView1.setVisibility(View.GONE);
                            textView2.setVisibility(View.GONE);}
                    }
                  if (level==1){
                    if (tempnumber.get(1)==3){
                        textView2.setVisibility(View.VISIBLE);
                        textView1.setVisibility(View.VISIBLE);}
                    if (tempnumber.get(1)==2) {
                        textView1.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.GONE);}
                    if (tempnumber.get(1)==1) {
                        textView1.setVisibility(View.GONE);
                        textView2.setVisibility(View.GONE);}}

                  if (level==0) {
                      if (tempnumber.get(0) == 3) {
                          textView2.setVisibility(View.VISIBLE);
                          textView1.setVisibility(View.VISIBLE);
                      }
                      if (tempnumber.get(0) == 2) {
                          textView1.setVisibility(View.VISIBLE);
                          textView2.setVisibility(View.GONE);
                      }
                      if (tempnumber.get(0) == 1) {
                          textView1.setVisibility(View.GONE);
                          textView2.setVisibility(View.GONE);
                      }
                  }
                    if (level==tempnumber.size()-1){
                        Winner1.setText("");
                        Winner2.setText("");
                        Winner3.setText("");
                        textView1.setText("");
                    if (tempnumber.get(tempnumber.size()-1)==2){
                        textView2.setVisibility(View.GONE);
                    }
                    if (tempnumber.get(tempnumber.size()-1)==1){
                        textView1.setVisibility(View.GONE);
                        textView2.setVisibility(View.GONE);
                    }}
                    handler.post(myThread);
                }
                else {
                    CustomDialog customDialog = new CustomDialog(LetteryActivity.this);
                    customDialog.setTitle("提醒");
                    customDialog.setMessage("人数不足,无法抽奖");
                    customDialog.setConfirm("确定", new CustomDialog.IOnConfirmListener(){
                        @Override
                        public void onConfirm(CustomDialog dialog) {
                        }
                    });
                    customDialog.show();
                }
                }
                else {
                    Toast.makeText(LetteryActivity.this,"在抽奖中", Toast.LENGTH_SHORT).show();
                }
                break;
                default:break;
        }
    }
}
