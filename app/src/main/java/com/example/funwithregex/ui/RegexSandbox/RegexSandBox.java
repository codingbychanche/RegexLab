package com.example.funwithregex.ui.RegexSandbox;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.funwithregex.R;
import com.example.funwithregex.databinding.RegexSandboxBinding;
import com.google.android.material.textfield.TextInputLayout;

public class RegexSandBox extends Fragment {

    private RegexSandboxBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = RegexSandboxBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //
        // View model
        //
        RegexSandBoxViewModel regexSandBoxViewModel = new ViewModelProvider(this).get(RegexSandBoxViewModel.class);

        //
        // UI
        //
        EditText testTextInputView = view.findViewById(R.id.test_text);

        // Regex input field
        EditText regexInputView = view.findViewById(R.id.regex_input);
        TextInputLayout regexInputLayout = view.findViewById(R.id.regex_input_layout);

        EditText resultView=view.findViewById(R.id.regex_result_text);

        //
        // Start search...
        //
        regexInputLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testText = testTextInputView.getText().toString();
                SpannableString t = new SpannableString(testText);
                t.setSpan(0, 0, t.length(), 0);
                regexSandBoxViewModel.testText.setValue(t);

                String regex = regexInputView.getText().toString();
                regexInputView.setText("");
                regexSandBoxViewModel.regexText.setValue(regex);

                regexInputLayout.setError(null);
                regexSandBoxViewModel.runRegex(System.currentTimeMillis());
            }
        });

        // Pop up
        regexInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(requireActivity().getApplicationContext(), v);

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.regex_input_pop_up, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        // Clear the current regex input
                        if (item.getItemId() == R.id.clear)
                            regexInputView.setText("");

                        // Insert a backslash
                        if (item.getItemId() == R.id.backslach)
                            regexInputView.getText().insert(regexInputView.getSelectionStart(), "\\");

                        // Insert ?
                        if (item.getItemId() == R.id.can)
                            regexInputView.getText().insert(regexInputView.getSelectionStart(), "?");

                        // Insert a backslash
                        if (item.getItemId() == R.id.or)
                            regexInputView.getText().insert(regexInputView.getSelectionStart(), "|");

                        // Insert paras. and move cursor between them...
                        if (item.getItemId() == R.id.para) {
                            regexInputView.getText().insert(regexInputView.getSelectionStart(), "()");
                            regexInputView.setSelection(regexInputView.getSelectionStart() - 1);
                        }

                        // Insert paras. and move cursor between them...
                        if (item.getItemId() == R.id.curly) {
                            regexInputView.getText().insert(regexInputView.getSelectionStart(), "{}");
                            regexInputView.setSelection(regexInputView.getSelectionStart() - 1);
                        }

                        // Insert paras. and move cursor between them...
                        if (item.getItemId() == R.id.brackets) {
                            regexInputView.getText().insert(regexInputView.getSelectionStart(), "[]");
                            regexInputView.setSelection(regexInputView.getSelectionStart() - 1);
                        }

                        return false;
                    }
                });
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Observers.
        //
        regexSandBoxViewModel.getRegexText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                regexInputView.getText().insert(regexInputView.getSelectionStart(), s);
            }
        });

        regexSandBoxViewModel.getTestText().observe(getViewLifecycleOwner(), new Observer<SpannableString>() {
            @Override
            public void onChanged(SpannableString s) {
                testTextInputView.setText(s);
            }
        });

        regexSandBoxViewModel.getErrorText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.isEmpty())
                    regexInputLayout.setError(null);
                else
                    regexInputLayout.setError(s);
            }
        });

        regexSandBoxViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                resultView.setText(s);
            }
        });

        regexSandBoxViewModel.getResultsFound().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                regexInputLayout.setHelperText(integer + "");
            }
        });


        // final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}