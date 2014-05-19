package uy.edu.fing.proygrad.simple.model;

import android.content.ContentValues;

import uy.edu.fing.proygrad.simple.db.SampleContract;

/**
 * @author gonzalomelov
 */
public class Item extends BaseModel{
  private String datetime;
  private String photoUri;

  public Item(String datetime, String photoUri) {

    this.datetime = datetime;
    this.photoUri = photoUri;
  }

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public String getPhotoUri() {
    return photoUri;
  }

  public void setPhotoUri(String photoUri) {
    this.photoUri = photoUri;
  }

  @Override
  public ContentValues toContentValues() {
    ContentValues values = new ContentValues();
    values.put(SampleContract.Item.COLUMN_NAME_DATETIME, this.getDatetime());
    values.put(SampleContract.Item.COLUMN_NAME_PHOTO_URI, this.getPhotoUri());
    return values;
  }
}
