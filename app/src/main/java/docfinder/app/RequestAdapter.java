package docfinder.app;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<RequestList> myListList;
    private Context ct;


    public RequestAdapter(List<RequestList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_affiliates_rec,parent,false);
        return new ViewHolder(view);





    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestList myList=myListList.get(position);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Facility").document(myList.getDoctorID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        holder.confirmFacility.setText(document.getString("facility_name"));

                    }
                } else {

                }
            }
        });

        db.collection("Users").document(myList.getDoctorID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");

                        SpannableString str = new SpannableString(name);
                        str.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.confirmText.setText(str+  " would like to request an affiliation with you");

                    }
                } else {

                }
            }
        });




        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(myList.getDoctorID()+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct).load(uri).centerCrop().dontAnimate().into(holder.affiliateConfirmProfile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(ct).load(R.drawable.doctor_profile).centerCrop().dontAnimate().into(holder.affiliateConfirmProfile);

            }
        });

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                db.collection("Facility").document(myList.getDoctorID()).collection("Affiliates").document(firebaseAuth.getCurrentUser().getUid())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ct, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });





                db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Request").document(myList.getDoctorID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ct, "Rejected", Toast.LENGTH_SHORT).show();

                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ct, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });

        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Request").document(myList.getDoctorID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Map<String, Object> data = new HashMap<>();
                                data.put("approved", true);


                                db.collection("Facility").document(myList.getDoctorID()).collection("Affiliates").document(firebaseAuth.getCurrentUser().getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ct, "Affiliation accepted", Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(ct, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });



    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView confirmText,confirmFacility;
        private ImageView affiliateConfirmProfile;
        private Button cancelButton,confirmButton;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            confirmText = (TextView) itemView.findViewById(R.id.confirmText);
            confirmFacility = (TextView) itemView.findViewById(R.id.confirmFacility);
            affiliateConfirmProfile = (ImageView) itemView.findViewById(R.id.affiliateConfirmProfile);
            cancelButton= (Button) itemView.findViewById(R.id.cancelButton);
            confirmButton= (Button) itemView.findViewById(R.id.confirmButton);





        }
    }
}