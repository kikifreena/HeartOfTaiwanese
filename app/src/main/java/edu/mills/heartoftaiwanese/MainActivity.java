package edu.mills.heartoftaiwanese;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFilesTask().execute(
                        ((EditText) findViewById(R.id.editText))
                        .getText().toString()
                );
            }
        });

    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, Boolean> {
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param s The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        private Word w;

        @Override
        protected Boolean doInBackground(String... s) {
            w = new Word(s[0]);
            return w.run();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (status){
                Log.d("MainActivity-Taiwanese", w.toString());
                TextView t = findViewById(R.id.result);
                t.setText(w.toString());
            }
            else {
                String error = MainActivity.this.getText(R.string.error).toString();
                Toast.makeText(MainActivity.this, error , Toast.LENGTH_SHORT);
            }
            super.onPostExecute(status);
        }
    }
}