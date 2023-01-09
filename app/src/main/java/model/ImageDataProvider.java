package model;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.rxjava3.core.Single;

/***
 * 서버로부터 Image, Title 을 받아오기 위한 Provider.
 *
 * @author Kyeongrak Choi
 * @version 0.0.1
 */
public class ImageDataProvider {
    private static final String TAG = ImageDataProvider.class.getSimpleName();

    private static final String mBaseURL = "https://serpapi.com/search.json?q=%s&tbm=isch&ijn=%d";

    public Single<List<Data>> get(String search, int index) {
        // insert 를 주로 사용하므로 LinkedList
        LinkedList<Data> datas = new LinkedList<>();

        try {
            String encoded = URLEncoder.encode(search, "UTF-8");
            URL url = new URL(String.format(mBaseURL, encoded, index));

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(2000);
                conn.setUseCaches(false);

                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    String jsonString = "";
                    if (is.available() > 0) {
                        byte[] buffer = new byte[is.available()];
                        is.read(buffer);
                        jsonString = new String(buffer);
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String temp;
                        while (!TextUtils.isEmpty(temp = br.readLine())) {
                            jsonString = jsonString + temp;
                        }
                        br.close();
                    }
                    /*
                    {"search_metadata":{}, "search_parameters":{}, "search_information":{}, "images_results":[], "error":{}}
                    error 가 있으면 search_information 이 없다.
                     */
                    JSONObject json = new JSONObject(jsonString);
                    if (!json.has("error")) {
                        JSONArray array = json.getJSONArray("images_results");
                        /*
                        "images_results":[
                        {"potision":, "thumbnail:", "source":, "title":, "link":, "original":, "original_width":, "original_height":, "is_product"}
                        ]
                        original 써야 하지만 데이터 덜쓰기위해 thumbnail 사용한다.
                         */
                        for (int i = 0; i < array.length(); ++i) {
                            JSONObject obj = array.getJSONObject(i);
                            ImageData data = new ImageData();
                            data.setData(obj.getString("thumbnail"), obj.getString("title"));
                            datas.add(data);
                        }
                    }

                    conn.disconnect();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            //에러 상황에서는 onError 로 throwable 회신
            return Single.create(single -> {
                single.onError(e);
            });
        }
        return Single.create(single -> {
            single.onSuccess(datas);
        });
    }
}
