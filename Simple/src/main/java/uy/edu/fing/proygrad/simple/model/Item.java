package uy.edu.fing.proygrad.simple.model;

import android.content.ContentValues;

import uy.edu.fing.proygrad.simple.db.SampleContract;

/**
 * @author gonzalomelov
 */
public class Item extends BaseModel{
  private String datetime;
  private String fileUri;

  public Item(String datetime, String fileUri) {
    this.datetime = datetime;
    this.fileUri = fileUri;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public String getPhotoUri() {
    return fileUri;
  }

  public void setPhotoUri(String fileUri) {
    this.fileUri = fileUri;
  }

  @Override
  public ContentValues toContentValues() {
    ContentValues values = new ContentValues();
    values.put(SampleContract.Item.COLUMN_NAME_DATETIME, this.getDatetime());
    values.put(SampleContract.Item.COLUMN_NAME_FILE_URI, this.getPhotoUri());
    return values;
  }
}
