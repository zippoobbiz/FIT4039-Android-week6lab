package com.assignment.xiaoduo.week6lab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by xiaoduo on 4/19/15.
 */
public class NewsAdapter extends BaseAdapter {

    List<News> ReminderList;
    Context context;

    public NewsAdapter(Context context, List<News> list){
        ReminderList = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return ReminderList.size();
    }

    @Override
    public Object getItem(int position) {
        return ReminderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_news_details, parent, false);
        }
        News news = (News)getItem(position);
        TextView item_title_et = (TextView) convertView.findViewById(R.id.item_title_et);
        TextView item_description_et = (TextView) convertView.findViewById(R.id.item_description_et);
//        ImageView item_thumbnail_iv = (ImageView) convertView.findViewById(R.id.item_thumbnail_iv);
        item_title_et.setText(news.getTitle());
        item_description_et.setText(news.getDescription());
        if(news.getImageLink() == null || news.getImageLink().equals(""))
        {
            ImageView item_thumbnail_iv = (ImageView) convertView.findViewById(R.id.item_thumbnail_iv);
            item_thumbnail_iv.setVisibility(View.GONE);
        }else
        {
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.item_thumbnail_iv))
                    .execute(news.getImageLink());
        }

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            bmImage.setImageBitmap(null);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
