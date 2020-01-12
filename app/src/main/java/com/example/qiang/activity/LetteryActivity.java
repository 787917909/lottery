package com.example.qiang.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.util.StringUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.qiang.R;
import com.example.qiang.entity.Mainlottery;
import com.example.qiang.entity.People;
import com.example.qiang.fireWorks.FireworkView;
import com.example.qiang.gson.Winners;
import com.example.qiang.http.HttpUtil;
import com.example.qiang.tool.CustomDialog;
import com.example.qiang.util.ToastUtil;
import com.example.qiang.websocket.Client;
import com.example.qiang.websocket.ClientService;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LetteryActivity extends AppCompatActivity implements View.OnClickListener {
    private Client client;
    private ClientService clientService;
    private ClientService.ClientBinder binder;
    private static final String TAG = "LetteryActivity";
    private List<String> datas = new ArrayList<>();
    private List<String> winnners = new ArrayList<>();
    private List<String> workid = new ArrayList<>();
    private boolean running;
    private boolean wasrunning;
    private boolean flag =false;
    private boolean end =false;
    private int temp1 ;
    private int count = 0;
    private int level=0;
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
    private List<TextView> textViews = new ArrayList<TextView>();
  private TextView Winner1;
  private TextView Winner2;
   private Winners win;
  private   String temp[];
    private   String tempwin[];
  private TextView jiangping;
  private FireworkView fv;
    protected ActionBar mActionBar;
   private int tempnumber;
    private Mainlottery note;
    final Handler handler = new Handler();
    Thread myThread;
    Thread webSocketThread;
    Boolean flagadd;
    private Long noteId;
    private Long meetingId;
     private int[] random;
    private List<Mainlottery> mainlist = new ArrayList<>();
    private String wintemp="";
    private String[] wintempshuzu;
    boolean k;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("LotteryActivity", "服务与活动成功绑定");
            binder = (ClientService.ClientBinder) iBinder;
            clientService = binder.getService();
            client = clientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("LotteryActivity", "服务与活动成功断开");
        }
    };

//    private class LotteryReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String message=intent.getStringExtra("message").trim();
//
//            if (message.contains("-")&& message.split("-")[1].equals(noteId.toString())){
//
//                level=Integer.valueOf(message.split("-")[0]);
//                running=true;
//                wasrunning =true;
//                if (temp1==level&&!note.isEndless()){
//                    winnners.clear();
//                }
//
//
//                handler.post(webSocketThread);
////                ToastUtil.showMsg(LetteryActivity.this,"hahahah");
//            }else if(message.contains(",")) {
//                String[] data = message.split(",");
//                Log.i(TAG, "onReceive: " + data[1]);
//                String[] name = data[0].split(" +");
//                if (data[1].equals(noteId.toString())) {
//
////                    ToastUtil.showMsg(LetteryActivity.this, "aoligei"+name.toString()+level);
//                    Log.i(TAG, "onReceive: " + name.length);
//                    running = false;
//                    handler.removeCallbacks(webSocketThread);
//                    if (name.length == 1) {
//                        textView.setText(name[0]);
//                    } else if (name.length == 2) {
//                        textView.setText(name[0]);
//                        textView1.setText(name[1]);
//                    } else if (name.length == 3) {
//                        textView.setText(name[0]);
//                        textView1.setText(name[1]);
//                        textView2.setText(name[2]);
//                    }
//                    if (level == 2) {
//                        Winner1.setText(Joiner.on("   ").join(name));
//                        winnners.addAll(Arrays.asList(name));
//                        fv.setFireworkCount(2);
//                        fv.showFirework();
//                    } else if (level == 1) {
//                        Winner2.setText(Joiner.on("   ").join(name));
//                        winnners.addAll(Arrays.asList(name));
//                        fv.setFireworkCount(3);
//                        fv.showFirework();
//                    } else if (level == 0) {
//                        Winner3.setText(Joiner.on("   ").join(name));
//                        winnners.addAll(Arrays.asList(name));
//                        fv.setFireworkCount(5);
//                        fv.showFirework();
//                    }
//                }
//            }
//        }
//    }

    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lettery);

//        doRegisterReceiver();
//        startClientService();
//        bindService();
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
        textView = findViewById(R.id.DataView1);
        textView1= findViewById(R.id.DataView2);
        textView2= findViewById(R.id.DataView3);
        chushihua = findViewById(R.id.chushihua);
        AddItem = findViewById(R.id.Add);
        ClearData = findViewById(R.id.clearData);
        Lettery = findViewById(R.id.Start);
        jiangping = findViewById(R.id.jiangping);
//        Winner1 = findViewById(R.id.Winner1);
//        Winner2 = findViewById(R.id.Winner2);
//        Winner3 = findViewById(R.id.Winner3);
        WinningList = findViewById(R.id.zhongjiang);
//        award1 = findViewById(R.id.First);
//        award2 = findViewById(R.id.Second);
//        award3 = findViewById(R.id.Third);
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
        if (intent.hasExtra("meetingId")) {
            meetingId = intent.getLongExtra("meetingId", -1);
        }
        editText.setVisibility(View.GONE);
        findViewById(R.id.close).setVisibility(View.GONE);
        if (noteId!=null) {
            loadNote();
        }
        initdata();

        webSocketThread = new Thread(){
            @Override
            public void run() {
                int[] random = new int[1];
                Set<Integer> set=new LinkedHashSet();
                boolean panduan;
                while(true){
                    int z=(int)(Math.random()*datas.size());
                    panduan=set.add(z);
                    if(!panduan){
                        continue;
                    }
                    if(set.size()>=tempnumber){
                        break;
                    }
                }
                Object[] temp = set.toArray();
                for(int i = 0;i<temp.length;i++) {
                    random[i] = (int)temp[i];
                }
                if (running ) {
                    count++;
                    Log.d(TAG, "run: "+random.length);
                    textView.setText(datas.get(random[0]));
                    if (random.length>1){
                        textView1.setText(datas.get(random[1]));
                        if (random.length>2){
                            textView2.setText(datas.get(random[2]));}
                    }
                    Log.d(TAG, "run: "+"lottery的跑方式");
                    handler.postDelayed(this,10);
                }else {
                    wasrunning = false;
                }
            }
        };

        myThread = new Thread(){
            @Override
            public void run() {

                if (running ) {
                    random  = new int[tempnumber];
                    temp1=0;
                    Set<Integer> set=new LinkedHashSet();
                    boolean panduan;
                    while(true){
                        int z=(int)(Math.random()*datas.size());
                        panduan=set.add(z);
                        if(!panduan){
                            continue;
                        }
                        if(set.size()>=tempnumber){
                            break;
                        }
                    }
                    Object[] temp = set.toArray();
                    for(int i = 0;i<temp.length;i++) {
                        random[i] = (int)temp[i];
                    }


                    Log.d(TAG, "run: "+random.length);
//                    textView.setText(datas.get(random[0]));
//                    if (random.length>1){
//                    textView1.setText(datas.get(random[1]));
//                        if (random.length>2){
//                            textView2.setText(datas.get(random[2]));}
//                    }

                    for (TextView textView:textViews)
                    {
                       textView.setText(datas.get(random[temp1]).split("\\(")[0]);
                      temp1++;
                    }

                    handler.postDelayed(this,10);
                }
                if (!running && end ) {
                        fv.setFireworkCount(5);
                        fv.showFirework();
                        int k = 0;
                    for (TextView textView:textViews)
                    {
                       winnners.add(datas.get(random[k]));

                        k++;
                    }
                    jiangping.setVisibility(View.VISIBLE);
                    jiangping.setText("奖品："+note.getAward1());
                    datas.remove(winnners);
//                        if (tempnumber==3){
//                            winnners.add(textView.getText().toString());
//                            winnners.add(textView1.getText().toString());
//                            winnners.add(textView2.getText().toString());
//                            datas.remove(textView.getText().toString());
//                            datas.remove(textView1.getText().toString());
//                            datas.remove(textView2.getText().toString());}
//                        if (tempnumber==2) {
//                            winnners.add(textView.getText().toString());
//                            winnners.add(textView1.getText().toString());
//                            datas.remove(textView.getText().toString());
//                            datas.remove(textView1.getText().toString());
//                        }
//                        if (tempnumber==1) {
//                            winnners.add(textView.getText().toString());
//                            datas.remove(textView.getText().toString());
//                        }
//                        clientService.sendMsg(Winner3.getText()+","+noteId.toString());
//                        level=tempnumber.size()-1;
//                        textView1.setVisibility(View.VISIBLE);
//                        textView2.setVisibility(View.VISIBLE);

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
                                      runOnUiThread(
                                              new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      WinningList.setVisibility(View.VISIBLE);
                                                  }
                                              }
                                      );
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


                    handler.removeCallbacks(this);
                    Log.d(TAG, "一直在运行的可怜人");
                }
                }
        };}


    private void setNote(Mainlottery note) {
        this.note = note;
        Log.d(TAG, "setNote: "+note.isEndless());
        if (note.getPeoplefirst()!=0){
            tempnumber=note.getPeoplefirst();
        }
        textViews=getAllTextView(tempnumber);

//        level = tempnumber.size()-1;
        setTitle(note.getTitle());
        Resources res=getResources();
        if (note.getPeoplefirst()<40){
            for (int j=note.getPeoplefirst()+1;j<=40;j++){
                int id=res.getIdentifier("DataView"+j,"id",getPackageName());
                TextView TextView= (TextView) findViewById(id);//关联控件
                TextView.setVisibility(View.GONE);
            }
        }
        if (note.getWinjson().equals("")){
            Lettery.setVisibility(View.VISIBLE);
        }
//        award1.setText(note.getAward3());
//        award1.append(":");
//        award2.setText(note.getAward2());
//        award2.append(":");}
//        award3.setText(note.getAward1());
//        award3.append(":");
//        temp1=level;
    }

    public List<TextView> getAllTextView(int k){
        //定义一个存储按钮的list数组(0-68按钮)
        List<TextView> editTexts=new ArrayList<TextView>();
        //获取R 资源
        Resources res=getResources();
        //下面用for循环进去findviewid
        for (int i=1;i<=k;i++){
            int id=res.getIdentifier("DataView"+i,"id",getPackageName());
            TextView TextView= (TextView) findViewById(id);//关联控件
            editTexts.add(TextView);
        }
        return editTexts;
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
                        Map<String,String> param2 = new HashMap<String, String>();
                        param2.put("id",noteId.toString());
                        HttpUtil.sendOkHttpRequestText(HttpUtil.BASE_URL + "lottery/findmlotterybyid.do", param2, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                final Mainlottery note = new Gson().fromJson(response.body().string(),Mainlottery.class);
                                if (!note.getWinjson().equals("")){
                                    temp = note.getWinjson().split(",");

                                    winnners.clear();
                                    winnners.addAll( Arrays.asList(temp));
                                    if (note.isEndless()) {
                                        datas.removeAll(winnners);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jiangping.setVisibility(View.VISIBLE);
                                            jiangping.setText("奖品："+note.getAward1());
                                            WinningList.setVisibility(View.VISIBLE);
                                            int j=0;
                                            for (TextView textView:textViews){
                                                textView.setText(winnners.get(j).split("\\(")[0]);
                                                j++;
                                            }
                                        }
                                    });
                                }

                            }
                        });
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

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(LetteryActivity.this, ClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }
    /**
     * 启动服务（websocket客户端服务）
     */
    private void startClientService() {
        Intent intent = new Intent(LetteryActivity.this, ClientService.class);
        startService(intent);
    }
    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
//        LotteryReceiver receiver = new LotteryReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
//        registerReceiver(receiver, filter);
        Log.d(TAG, "doRegisterReceiver: ");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lettery, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Map<String,String> param = new HashMap<String, String>();
//        param.put("id",noteId.toString());
//        HttpUtil.sendOkHttpRequestText(HttpUtil.BASE_URL + "lottery/findmlotterybyid.do", param, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                final Mainlottery note = new Gson().fromJson(response.body().string(),Mainlottery.class);
//                if (!note.getWinjson().equals("")){
//                temp = note.getWinjson().split(",");
//                winnners.clear();
//
////                for (String a:temp){
////                    if(note.isEndless()){
////                        datas.remove(a);
////                    }
////                    winnners.add(a.split("\\(")[0]);
////                }
//                winnners.addAll( Arrays.asList(temp));
//                    datas.removeAll(winnners);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            WinningList.setVisibility(View.VISIBLE);
//                            int j=0;
//                            for (TextView textView:textViews){
//                                textView.setText(winnners.get(j).split("\\(")[0]);
//                                j++;
//                            }
//                        }
//                    });
//            }
//
//            }
//        });
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

       HashMap<String,String> param = new HashMap<>();
       param.put("meetingid",meetingId.toString());
        HttpUtil.sendOkHttpRequestText(HttpUtil.BASE_URL + "lottery/findmlotterybymid.do", param, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONArray jsonArray = JSONArray.parseArray(response.body().string());
               mainlist = jsonArray.toJavaList(Mainlottery.class);
               int tempnum=1;
               for (Mainlottery ha : mainlist){
                   if (!ha.getWinjson().equals("")) {
                       if (tempnum < mainlist.size()) {
                           wintemp = wintemp + ha.getWinjson() + ",";
                       } else {
                           wintemp = wintemp + ha.getWinjson();
                       }
                   }
                   tempnum++;
               }
               if (!wintemp.equals("")){
                   wintempshuzu = wintemp.trim().split(",");
               for (String temp:wintempshuzu){
                   workid.add(temp.substring(temp.indexOf("(")+1,temp.indexOf(")")));
                   Log.d(TAG, "onResponse: "+temp.substring(temp.indexOf("(")+1,temp.indexOf(")")));
               }}
                HttpUtil.sendOkHttpRequest(HttpUtil.BASE_URL+"lottery/findlotteryp.do",new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        JSONArray jsonArray = JSONArray.parseArray(response.body().string());
                        Gson gson = new Gson();
                        List<People> list = jsonArray.toJavaList(People.class);
                        datas.clear();
                        k = true;
                        for (People a:list){
                            for (String  ha:workid) {
                                if (ha.trim().equals(a.getWorkid().trim())){
                                    k=false;
                                }
                            }
                            if (k==true) {
                                datas.add(a.getName() + "(" + a.getWorkid() + ")");
                            }
                       k=true;
                        }
                    }
                });
            }
        });


//       String address = "http://10.132.212.167:8080/bird/lottery/findlotteryp.do";
//       HttpUtil.sendOkHttpRequest(HttpUtil.BASE_URL+"lottery/findlotteryp.do",new Callback() {
//           @Override
//           public void onFailure(@NotNull Call call, @NotNull IOException e) {
//           }
//
//           @Override
//           public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//               JSONArray jsonArray = JSONArray.parseArray(response.body().string());
//               Gson gson = new Gson();
//               List<People> list = jsonArray.toJavaList(People.class);
//               datas.clear();
//               for (People a:list){
//                   datas.add(a.getName()+"("+a.getWorkid()+")");
//               }
////                datas.addAll(Arrays.asList(response.body().string().trim().split(",")));
////                if (winnners.size()!=0){
////                    datas.removeAll(winnners);
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            WinningList.setVisibility(View.VISIBLE);
////                        }
////                    });
////                }
//           }
//       });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        clientService.onDestroy();
//        handler.removeCallbacks(webSocketThread);
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
//                            datas.addAll(winnners);
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
                break;
            case R.id.zhongjiang:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(note.getTitle()+":"+note.getAward1());
                StringBuilder stringBuilder = new StringBuilder();
                int i=0,j=0,k=0;
                for(String a :winnners){
                        if (i%2==0&&i!=0){
                            stringBuilder.append("\r\n");
                        }
                        stringBuilder.append(a+"     ");
                        i++;}

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
                if(running){
                    running=false;
                    end = true;
                    Lettery.setVisibility(View.INVISIBLE);
                }else {
                    Lettery.setText("停止抽奖");
//                    clientService.sendMsg(level+"-"+noteId.toString());
                if(datas.size()>=note.getTotalpeople()){
                    count = 0;
                    running = true;


                    if (!note.isEndless()){
                        winnners.clear();
                    }
                    handler.post(myThread);

//                    if (level==2){
//                        if (tempnumber.get(2)==3){
//                            textView2.setVisibility(View.VISIBLE);
//                            textView1.setVisibility(View.VISIBLE);}
//                        if (tempnumber.get(2)==2) {
//                            textView1.setVisibility(View.VISIBLE);
//                            textView2.setVisibility(View.GONE);}
//                        if (tempnumber.get(2)==1) {
//                            textView1.setVisibility(View.GONE);
//                            textView2.setVisibility(View.GONE);}
//                    }
//                  if (level==1){
//                    if (tempnumber.get(1)==3){
//                        textView2.setVisibility(View.VISIBLE);
//                        textView1.setVisibility(View.VISIBLE);}
//                    if (tempnumber.get(1)==2) {
//                        textView1.setVisibility(View.VISIBLE);
//                        textView2.setVisibility(View.GONE);}
//                    if (tempnumber.get(1)==1) {
//                        textView1.setVisibility(View.GONE);
//                        textView2.setVisibility(View.GONE);}}
//
//                  if (level==0) {
//                      if (tempnumber.get(0) == 3) {
//                          textView2.setVisibility(View.VISIBLE);
//                          textView1.setVisibility(View.VISIBLE);
//                      }
//                      if (tempnumber.get(0) == 2) {
//                          textView1.setVisibility(View.VISIBLE);
//                          textView2.setVisibility(View.GONE);
//                      }
//                      if (tempnumber.get(0) == 1) {
//                          textView1.setVisibility(View.GONE);
//                          textView2.setVisibility(View.GONE);
//                      }
//                  }
//                    if (level==tempnumber.size()-1){
////                        Winner1.setText("");
////                        Winner2.setText("");
////                        Winner3.setText("");
//                        textView1.setText("");
//                    if (tempnumber.get(tempnumber.size()-1)==2){
//                        textView2.setVisibility(View.GONE);
//                    }
//                    if (tempnumber.get(tempnumber.size()-1)==1){
//                        textView1.setVisibility(View.GONE);
//                        textView2.setVisibility(View.GONE);
//                    }}

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
                } }

                break;
                default:break;
        }
    }
}
