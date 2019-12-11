package com.example.qiang.activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.example.qiang.R;
import com.example.qiang.gson.Note;
import com.google.gson.Gson;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditNoteActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG="EditNoteActivity";
    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";
    public static final  int UPDATE_TEXT = 1;
    private EditText title;
    private CheckBox checkfirst;
    private CheckBox checksecond;
    private CheckBox checkthird;
    private EditText award1;
    private EditText award2;
    private EditText award3;
    private EditText peoplefirst;
    private EditText peoplesecond;
    private EditText peoplethird;
    private Long noteId;
    private Note note;
    protected ActionBar mActionBar;
    public static Bundle newInstanceBundle(long noteId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_NOTE_ID, noteId);
        return bundle;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        checkfirst = findViewById(R.id.check_first);
        checksecond = findViewById(R.id.check_second);
        checkthird = findViewById(R.id.check_third);
        award1 = findViewById(R.id.award1);
        award2 = findViewById(R.id.award2);
        award3 = findViewById(R.id.award3);
        peoplefirst = findViewById(R.id.peoplefirst);
        peoplesecond = findViewById(R.id.peoplesecond);
        peoplethird = findViewById(R.id.peoplethird);
        award1.setFocusable(false);
        award1.setFocusableInTouchMode(false);
        award2.setFocusable(false);
        award2.setFocusableInTouchMode(false);
        award3.setFocusable(false);
        award3.setFocusableInTouchMode(false);
        peoplefirst.setFocusable(false);
        peoplefirst.setFocusableInTouchMode(false);
        peoplesecond.setFocusable(false);
        peoplesecond.setFocusableInTouchMode(false);
        peoplethird.setFocusable(false);
        peoplethird.setFocusableInTouchMode(false);
        mActionBar = getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1);
        }
        title = (EditText) findViewById(R.id.title);
        //category = (TextView) findViewById(R.id.category);
        findViewById(R.id.save).setOnClickListener(this);
        checkfirst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    award1.setFocusable(true);
                    peoplefirst.setFocusable(true);
                    award1.setFocusableInTouchMode(true);
                    peoplefirst.setFocusableInTouchMode(true);
                }else {
                    award1.setFocusable(false);
                    award1.setFocusableInTouchMode(false);
                    peoplefirst.setFocusable(false);
                    peoplefirst.setFocusableInTouchMode(false);
                    award1.setText("");
                    peoplefirst.setText("");
                    checksecond.setChecked(false);
                    checkthird.setChecked(false);
                }
            }
        });
        checksecond.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkfirst.isChecked()){
                    award2.setFocusable(true);
                    peoplesecond.setFocusable(true);
                    award2.setFocusableInTouchMode(true);
                    peoplesecond.setFocusableInTouchMode(true);}else {
                        Toast.makeText(EditNoteActivity.this,"请开启一等奖",Toast.LENGTH_SHORT).show();
                        checksecond.setChecked(false);
                    }
                }else {
                    award2.setFocusable(false);
                    award2.setFocusableInTouchMode(false);
                    peoplesecond.setFocusable(false);
                    peoplesecond.setFocusableInTouchMode(false);
                    award2.setText("");
                    peoplesecond.setText("");
                    checkthird.setChecked(false);
                }
            }
        });
        checkthird.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checksecond.isChecked()){
                    award3.setFocusable(true);
                    peoplethird.setFocusable(true);
                    award3.setFocusableInTouchMode(true);
                    peoplethird.setFocusableInTouchMode(true);}else {
                        Toast.makeText(EditNoteActivity.this,"请开启二等奖",Toast.LENGTH_SHORT).show();
                        checkthird.setChecked(false);
                    }
                }else {
                    award3.setFocusable(false);
                    award3.setFocusableInTouchMode(false);
                    peoplethird.setFocusable(false);
                    peoplethird.setFocusableInTouchMode(false);
                    award3.setText("");
                    peoplethird.setText("");
                }
            }
        });
          peoplefirst.setOnFocusChangeListener(new View.OnFocusChangeListener() {
              @Override
              public void onFocusChange(View v, boolean hasFocus) {
                  if (hasFocus){
                      Toast.makeText(EditNoteActivity.this,"只允许输入1到3位人数",Toast.LENGTH_SHORT).show();
                  }else {
                      String tel = peoplefirst.getText().toString();
                      Pattern p = Pattern.compile("^[1-3]$");
                      Matcher m = p.matcher(tel);
                      if (m.matches()){

                      }else {
                          Toast.makeText(EditNoteActivity.this,"只允许输入1到3位人数",Toast.LENGTH_SHORT).show();
                          peoplefirst.setText("");
                      }
                  }
              }
          });
        peoplesecond.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Toast.makeText(EditNoteActivity.this,"只允许输入1到3位人数",Toast.LENGTH_SHORT).show();
                }else {
                    String tel = peoplefirst.getText().toString();
                    Pattern p = Pattern.compile("^[1-3]$");
                    Matcher m = p.matcher(tel);
                    if (m.matches()){

                    }else {
                        Toast.makeText(EditNoteActivity.this,"只允许输入1到3位人数",Toast.LENGTH_SHORT).show();
                        peoplefirst.setText("");
                    }
                }
            }
        });
        peoplethird.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Toast.makeText(EditNoteActivity.this,"只允许输入1到3位人数",Toast.LENGTH_SHORT).show();
                }else {
                    String tel = peoplefirst.getText().toString();
                    Pattern p = Pattern.compile("^[1-3]$");
                    Matcher m = p.matcher(tel);
                    if (m.matches()){

                    }else {
                        Toast.makeText(EditNoteActivity.this,"只允许输入1到3位人数",Toast.LENGTH_SHORT).show();
                        peoplefirst.setText("");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (noteId != null) {
            loadNote();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                Pattern p = Pattern.compile("^[1-3]$");
                Matcher m1 = p.matcher(peoplefirst.getText().toString());
                Matcher m2 = p.matcher(peoplefirst.getText().toString());
                Matcher m3 = p.matcher(peoplefirst.getText().toString());
                if (peoplefirst.getText().toString().equals("")&&checkfirst.isChecked()){
                    Toast.makeText(EditNoteActivity.this,"请填一等奖人数",Toast.LENGTH_SHORT).show();
                }
                else if (peoplesecond.getText().toString().equals("")&&checksecond.isChecked()){
                    Toast.makeText(EditNoteActivity.this,"请填二等奖人数",Toast.LENGTH_SHORT).show();

                }
                else if (peoplethird.getText().toString().equals("")&&checkthird.isChecked()){
                    Toast.makeText(EditNoteActivity.this,"请填三等奖人数",Toast.LENGTH_SHORT).show();
                }
                else if((!peoplefirst.getText().toString().equals("")||m1.matches())
                        &&(!peoplesecond.getText().toString().equals("")||m2.matches())
                    &&(!peoplethird.getText().toString().equals("")||m3.matches())){
                saveNote();
            } else {
                    Toast.makeText(EditNoteActivity.this,"输入数据不标准",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

     private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_TEXT:
                   Bundle a = msg.getData();
                   String data = a.getString("data");
                    Log.d(TAG, "在这里爱仕达所多"+data);
                   Note note = new Gson().fromJson(data,Note.class);
                    setNote(note);
                    break;
            }
        }
     };

    private void loadNote() {
        new AsyncTask<Void, Void, Note>() {
            @Override
            protected Note doInBackground(Void... params) {
                List<Note> noteList= DataSupport.where("id=?",noteId.toString()).find(Note.class);
                Note note = noteList.get(0);
                Log.d(TAG, "doInBackground: "+note.toString());
                return note;
            }
            @Override
            protected void onPostExecute(Note note) {
                setNote(note);
            }
        }.execute();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, noteId.toString()+"大家好");
                List<Note> noteList= DataSupport.where("id=?",noteId.toString()).find(Note.class);
                Note note = noteList.get(0);
                Message message = new Message();
                message.what = UPDATE_TEXT;
                Bundle bundle = new Bundle();
                bundle.putString("data", JSONObject.toJSONString(note));
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();*/
    }

    private boolean zerotonull(int temp){
        if (temp==0){
            return false;
        }else return true;
    }

    private void setNote(Note note) {
        this.note = note;
        title.setText(note.getTitle());
        award1.setText(note.getAward1());
        award2.setText(note.getAward2());
        award3.setText(note.getAward3());
        if (zerotonull(note.getPeoplefirst())){
            checkfirst.setChecked(true);
        peoplefirst.setText(String.valueOf(note.getPeoplefirst()));
        }
        if (zerotonull(note.getPeoplesecond())){
            checksecond.setChecked(true);
        peoplesecond.setText(String.valueOf(note.getPeoplesecond()));}
        if (zerotonull(note.getPeoplethird())){
            checkthird.setChecked(true);
        peoplethird.setText(String.valueOf(note.getPeoplethird()));}
    }

    /*private void loadCategories() {
        new AsyncTask<Void, Void, List<Category>>() {
            @Override
            protected List<Category> doInBackground(Void... params) {
                return db.getCategoryDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Category> adapterCategories) {
                *//*setCategories(adapterCategories);*//*
                if (noteId != null) {
                    loadNote();
                }
            }
        }.execute();
    }*/
    /*private void setCategories(List<Category> categories) {
        adapter = new CategoryAdapter(this, android.R.layout.simple_list_item_1, categories);
    }*/

    private int autozero(String temp){
        if (temp!=null){
            temp = temp.trim();
        if (temp.equals(null)||temp.equals("")){
        return 0;
        }else {
            return Integer.parseInt(temp.trim());
        }
        }
        else
            {return 0;}
    }

    private void saveNote() {
        if (note == null) {
            note = new Note();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = formatter.format(curDate);
        note.setTitle(title.getText().toString().trim());
        note.setAward1(award1.getText().toString().trim());
        note.setAward2(award2.getText().toString().trim());
        note.setAward3(award3.getText().toString().trim());
        note.setPeoplefirst(autozero(peoplefirst.getText().toString()));
        note.setPeoplesecond(autozero(peoplesecond.getText().toString()));
        note.setPeoplethird(autozero(peoplethird.getText().toString()));
        note.setDate(date);
        note.setTotalpeople(note.getPeoplefirst()+note.getPeoplesecond()+note.getPeoplethird());
//        note.setDescription(description.getText().toString().trim());
        new AsyncTask<Note, Void, Void>() {
            @Override
            protected Void doInBackground(Note... params) {
                Note saveNote = params[0];
                if (saveNote.getId() > 0) {
                   /* db.getNoteDao().updateAll(saveNote);*/
                    Note note = new Note();
                    note.setTitle(saveNote.getTitle());
                    note.setAward1(saveNote.getAward1());
                    note.setAward2(saveNote.getAward2());
                    note.setAward3(saveNote.getAward3());
                    if (saveNote.getPeoplefirst()==0){
                        note.setToDefault("peoplefirst");
                    }else {
                        note.setPeoplefirst(saveNote.getPeoplefirst());}
                    if (saveNote.getPeoplesecond()==0) {
                        note.setToDefault("peoplesecond");
                    }else {
                        note.setPeoplesecond(saveNote.getPeoplesecond());}
                    if (saveNote.getPeoplethird()==0){
                        note.setToDefault("peoplethird");}else {
                        note.setPeoplethird(saveNote.getPeoplethird());
                    }
                    note.setTotalpeople(saveNote.getTotalpeople());
//                    note.setDescription(saveNote.getDescription());
                    note.updateAll("id=?", String.valueOf(saveNote.getId()));
                    List<Note> noteList= DataSupport.where("id=?","10").find(Note.class);
                    Note note1 = noteList.get(0);
                    Log.d(TAG, "doInBackground: "+note.toString());
                    Log.d(TAG, "doInBackground: "+String.valueOf(saveNote.getId()));
                    Log.d(TAG, "doInBackground: "+String.valueOf(note1.toString()));
                } else {
                    /*db.getNoteDao().insertAll(saveNote);*/
                    Note note = new Note();
                    note.setTitle(saveNote.getTitle());
                    note.setAward1(saveNote.getAward1());
                    note.setAward2(saveNote.getAward2());
                    note.setAward3(saveNote.getAward3());
                    note.setPeoplefirst(saveNote.getPeoplefirst());
                    note.setPeoplesecond(saveNote.getPeoplesecond());
                    note.setPeoplethird(saveNote.getPeoplethird());
                    note.setTotalpeople(saveNote.getTotalpeople());
                    note.setDate(date);
                    note.save();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }
        }.execute(note);
    }
}
