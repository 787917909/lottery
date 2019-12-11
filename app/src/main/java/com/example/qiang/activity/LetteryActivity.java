package com.example.qiang.activity;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.AsyncTask;
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

import com.example.qiang.R;
import com.example.qiang.fireWorks.Firework;
import com.example.qiang.fireWorks.FireworkView;
import com.example.qiang.gson.Note;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LetteryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LetteryActivity";
    private List<String> datas = new ArrayList<>();
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
  private Button ClearData;
  private Button Lettery;
  private EditText editText;
  private TextView textView;
    private TextView textView1;
    private TextView textView2;
  private TextView Winner1;
  private TextView Winner2;
  private TextView Winner3;
  private FireworkView fv;
    protected ActionBar mActionBar;
   private List<Integer> tempnumber = new ArrayList<>();
    private Note note;
    final Handler handler = new Handler();
    Thread myThread;
    private Long noteId;
    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lettery);
        final String[] data = {"Andrew","Henk","Loki","Morgan","Henry","Jay","Sofia","Jeff","Rio"};
        datas.addAll(Arrays.asList(data));
//      startLettery();
        editText = findViewById(R.id.addData);
        textView = findViewById(R.id.DataView);
        textView1= findViewById(R.id.DataView1);
        textView2= findViewById(R.id.DataView2);
        AddItem = findViewById(R.id.Add);
        ClearData = findViewById(R.id.clearData);
        Lettery = findViewById(R.id.Start);
        Winner1 = findViewById(R.id.Winner1);
        Winner2 = findViewById(R.id.Winner2);
        Winner3 = findViewById(R.id.Winner3);
        award1 = findViewById(R.id.First);
        award2 = findViewById(R.id.Second);
        award3 = findViewById(R.id.Third);
        fv = findViewById(R.id.fv);
        running = false;
        AddItem.setOnClickListener(this);
        ClearData.setOnClickListener(this);
        Lettery.setOnClickListener(this);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1);
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
                        datas.remove(textView.getText().toString());
                        datas.remove(textView1.getText().toString());
                        datas.remove(textView2.getText().toString());}
                            if (tempnumber.get(2)==2) {
                                textView2.setVisibility(View.GONE);
                                Winner1.setText(textView.getText().toString() + "  " + textView1.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                                datas.remove(textView.getText().toString());
                                datas.remove(textView1.getText().toString());
                            }
                            if (tempnumber.get(2)==1) {
                                Winner1.setText(textView.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
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
                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                            datas.remove(textView2.getText().toString());}
                        if (tempnumber.get(1)==2) {
                            Winner2.setText(textView.getText().toString() + "  " + textView1.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                        }
                        if (tempnumber.get(1)==1) {
                            Winner2.setText(textView.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            datas.remove(textView.getText().toString());
                        }
//                        if (tempnumber.get(0)==3){
//                            textView2.setVisibility(View.VISIBLE);
//                            textView1.setVisibility(View.VISIBLE);}
//                        if (tempnumber.get(0)==2) {
//                            textView1.setVisibility(View.VISIBLE);
//                            textView2.setVisibility(View.GONE);}
//                        if (tempnumber.get(0)==1) {
//                            textView1.setVisibility(View.GONE);
//                            textView2.setVisibility(View.GONE);}
                        level--;

                    }else if (level==0){
                        fv.setFireworkCount(5);
                        fv.showFirework();
                        if (tempnumber.get(0)==3){
                            Winner3.setText(textView.getText().toString()+"  "+textView1.getText().toString()+"  "+textView2.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                            datas.remove(textView2.getText().toString());}
                        if (tempnumber.get(0)==2) {
                            Winner3.setText(textView.getText().toString() + "  " + textView1.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            datas.remove(textView.getText().toString());
                            datas.remove(textView1.getText().toString());
                        }
                        if (tempnumber.get(0)==1) {

                            Winner3.setText(textView.getText().toString());
                      /*  Winner2.setText(textView1.getText().toString());
                        Winner3.setText(textView2.getText().toString());*/
                            datas.remove(textView.getText().toString());
                        }
                        level=tempnumber.size()-1;
//                        textView1.setVisibility(View.VISIBLE);
//                        textView2.setVisibility(View.VISIBLE);
                        datas.addAll(Arrays.asList(data));
                        Toast.makeText(LetteryActivity.this,"抽奖完毕", Toast.LENGTH_SHORT).show();

                    }
                    handler.removeCallbacks(this);
                    Lettery.setVisibility(View.VISIBLE);

                }
                Log.d(TAG, "一直在运行的可怜人");
            }
        };

    }

    private void setNote(Note note) {
        this.note = note;
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
    }

    private void loadNote(){
        new AsyncTask<Void, Void, Note>() {
            @Override
            protected Note doInBackground(Void... longs) {
                List<Note> noteList= DataSupport.where("id=?",noteId.toString()).find(Note.class);
                Note note = noteList.get(0);
                Log.d(TAG, "onPostExecute: "+note.toString());
                return note;
            }
            @Override
            protected void onPostExecute(Note note) {
                setNote(note);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lettery, menu);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        editText.setVisibility(View.GONE);
        findViewById(R.id.close).setVisibility(View.GONE);
        if (noteId!=null) {
            loadNote();
        }
    }


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
            public void run() {
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
            case R.id.Add:
                if(!wasrunning && !editText.getText().toString().matches("")){
                    datas.addAll(Arrays.asList( editText.getText().toString().split("-")));
                }
                break;
            case R.id.clearData:
                if(!wasrunning) {
                    datas.clear();
                }
                break;
            case R.id.Start:
                if (!wasrunning){
                if(datas.iterator().hasNext()){
                    count = 0;
                    running = true;
                    wasrunning = true;
                    Lettery.setVisibility(View.INVISIBLE);
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
                    textView.setText("Your Data is null!!");
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
