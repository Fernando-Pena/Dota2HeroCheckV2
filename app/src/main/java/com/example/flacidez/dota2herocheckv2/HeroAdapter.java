package com.example.flacidez.dota2herocheckv2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HeroAdapter extends ArrayAdapter<Hero> {

    Context mContext;
    ArrayList<Hero> mHeroes;

    public class ViewHolder {
        public final TextView textViewHeroName;
        public final ImageView imageViewHeroPicture;

        public ViewHolder(View view) {
            textViewHeroName = (TextView) view.findViewById(R.id.textView_hero_name);
            imageViewHeroPicture = (ImageView) view.findViewById(R.id.imageView_hero_picture);
        }
    }

    public HeroAdapter(Context context, ArrayList<Hero> heroes) {
        super(context, R.layout.hero_list_item, heroes);
        mContext = context;
        mHeroes = heroes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = reuseOrGenerateRowView(convertView, parent);
        displayContentInRowView(position, rowView);
        return rowView;
    }

    private void displayContentInRowView(final int position, View rowView) {
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.textViewHeroName.setText(mHeroes.get(position).getLocalized_name());
        fetchHeroPicture(mHeroes.get(position).getPicture(), viewHolder.imageViewHeroPicture);
    }

    private View reuseOrGenerateRowView(View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.hero_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        return rowView;
    }

    private void fetchHeroPicture(String url, final ImageView imageView){
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });


            }
        });
    }
}
