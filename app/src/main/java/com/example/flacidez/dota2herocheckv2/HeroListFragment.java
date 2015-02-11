package com.example.flacidez.dota2herocheckv2;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HeroListFragment extends ListFragment {

    Button mButtonGetHeroes;
    HeroAdapter mHeroAdapter;

    public HeroListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Hero> heroes = new ArrayList<>();
        mHeroAdapter = new HeroAdapter(getActivity(), heroes);
        setListAdapter(mHeroAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hero_list, container, false);
        wireUpButton(rootView);
        return rootView;
    }

    private void wireUpButton(View rootView) {
        mButtonGetHeroes = (Button) rootView.findViewById(R.id.button_get_hero_list);
        mButtonGetHeroes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchHeroesInQueue();
            }
        });
    }

    private URL constructHeroUrlQuery() throws MalformedURLException {
        final String BASE_URL = "api.steampowered.com";
        final String PATH = "IEconDOTA2_570";
        final String PATH_ONE = "GetHeroes";
        final String PATH_TWO = "v0001";
        final String KEY_PARAM = "3F42B6541BCBAD047A1FC336A6D7F2CA";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority(BASE_URL).
                appendPath(PATH).appendPath(PATH_ONE).appendPath(PATH_TWO).
                appendQueryParameter("key", KEY_PARAM).
                appendQueryParameter("language", "en_us");
        return new URL(builder.toString());
    }

    private void fetchHeroesInQueue() {
        try {
            URL url = constructHeroUrlQuery();
            Request request = new Request.Builder().url(url.toString()).build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Query failed!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    String responseString = response.body().string();
                    final ArrayList<Hero> listOfHeroes = parseResponse(responseString);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHeroAdapter.clear();
                            mHeroAdapter.addAll(listOfHeroes);
                            mHeroAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Hero> parseResponse(String response) {
        final String NAME = "name";
        final String ID = "id";
        final String LOCALIZED_NAME = "localized_name";
        final String RESULT = "result";
        final String HEROES = "heroes";
        ArrayList<Hero> heroes = new ArrayList<>();
        try {
            JSONObject responseJSONObject = new JSONObject(response);
            JSONObject resultJSONObject = new JSONObject(responseJSONObject.getString(RESULT));
            JSONArray heroesJSONArray = new JSONArray(resultJSONObject.getString(HEROES));
            JSONObject object;
            for (int i = 0; i < heroesJSONArray.length(); i++) {
                object = heroesJSONArray.getJSONObject(i);
                heroes.add(new Hero(object.getString(NAME), object.getString(ID), object.getString(LOCALIZED_NAME)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return heroes;
    }

}
