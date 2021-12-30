package docfinder.app;

import android.content.Context;
import android.net.Uri;
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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<UsersList> myListList;
    private Context ct;


    public UsersAdapter(List<UsersList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_rec,parent,false);
        return new ViewHolder(view);





    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsersList myList=myListList.get(position);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();



        db.collection("Facility").document(myList.getDoctorID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.userFacility.setVisibility(View.VISIBLE);
                        holder.userFacility.setText(document.getString("facility_name"));
                        holder.userType.setText(document.getString("facility_details"));

                    } else {
                        holder.userFacility.setVisibility(View.GONE);
                        holder.userType.setVisibility(View.GONE);

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
                        holder.userName.setText(document.getString("name"));

                    } else {

                    }
                }
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(myList.getDoctorID()+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct).load(uri).centerCrop().dontAnimate().into(holder.userProfile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(ct).load(R.drawable.doctor_profile).centerCrop().dontAnimate().into(holder.userProfile);

            }
        });






    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView userFacility,userType,userName;
        private ImageView userProfile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userFacility = (TextView) itemView.findViewById(R.id.userFacility);
            userType = (TextView) itemView.findViewById(R.id.userType);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userProfile = (ImageView) itemView.findViewById(R.id.userProfile);





        }
    }
}