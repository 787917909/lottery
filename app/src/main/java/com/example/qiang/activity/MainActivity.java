package com.example.qiang.activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qiang.R;
import com.example.qiang.RecyclerView.OnItemMenuClickListener;
import com.example.qiang.RecyclerView.SwipeMenu;
import com.example.qiang.RecyclerView.SwipeMenuBridge;
import com.example.qiang.RecyclerView.SwipeMenuCreator;
import com.example.qiang.RecyclerView.SwipeMenuItem;
import com.example.qiang.RecyclerView.SwipeRecyclerView;
import com.example.qiang.adapter.NoteAdapter;
import com.example.qiang.domain.MyDividerItemDecoration;
import com.example.qiang.gson.Note;
import com.example.qiang.tool.CustomDialog;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private NoteAdapter adapter;
    private SearchView searchView;
    List<Note>  notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add).setOnClickListener(this);
        notes = new ArrayList<>();
        adapter = new NoteAdapter(notes);
        SwipeRecyclerView recyclerView = (SwipeRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setAdapter(adapter);
     /*   ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
        }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                deleteNote(adapter.getNote(viewHolder.getAdapterPosition()));
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtras(EditNoteActivity.newInstanceBundle(notes.get(viewHolder.getAdapterPosition()).getId()));
                 startActivity(intent);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_100);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加左侧的，如果不添加，则左侧不会出现菜单。
         /*{
                SwipeMenuItem addItem = new SwipeMenuItem(MainActivity.this).setBackground(R.drawable.selector_green)
                        .setImage(R.drawable.ic_action_add)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

                SwipeMenuItem closeItem = new SwipeMenuItem(MainActivity.this).setBackground(R.drawable.selector_red)
                        .setImage(R.drawable.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
            }*/
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this).setBackground(R.drawable.selector_green)
//                        .setImage(R.drawable.ic_action_delete)
                        .setText("编辑")
                        .setTextColor(Color.WHITE)
                        .setWidth(width+20)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(MainActivity.this).setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, final int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition==0){
                    Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                    intent.putExtras(EditNoteActivity.newInstanceBundle(notes.get(position).getId()));
                    startActivity(intent);
                }
                if (menuPosition==1){
                    CustomDialog customDialog = new CustomDialog(MainActivity.this);
                    customDialog.setTitle("提醒");
                    customDialog.setMessage("你确定要删除吗?");
                    customDialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
                        @Override
                        public void onCancel(CustomDialog dialog) {
                            Toast.makeText(MainActivity.this, "取消成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    customDialog.setConfirm("确定", new CustomDialog.IOnConfirmListener(){
                        @Override
                        public void onConfirm(CustomDialog dialog) {
                            deleteNote(adapter.getNote(position));
                        }
                    });
                    customDialog.show();

//                    deleteNote(adapter.getNote(position));
                }
            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(MainActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                startActivity(new Intent(this, EditNoteActivity.class));
                break;
        }
    }

    private void loadNotes() {
        new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... params) {
                return DataSupport.findAll(Note.class);
            }
            @Override
            protected void onPostExecute(List<Note> notes) {
                MainActivity.this.notes.clear();
                MainActivity.this.notes.addAll(notes);
                adapter.notifyDataSetChanged();
                //adapter.setNotes(notes);
            }
        }.execute();
    }

    private void deleteNote(Note note) {
        new AsyncTask<Note, Void, Void>() {
            @Override
            protected Void doInBackground(Note... params) {
               /* db.getNoteDao().deleteAll(params);*/
                Note note1 = params[0];
                DataSupport.deleteAll(Note.class,"id=?",String.valueOf(note1.getId()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadNotes();
            }
        }.execute(note);

    }
}
