package uy.edu.fing.proygrad.simple.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

import uy.edu.fing.proygrad.simple.db.SampleContract.Item;

/**
 * @author gonzalomelov
 */
public class SampleProvider extends ContentProvider {

  private static final String TAG = SampleProvider.class.getSimpleName();

  /*
   * Projections
   */
  private static HashMap<String, String> sItemsProjectionMap;

  /*
   * Constants used by the Uri matcher to choose an action based on the pattern of the incoming URI
   */
  private static final int ITEMS = 1;
  private static final int ITEM_ID = 2;

  /*
   * A UriMatcher instance
   */
  private static final UriMatcher sUriMatcher;

  // Handle to a new SampleDbHelper
  private SampleDbHelper mSampleDbHelper;

  static {
    /*
     * Creates and initializes the URI matcher
     */
    sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    sUriMatcher.addURI(SampleContract.AUTHORITY, "items", ITEMS);
    sUriMatcher.addURI(SampleContract.AUTHORITY, "items/#", ITEM_ID);

    /*
     * Creates and initializes a projection map that returns all columns
     */
    sItemsProjectionMap = new HashMap<String, String>();
    sItemsProjectionMap.put(Item._ID, Item._ID);
    sItemsProjectionMap.put(Item.COLUMN_NAME_DATETIME, Item.COLUMN_NAME_DATETIME);
    sItemsProjectionMap.put(Item.COLUMN_NAME_FILE_URI, Item.COLUMN_NAME_FILE_URI);
    
  }

  @Override
  public boolean onCreate() {
    mSampleDbHelper = new SampleDbHelper(getContext());
    return true;
  }

  @Override
  public String getType(Uri uri) {
    switch (sUriMatcher.match(uri)) {
      case ITEMS: return Item.CONTENT_TYPE;
      case ITEM_ID: return Item.CONTENT_ITEM_TYPE;
      default: throw new IllegalArgumentException("Unknown URI " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues contentValues) {
    String tableName;
    Uri contentIdUriBase;

    switch (sUriMatcher.match(uri)) {

      case ITEMS:
        tableName = Item.TABLE_NAME;
        contentIdUriBase = Item.CONTENT_ID_URI_BASE;
        break;

      default:
        throw new SQLException("Unknown URI " + uri);

    }

    SQLiteDatabase db = mSampleDbHelper.getWritableDatabase();

    // Performs the insert and returns the ID.
    long rowId = db.insert(tableName, null, contentValues);

    // If the insert succeeded, the row ID exists.
    if (rowId > 0) {
      // Creates and returns a URI with the note ID pattern and the new row ID appended to it.
      return ContentUris.withAppendedId(contentIdUriBase, rowId);
    }

    throw new SQLException("Failed to insert row into " + uri);
  }

  @Override
  public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
    Log.d(TAG, "update uri: " + uri.toString());
    return 0;
  }

  @Override
  public int delete(Uri uri, String s, String[] strings) {
    Log.d(TAG, "delete uri: " + uri.toString());
    return 0;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    switch (sUriMatcher.match(uri)) {
      case ITEMS:
        qb.setTables(Item.TABLE_NAME);
        qb.setProjectionMap(sItemsProjectionMap);
        break;

      case ITEM_ID:
        qb.setTables(Item.TABLE_NAME);
        qb.setProjectionMap(sItemsProjectionMap);
        qb.appendWhere(Item._ID + "=" + uri.getPathSegments().get(Item.ITEM_ID_PATH_POSITION));
        break;

      default:
        // If the URI doesn't match any of the known patterns, throw an exception.
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    // Opens the database object in "read" mode, since no writes need to be done.
    SQLiteDatabase db = mSampleDbHelper.getReadableDatabase();

    /*
     * Performs the query. If no problems occur trying to read the database, then a Cursor object is returned;
     * otherwise, the cursor variable contains null. If no records were selected, then
     * the Cursor object is empty, and Cursor.getCount() returns 0.
     */
    Cursor c = qb.query(
        db,            // The database to query
        projection,    // The columns to return from the query
        selection,     // The columns for the where clause
        selectionArgs, // The values for the where clause
        null,          // don't group the rows
        null,          // don't filter by row groups
        sortOrder        // The sort order
    );

    return c;
  }
}
