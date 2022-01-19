package docfinder.app;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Verify {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void displayVerify(Context context) {



        db.collection("Users").orderBy("name").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (!snapshots.isEmpty()){

                    VerifyAccount.verify_myLists.clear();
                    VerifyAccount.verify_adapter = new VerifyAdapter(VerifyAccount.verify_myLists, context);
                    VerifyAccount.verify_rv.setAdapter(VerifyAccount.verify_adapter);
                    VerifyAccount.verify_adapter.notifyDataSetChanged();


                    if (error != null) {
                        Toast.makeText(context, "Listen failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (DocumentSnapshot document : snapshots.getDocuments()) {

                        if (document.exists()){
                            if(document.getBoolean("verified") == null || document.getBoolean("verified") == false){
                                VerifyAccount.verify_myLists.add(new VerifyList(document.getId()));
                            }


                        }


                    }

                    VerifyAccount.verify_adapter = new VerifyAdapter(VerifyAccount.verify_myLists, context);
                    VerifyAccount.verify_rv.setAdapter(VerifyAccount.verify_adapter);
                    VerifyAccount.verify_adapter.notifyDataSetChanged();

                }

            }
        });


    }

}
