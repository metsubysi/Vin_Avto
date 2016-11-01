package solodovnik.autovin.com.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by grez on 01.11.2016.
 */

public class MainFragment extends Fragment {

    // благодоря этому классу мы будет разбирать данные на куски
    private Elements title;
    // то в чем будем хранить данные пока не передадим адаптеру
    private ArrayList<String> titleList = new ArrayList<String>();
    // Listview Adapter для вывода данных

    private String[] massiv1;
    private String[] massiv2;
    String vin;
    private List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();

    // List view
    private EditText edit;
    private ProgressBar progressBar;
    private Button buttonCheck;

    // Listview Adapter для вывода данных
    private SimpleAdapter adapter;
    private ListView lv;
    /*private Intent about;
    private Intent full_inf;
    private Intent abouts;
    private Intent contacts;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main,null);

        /* about = new Intent(MainActivity.this, About.class);
        contacts = new Intent(MainActivity.this, Contacts.class);
        abouts = new Intent(MainActivity.this, Abouts.class);
        full_inf = new Intent(MainActivity.this, Full_inf.class);*/
        // определение данных
        massiv1 = new String[100];
        massiv2 = new String[100];
        lv = (ListView) v.findViewById(R.id.listView1);
        edit = (EditText) v.findViewById(R.id.editText);
        edit.setText("WBAPK73559A452247");
        buttonCheck = (Button) v.findViewById(R.id.buttonCheck);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCheckButton(v);
            }
        });
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);


        return v;
    }

    /**
     * А вот и внутрений класс который делает запросы, если вы не читали статьи у меня в блоге про отдельные
     * потоки советую почитать
     */
    public class NewThread extends AsyncTask<String, Void, String> {

        // Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
        // нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред!!!
        @Override
        protected String doInBackground(String... arg) {

            // класс который захватывает страницу
            Document doc;

            try {
                // определяем откуда будем воровать данные
                doc = Jsoup.connect("http://avtostat.com/proverka-avto?vin=" + vin).get();
                // задаем с какого места, я выбрал заголовке статей

                Element tab = doc.getElementsByTag("table").get(0);
                title = tab.getElementsByTag("td");//doc.select("table.mytables");
                // чистим наш аррей лист для того что бы заполнить
                titleList.clear();
                // и в цикле захватываем все данные какие есть на странице
                boolean b = true;
                int i1 = 0, i2 = 0;
                for (Element titles : title) {
                    // записываем в аррей лист
                    if (b) {
                        massiv1[i1] = titles.text();
                        i1++;
                    } else {
                        massiv2[i2] = titles.text();
                        i2++;
                    }
                    b = !b;
                }
                aList = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < i2; i++) {
                    HashMap<String, Object> hm = new HashMap<String, Object>();
                    hm.put("massiv1", massiv1[i]);
                    hm.put("massiv2", massiv2[i]);

                    aList.add(hm);
                }
                // Keys used in Hashmap
                String[] from = {"massiv1", "massiv2"};

                // Ids of views in listview_layout
                int[] to = {R.id.text_itemHead, R.id.text_itemInfo};

                // Instantiating an adapter to store each items
                // R.layout.listview_layout defines the layout of each item
                adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.list_item, from, to);
                // titleList.add(titles.text());
                // titleList.add(count+"))");


            } catch (IOException e) {
                e.printStackTrace();
            }
            // ничего не возвращаем потому что я так захотел)
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            // после запроса обновляем листвью
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            lv.setVisibility(ListView.VISIBLE);
            lv.setAdapter(adapter);
        }
    }

    public void onClickCheckButton(View v) {
        Activity a = getActivity();
        vin = edit.getText().toString();

        if (vin == null || vin.length() != 17) {
            LayoutInflater inflater = a.getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) v.findViewById(R.id.toast_layout));
            Toast toast = new Toast(a.getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            // Toast toast = Toast.makeText(getApplicationContext(),this.getString(R.string.message), Toast.LENGTH_SHORT);toast.show();
        } else {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            lv.setVisibility(ListView.INVISIBLE);
            MainFragment.NewThread mt = new MainFragment.NewThread();
            mt.execute();
        }
    }


}
