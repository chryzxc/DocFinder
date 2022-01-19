package docfinder.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;


public class VerifyAccount extends AppCompatActivity {

    static List<VerifyList> verify_myLists;
    static RecyclerView verify_rv;
    static VerifyAdapter verify_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.lightbrown));

        verify_rv = (RecyclerView) findViewById(R.id.verify_rec);
        verify_rv.setHasFixedSize(true);
        verify_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        verify_myLists = new ArrayList<>();


        Verify verify = new Verify();
        verify.displayVerify(this.getApplicationContext());


    }
}