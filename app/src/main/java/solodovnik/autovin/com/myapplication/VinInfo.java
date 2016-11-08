package solodovnik.autovin.com.myapplication;

import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by grez on 04.11.2016.
 */

public class VinInfo {
    public static String[] from = {"itemHead", "itemInfo"};
    private Elements title;
    private String mVin;
    private List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();

    public VinInfo(String vin){
        mVin = vin;
    }

    public boolean fillData(){
        Document doc;

        try {
            // определяем откуда будем воровать данные
            doc = Jsoup.connect("http://avtostat.com/proverka-avto?vin=" + mVin).get();
            // задаем с какого места, я выбрал заголовке статей

            Element tab = doc.getElementsByTag("table").get(0);
            title = tab.getElementsByTag("td");//doc.select("table.mytables");
            // и в цикле захватываем все данные какие есть на странице
            aList = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < title.size(); i++) {
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("itemHead", title.get(i).text());
                i++;
                hm.put("itemInfo", title.get(i).text());
                aList.add(hm);
            }
/*

            // Ids of views in listview_layout
            int[] to = {R.id.text_itemHead, R.id.text_itemInfo};*/

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HashMap<String, Object>> getaList() {
        return aList;
    }

    public String getVin() {
        return mVin;
    }
}
