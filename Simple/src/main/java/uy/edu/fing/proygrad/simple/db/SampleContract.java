package uy.edu.fing.proygrad.simple.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author gonzalomelov
 */
public final class SampleContract {

  /*
   * Class cannot be instantiated
   */
  private SampleContract() {}

  private static final String SCHEME = "content://";
  public static final String AUTHORITY = "uy.edu.fing.proygrad.simple.provider";

  private static final String TEXT_TYPE = " Text ";
  private static final String INTEGER_TYPE = " Integer ";
  private static final String DATE_TYPE = " Integer ";

  private static final String PRIMARY_KEY = " PRIMARY KEY ";
  private static final String COMMA_SEP = ",";
  public static final String DROP_TABLE_DEFINITION = "DROP TABLE IF EXISTS ";

  public static final class Item implements BaseColumns {

    /*
     * Class cannot be instantiated
     */
    private Item() {}

    public static final String TABLE_NAME = "items";

    /*
     * Column definitions
     */
    public static final String COLUMN_NAME_DATETIME = "datetime";
    public static final String COLUMN_NAME_FILE_URI = "file_uri";

    /*
     * Create and Drop definitions
     */
    public static final String CREATE_TABLE =
      "CREATE TABLE " + TABLE_NAME + " (" +
          _ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
          COLUMN_NAME_DATETIME + DATE_TYPE + COMMA_SEP +
              COLUMN_NAME_FILE_URI + TEXT_TYPE +
          " )";

    public static final String DROP_TABLE = DROP_TABLE_DEFINITION + TABLE_NAME;

    /*
     * URI definitions
     */
    private static final String PATH_ITEMS = "/items";
    private static final String PATH_ITEMS_ID = PATH_ITEMS + "/";
    public static final int ITEM_ID_PATH_POSITION = 1;
    public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS);
    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS_ID);
    public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS_ID + "/#");

    /*
     * MIME type definitions
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.proygrad.sample.simple";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.proygrad.sample.simple";

    /*
     * Projections
     */
    public static final String[] ITEM_PROJECTION = new String[] {
        Item._ID,
        COLUMN_NAME_DATETIME,
            COLUMN_NAME_FILE_URI
    };
  }
}
