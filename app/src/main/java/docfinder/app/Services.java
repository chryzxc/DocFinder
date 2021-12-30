package docfinder.app;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Services {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void displayServices(Context context, String id) {
       MainActivity.service_myLists.clear();
       MainActivity.service_adapter = new ServicesAdapter(MainActivity.service_myLists, context);
       MainActivity.service_rv.setAdapter(MainActivity.service_adapter);
       MainActivity.service_adapter.notifyDataSetChanged();

        if (MainActivity.currentServices != null){
            MainActivity.currentServices.remove();
        }

        MainActivity.currentServices =  db.collection("Facility").document(id).collection("Services").orderBy("service", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (!snapshots.isEmpty()){
                    MainActivity.service_myLists.clear();
                    //MainActivity.service_adapter = new ServicesAdapter(MainActivity.service_myLists, context);
                  //  MainActivity.service_rv.setAdapter(MainActivity.service_adapter);
                 //   MainActivity.service_adapter.notifyDataSetChanged();

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
                            MainActivity.service_myLists.add(new ServicesList(document.getId(),document.getString("service")));

                        }


                    }

                    MainActivity.service_adapter = new ServicesAdapter(MainActivity.service_myLists, context);
                    MainActivity.service_rv.setAdapter(MainActivity.service_adapter);
                    MainActivity.service_adapter.notifyDataSetChanged();





                } else {
                    MainActivity.service_myLists.clear();
                    MainActivity.service_adapter = new ServicesAdapter(MainActivity.service_myLists, context);
                    MainActivity.service_rv.setAdapter(MainActivity.service_adapter);
                    MainActivity.service_adapter.notifyDataSetChanged();

                    // Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
                }




                if (MainActivity.currentViewId.matches(firebaseAuth.getCurrentUser().getUid())){
                    MainActivity.servicesLayout.setVisibility(VISIBLE);
                    MainActivity.services_edit.setVisibility(VISIBLE);
                }else{

                    MainActivity.services_edit.setVisibility(GONE);
                    if (MainActivity.service_myLists.isEmpty()){
                        MainActivity.servicesLayout.setVisibility(GONE);
                    }else{
                        MainActivity.servicesLayout.setVisibility(VISIBLE);
                    }

                }







            }
        });


    }
}




