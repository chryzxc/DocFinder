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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Add {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    static ArrayList<String> doctorsRequest,doctorsApproved;
    public void displayDoctors(Context context) {

        doctorsRequest = new ArrayList<>();
        doctorsApproved = new ArrayList<>();





        db.collection("Facility").document(MainActivity.currentViewId).collection("Affiliates").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (!snapshots.isEmpty()){

                    for (DocumentSnapshot document : snapshots.getDocuments()) {

                        if (document.exists() && document.getBoolean("approved") != null){
                            if (document.getBoolean("approved") == true){
                               doctorsRequest.add(document.getId());

                            }else{
                                doctorsApproved.add(document.getId());
                            }

                        }

                    }


                    db.collection("Facility").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                            if (!snapshots.isEmpty()){

                                MainActivity.doctors_myLists.clear();
                                MainActivity.doctors_adapter = new DoctorsAdapter(MainActivity.doctors_myLists, context);
                                MainActivity.doctors_rv.setAdapter(MainActivity.doctors_adapter);
                                MainActivity.doctors_adapter.notifyDataSetChanged();

                                // appointment_myLists.clear();

                                if (error != null) {
                                    Toast.makeText(context, "Listen failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (firebaseAuth.getCurrentUser() != null) {
                                }
                                for (DocumentSnapshot document : snapshots.getDocuments()) {

                                    if (document.exists()){
                                        if (!firebaseAuth.getCurrentUser().getUid().matches(document.getId())){
                                            if (!doctorsRequest.contains(document.getId())){
                                                MainActivity.doctors_myLists.add(new DoctorsList(document.getId()));

                                            }
                                        }

                                      //  if (!doctorsRequest.contains(document.getId())){
                                     //       MainActivity.doctors_myLists.add(new DoctorsList(document.getId()));

                                     //   }

                                        // Toast.makeText(context, document.getString("service"), Toast.LENGTH_SHORT).show();

                                    }


                                }

                                MainActivity.doctors_adapter = new DoctorsAdapter(MainActivity.doctors_myLists, context);
                                MainActivity.doctors_rv.setAdapter(MainActivity.doctors_adapter);
                                MainActivity.doctors_adapter.notifyDataSetChanged();



                            }




                        }
                    });


                }else{

                    db.collection("Facility").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                            if (!snapshots.isEmpty()){
                                MainActivity.doctors_myLists.clear();
                                MainActivity.doctors_adapter = new DoctorsAdapter(MainActivity.doctors_myLists, context);
                                MainActivity.doctors_rv.setAdapter(MainActivity.doctors_adapter);
                                MainActivity.doctors_adapter.notifyDataSetChanged();

                                // appointment_myLists.clear();

                                if (error != null) {
                                    Toast.makeText(context, "Listen failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (firebaseAuth.getCurrentUser() != null) {
                                }
                                for (DocumentSnapshot document : snapshots.getDocuments()) {

                                    if (document.exists()){
                                        if (!firebaseAuth.getCurrentUser().getUid().matches(document.getId())){
                                            if (!doctorsRequest.contains(document.getId())){
                                                MainActivity.doctors_myLists.add(new DoctorsList(document.getId()));

                                            }
                                        }

                                     //   if (!doctorsRequest.contains(document.getId())){
                                     //       MainActivity.doctors_myLists.add(new DoctorsList(document.getId()));

                                      //  }

                                        // Toast.makeText(context, document.getString("service"), Toast.LENGTH_SHORT).show();

                                    }


                                }

                                MainActivity.doctors_adapter = new DoctorsAdapter(MainActivity.doctors_myLists, context);
                                MainActivity.doctors_rv.setAdapter(MainActivity.doctors_adapter);
                                MainActivity.doctors_adapter.notifyDataSetChanged();



                            }




                        }
                    });



                }


            }
        });




    }

}
