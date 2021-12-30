package docfinder.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.Inflater;

import static docfinder.app.MainActivity.displayGallery;
import static docfinder.app.MainActivity.firebaseAuth;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<GalleryList> myListList;
    private Context ct;
    public TimelineView mTimelineView;

    public GalleryAdapter(List<GalleryList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_rec,parent,false);
        return new ViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryList myList=myListList.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (MainActivity.currentViewId.matches(MainActivity.loadId)){
            holder.deleteGallery.setVisibility(View.VISIBLE);
        }else{
            holder.deleteGallery.setVisibility(View.GONE);
        }



        storageReference.child(myList.getViewId()+"/gallery/"+ myList.getImageId()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(ct).load(uri).centerCrop().placeholder(R.drawable.loading).dontAnimate().into(holder.galleryImage);
/*
                holder.postImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ct, ImageViewer.class);
                        intent.putExtra("url", "posts/"+myList.getPost_id()+"/post_image.jpg");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ct.startActivity(intent);
                    }
                });

*

 */

                holder.deleteGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(ct);
                        View myLayout = LayoutInflater.from(ct).inflate(R.layout.delete_gallery, null);

                        Button galleryDelete = (Button) myLayout.findViewById(R.id.galleryDelete);
                        Button galleryDismiss = (Button) myLayout.findViewById(R.id.galleryDismiss);


                        galleryDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                db.collection("Facility").document(firebaseAuth.getCurrentUser().getUid()).collection("gallery").document(myList.getImageId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                                StorageReference deleteRef = storageRef.child(firebaseAuth.getCurrentUser().getUid()+"/gallery/"+ myList.getImageId()+".jpg");


                                                deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ct, "Deleted", Toast.LENGTH_SHORT).show();
                                                        holder.deleteDialog.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        holder.deleteDialog.dismiss();
                                                        Toast.makeText(ct, exception.getMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ct, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                holder.deleteDialog.dismiss();
                                            }
                                        });

                            }
                        });

                        galleryDismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.deleteDialog.dismiss();
                            }
                        });



                        builder.setView(myLayout);
                        holder.deleteDialog = builder.create();

                        holder.deleteDialog.setCancelable(true);
                        holder.deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        holder.deleteDialog.show();





                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });




    }



    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout initialAppointment,nextAppointment;

        private ImageView galleryImage,deleteGallery;
        private TextView postCaption,postUser,postUserDetails,postDate;
        androidx.appcompat.app.AlertDialog deleteDialog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            galleryImage = (ImageView)itemView.findViewById(R.id.galleryImage);
            deleteGallery = (ImageView)itemView.findViewById(R.id.deleteGallery);


        }
    }
}