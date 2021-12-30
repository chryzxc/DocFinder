package docfinder.app;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<AppointmentList> myListList;
    private Context ct;
    public TimelineView mTimelineView;

    public AppointmentAdapter(List<AppointmentList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_rec,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentList myList=myListList.get(position);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.appointmentDate.setText(DateFormat.format("MMM dd yyyy",myList.getDate())+ " ( " + DateFormat.format("EEEE",myList.getDate())+" )");
        holder.appointmentDetails.setText(myList.getDetails());
        holder.appointmentTime.setText(DateFormat.format("hh:mm aa",myList.getStart()) + " - " + DateFormat.format("hh:mm aa",myList.getEnd()));



        if (MainActivity.profileMedical.getVisibility() == View.VISIBLE){
            holder.appointmentImageContainer.setVisibility(View.GONE);
            DocumentReference docRef = db.collection("Users").document(myList.getPatientID());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            holder.appointmentPerson.setText(task.getResult().getString("name"));
                        } else {

                        }
                    } else {
                        Toast.makeText(ct, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{

            DocumentReference docRef = db.collection("Users").document(myList.getDoctorID());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            holder.appointmentPerson.setText(task.getResult().getString("name"));
                        } else {

                        }
                    } else {
                        Toast.makeText(ct, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child(myList.getDoctorID()+"/cover.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(ct).load(uri).centerCrop().placeholder(R.drawable.loading).dontAnimate().into(holder.appointmentImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Glide.with(ct).load(R.drawable.loading).centerInside().placeholder(R.drawable.loading).dontAnimate().into(holder.appointmentImage);
                }
            });

        }



        if (position == 0 ){
            holder.initialAppointment.setVisibility(View.VISIBLE);
            holder.nextAppointment.setVisibility(View.GONE);
        }else{
            holder.nextAppointment.setVisibility(View.VISIBLE);
            holder.initialAppointment.setVisibility(View.GONE);
        }






       // holder.memberProf




    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout initialAppointment,nextAppointment;
        private CardView appointmentImageContainer;

        private ImageView appointmentImage;
        private TextView appointmentDetails,appointmentDate,appointmentTime,appointmentPerson;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initialAppointment = (ConstraintLayout) itemView.findViewById(R.id.initialAppointment);
            nextAppointment = (ConstraintLayout) itemView.findViewById(R.id.nextAppointment);

            appointmentTime = (TextView) itemView.findViewById(R.id.appointmentTime);
            appointmentDate = (TextView)  itemView.findViewById(R.id.appointmentDate);
            appointmentPerson = (TextView)  itemView.findViewById(R.id.appointmentPerson);
            appointmentDetails = (TextView)  itemView.findViewById(R.id.appointmentDetails);

            appointmentImageContainer = (CardView)  itemView.findViewById(R.id.appointmentImageContainer);
            appointmentImage = (ImageView)  itemView.findViewById(R.id.appointmentImage);






        }
    }
}