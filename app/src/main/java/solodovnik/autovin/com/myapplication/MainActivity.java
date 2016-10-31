package solodovnik.autovin.com.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {
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
    private Intent about;
    private Intent full_inf;
    private Intent abouts;
    private Intent contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        about = new Intent(MainActivity.this, About.class);
        contacts = new Intent(MainActivity.this, Contacts.class);
        abouts = new Intent(MainActivity.this, Abouts.class);
        full_inf = new Intent(MainActivity.this, Full_inf.class);
        // определение данных
        massiv1 = new String[100];
        massiv2 = new String[100];
        lv = (ListView) findViewById(R.id.listView1);
        edit = (EditText) findViewById(R.id.editText);
        edit.setText("WBAPK73559A452247");
        buttonCheck = (Button) findViewById(R.id.buttonCheck);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCheckButton(v);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popmenu, menu);
        return true;
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
                int[] to = {R.id.text1, R.id.textview};

                // Instantiating an adapter to store each items
                // R.layout.listview_layout defines the layout of each item
                adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_item, from, to);
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
        vin = edit.getText().toString();

        if (vin == null || vin.length() != 17) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout));
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            // Toast toast = Toast.makeText(getApplicationContext(),this.getString(R.string.message), Toast.LENGTH_SHORT);toast.show();
        } else {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            lv.setVisibility(ListView.INVISIBLE);
            NewThread mt = new NewThread();
            mt.execute();
        }
    }

    public void about(View v) {
        startActivity(about);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();

        //TextView infoTextView = (TextView) findViewById(R.id.textView4);

        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_full_inf:
                startActivity(full_inf);
                break;

            case R.id.action_contacts:
                startActivity(contacts);
                break;

            case R.id.action_about:
                startActivity(abouts);
                break;

            case R.id.action_exit:
                //infoTextView.setText("Вы выбрали котёнка!");
                break;
        }
        return true;
    }
}