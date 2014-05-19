package uy.edu.fing.proygrad.simple.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author gonzalomelov
 */
public class SampleDbHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "Sample.db";
  private static final int DATABASE_VERSION = 1;

  private static final String TAG = SampleDbHelper.class.getSimpleName();

  public SampleDbHelper(Context context){
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SampleContract.Item.CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(SampleContract.Item.DROP_TABLE);
    onCreate(db);
  }
}
