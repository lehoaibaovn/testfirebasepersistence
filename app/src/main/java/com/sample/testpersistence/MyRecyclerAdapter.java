package com.sample.testpersistence;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by masterchief on 5/25/16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.LessonViewHolder> {

  FirebaseArray<ItemModel> mSnapshots;

  private String TAG = getClass().getSimpleName();
  public void next(){
    Log.i(TAG, "next");
    this.mSnapshots.next();
  }

  private MainActivity context;
  public MyRecyclerAdapter(Context context) {
    this.context = (MainActivity) context;
    mSnapshots = new FirebaseArray(ItemModel.class);
    mSnapshots.setOnChangedListener(new FirebaseArray.OnChangedListener() {
      @Override
      public void onChanged(EventType type, int index, int oldIndex) {
        switch (type) {
          case Added:
            notifyItemInserted(index);
            break;
          case Changed:
            notifyItemChanged(index);
            break;
          case Removed:
            notifyItemRemoved(index);
            break;
          case Moved:
            notifyItemMoved(oldIndex, index);
            break;
          default:
            throw new IllegalStateException("Incomplete case statement");
        }
      }
    });

  }

  public void cleanup() {
    mSnapshots.cleanup();
  }

  @Override
  public int getItemCount() {
    return mSnapshots.getCount();
  }

  public ItemModel getItem(int position) {
    return mSnapshots.getItem(position);
  }

  @Override
  public long getItemId(int position) {
    return mSnapshots.getItem(position).getKey().hashCode();
  }

  @Override
  public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_item, parent, false);
      LessonViewHolder lessonViewHolder = new LessonViewHolder(view);
    return lessonViewHolder;
  }

  private String selectedKey;
  private int selectedPosition;
  @Override
  public void onBindViewHolder(LessonViewHolder holder, final int position) {
    if(getItem(position).getKey().equals(selectedKey)){
      holder.itemView.setSelected(true);
      selectedPosition = position;
    }else{
      holder.itemView.setSelected(false);
    }
    populateViewHolder(holder, position);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ItemModel model = getItem(position);
        v.setSelected(true);
        notifyItemChanged(position);
        int oldPosition = selectedPosition;
        selectedKey = model.getKey();
        selectedPosition = position;
        notifyItemChanged(oldPosition);
      }
    });
  }


  void populateViewHolder(LessonViewHolder viewHolder, int position){
    ((ItemFrame)viewHolder.itemView).setValue(getItem(position));
  }


  public static class LessonViewHolder extends RecyclerView.ViewHolder {
    public LessonViewHolder(View v) {
      super(v);
    }
  }

}
