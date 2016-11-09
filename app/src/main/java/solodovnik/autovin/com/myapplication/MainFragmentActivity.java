package solodovnik.autovin.com.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainFragmentActivity extends AppCompatActivity {
    //private Intent about;
    private Intent full_inf;
    private Intent abouts;
    private Intent contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new MainFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer,fragment)
                    .commit();
        }

        //about = new Intent(MainFragmentActivity.this, About.class);
        full_inf = new Intent(MainFragmentActivity.this, Full_inf.class);
        abouts = new Intent(MainFragmentActivity.this, Abouts.class);
        contacts = new Intent(MainFragmentActivity.this, Contacts.class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popmenu, menu);
        return true;
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
