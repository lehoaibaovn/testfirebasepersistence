package com.sample.testpersistence;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by masterchief on 6/8/16.
 */
public class ItemFrame extends FrameLayout {
  public ItemFrame(Context context) {
    super(context);
    init();
  }

  public ItemFrame(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private TextView txtTitle;
  private TextView txtTime;
  private void init(){
    View view = inflate(getContext(), R.layout.item_layout, null);
    txtTitle = (TextView) view.findViewById(R.id.txt_title_lesson_item);
    txtTime = (TextView) view.findViewById(R.id.txt_time_lesson_item);
    this.addView(view);
  }

  private boolean isSelected;

  public void setSelected(boolean isSelect){
    if(isSelected!=isSelect) {
      if (isSelect) {
        this.setBackgroundColor(Color.LTGRAY);
      } else {
        this.setBackgroundColor(Color.TRANSPARENT);
      }
      isSelected = isSelect;
    }
  }

  public void setValue(ItemModel model){
    txtTitle.setText(model.getName());
    txtTime.setText(new Date(model.getTime()).toString());
  }
}
