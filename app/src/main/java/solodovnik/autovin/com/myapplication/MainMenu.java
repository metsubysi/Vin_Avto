package solodovnik.autovin.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainMenu extends Activity {
    public Intent  mains, about, contacts ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);
        mains = new Intent(MainMenu.this, MainActivity.class);
        about = new Intent(MainMenu.this, About.class);
        contacts = new Intent(MainMenu.this, Contacts.class);
    }
    public void mains(View v)
    {startActivity(mains);}
    public void about(View v)
    {startActivity(about);}
    public void contacts(View v)
    {startActivity(contacts);}
}
