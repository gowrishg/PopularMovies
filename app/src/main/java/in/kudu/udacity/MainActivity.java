package in.kudu.udacity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.spotify_streamer).setOnClickListener(this);
        findViewById(R.id.scorer_app).setOnClickListener(this);
        findViewById(R.id.library_app).setOnClickListener(this);
        findViewById(R.id.build_it_bigger).setOnClickListener(this);
        findViewById(R.id.xyz_reader).setOnClickListener(this);
        findViewById(R.id.capstone).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View view) {
        String buttonToastText = getResources().getString(R.string.button_click_toast);
        switch (view.getId()) {
            case R.id.spotify_streamer:
            case R.id.scorer_app:
            case R.id.library_app:
            case R.id.build_it_bigger:
            case R.id.xyz_reader:
            case R.id.capstone:
                String toastMsg = String.format(buttonToastText, new String[] { ((Button) view).getText().toString() } );
                Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
