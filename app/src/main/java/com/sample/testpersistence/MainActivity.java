package com.sample.testpersistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.sample.testpersistence.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private LinearLayoutManager layoutManager;
  private MyRecyclerAdapter adapter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
    adapter = new MyRecyclerAdapter(this);
    layoutManager = new LinearLayoutManager(this);
    layoutManager.setStackFromEnd(false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
          checkScroll(scrollX, scrollY);;
        }
      });
    }else{
      recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          checkScroll(dx, dy);
        }
      });
    }
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
  }
  private void checkScroll(int dx, int dy){
    if(dy>0 &&layoutManager.findLastCompletelyVisibleItemPosition()==adapter.getItemCount()-1){
      adapter.next();
    }
  }
  @Override
  protected void onStart() {
    super.onStart();
//    initializeData();
    adapter.next();
  }
  public static final String ITEMS = "items";
  private void initializeData(){
    DatabaseReference reference = DatabaseHelper.getInstance().getReference(ITEMS);
    ItemModel itemModel;
    for(int i=0;i<40;i++) {
      itemModel = new ItemModel();
      itemModel.setName("This is Sample Item " + i);
      itemModel.setTime(new Date().getTime());
      reference.push().setValue(itemModel);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
