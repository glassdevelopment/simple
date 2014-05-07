package uy.edu.fing.proygrad.simple;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gmelo on 5/7/14.
 */
public class ItemsActivity extends Activity {

    private ArrayList<Card> mlcCards = new ArrayList<Card>();
    private ArrayList<String> mlsText = new ArrayList<String>(Arrays.asList("Hello", "World"));

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < mlsText.size(); i++)
        {
            String filepath = "/sdcard/Pictures/CameraAPIDemo/Picture_20140607120624.jpg";
            File imagefile = new File(filepath);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bm = BitmapFactory.decodeStream(fis);

            // Create a card with a full-screen background image.
            Card card2 = new Card(this);
            card2.setText("One word description");
            card2.setFootnote("11 May 2014");
            card2.setImageLayout(Card.ImageLayout.FULL);
            card2.addImage(bm);
            mlcCards.add(card2);

            /*Card newCard = new Card(this);
            newCard.setImageLayout(Card.ImageLayout.FULL);
            newCard.setText(mlsText.get(i));
            mlcCards.add(newCard);*/
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
