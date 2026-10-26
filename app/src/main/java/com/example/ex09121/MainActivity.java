package com.example.ex09121;

import static com.example.ex09121.FBRef.refAuth;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity implements View.OnCreateContextMenuListener {
    EditText et1, et2;
    ImageButton ib;
    TextView tv;
    private EditText eTEmail, eTPass;
    private TextView tvMsg;
   private CheckBox cb;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1=findViewById(R.id.num1);
        et2=findViewById(R.id.num2);
        ib=findViewById(R.id.ib);
        tv=findViewById(R.id.res);
        //רישום הכפתור לתפריט צף
        ib.setOnCreateContextMenuListener(this);
        registerForContextMenu(ib);
        eTEmail = findViewById(R.id.eTEmail);
        eTPass = findViewById(R.id.eTPass);
        tvMsg = findViewById(R.id.tvMsg);
        cb = findViewById(R.id.cb);
    }

    //הפעולה הראשונה: יצירת תפריט
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.setHeaderTitle("בחר פעולה חשבונית");
    }
    //פעולה שנייה:טיפול בבחירה וביצוע מחדש
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        String s1=et1.getText().toString();
        String s2=et2.getText().toString();

        if(s1.isEmpty() || s2.isEmpty()){
            Toast.makeText(this,"נא להזין שני מספרים", Toast.LENGTH_SHORT).show();
            return true;
        }
        double n1 =Double.parseDouble(s1);
        double n2 =Double.parseDouble(s2);
        double result=0;
        int id =item.getItemId();

        if (id== R.id.menuPlus){
            result = n1 + n2;
        } else  if (id== R.id.menuMinus){
            result = n1 - n2;
        }else  if (id== R.id.menuMult){
            result = n1 * n2;
        }
        else  if (id== R.id.menuDiv){
            if(n2!=0) {
                result = n1 / n2;
            } else {
            tv.setText("שגיאה:חילוק ב-0");
            return true;
        }
        }
        tv.setText(" תוצאה:" +result);
        return true;
    }
    public void createUser(View view){
        String email = eTEmail.getText().toString();
        String pass = eTPass.getText().toString();
        if (email.isEmpty() || pass.isEmpty()){
            tvMsg.setText("please fill all fields");
        } else {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Connecting");
            pd.setMessage("Creating user...");
            pd.show();
            refAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        sharedPref.edit().putBoolean("stayConnect", cb.isChecked()).apply();
                        Log.i("MainActivity", "createUserWithEmailAndPassword:success");
                        FirebaseUser user = refAuth.getCurrentUser();
                        tvMsg.setText("user created successfully\nUid: " + user.getUid());
                    } else {
                        Exception exp = task.getException();
                        if (exp instanceof FirebaseAuthInvalidUserException) {
                            tvMsg.setText("Invalid email address");
                        } else if (exp instanceof FirebaseAuthWeakPasswordException) {
                            tvMsg.setText("Password too weak");
                        } else if (exp instanceof FirebaseAuthUserCollisionException) {
                            tvMsg.setText("User already exists");
                        } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                            tvMsg.setText("General authentication failure");
                        } else if (exp instanceof FirebaseNetworkException) {
                            tvMsg.setText("Network error. Please check your connection and try again.");
                        } else {
                            tvMsg.setText("An error occurred. Please try again later.");
                        }
                    }
                }

        });

        }
    }
    public void loginUser(View view){
        String email = eTEmail.getText().toString();
        String pass = eTPass.getText().toString();
        if (email.isEmpty() || pass.isEmpty()){
            tvMsg.setText("please fill all fields");
        } else {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Connecting");
            pd.setMessage("Logging in user...");
            pd.show();
            refAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        Log.i("MainActivity", "createUserWithEmailAndPassword:success");
                        FirebaseUser user = refAuth.getCurrentUser();
                        tvMsg.setText("user logged successfully");
                    } else {
                        Exception exp = task.getException();
                        if (exp instanceof FirebaseAuthInvalidUserException) {
                            tvMsg.setText("Invalid email address");
                        } else if (exp instanceof FirebaseAuthWeakPasswordException) {
                            tvMsg.setText("Password too weak");
                        } else if (exp instanceof FirebaseAuthUserCollisionException) {
                            tvMsg.setText("User already exists");
                        } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                            tvMsg.setText("General authentication failure");
                        } else if (exp instanceof FirebaseNetworkException) {
                            tvMsg.setText("Network error. Please check your connection and try again.");
                        } else {
                            tvMsg.setText("An error occurred. Please try again later.");
                        }
                    }
                }

            });

        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isChecked = sharedPref.getBoolean("stayConnect", false);
        if (refAuth.getCurrentUser() != null && isChecked) {
           cb.setChecked(true);
        }
    }
}