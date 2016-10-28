package solodovnik.autovin.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class About extends Activity {
public Intent back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        back = new Intent(About.this, MainActivity.class);
    }
    public void backs(View v)
    {startActivity(back);}
}
