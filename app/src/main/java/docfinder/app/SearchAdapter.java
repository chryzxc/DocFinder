package docfinder.app;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchList> myListList;
    private Context ct;

    public SearchAdapter(List<SearchList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }

    public void updateList(List<SearchList> list){
        myListList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_rec,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchList myList=myListList.get(position);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       // MainActivity.searchRatingBar.

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.searchName.setText(myList.getName());
        holder.searchType.setText(myList.getDetails());
        holder.searchRatings.setText(String.format("%.1f", myList.getRatings()));
        holder.searchCounts.setText(String.valueOf("("+myList.getCount()+" reviews)"));



        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(myList.getId()+"/facilityLocation.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct).load(uri).centerCrop().dontAnimate().dontTransform().into(holder.searchImage);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });


        holder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.chipNavigationBar.setItemSelected(R.id.map, true);
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(myList.getMapLat(), myList.getMapLong()))
                        .zoom(16.5)
                        .tilt(60)
                        .build();

                MainActivity.mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 5000);

            }
        });

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                MainActivity.currentViewId = myList.getId();
                MainActivity.viewSearch.setVisibility(View.VISIBLE);




                if (myList.getId().matches(firebaseAuth.getCurrentUser().getUid())){


                    MainActivity.patientBar.setVisibility(View.GONE);
                    MainActivity.uploadGallery.setVisibility(View.VISIBLE);
                    MainActivity.uploadCover.setVisibility(View.VISIBLE);
                    MainActivity.calendarTable.setVisibility(View.VISIBLE);

                }else{

                    if(MainActivity.profileMedical.getVisibility() == View.VISIBLE){

                        MainActivity.patientBar.setVisibility(View.GONE);
                        MainActivity.uploadGallery.setVisibility(View.GONE);
                        MainActivity.uploadCover.setVisibility(View.GONE);
                        MainActivity.calendarTable.setVisibility(View.GONE);

                    }else{

                        MainActivity.patientBar.setVisibility(View.VISIBLE);
                        MainActivity.uploadGallery.setVisibility(View.GONE);
                        MainActivity.uploadCover.setVisibility(View.GONE);
                        MainActivity.calendarTable.setVisibility(View.GONE);

                    }


                }




                MainActivity.hideKeyboard();
                MainActivity.displayGallery(ct,MainActivity.currentViewId);


                MainActivity.searchFacilityName.setText(myList.getName());
                MainActivity.searchFacilityLocation.setText(myList.getAddress());
                MainActivity.searchRatings.setText(String.format("%.1f", myList.getRatings()));
                MainActivity.searchCounts.setText((String.valueOf("("+myList.getCount()+" reviews)")));
                MainActivity.searchFacilityDetails.setText(myList.getDetails());
                MainActivity.searchRatingBar.setRating(myList.getMy_rating().floatValue());

                MainActivity.service_myLists.clear();
                MainActivity.affiliates_myLists.clear();


                MainActivity.services.displayServices(ct,myList.getId());

                MainActivity.affiliates.displayAffiliates(ct,myList.getId());

                db.collection("Users").document(myList.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        MainActivity.doctorName.setText("");
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                MainActivity.doctorName.setText(document.getString("name"));

                                //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                //  Log.d(TAG, "No such document");
                            }
                        } else {
                            // Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });






                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child(myList.getId()+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(ct).load(uri).placeholder(R.drawable.empty).centerCrop().dontAnimate().into(MainActivity.doctorImage);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Glide.with(ct).load(R.drawable.doctor_profile).placeholder(R.drawable.empty).centerCrop().dontAnimate().into(MainActivity.doctorImage);

                    }
                });





                MainActivity.searchRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {

              //          Toast.makeText(ct, String.valueOf(rating), Toast.LENGTH_SHORT).show();

                        if (MainActivity.currentViewId.matches(myList.getId())){

                            Map<String, Object> childHash = new HashMap<>();
                            childHash.put(MainActivity.firebaseAuth.getCurrentUser().getUid(), rating);
                            Map<String, Object> parentHash = new HashMap<>();
                            parentHash.put("facility_ratings", childHash);


                            DocumentReference documentReference1 = db.collection("Facility").document(myList.getId());
                            documentReference1.set(parentHash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                        }





                        }







                });
               // MainActivity.searchFacilityLocation.setText(myList.get());






            }
        });


    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView searchImage;
        private TextView searchName,searchRatings,searchCounts,searchType;
        private Button viewButton,locationButton;
        Boolean sendRatings = false;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewButton=(Button)itemView.findViewById(R.id.viewButton);
            locationButton=(Button)itemView.findViewById(R.id.locationButton);
            searchName=(TextView)itemView.findViewById(R.id.searchName);
            searchType=(TextView)itemView.findViewById(R.id.searchType);
            searchImage=(ImageView)itemView.findViewById(R.id.searchImage);
            searchCounts=(TextView)itemView.findViewById(R.id.searchCounts);
            searchRatings=(TextView)itemView.findViewById(R.id.searchRatings);

        }
    }
}