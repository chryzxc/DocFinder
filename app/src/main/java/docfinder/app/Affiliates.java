package docfinder.app;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Affiliates {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void displayAffiliates(Context context, String id) {
        MainActivity.affiliates_myLists.clear();
        MainActivity.affiliates_adapter = new AffiliateAdapter(MainActivity.affiliates_myLists, context);
        MainActivity.affiliates_rv.setAdapter(MainActivity.affiliates_adapter);
        MainActivity.affiliates_adapter.notifyDataSetChanged();

        if (MainActivity.currentAffiliations != null){
            MainActivity.currentAffiliations.remove();
        }
        MainActivity.currentAffiliations = db.collection("Facility").document(id).collection("Affiliates").whereEqualTo("approved",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (!snapshots.isEmpty()){
                    MainActivity.affiliates_myLists.clear();

                   // appointment_myLists.clear();

                    if (error != null) {
                        Toast.makeText(context, "Listen failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (firebaseAuth.getCurrentUser() != null) {
                    }
                    for (DocumentSnapshot document : snapshots.getDocuments()) {

                        if (document.exists()){
                            // Toast.makeText(context, document.getString("service"), Toast.LENGTH_SHORT).show();
                            MainActivity.affiliates_myLists.add(new AffiliateList(document.getId()));

                        }


                    }

                    MainActivity.affiliates_adapter = new AffiliateAdapter(MainActivity.affiliates_myLists, context);
                    MainActivity.affiliates_rv.setAdapter(MainActivity.affiliates_adapter);
                    MainActivity.affiliates_adapter.notifyDataSetChanged();


                } else {
                    MainActivity.affiliates_myLists.clear();
                    MainActivity.affiliates_adapter = new AffiliateAdapter(MainActivity.affiliates_myLists, context);
                    MainActivity.affiliates_rv.setAdapter(MainActivity.affiliates_adapter);
                    MainActivity.affiliates_adapter.notifyDataSetChanged();
                   // Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
                }

                if (MainActivity.currentViewId.matches(firebaseAuth.getCurrentUser().getUid())){
                    MainActivity.affiliatesLayout.setVisibility(VISIBLE);
                    MainActivity.affiliates_add.setVisibility(VISIBLE);
                }else{

                    MainActivity.affiliates_add.setVisibility(GONE);
                    if (MainActivity.affiliates_myLists.isEmpty()){
                        MainActivity.affiliatesLayout.setVisibility(GONE);
                    }else{
                        MainActivity.affiliatesLayout.setVisibility(VISIBLE);
                    }

                }







            }
        });


    }
}




