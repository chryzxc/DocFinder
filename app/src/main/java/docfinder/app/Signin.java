package docfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Signin extends AppCompatActivity {

    Button loginButton,verificationButton,registerButton;
    EditText loginNumber,verificationCode;
    private ProgressDialog progressDialog;
    private String verificationId;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    KProgressHUD hud;

    TextView verifyBack,verificationResend;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;

    LinearLayout loginLayout;
    ConstraintLayout registerLayout;
    ConstraintLayout verificationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        transparentStatusBar();
        ImageView signinCover = (ImageView) findViewById(R.id.signinCover);
        ImageView registerCover = (ImageView) findViewById(R.id.registerCover);
        Glide.with(this).load(R.drawable.signin).fitCenter().centerCrop().into(signinCover);
        Glide.with(this).load(R.drawable.signin).fitCenter().centerCrop().into(registerCover);
        db = FirebaseFirestore.getInstance();

        loginButton = findViewById(R.id.loginButton);
        verificationButton = findViewById(R.id.verificationButton);


        loginNumber = findViewById(R.id.loginNumber);
        verificationCode = findViewById(R.id.verificationCode);
        verificationResend = findViewById(R.id.verificationResend);
        verificationResend.setPaintFlags(verificationResend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginLayout = findViewById(R.id.loginLayout);
        verificationLayout = findViewById(R.id.verificationLayout);
        registerLayout = findViewById(R.id.registerLayout);

        verifyBack = findViewById(R.id.verifyBack);
        verifyBack.setPaintFlags(verifyBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        verifyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginLayout.setVisibility(VISIBLE);
                verificationLayout.setVisibility(GONE);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = loginNumber.getText().toString().trim();
                if (TextUtils.isEmpty(number)){
                    loginNumber.setError("Phone number is required");
                    loginNumber.requestFocus();
                    hideKeyboard();
                    return;

                }else if (number.length() != 13){
                    loginNumber.setError("Phone number is incomplete");
                    loginNumber.requestFocus();
                    hideKeyboard();
                    return;
                }else{
                    hideKeyboard();
                    verification(number);

                }
            }
        });

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verificationCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)){
                    verificationCode.setError("Verification code is required");
                    verificationCode.requestFocus();
                    hideKeyboard();
                    return;

                }else{
                    hideKeyboard();
                    verifyPhoneNumber(verificationId,code);

                }


            }
        });




        firebaseAuth = FirebaseAuth.getInstance();

        if (hud !=null){
            hud.dismiss();
        }
        hud = KProgressHUD.create(Signin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

      //  progressDialog = new ProgressDialog(this);



        onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signInCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                hud.dismiss();
              //  progressDialog.dismiss();
                Toast.makeText(Signin.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
                forceResendingToken = token;


                hud.dismiss();
              //  progressDialog.dismiss();
                loginLayout.setVisibility(GONE);
                verificationLayout.setVisibility(VISIBLE);
                verificationCode.setText("");

            }
        };

        verificationResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = loginNumber.getText().toString().trim();
                resendVerification(number,forceResendingToken);
            }
        });






    }


    public void checkIfRegistered(String number){

        db.collection("Users")
                .whereEqualTo("phone", number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            registerLayout.setVisibility(VISIBLE);
                            verificationLayout.setVisibility(GONE);
                            Button registerButton = (Button) findViewById(R.id.registerButton);
                            EditText registerName = (EditText)findViewById(R.id.registerName);
                            EditText registerAddress = (EditText)findViewById(R.id.registerAddress);

                            registerButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String name = registerName.getText().toString().trim();
                                    String address = registerAddress.getText().toString().trim();

                                    if (name.isEmpty()){
                                        registerName.setError("Name is required");
                                        registerName.requestFocus();
                                        return;
                                    }

                                    if (address.isEmpty()){
                                        registerAddress.setError("Address is required");
                                        registerAddress.requestFocus();
                                        return;
                                    }


                                    Date currentTime = Calendar.getInstance().getTime();


                                    Map<String, Object> data = new HashMap<>();

                                    data.put("name", registerName.getText().toString());
                                    data.put("phone", firebaseAuth.getCurrentUser().getPhoneNumber());
                                    data.put("date_registered", currentTime);
                                    data.put("address", registerAddress.getText().toString());



                                    DocumentReference documentReference = db.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
                                    documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(Signin.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });

                        }else{
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }

                });


    }



    public void verification(String number){

        hud.setLabel("Verifying phone number");
        hud.setCancellable(false);
        hud.show();

      //  progressDialog.setMessage("Verifying phone number");
      //  progressDialog.show();

        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }

    public void resendVerification(String number, PhoneAuthProvider.ForceResendingToken token){
        hud.setLabel("Resending verification code");
        hud.setCancellable(false);
        hud.show();


      //  progressDialog.setMessage("Resending verification code");
     //   progressDialog.show();

        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setForceResendingToken(token)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

    }

    public void verifyPhoneNumber(String verificationId, String code){
        hud.setLabel("Verifying code");
        hud.setCancellable(false);
        hud.show();

      //  progressDialog.setMessage("Verifying code");
     //   progressDialog.show();

        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,code);
        signInCredential(phoneAuthCredential);
    }

    public void signInCredential(PhoneAuthCredential phoneAuthCredential){


        hud.setLabel("Logging in");
      //  progressDialog.setMessage("Logging in");

        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        hud.dismiss();
                       // progressDialog.dismiss();
                        checkIfRegistered(firebaseAuth.getCurrentUser().getPhoneNumber());

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hud.dismiss();
              //  progressDialog.dismiss();
                Toast.makeText(Signin.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }







    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
               getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
          //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }



    public static void setWindowFlag(Signin activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}