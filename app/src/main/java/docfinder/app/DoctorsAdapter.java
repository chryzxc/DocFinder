package docfinder.app;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {
    private List<DoctorsList> myListList;
    private Context ct;


    public DoctorsAdapter(List<DoctorsList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_affiliates_rec,parent,false);
        return new ViewHolder(view);





    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorsList myList=myListList.get(position);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Facility").document(myList.getDoctorID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.affiliateAddFacility.setText(document.getString("facility_name"));
                        holder.affiliateAddType.setText(document.getString("facility_details"));

                    } else {

                    }
                }
            }
        });

        db.collection("Users").document(myList.getDoctorID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.affiliateAddName.setText(document.getString("name"));

                    } else {

                    }
                }
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(myList.getDoctorID()+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct).load(uri).centerCrop().dontAnimate().into(holder.affiliateAddProfile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(ct).load(R.drawable.doctor_profile).centerCrop().dontAnimate().into(holder.affiliateAddProfile);

            }
        });

        holder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put("approved", false);


                db.collection("Facility").document(firebaseAuth.getCurrentUser().getUid()).collection("Affiliates").document(myList.getDoctorID())
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("date", new Date().getTime());

                                db.collection("Users").document(myList.getDoctorID()).collection("Request").document(firebaseAuth.getCurrentUser().getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                MainActivity.doctors_myLists.remove(position);
                                                MainActivity.doctors_adapter = new DoctorsAdapter(MainActivity.doctors_myLists, ct);
                                                MainActivity.doctors_rv.setAdapter(MainActivity.doctors_adapter);
                                                MainActivity.doctors_adapter.notifyDataSetChanged();

                                                //  Log.d(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //  Log.w(TAG, "Error writing document", e);
                                            }
                                        });
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                              //  Log.w(TAG, "Error writing document", e);
                            }
                        });



            }
        });

        if (Add.doctorsApproved.contains(myList.getDoctorID())){
            holder.sendButton.setText("Pending");
            holder.sendButton.setClickable(false);

        }



    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView affiliateAddFacility,affiliateAddType,affiliateAddName;
        private ImageView affiliateAddProfile;
        private Button sendButton;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            affiliateAddFacility = (TextView) itemView.findViewById(R.id.affiliateAddFacility);
            affiliateAddType = (TextView) itemView.findViewById(R.id.affiliateAddType);
            affiliateAddName = (TextView) itemView.findViewById(R.id.affiliateAddName);
            affiliateAddProfile = (ImageView) itemView.findViewById(R.id.affiliateAddProfile);
            sendButton= (Button) itemView.findViewById(R.id.sendButton);





        }
    }
}