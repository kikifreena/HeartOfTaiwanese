package edu.mills.heartoftaiwanese;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText chineseEditText = findViewById(R.id.editText);
        final EditText englishEditText = findViewById(R.id.editTextEng);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.submit_ch);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.tw_result).setVisibility(View.GONE);
                findViewById(R.id.result).setVisibility((View.GONE));
                new ParseWordTask().execute((new LanguageContainer(chineseEditText
                        .getText().toString(), Word.LANGUAGE_CHINESE))
                );
            }
        });
        Button b2 = findViewById(R.id.submit_en);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.tw_result).setVisibility(View.GONE);
                findViewById(R.id.result).setVisibility((View.GONE));
                new ParseWordTask().execute((new LanguageContainer(englishEditText
                        .getText().toString(), Word.LANGUAGE_ENGLISH))
                );

            }
        });
        Button clearButton = findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                chineseEditText.setText("");
                englishEditText.setText("");
                findViewById(R.id.tw_result)
                        .setVisibility(View.GONE);
                findViewById(R.id.result)
                        .setVisibility(View.GONE);
            }
        });
    }

    private class LanguageContainer {
        private final int lang;
        private final String text;

        LanguageContainer(String input, int languageCode) {
            text = input;
            lang = languageCode;
        }

        int getLanguage() {
            return lang;
        }

        String getText() {
            return text;
        }
    }

    private class ParseWordTask extends AsyncTask<LanguageContainer, Integer, Boolean> {
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        private Word w;

        @Override
        protected Boolean doInBackground(@org.jetbrains.annotations.NotNull LanguageContainer... lang) {
            w = new Word(lang[0].getText(), lang[0].getLanguage());
            return w.run();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (status) {
                Log.d("MainActivity-Taiwanese", w.toString());
                findViewById(R.id.tw_result).setVisibility(View.VISIBLE);
                TextView t = findViewById(R.id.result);
                t.setVisibility(View.VISIBLE);
                t.setText(w.getTaiwanese());
            } else {
                String error = MainActivity.this.getText(R.string.error).toString();
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(status);
        }
    }
}