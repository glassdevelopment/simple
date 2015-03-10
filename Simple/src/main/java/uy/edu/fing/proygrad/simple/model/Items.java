package uy.edu.fing.proygrad.simple.model;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gonzalomelov
 */
public class Items extends BaseList {
  private List<Item> items;

  public Items() {
    items = new ArrayList<Item>();
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public void add(Item item) {
    items.add(item);
  }

  @Override
  public ContentValues[] toContentValues() {
    List<ContentValues> contentValueses = new ArrayList<ContentValues>();
    for(Item item : items) {
      contentValueses.add(item.toContentValues());
    }
    ContentValues[] arrContentValueses = new ContentValues[contentValueses.size()];
    return contentValueses.toArray(arrContentValueses);
  }
}
