package uy.edu.fing.proygrad.simple;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import uy.edu.fing.proygrad.simple.db.SampleContract;

/**
 * Created by gmelo on 5/7/14.
 */
public class ItemsActivity extends Activity {

    private ArrayList<Card> mlcCards = new ArrayList<Card>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Cursor c = this.getContentResolver().query(SampleContract.Item.CONTENT_URI, SampleContract.Item.ITEM_PROJECTION, null, null, null);
        while (c.moveToNext()) {
            File imagefile = new File(c.getString(c.getColumnIndex(SampleContract.Item.COLUMN_NAME_PHOTO_URI)));
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bm = BitmapFactory.decodeStream(fis);

            // Create a card with a full-screen background image.
            Card card2 = new Card(this);
            card2.setText("One word description for picture no.: " + c.getPosition());
            card2.setFootnote(c.getString(c.getColumnIndex(SampleContract.Item.COLUMN_NAME_DATETIME)));
            card2.setImageLayout(Card.ImageLayout.FULL);
            card2.addImage(bm);
            mlcCards.add(card2);
        }

        CardScrollView csvCardsView = new CardScrollView(this);
        CsaAdapter cvAdapter = new CsaAdapter();
        csvCardsView.setAdapter(cvAdapter);
        csvCardsView.activate();
        setContentView(csvCardsView);
    }

    private class CsaAdapter extends CardScrollAdapter
    {
        @Override
        public int getCount()
        {
            return mlcCards.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mlcCards.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return mlcCards.get(position).getView();
        }

        @Override
        public int getPosition(Object o)
        {
            return 0;
        }
    }

}
