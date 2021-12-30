package docfinder.app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AffiliateAdapter extends RecyclerView.Adapter<AffiliateAdapter.ViewHolder> {
    private List<AffiliateList> myListList;
    private Context ct;


    public AffiliateAdapter(List<AffiliateList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.affiliates_rec,parent,false);
        return new ViewHolder(view);




    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AffiliateList myList=myListList.get(position);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();



        if (MainActivity.currentViewId.matches(firebaseAuth.getCurrentUser().getUid())){
            holder.affiliateRemove.setVisibility(View.VISIBLE);

        }else{
            holder.affiliateRemove.setVisibility(View.GONE);
        }

        holder.affiliateRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.affiliateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        db.collection("Facility").document(myList.getAffiliateID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.affiliateType.setText(document.getString("facility_details"));

                    }
                } else {
                    holder.affiliateType.setVisibility(View.GONE);
                }
            }
        });


        db.collection("Users").document(myList.getAffiliateID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.affiliateName.setText(document.getString("name"));


                    }
                } else {

                }
            }
        });


        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(myList.getAffiliateID()+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(ct).load(uri).placeholder(R.drawable.empty).centerCrop().dontAnimate().into(holder.affiliateProfile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(ct).load(R.drawable.doctor_profile).placeholder(R.drawable.empty).centerCrop().dontAnimate().into(holder.affiliateProfile);

            }
        });

        holder.affiliateRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("Facility").document(firebaseAuth.getCurrentUser().getUid()).collection("Affiliates").document(myList.getAffiliateID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ct, "Affiliation removed", Toast.LENGTH_SHORT).show();
                                MainActivity.affiliates.displayAffiliates(ct,firebaseAuth.getCurrentUser().getProviderId());
                                
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







    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView affiliateName,affiliateType;
        private ImageView affiliateProfile;
        private ImageView affiliateRemove;
        private CardView affiliateCard;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            affiliateCard = (CardView) itemView.findViewById(R.id.affiliateCard);
            affiliateName = (TextView) itemView.findViewById(R.id.affiliateName);
            affiliateType = (TextView) itemView.findViewById(R.id.affiliateType);
            affiliateProfile = (ImageView) itemView.findViewById(R.id.affiliateProfile);
            affiliateRemove = (ImageView) itemView.findViewById(R.id.affiliateRemove);



        }
    }
}