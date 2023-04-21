package com.example.funwithregex;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.funwithregex.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //
        // View model
        //
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        //
        // UI
        //
        EditText testTextInputView = findViewById(R.id.test_text);

        // Regex input field
        EditText regexInputView = findViewById(R.id.regex_input);
        TextInputLayout regexInputLayout = findViewById(R.id.regex_input_layout);

        // Start search...
        regexInputLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testText=testTextInputView.getText().toString();
                SpannableString t=new SpannableString(testText);
                t.setSpan(0, 0, t.length(), 0);
                mainViewModel.testText.setValue(t);

                String regex=regexInputView.getText().toString();
                regexInputView.setText("");
                mainViewModel.regexText.setValue(regex);

                regexInputLayout.setError(null);
                mainViewModel.runRegex();
            }
        });

        // Pop up
        regexInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.regex_input_pop_up, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mainViewModel.regexText.postValue("666");


                        return false;
                    }
                });
            }
        });

        //
        // Observers.
        //
        mainViewModel.getRegexText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                regexInputView.getText().insert(regexInputView.getSelectionStart(),s);
            }
        });

        mainViewModel.getTestText().observe(this, new Observer<SpannableString>() {
            @Override
            public void onChanged(SpannableString s) {
                testTextInputView.setText(s);
            }
        });

        mainViewModel.getErrorText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.isEmpty())
                    regexInputLayout.setError(null);
                else
                    regexInputLayout.setError(s);
            }
        });

        mainViewModel.getResultsFound().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                regexInputLayout.setHelperText  (integer+"");
            }
        });

        }
}