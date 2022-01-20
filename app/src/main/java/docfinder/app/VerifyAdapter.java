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
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.willy.ratingbar.BaseRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccy.focuslayoutmanager.FocusLayoutManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ccy.focuslayoutmanager.FocusLayoutManager.dp2px;

public class VerifyAdapter extends RecyclerView.Adapter<VerifyAdapter.ViewHolder> {
    private List<VerifyList> myListList;
    private Context ct;

    public VerifyAdapter(List<VerifyList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }

    public void updateList(List<VerifyList> list){
        myListList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.verify_rec,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        VerifyList myList=myListList.get(position);
        holder.expandVerify.setBackgroundResource(R.drawable.arrow_collapse);
        holder.expandText.setText("More details");
        holder.documentsText.setText("Documents");

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(myList.getDoctorID())
                        .update("verified", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ct, "Success", Toast.LENGTH_SHORT).show();

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

        holder.expandVerify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (holder.hasFacility == true){
                    holder.noFacilityText.setVisibility(GONE);

                    if (holder.expandFacility.getVisibility() == View.VISIBLE) {
                        holder.expandFacility.setVisibility(View.GONE);
                        holder.expandVerify.setBackgroundResource(R.drawable.arrow_collapse);
                        holder.expandText.setText("More details");
                    }else{
                        holder.expandFacility.setVisibility(View.VISIBLE);
                        holder.expandVerify.setBackgroundResource(R.drawable.arrow_expand);
                        holder.expandText.setText("Hide details");
                    }

                }else{
                    if (holder.noFacilityText.getVisibility() == VISIBLE){
                        holder.noFacilityText.setVisibility(GONE);
                        holder.expandVerify.setBackgroundResource(R.drawable.arrow_collapse);
                        holder.expandText.setText("More details");

                    }else{
                        holder.noFacilityText.setVisibility(VISIBLE);
                        holder.expandVerify.setBackgroundResource(R.drawable.arrow_expand);
                        holder.expandText.setText("Hide details");

                    }


                }




            }
        });


        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(myList.getDoctorID()+"/facilityLocation.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct).load(uri).centerCrop().dontAnimate().dontTransform().into(holder.verifiyImageLocation);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });





        holder.document_myLists = new ArrayList<>();
        FocusLayoutManager focusLayoutManager =
                new FocusLayoutManager.Builder()
                        .layerPadding(dp2px(ct, 100))
                        .normalViewGap(dp2px(ct, 5))
                        .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                        .isAutoSelect(true)
                        .maxLayerCount(1)
                        .setOnFocusChangeListener(new FocusLayoutManager.OnFocusChangeListener() {
                            @Override
                            public void onFocusChanged(int focusdPosition, int lastFocusdPosition) {

                            }
                        })
                        .build();
        holder.document_rv.setLayoutManager(focusLayoutManager);


        holder.documents =  db.collection("Facility").document(myList.getDoctorID()).collection("gallery")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        holder.document_myLists.clear();
                        if (e != null) {

                            return;
                        }


                        for (QueryDocumentSnapshot doc : value) {
                            holder.document_myLists.add(new VerifyDocumentList(doc.getId(),myList.getDoctorID()));

                        }

                        if (holder.document_myLists.size() == 0){
                            holder.documentsText.setText("No documents attached");
                        }

                        holder.document_adapter = new VerifyDocumentAdapter(holder.document_myLists, ct);
                        holder.document_rv.setAdapter(holder.document_adapter);
                        holder.document_adapter.notifyDataSetChanged();
                    }
                });




/*
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
        });*/

        db.collection("Users").document(myList.getDoctorID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        holder.userVerifyName.setText(document.getString("name"));
                        holder.userVerifyAddress.setText(document.getString("address"));
                        holder.userVerifyNumber.setText(document.getString("phone"));

                    } else {

                    }
                }
            }
        });

        db.collection("Facility").document(myList.getDoctorID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        holder.userVerifyType.setText(document.getString("facility_details"));
                        holder.userVerifyFacility.setText(document.getString("facility_name"));
                        holder.userVerifyFacilityAddress.setText(document.getString("facility_address"));
                        holder.hasFacility = true;

                    } else {

                        holder.hasFacility = false;

                    }
                }else{
                    holder.hasFacility = null;
                }
            }
        });

        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference();
        storageReference1.child(myList.getDoctorID()+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ct).load(uri).centerCrop().dontAnimate().into(holder.userVerifyProfile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Glide.with(ct).load(R.drawable.doctor_profile).centerCrop().dontAnimate().into(holder.userVerifyProfile);

            }
        });




/*
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


     */
        /*

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




            }
        });

*/
    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
/*
        private ImageView searchImage;
        private TextView searchName,searchRatings,searchCounts,searchType;
        private Button viewButton,locationButton;
        Boolean sendRatings = false;
*/
        List<VerifyDocumentList> document_myLists;
        RecyclerView document_rv;
        VerifyDocumentAdapter document_adapter;
        ListenerRegistration documents;

        ImageView userVerifyProfile,verifiyImageLocation;
        TextView userVerifyName,userVerifyAddress,userVerifyNumber,userVerifyType,userVerifyFacility,userVerifyFacilityAddress,expandText,documentsText,noFacilityText;
        ImageView expandVerify;
        ConstraintLayout expandFacility;
        Boolean hasFacility;
        Button approveButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            approveButton = (Button) itemView.findViewById(R.id.approveButton);

            document_rv = (RecyclerView) itemView.findViewById(R.id.document_rec);
            documentsText=(TextView)itemView.findViewById(R.id.documentsText);
            expandText=(TextView)itemView.findViewById(R.id.expandText);
            expandVerify = (ImageView) itemView.findViewById(R.id.expandVerify);
            verifiyImageLocation = (ImageView) itemView.findViewById(R.id.verifiyImageLocation);

            expandFacility = (ConstraintLayout) itemView.findViewById(R.id.expandFacility);
            userVerifyName=(TextView)itemView.findViewById(R.id.userVerifyName);
            userVerifyAddress=(TextView)itemView.findViewById(R.id.userVerifyAddress);
            userVerifyNumber=(TextView)itemView.findViewById(R.id.userVerifyNumber);
            userVerifyProfile=(ImageView)itemView.findViewById(R.id.userVerifyProfile);

            userVerifyType=(TextView)itemView.findViewById(R.id.userVerifyType);
            userVerifyFacility=(TextView)itemView.findViewById(R.id.userVerifyFacility);
            userVerifyFacilityAddress=(TextView)itemView.findViewById(R.id.userVerifyFacilityAddress);
            noFacilityText=(TextView)itemView.findViewById(R.id.noFacilityText);

          /*
            searchName=(TextView)itemView.findViewById(R.id.searchName);
            searchType=(TextView)itemView.findViewById(R.id.searchType);
            searchImage=(ImageView)itemView.findViewById(R.id.searchImage);
            searchCounts=(TextView)itemView.findViewById(R.id.searchCounts);
            searchRatings=(TextView)itemView.findViewById(R.id.searchRatings);
*/
        }
    }
}