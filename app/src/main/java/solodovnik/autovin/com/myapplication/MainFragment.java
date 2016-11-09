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

    String vin;

    // List view
    private EditText edit;
    private ProgressBar progressBar;
    private Button buttonCheck;

    // Listview Adapter для вывода данных
    private SimpleAdapter adapter;
    private ListView lv;

    private VinInfo mVinInfo;

    /*private Intent about;
    private Intent full_inf;
    private Intent abouts;
    private Intent contacts;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.activity_main,null);

        /* about = new Intent(MainActivity.this, About.class);
        contacts = new Intent(MainActivity.this, Contacts.class);
        abouts = new Intent(MainActivity.this, Abouts.class);
        full_inf = new Intent(MainActivity.this, Full_inf.class);*/
        // определение данных
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

            mVinInfo = VinInfo.getInstance();
            mVinInfo.setVin(vin);
            if (mVinInfo.fillData() && mVinInfo.aListIsReady()) {
                int[] to = {R.id.text_itemHead, R.id.text_itemInfo};
                adapter = new SimpleAdapter(getActivity().getBaseContext(), mVinInfo.getaList(), R.layout.list_item, mVinInfo.from, to);
            }
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

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            lv.setAdapter(adapter);
        }

    }
}
