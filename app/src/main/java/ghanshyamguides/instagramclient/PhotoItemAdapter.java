package ghanshyamguides.instagramclient;

/**
 * Created by hitpanchal on 12/2/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PhotoItemAdapter extends ArrayAdapter<ghanshyamguides.instagramclient.PhotoItem> {

    public PhotoItemAdapter(Context context, ArrayList<ghanshyamguides.instagramclient.PhotoItem> todoItems) {
        super(context, 0, todoItems);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        ghanshyamguides.instagramclient.PhotoItem item = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_photo_item, parent, false);
        }

        ImageView imgView = (ImageView) view.findViewById(R.id.imageView);

        TextView imgUserName = (TextView) view.findViewById(R.id.imgUserName);
        imgUserName.setText(item.getImageUserName() + " :-");

        TextView imgCaption = (TextView) view.findViewById(R.id.imgCaption);
        imgCaption.setText(item.getImageCaption());

        /*WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenwidth = size.x;
        int screenheight = size.y;

    float fittedimgheight;
        float fittedimgwidth;
        fittedimgwidth = (float) screenwidth;
        fittedimgheight = fittedimgwidth * ((float) item.imgheight / (float) item.imgWidth);*/


        Picasso.with(getContext()).load(item.getImageURL()).into(imgView);

        return view;
    }
}
