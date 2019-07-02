package com.pulimoottil.richu.momsmagickeralafoodrecipe;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportProblem extends AppCompatActivity {

    EditText input_email,input_title,input_desc;
    Button submit;
    TextInputLayout input_layout_email,input_layout_title,input_layout_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        input_layout_desc = findViewById(R.id.input_layout_desc);
        input_layout_title = findViewById(R.id.input_layout_title);
        input_layout_email = findViewById(R.id.input_layout_email);
        input_desc = findViewById(R.id.input_desc);
        input_email= findViewById(R.id.input_email);
        input_title = findViewById(R.id.input_title);
        input_title.addTextChangedListener(new MyTextWatcher(input_title));
        input_email.addTextChangedListener(new MyTextWatcher(input_email));
        input_desc.addTextChangedListener(new MyTextWatcher(input_desc));
        submit = findViewById(R.id.input_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void submitReport(){
        String email = input_email.getText().toString().trim();
        String title = input_title.getText().toString().trim();
        String desc = input_desc.getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Report");
        String id = myRef.push().getKey();
        myRef.child(id).child("title").setValue(title);
        myRef.child(id).child("email").setValue(email);
        myRef.child(id).child("desc").setValue(desc);
        Toast.makeText(getApplicationContext(), "Thank You for reporting", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,HomeActivity.class);
        finish();
        startActivity(intent);
    }




    private void submitForm() {
        if (!validateTitle()) {
            return;
        }

        if (!validateDesc()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }
        submitReport();
    }

    private boolean validateEmail() {
        String email = input_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            input_layout_email.setError(getString(R.string.err_msg_email));
            requestFocus(input_email);
            return false;
        } else {
            input_layout_email.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateTitle() {
        if (input_title.getText().toString().trim().isEmpty()) {
            input_layout_title.setError(getString(R.string.err_msg_title));
            requestFocus(input_title);
            return false;
        } else {
            input_layout_title.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateDesc() {
        if (input_desc.getText().toString().trim().isEmpty()) {
            input_layout_desc.setError(getString(R.string.err_msg_desc));
            requestFocus(input_desc);
            return false;
        } else {
            input_layout_desc.setErrorEnabled(false);
        }

        return true;
    }




    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_title:
                    validateTitle();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_desc:
                    validateDesc();
                    break;
            }
        }
    }
}
