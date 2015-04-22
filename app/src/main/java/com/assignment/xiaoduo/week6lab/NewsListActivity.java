package com.assignment.xiaoduo.week6lab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewsListActivity extends Activity {

    List<News> NewsList;
    ListView newsList_lv;
    NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news_list);
        newsList_lv = (ListView) this.findViewById(R.id.news_list_lv);
        NewsList = new ArrayList<News>();
        adapter = new NewsAdapter(this, NewsList);
        newsList_lv.setAdapter(adapter);
        newsList_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse(NewsList.get(arg2).getNewsLink()));
                Intent intent = new Intent(NewsListActivity.this, WebViewActivity.class);
                intent.putExtra("newsLink",NewsList.get(arg2).getNewsLink());
                intent.putExtra("newsTitle",NewsList.get(arg2).getTitle());
                startActivity(intent);
            }

        });
        LoadNews load = new LoadNews(this);
        load.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class LoadNews extends AsyncTask<String, String, String> {

        Context mContext;

        public LoadNews(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... args) {
            if(NewsList!=null)
            {
                NewsList.clear();
            }else
            {
                NewsList = new ArrayList<News>();
            }

            JSONObject object = RequestHelper.get("http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://www.abc.net.au/news/feed/51120/rss.xml&num=-1");
            try{
                JSONArray entries = object.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries");
                for(int i = 0 ; i < entries.length(); i ++)
                {
                    News news = new News();
                    JSONObject jo = entries.getJSONObject(i);
                    news.setTitle(jo.getString("title").toString());
                    news.setDescription(clearForContent(jo.getString("content").toString()));
                    news.setNewsLink(jo.getString("link").toString());
                    String imageUrl;
                    if(jo.has("mediaGroups"))
                    {
                        imageUrl = jo.getJSONArray("mediaGroups").getJSONObject(0).getJSONArray("contents").getJSONObject(0).getJSONArray("thumbnails").getJSONObject(0).getString("url");
                        news.setImageLink(imageUrl);
                    }
                    NewsList.add(news);
                }
                return "success";
            }catch(JSONException e)
            {
                e.printStackTrace();
                return "fail";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("success"))
            {
                adapter.notifyDataSetChanged();
            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        NewsListActivity.this);
                // set title
                alertDialogBuilder.setTitle("Network anomaly!");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Check your network connection!")
                        .setCancelable(false)
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                LoadNews load = new LoadNews(NewsListActivity.this);
                                load.execute();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        }
    }

    //get rid of <p> and </p>
    public String clearForContent(String content)
    {
        return content.replace("<p>","").replace("</p>","");
    }
}
