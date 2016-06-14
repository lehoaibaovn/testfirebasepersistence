package com.sample.testpersistence;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by masterchief on 6/13/16.
 */
public class DatabaseHelper {
  private static boolean persistenceEnable = true;
  private static FirebaseDatabase mDatabase;


  public static boolean isPersistenceEnable(){
    return persistenceEnable;
  }
  public static FirebaseDatabase getInstance() {
    if (mDatabase == null) {
      mDatabase = FirebaseDatabase.getInstance();
      mDatabase.setPersistenceEnabled(persistenceEnable);
    }

    return mDatabase;
  }
}
