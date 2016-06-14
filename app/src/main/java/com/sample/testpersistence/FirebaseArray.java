package com.sample.testpersistence;

import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by masterchief on 5/25/16.
 */
class FirebaseArray<C extends ItemModel> implements ChildEventListener {
  public interface OnChangedListener {
    enum EventType { Added, Changed, Removed, Moved }
    void onChanged(EventType type, int index, int oldIndex);
  }

  private Query mQuery;
  private Query originQuery;
  private OnChangedListener mListener;
  private ArrayList<C> mItems;
  private Class<C> c;
  private boolean isSyncing;
  private int pageSize;
  private String TAG = getClass().getSimpleName();
  public FirebaseArray(Class<C> c) {
    this.c = c;
    mItems = new ArrayList<>();
    pageSize = 10;
    originQuery = DatabaseHelper.getInstance().getReference().child(MainActivity.ITEMS).orderByChild("time");
  }



  public void next() {
    Log.d(TAG,"nextpage");
    if( mItems.size() == 0){
          mQuery = originQuery.limitToLast(pageSize);
    }else{
        long start = getItem( mItems.size()-1).getTime()-1;
          mQuery = originQuery.endAt(start).limitToLast(pageSize);
    }
    mQuery.addChildEventListener(this);
  }

  public void cleanup() {
    if(mQuery !=null) {
      mQuery.removeEventListener(this);
    }
    mItems.clear();
  }

  public int getCount() {
    return mItems.size();

  }
  public C getItem(int index) {
    return mItems.get(index);
  }


  private int getIndexForKey(String key) {
    int index = 0;
    for (C snapshot : mItems) {
      if (snapshot.getKey().equals(key)) {
        return index;
      } else {
        index++;
      }
    }
    return -1;
  }

  public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
    Log.d("onchildadded", snapshot.getKey());
    int index;
    if (previousChildKey != null) {
      index = getIndexForKey(previousChildKey);
      if(index<0){
        index = mItems.size();;
      }
    }else{
      index = mItems.size();
    }
    if(index>0){
     if(snapshot.getKey().equals(mItems.get(index-1).getKey())){
        return;
      }
    }
    C item = snapshot.getValue(c);
    item.setKey(snapshot.getKey());

    mItems.add(index, item);
    notifyChangedListeners(OnChangedListener.EventType.Added, index);
  }

  public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
    int index = getIndexForKey(snapshot.getKey());
    C item = snapshot.getValue(c);
    item.setKey(snapshot.getKey());
    mItems.set(index,item);
    notifyChangedListeners(OnChangedListener.EventType.Changed, index);
  }

  public void onChildRemoved(DataSnapshot snapshot) {
    int index = getIndexForKey(snapshot.getKey());
    if(index>0) {
      mItems.remove(index);
      notifyChangedListeners(OnChangedListener.EventType.Removed, index);
    }
  }

  public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
    int oldIndex = getIndexForKey(snapshot.getKey());
    mItems.remove(oldIndex);
    int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
    C item = snapshot.getValue(c);
    item.setKey(snapshot.getKey());
    mItems.add(newIndex, item);
    notifyChangedListeners(OnChangedListener.EventType.Moved, newIndex, oldIndex);
  }

  public void onCancelled(DatabaseError firebaseError) {
    Log.i("oncanceled", firebaseError.getMessage());
  }

  public void setOnChangedListener(OnChangedListener listener) {
    mListener = listener;
  }
  protected void notifyChangedListeners(OnChangedListener.EventType type, int index) {
    notifyChangedListeners(type, index, -1);
  }
  protected void notifyChangedListeners(OnChangedListener.EventType type, int index, int oldIndex) {
    if (mListener != null) {
      mListener.onChanged(type, index, oldIndex);
    }
  }
}