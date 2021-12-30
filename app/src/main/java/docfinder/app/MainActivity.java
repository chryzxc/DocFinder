package docfinder.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikartm.support.ImageBadgeView;
import timber.log.Timber;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineDasharray;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class MainActivity extends AppCompatActivity implements PermissionsListener {



    private static Activity activity;
    Context context;

    static FirebaseAuth firebaseAuth;
    static FirebaseFirestore db;
    FirebaseStorage storage;
    static String loadName,loadNumber,loadAddress,loadId;

    MapView mapView;
    private BuildingPlugin buildingPlugin;
    static MapboxMap mapboxMap;

    static ChipNavigationBar chipNavigationBar;
    ConstraintLayout mapLayout,searchLayout,appointmentLayout,profileLayout,usersLayout;

    static List<NearbyList> nearby_myLists;
    static RecyclerView nearby_rv;
    static NearbyAdapter nearby_adapter;

    static List<SearchList> search_myLists;
    static RecyclerView search_rv;
    static SearchAdapter search_adapter;

    List<TypeList> type_myLists;
    RecyclerView type_rv;
    TypeAdapter type_adapter;

    List<AppointmentList> appointment_myLists;
    RecyclerView appointment_rv;
    AppointmentAdapter appointment_adapter;


    static List<GalleryList> gallery_myLists;
    static RecyclerView gallery_rv;
    static GalleryAdapter gallery_adapter;

    static List<ServicesList> service_myLists;
    static RecyclerView service_rv;
    static ServicesAdapter service_adapter;

    static List<DoctorsList> doctors_myLists;
    static RecyclerView doctors_rv;
    static DoctorsAdapter doctors_adapter;

    static List<UsersList> users_myLists;
    static RecyclerView users_rv;
    static UsersAdapter users_adapter;


    static ServicesEditAdapter edit_service_adapter;
    static RecyclerView edit_service_rv;

    static List<AffiliateList> affiliates_myLists;
    static RecyclerView affiliates_rv;
    static AffiliateAdapter affiliates_adapter;

    static List<RequestList> request_myLists;
    static RecyclerView request_rv;
    static RequestAdapter request_adapter;


    KProgressHUD hud;
    androidx.appcompat.app.AlertDialog uploadDialog;

    //MAP

    ImageView nearby_expand,nearby_collapse;
    CardView cardNearby,cardLocation;
    ConstraintLayout nearbyExpand,nearbyCollapse,setupForm,setupLocation;
    Button confirmButton,setupDone;
    TextView setupSelectLocation;
    ImageView setupMapImage,setupFormMarker;

    private static final String FACILITYMARKER_LAYER_ID = "FACILITYMARKER_LAYER_ID";
    private static final String FACILITYGEOJSON_SOURCE_ID = "FACILITYGEOJSON_SOURCE_ID";

    private static final String DIRECTIONS_LAYER_ID = "DIRECTIONS_LAYER_ID";
    private static final String LAYER_BELOW_ID = "road-label-small";
    private static final String SOURCE_ID = "SOURCE_ID";
    private FeatureCollection dashedLineDirectionsFeatureCollection;

    GeoJsonSource facilityGeoJsonSource;
    SymbolLayer facilitySymbolLayer;
    static Marker facilityMarker;
    CompactCalendarView calendarView;


    //SEARCH
    static CardView viewSearch;
    CardView searchBack,calendarBack;
    static SearchView search_name;
    static String currentViewId;
    List<String> doctorType = new ArrayList<>();
    static TextView doctorName;

    static androidx.appcompat.app.AlertDialog editServicesDialog,addAffiliates;
    static View myLayout;

    //APPOINTMENT

    TextView appointmentTime,appointmentDate,appointmentPerson,appoinmentDetails;

    //PROFILE
    Button signoutButton;
    static ConstraintLayout profilePatient,profileMedical;
    TextView userName,userNumber,userAddress;
    MaterialCardView setupFacilityProfile;
    CardView facilityDelete,facilityView;
    androidx.appcompat.app.AlertDialog deleteDialog;
    View profileUploadLayout;
    androidx.appcompat.app.AlertDialog profileDialog;
    CardView profileImageCard;
    ImageView profileImage;

    //VIEW SEARCH
    static TextView searchFacilityName,searchRatings,searchCounts,searchFacilityDetails,searchFacilityLocation;
    static ScaleRatingBar searchRatingBar;
    static ConstraintLayout patientBar;
    static TextView uploadGallery;
    static CardView uploadCover;
    Style loadedStyle;
    Button searchAppointment;
    View searchAppointmentLayout;
    LayoutInflater inflater;
    androidx.appcompat.app.AlertDialog appointmentDialog;
    ConstraintLayout appointmentExpand;
    CardView hideAppointmentExpand;
    ImageView uploadImage;
    static ImageView facilityCover,doctorImage;
    ConstraintLayout viewSelected;

    static ListenerRegistration gallery;

    String selectedDate;
    String startTime,endTime;

    Date requesteddate,startdate,startdate2,enddate,enddate2;

    //GALLERY
    static TextView noGalleryText;
    static ConstraintLayout galleryLayout;
    static ConstraintLayout servicesLayout;
    static ConstraintLayout affiliatesLayout;
    static TextView services_edit;
    static TextView affiliates_add;



    //LOCATION
    boolean isGpsEnabled;
    private LocationEngine locationEngine;
    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);
    private PermissionsManager permissionsManager;

    //NEARBY

    static ArrayList toDelete = new ArrayList();

    private DirectionsRoute currentRoute;
    private MapboxDirections client;

    Point origin,destination;

    //Calendar
    CardView viewAppointment;
    static CardView calendarTable;
    String timeStart = "09:00";
    String timeEnd = "19:00";

    static ListenerRegistration currentServices,currentAffiliations;
    static Services services;
    static Affiliates affiliates;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(), Signin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }



        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_main);

        transparentStatusBar();
        activity = MainActivity.this;
        chipNavigationBar = findViewById(R.id.menu);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        inflater = getLayoutInflater();
        context = getApplicationContext();




        mapLayout = findViewById(R.id.mapLayout);
        searchLayout = findViewById(R.id.searchLayout);
        appointmentLayout = findViewById(R.id.appointmentLayout);
        profileLayout = findViewById(R.id.profileLayout);
        usersLayout = findViewById(R.id.usersLayout);
        viewSearch = findViewById(R.id.viewSearch);
        facilityCover = findViewById(R.id.facilityCover);
        doctorImage = findViewById(R.id.doctorImage);
        viewSelected = (ConstraintLayout) findViewById(R.id.viewSelected);


        nearby_rv = (RecyclerView) findViewById(R.id.nearby_rec);
        nearby_rv.setHasFixedSize(true);
        nearby_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nearby_myLists = new ArrayList<>();



        search_rv = (RecyclerView) findViewById(R.id.search_rec);
        search_rv.setHasFixedSize(true);
        search_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        search_myLists = new ArrayList<>();

        type_rv = (RecyclerView) findViewById(R.id.type_rec);
        type_rv.setHasFixedSize(true);
        type_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        type_myLists = new ArrayList<>();

        appointment_rv = (RecyclerView) findViewById(R.id.appointment_rec);
        appointment_rv.setHasFixedSize(true);
        appointment_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        appointment_myLists = new ArrayList<>();

        gallery_rv = (RecyclerView) findViewById(R.id.gallery_rec);
        gallery_rv.setHasFixedSize(true);
        gallery_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gallery_myLists = new ArrayList<>();


        service_rv = (RecyclerView) findViewById(R.id.services_rec);
        service_rv.setHasFixedSize(true);
        service_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        service_myLists = new ArrayList<>();

        affiliates_rv = (RecyclerView) findViewById(R.id.affiliates_rec);
        affiliates_rv.setHasFixedSize(true);
        affiliates_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        affiliates_myLists = new ArrayList<>();

        services = new Services();
        affiliates = new Affiliates();









        searchFacilityName = findViewById(R.id.searchFacilityName);
        searchRatings = findViewById(R.id.searchRatings);
        searchCounts = findViewById(R.id.searchCounts);
        searchFacilityDetails = findViewById(R.id.searchFacilityDetails);
        searchFacilityLocation = findViewById(R.id.searchFacilityLocation);
        searchRatingBar = findViewById(R.id.searchRatingBar);


        patientBar = findViewById(R.id.patientBar);
        uploadCover = findViewById(R.id.uploadCover);

        uploadGallery = findViewById(R.id.uploadGallery);
        uploadGallery.setPaintFlags(uploadGallery.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


        uploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCover();
            }
        });

        uploadGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadGallery();
            }
        });

        appointmentExpand = (ConstraintLayout) findViewById(R.id.appointmentExpand);
        hideAppointmentExpand = (CardView) findViewById(R.id.hideAppointmentExpand);



        profileMedical = findViewById(R.id.profileMedical);
        profilePatient = findViewById(R.id.profilePatient);
        CardView profileMedicalLayout = findViewById(R.id.profileMedicalLayout);
        doctorName = findViewById(R.id.doctorName);

        facilityDelete = findViewById(R.id.facilityDelete);
        facilityView = findViewById(R.id.facilityView);
        calendarView = findViewById(R.id.calendarView);

        noGalleryText = findViewById(R.id.noGalleryText);
        galleryLayout = findViewById(R.id.galleryLayout);
        servicesLayout = findViewById(R.id.servicesLayout);
        affiliatesLayout = findViewById(R.id.affiliatesLayout);

        profileImage = findViewById(R.id.profileImage);
        profileImageCard = findViewById(R.id.profileImageCard);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                startActivityForResult(gallery, 200);
            }
        });




        profileMedicalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int x=0;
                do{
                    if (search_myLists.get(x).getId().matches(firebaseAuth.getCurrentUser().getUid())){

                        int finalX = x;

                        MainActivity.currentViewId = firebaseAuth.getCurrentUser().getUid();
                        MainActivity.viewSearch.setVisibility(View.VISIBLE);
                        MainActivity.patientBar.setVisibility(View.GONE);
                        facilityCover.setImageResource(0);
                        uploadCover.setVisibility(VISIBLE);
                        uploadGallery.setVisibility(VISIBLE);
                        MainActivity.calendarTable.setVisibility(View.VISIBLE);

                        MainActivity.searchFacilityName.setText(search_myLists.get(finalX).getName());
                        MainActivity.searchFacilityLocation.setText(search_myLists.get(finalX).getAddress());
                        MainActivity.searchRatings.setText(String.format("%.1f", search_myLists.get(finalX).getRatings()));
                        MainActivity.searchCounts.setText((String.valueOf("("+search_myLists.get(finalX).getCount()+" reviews)")));
                        MainActivity.searchFacilityDetails.setText(search_myLists.get(finalX).getDetails());
                        MainActivity.searchRatingBar.setRating(search_myLists.get(finalX).getMy_rating().floatValue());

                        displayGallery(v.getContext(),search_myLists.get(finalX).getId());

                        services_edit.setVisibility(VISIBLE);
                        affiliates_add.setVisibility(VISIBLE);

                        MainActivity.service_myLists.clear();
                        MainActivity.affiliates_myLists.clear();


                        services.displayServices(context,firebaseAuth.getCurrentUser().getUid());

                        affiliates.displayAffiliates(context,firebaseAuth.getCurrentUser().getUid());


                        MainActivity.searchRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                            @Override
                            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {

                                //          Toast.makeText(ct, String.valueOf(rating), Toast.LENGTH_SHORT).show();

                                Map<String, Object> childHash = new HashMap<>();
                                childHash.put(MainActivity.firebaseAuth.getCurrentUser().getUid(), rating);
                                Map<String, Object> parentHash = new HashMap<>();
                                parentHash.put("facility_ratings", childHash);


                                DocumentReference documentReference1 = db.collection("Facility").document(search_myLists.get(finalX).getId());
                                documentReference1.set(parentHash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });

                            }


                        });

                        break;

                    }
                    x++;
                }while(x < search_myLists.size());





            }
        });

        services_edit = (TextView) findViewById(R.id.services_edit);
        services_edit.setPaintFlags(services_edit.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        services_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    services.displayServices(MainActivity.this,firebaseAuth.getCurrentUser().getUid());

                displayEditServices();


            }
        });

        affiliates_add = (TextView) findViewById(R.id.affiliates_add);
        affiliates_add.setPaintFlags(services_edit.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        affiliates_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddAffiliates();

            }
        });


        displayAppointments();
        displayUsers();



        chipNavigationBar.setItemSelected(R.id.map, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                switch (i) {

                    case R.id.map:

                        mapLayout.setVisibility(VISIBLE);
                        searchLayout.setVisibility(INVISIBLE);
                        appointmentLayout.setVisibility(INVISIBLE);
                        profileLayout.setVisibility(INVISIBLE);
                        usersLayout.setVisibility(INVISIBLE);
                        setupForm.setVisibility(INVISIBLE);
                        setupLocation.setVisibility(INVISIBLE);
                        CameraPosition animate = new CameraPosition.Builder()
                                .target(mapboxMap.getCameraPosition().target)
                                .tilt(60)
                                .build();

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(animate), 1000);
                        displaySearchList();


                        break;
                    case R.id.search:

                        mapLayout.setVisibility(INVISIBLE);
                        searchLayout.setVisibility(VISIBLE);
                        appointmentLayout.setVisibility(INVISIBLE);
                        profileLayout.setVisibility(INVISIBLE);
                        usersLayout.setVisibility(INVISIBLE);
                        search_name.setIconified(false);

                        CameraPosition animate1 = new CameraPosition.Builder()
                                .target(mapboxMap.getCameraPosition().target)
                                .tilt(0)
                                .build();

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(animate1), 3000);

                        displaySearchList();

                        break;

                    case R.id.appointment:


                        mapLayout.setVisibility(INVISIBLE);
                        searchLayout.setVisibility(INVISIBLE);
                        appointmentLayout.setVisibility(VISIBLE);
                        profileLayout.setVisibility(INVISIBLE);

                        CameraPosition animate2 = new CameraPosition.Builder()
                                .target(mapboxMap.getCameraPosition().target)
                                .tilt(0)
                                .build();

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(animate2), 3000);

                        displayAppointments();


                        break;


                    case R.id.profile:

                        mapLayout.setVisibility(INVISIBLE);
                        searchLayout.setVisibility(INVISIBLE);
                        appointmentLayout.setVisibility(INVISIBLE);
                        profileLayout.setVisibility(VISIBLE);
                        usersLayout.setVisibility(INVISIBLE);
                        CameraPosition animate3 = new CameraPosition.Builder()
                                .target(mapboxMap.getCameraPosition().target)
                                .tilt(0)
                                .build();

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(animate3), 3000);


                        break;
                    case R.id.users:

                        mapLayout.setVisibility(INVISIBLE);
                        searchLayout.setVisibility(INVISIBLE);
                        appointmentLayout.setVisibility(INVISIBLE);
                        profileLayout.setVisibility(INVISIBLE);
                        usersLayout.setVisibility(VISIBLE);
                        CameraPosition animate4 = new CameraPosition.Builder()
                                .target(mapboxMap.getCameraPosition().target)
                                .tilt(0)
                                .build();

                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(animate4), 3000);


                        break;


                    default:
                        break;
                }
            }
        });

        hideAppointmentExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentExpand.setVisibility(GONE);
            }
        });



        nearbyCollapse = findViewById(R.id.nearbyCollapse);
        nearbyExpand = findViewById(R.id.nearbyExpand);


        nearby_expand = findViewById(R.id.nearby_expand);
        nearby_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nearbyExpand.setVisibility(VISIBLE);
                nearbyCollapse.setVisibility(GONE);

            }
        });

        nearby_collapse = findViewById(R.id.nearby_collapse);
        nearby_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearbyExpand.setVisibility(GONE);
                cardNearby.setVisibility(VISIBLE);
               // nearbyCollapse.setVisibility(VISIBLE);
            }
        });

        cardLocation = findViewById(R.id.cardLocation);
        cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableLocationComponent(loadedStyle);
            }
        });

        cardNearby = findViewById(R.id.cardNearby);
        cardNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nearbyExpand.setVisibility(VISIBLE);
                cardNearby.setVisibility(GONE);
                sortNearby();

            }
        });

        setupForm = findViewById(R.id.setupForm);
        setupLocation = findViewById(R.id.setupLocation);
        setupMapImage = findViewById(R.id.setupMapImage);
        setupFormMarker = findViewById(R.id.setupFormMarker);
        setupSelectLocation = findViewById(R.id.setupSelectLocation);
        setupSelectLocation.setPaintFlags(setupSelectLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        setupSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                setupForm.setVisibility(INVISIBLE);
                setupLocation.setVisibility(VISIBLE);

                setupFormMarker.setVisibility(INVISIBLE);
                setupMapImage.setVisibility(INVISIBLE);

                UiSettings uiSettings = mapboxMap.getUiSettings();

                CameraPosition position = new CameraPosition.Builder()
                        .target(mapboxMap.getCameraPosition().target)
                        .zoom(16.5)
                        .tilt(0)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);



                uiSettings.setTiltGesturesEnabled(false);


            }
        });


        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupForm.setVisibility(VISIBLE);
                setupLocation.setVisibility(INVISIBLE);


                MapSnapshotter.Options snapShotOptions = new MapSnapshotter.Options(500, 500);

                snapShotOptions.withRegion(mapboxMap.getProjection().getVisibleRegion().latLngBounds);
                snapShotOptions.withLogo(false);



                snapShotOptions.withStyle(mapboxMap.getStyle().getUrl());

                MapSnapshotter mapSnapshotter = new MapSnapshotter(MainActivity.this, snapShotOptions);

                mapSnapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(MapSnapshot snapshot) {

                        setupMapImage.setVisibility(VISIBLE);
                        setupFormMarker.setVisibility(VISIBLE);
                        Bitmap bitmapImage = snapshot.getBitmap();
                        Glide.with(MainActivity.this).load(bitmapImage).centerCrop().into(setupMapImage);




                    }
                });

            }
        });

        setupDone = findViewById(R.id.setupDone);
        setupDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (firebaseAuth.getCurrentUser() != null){

                    TextInputEditText setupName = (TextInputEditText) findViewById(R.id.setupName);
                    TextInputEditText setupDetails = (TextInputEditText) findViewById(R.id.setupDetails);
                    TextInputEditText setupAddress = (TextInputEditText) findViewById(R.id.setupAddress);

                    if (TextUtils.isEmpty(setupName.getText())) {
                        setupName.setError("Facility name is required");
                        setupName.requestFocus();
                        hideKeyboard();
                        return;
                    }else if (TextUtils.isEmpty(setupDetails.getText())){
                        setupDetails.setError("Details is required");
                        setupDetails.requestFocus();
                        hideKeyboard();
                        return;
                    }else if (TextUtils.isEmpty(setupAddress.getText())){
                        setupAddress.setError("Address is required");
                        setupAddress.requestFocus();
                        hideKeyboard();
                        return;
                    }else if (setupMapImage.getVisibility() == GONE){
                        Toast.makeText(MainActivity.this, "Please select a location in the map", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Date currentTime = Calendar.getInstance().getTime();


                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> hash = new HashMap<>();

                    data.put("facility_name", setupName.getText().toString());
                    data.put("facility_ratings", hash);
                    data.put("facility_details", setupDetails.getText().toString());
                    data.put("facility_address", setupAddress.getText().toString());
                    data.put("facility_latitude", mapboxMap.getCameraPosition().target.getLatitude());
                    data.put("facility_longitude", mapboxMap.getCameraPosition().target.getLongitude());

                    DocumentReference documentReference = db.collection("Facility").document(firebaseAuth.getCurrentUser().getUid());
                    documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {





                            StorageReference storageRef = storage.getReference();
                            StorageReference childRef = storageRef.child("facilityLocation.jpg");

                           // StorageReference parentRef = storageRef.child( "test"+ "/facilityLocation.jpg");
                            StorageReference parentRef = storageRef.child(firebaseAuth.getCurrentUser().getUid()+ "/facilityLocation.jpg");

                            setupMapImage.setDrawingCacheEnabled(true);
                            setupMapImage.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) setupMapImage.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = parentRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(MainActivity.this, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                setupForm.setVisibility(INVISIBLE);
                                hideKeyboard();
                                UiSettings uiSettings = mapboxMap.getUiSettings();
                                uiSettings.setTiltGesturesEnabled(true);
                                }
                            });


                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    displaySearchList();

                    }else{
                      //  Toast.makeText(MainActivity.this, "Something went wrong with your account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Signin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }




            }
        });

        signoutButton = findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), Signin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });

        setupFacilityProfile = findViewById(R.id.setupFacilityProfile);
        setupFacilityProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapLayout.setVisibility(VISIBLE);
                profileLayout.setVisibility(INVISIBLE);
                setupForm.setVisibility(VISIBLE);
                setupLocation.setVisibility(INVISIBLE);
                setupMapImage.setImageBitmap(null);
                setupMapImage.setVisibility(View.GONE);
                setupFormMarker.setVisibility(View.GONE);
            }
        });

        search_name = findViewById(R.id.search_name);
        search_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {

                filterByName(text);
                if (!doctorType.isEmpty()){
                    if (doctorType.contains(text)){
                        filterByType(text);
                    }
                }

                return true;
            }
        });

        searchAppointment = findViewById(R.id.searchAppointment);
        searchAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointmentExpand.setVisibility(VISIBLE);

                TextView appointmentDate = (TextView)findViewById(R.id.appointmentDate);
                appointmentDate.setPaintFlags(appointmentDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                TextView appointmentTime = (TextView)findViewById(R.id.appointmentTime);
                appointmentTime.setPaintFlags(appointmentTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                Button createAppointment = (Button) findViewById(R.id.createAppointment);
                EditText appointmentDetails = (EditText) findViewById(R.id.appointmentDetails);


                appointmentDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appointmentDate.setError(null);

                        new SingleDateAndTimePickerDialog.Builder(v.getContext())
                                .bottomSheet()
                                //.curved()
                                //.stepSizeMinutes(15)
                                .mustBeOnFuture()
                                .displayHours(false)
                                .displayMinutes(false)
                                .title("Set date")
                                .titleTextColor(ContextCompat.getColor(v.getContext(),R.color.lightbrown))
                                .titleTextSize(20)
                                .mainColor(ContextCompat.getColor(v.getContext(),R.color.lightbrown))
                                //.todayText("aujourd'hui")

                                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                                    @Override
                                    public void onDisplayed(SingleDateAndTimePicker picker) {

                                        // Retrieve the SingleDateAndTimePicker
                                    }

                                    @Override
                                    public void onClosed(SingleDateAndTimePicker picker) {

                                    }

                                })

                                .listener(new SingleDateAndTimePickerDialog.Listener() {
                                    @Override
                                    public void onDateSelected(Date date) {
                                        if (date.before(new Date())){
                                            appointmentDate.setText("Select date");
                                            appointmentDate.setError(" ");
                                            Toast.makeText(MainActivity.this, "Date must be in future", Toast.LENGTH_LONG).show();
                                            appointmentDate.requestFocus();
                                            return;
                                        }else{
                                            appointmentDate.setText(DateFormat.format("MMM dd yyyy",date));
                                            selectedDate = DateFormat.format("MMM dd yyyy",date).toString();
                                            requesteddate = date;
                                        }


                                    }
                                }).display();
                    }
                });

                appointmentTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appointmentTime.setError(null);

                        new SingleDateAndTimePickerDialog.Builder(v.getContext())
                                .bottomSheet()
                                //.curved()
                                .minutesStep(30)
                                .displayHours(true)
                                .displayMinutes(true)
                                .displayDays(false)
                                .titleTextSize(20)
                                .title("From")

                                .titleTextColor(ContextCompat.getColor(v.getContext(),R.color.lightbrown))
                                .mainColor(ContextCompat.getColor(v.getContext(),R.color.lightbrown))

                                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                                    @Override
                                    public void onDisplayed(SingleDateAndTimePicker picker) {

                                        // Retrieve the SingleDateAndTimePicker
                                    }

                                    @Override
                                    public void onClosed(SingleDateAndTimePicker picker) {


                                    }

                                })


                                .listener(new SingleDateAndTimePickerDialog.Listener() {
                                    @Override
                                    public void onDateSelected(Date date) {
                                        appointmentTime.setText(DateFormat.format("hh:mm aa", date));
                                        startTime = DateFormat.format("hh:mm aa", date).toString();
                                        String currentTimeStart = DateFormat.format("HH:mm", date).toString();


                                        new SingleDateAndTimePickerDialog.Builder(v.getContext())
                                                .bottomSheet()
                                                //.curved()
                                                //.stepSizeMinutes(15)
                                                .minutesStep(30)
                                                .displayHours(true)
                                                .displayMinutes(true)
                                                .displayDays(false)
                                                .titleTextSize(20)
                                                .title("Until")
                                                .titleTextColor(ContextCompat.getColor(v.getContext(), R.color.lightbrown))
                                                .mainColor(ContextCompat.getColor(v.getContext(), R.color.lightbrown))

                                                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                                                    @Override
                                                    public void onDisplayed(SingleDateAndTimePicker picker) {

                                                        // Retrieve the SingleDateAndTimePicker
                                                    }

                                                    @Override
                                                    public void onClosed(SingleDateAndTimePicker picker) {


                                                    }

                                                })

                                                .listener(new SingleDateAndTimePickerDialog.Listener() {
                                                    @Override
                                                    public void onDateSelected(Date date) {

                                                        endTime = DateFormat.format("hh:mm aa", date).toString();
                                                        String currentTimeEnd = DateFormat.format("HH:mm", date).toString();

                                                        Date timeNowStart = parseDate(currentTimeStart);
                                                        Date timeNowEnd = parseDate(currentTimeEnd);
                                                        Date dateCompareOne = parseDate(timeStart);
                                                        Date dateCompareTwo = parseDate(timeEnd);

                                                        if (dateCompareOne.equals(timeNowStart) || dateCompareOne.before(timeNowStart)) {
                                                            if (dateCompareTwo.after(timeNowStart)){
                                                                if (dateCompareTwo.equals(timeNowEnd) || dateCompareTwo.after(timeNowEnd) ){
                                                                    if (dateCompareOne.before(timeNowEnd)){



                                                                        try {

                                                                            SimpleDateFormat timeStampFormat = new SimpleDateFormat("MMM dd yyyy hh:mm aa");
                                                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzzz yyyy");

                                                                            // start time
                                                                            startTime = selectedDate + " " + startTime;
                                                                            startdate = timeStampFormat.parse(startTime);
                                                                            startdate2 = simpleDateFormat.parse(simpleDateFormat.format(startdate));

                                                                            // end time
                                                                            endTime = selectedDate + " " + endTime;
                                                                            enddate = timeStampFormat.parse(endTime);
                                                                            enddate2 = simpleDateFormat.parse(simpleDateFormat.format(enddate));



                                                                            if (startdate2.before(enddate2)) {
                                                                                appointmentTime.setText(appointmentTime.getText() + " - " + DateFormat.format("hh:mm aa", date));
                                                                            } else {
                                                                                appointmentTime.setText("Select time");
                                                                                appointmentTime.setError("Start time must be less than end time");
                                                                                Toast.makeText(MainActivity.this, "Start time must be less than end time", Toast.LENGTH_LONG).show();


                                                                            }





                                                                        } catch (ParseException ex) {
                                                                            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                                                        }



                                                                    }else{
                                                                        appointmentTime.setText("Select time");
                                                                        Toast.makeText(MainActivity.this, "Time must be between 9am - 7pm", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }else{
                                                                    appointmentTime.setText("Select time");
                                                                    Toast.makeText(MainActivity.this, "Time must be between 9am - 7pm", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }else{
                                                                appointmentTime.setText("Select time");
                                                                Toast.makeText(MainActivity.this, "Time must be between 9am - 7pm", Toast.LENGTH_SHORT).show();

                                                            }


                                                        }else{
                                                            appointmentTime.setText("Select time");
                                                            Toast.makeText(MainActivity.this, "Time must be between 9am - 7pm", Toast.LENGTH_SHORT).show();

                                                        }


                                                    }
                                                }).display();


                                    }
                                }).display();

                    }
                });

                createAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if (appointmentDate.getText().toString().matches("Select date")){
                            appointmentDate.setError("Date is required");
                            appointmentDate.requestFocus();
                            return;
                        }else if (appointmentTime.getText().toString().matches("Select time")){
                            appointmentTime.setError("Time is required");
                            appointmentTime.requestFocus();
                            return;
                        }else if (appointmentDetails.getText().toString().isEmpty()){
                            appointmentDetails.setError("Fill up details");
                            appointmentDetails.requestFocus();
                            return;
                        }

                        Map<String, Object> data = new HashMap<>();

                        data.put("appointment_date", requesteddate);
                        data.put("appointment_start", startdate2);
                        data.put("appointment_end",enddate2);
                        data.put("appointment_details", appointmentDetails.getText().toString());
                        data.put("appointment_patientID",firebaseAuth.getCurrentUser().getUid());
                        data.put("appointment_doctorID",currentViewId);
                        data.put("appointment_patientName",loadName);

                        if (hud !=null){
                            hud.dismiss();
                        }
                        hud = KProgressHUD.create(MainActivity.this)
                               .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("Please wait while we check the availability of your selected time")
                                .setCancellable(false)
                                .show();



                        db.collection("Appointments")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.getResult().isEmpty()){

                                            DocumentReference documentReference = db.collection("Appointments").document();
                                            documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MainActivity.this, "Appointment created successfully", Toast.LENGTH_LONG).show();
                                                    appointmentExpand.setVisibility(GONE);
                                                    hud.dismiss();



                                                }

                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                        }else{

                                            if (task.isSuccessful()) {
                                                Boolean hasConflict = false;
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    if (document.exists()){

                                                        if (currentViewId.matches(document.getString("appointment_doctorID"))){




                                                            Timestamp appointment_date = (Timestamp) document.getTimestamp("appointment_date");
                                                            Timestamp appointment_start = (Timestamp) document.getTimestamp("appointment_start");
                                                            Timestamp appointment_end = (Timestamp) document.getTimestamp("appointment_end");
                                                            Date date = appointment_date.toDate();
                                                            Date start = appointment_start.toDate();
                                                            Date end = appointment_end.toDate();

                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                                            if (sdf.format(start).equals(sdf.format(startdate2))){

                                                                if (startdate2.after(start) || startdate2.equals(start) && enddate2.before(end) || enddate2.equals(end) && startdate2.before(end)){
                                                                       Toast.makeText(MainActivity.this, "conflict", Toast.LENGTH_SHORT).show();
                                                                    hasConflict = true;

                                                                    if (startdate2.after(end) || startdate2.equals(end)){
                                                                        hasConflict = false;
                                                                        Toast.makeText(MainActivity.this, "goods1", Toast.LENGTH_SHORT).show();
                                                                    }else{
                                                                         hasConflict = true;
                                                                        Toast.makeText(MainActivity.this, "conflict1", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }else if (enddate2.after(start) && enddate2.before(end)){
                                                                    hasConflict = true;
                                                                    Toast.makeText(MainActivity.this, "conflict2", Toast.LENGTH_SHORT).show();
                                                                }else if (startdate2.before(start) || startdate2.equals(start) && enddate2.after(end) || enddate2.equals(end)){
                                                                    Toast.makeText(MainActivity.this, "conflict3", Toast.LENGTH_SHORT).show();
                                                                    hasConflict = true;
                                                                    if (enddate2.equals(start) || enddate2.before(start)){
                                                                        Toast.makeText(MainActivity.this, "goods", Toast.LENGTH_SHORT).show();
                                                                        hasConflict = false;
                                                                    }
                                                                }



                                                            }


                                                        }

                                                    }else{

                                                    }
                                                }


                                                if (hasConflict == false){
                                                       DocumentReference documentReference = db.collection("Appointments").document();
                                                                documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(MainActivity.this, "Appointment created successfully", Toast.LENGTH_LONG).show();
                                                                        appointmentExpand.setVisibility(GONE);
                                                                        hud.dismiss();

                                                                    }

                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                        Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });

                                                }else{
                                                    Toast.makeText(MainActivity.this, "Schedule not available", Toast.LENGTH_SHORT).show();
                                                    hud.dismiss();
                                                }


                                            } else {
                                              //  Toast.makeText(MainActivity.this, " loasdw", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }
                                });


                    }
                });

                /*
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                searchAppointmentLayout = inflater.inflate(R.layout.appointment_popup, null);

                TextView appointmentDate = (TextView)searchAppointmentLayout.findViewById(R.id.appointmentDate);
                appointmentDate.setPaintFlags(appointmentDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                TextView appointmentTime = (TextView)searchAppointmentLayout.findViewById(R.id.appointmentTime);
                appointmentTime.setPaintFlags(appointmentTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                appointmentDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new SingleDateAndTimePickerDialog.Builder(v.getContext())
                                .bottomSheet()
                                .curved()
                                //.stepSizeMinutes(15)
                                //.displayHours(false)
                                //.displayMinutes(false)
                                //.todayText("aujourd'hui")

                                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                                    @Override
                                    public void onDisplayed(SingleDateAndTimePicker picker) {
                                        // Retrieve the SingleDateAndTimePicker
                                    }

                                })
                                .title("Simple")
                                .listener(new SingleDateAndTimePickerDialog.Listener() {
                                    @Override
                                    public void onDateSelected(Date date) {

                                    }
                                }).display();
                    }
                });


                appointmentDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        DatePickerPopup datePickerPopup = new DatePickerPopup.Builder()
                                .from(v.getContext())
                                .offset(3)
                                .pickerMode(MONTH_ON_FIRST)
                                .textSize(19)
                                .endDate(DateUtils.getTimeMiles(2050, 10, 25))
                                .currentDate(DateUtils.getCurrentTime())
                                .startDate(DateUtils.getTimeMiles(1995, 0, 1))
                                .listener(new DatePickerPopup.OnDateSelectListener() {
                                    @Override
                                    public void onDateSelected(DatePicker dp, long date, int day, int month, int year) {
                                        appointmentDate.setText(DateFormat.format("MMM dd, yyyy",dp.getDate()));
                                        mDate = appointmentDate.getText().toString();

                                      //  Toast.makeText(getApplicationContext(), "" + day + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(MainActivity.this, String.valueOf(dp.getDate()), Toast.LENGTH_SHORT).show();
                                    }

                                })
                                .build();

                        datePickerPopup.show();

                    }
                });


                appointmentTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        TimePickerPopup pickerPopup = new TimePickerPopup.Builder()
                                .from(v.getContext())
                                .offset(3)
                                .textSize(20)
                                .setTime(12, 00)
                                .listener(new TimePickerPopup.OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelected(TimePicker timePicker, int hour, int minute) {

                                        String AM_PM ;
                                        if(hour < 12) {
                                            AM_PM = " AM";
                                            if (minute == 0){
                                                appointmentTime.setText(String.valueOf(hour)+AM_PM+" - ");
                                            }else{
                                                appointmentTime.setText(String.valueOf(hour) +":"+ String.valueOf(minute)+AM_PM+" - ");
                                            }

                                        }else if (hour == 12){
                                            AM_PM = " PM";
                                            if (minute == 0){
                                                appointmentTime.setText(String.valueOf(hour)+AM_PM+" - ");
                                            }else{
                                                appointmentTime.setText(String.valueOf(hour) +":"+ String.valueOf(minute)+AM_PM+" - ");
                                            }

                                        }
                                        else {
                                            AM_PM = " PM";
                                            int formattedTime = hour - 12;
                                            if (minute == 0){
                                                appointmentTime.setText(String.valueOf(formattedTime)+AM_PM+" - ");
                                            }else{
                                                appointmentTime.setText(String.valueOf(formattedTime) +":"+ String.valueOf(minute)+AM_PM+" - ");
                                            }

                                        }

                                        mTime = String.valueOf(hour +":"+ minute);

                                        try {

                                            SimpleDateFormat timeStampFormat = new SimpleDateFormat("MMM d, yyyy hh:mm");

                                            mDate = mDate + " " +  mTime;
                                            startdate = timeStampFormat.parse(mDate);

                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzzz yyyy");
                                            startdate2 = simpleDateFormat.parse(simpleDateFormat.format(startdate));

                                            Toast.makeText(MainActivity.this, startdate2.toString() ,Toast.LENGTH_SHORT).show();


                                        } catch (ParseException ex) {
                                            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                        }




                                        TimePickerPopup pickerPopup = new TimePickerPopup.Builder()
                                                .from(v.getContext())
                                                .offset(3)
                                                .textSize(20)
                                                .setTime(12, 00)
                                                .listener(new TimePickerPopup.OnTimeSelectListener() {
                                                    @Override
                                                    public void onTimeSelected(TimePicker timePicker, int hour, int minute) {

                                                        String AM_PM ;
                                                        if(hour < 12) {
                                                            AM_PM = " AM";
                                                            if (minute == 0){
                                                                appointmentTime.setText(appointmentTime.getText().toString() +String.valueOf(hour)+AM_PM);
                                                            }else{
                                                                appointmentTime.setText(appointmentTime.getText().toString() +String.valueOf(hour) +":"+ String.valueOf(minute)+AM_PM);
                                                            }

                                                        }else if (hour == 12){
                                                            AM_PM = " PM";
                                                            if (minute == 0){
                                                                appointmentTime.setText(appointmentTime.getText().toString() +String.valueOf(hour)+AM_PM);
                                                            }else{
                                                                appointmentTime.setText(appointmentTime.getText().toString() +String.valueOf(hour) +":"+ String.valueOf(minute)+AM_PM);
                                                            }

                                                        }
                                                        else {
                                                            AM_PM = " PM";
                                                            int formattedTime = hour - 12;
                                                            if (minute == 0){
                                                                appointmentTime.setText(appointmentTime.getText().toString() +String.valueOf(formattedTime) +AM_PM);
                                                            }else{
                                                                appointmentTime.setText(appointmentTime.getText().toString() +String.valueOf(formattedTime) +":"+ String.valueOf(minute)+AM_PM);
                                                            }

                                                        }


                                                    }
                                                })
                                                .build();
                                        pickerPopup.show();
                                    }
                                })
                                .build();

                        pickerPopup.show();
                    }
                });


                builder.setView(searchAppointmentLayout);
                appointmentDialog = builder.create();

                appointmentDialog.setCancelable(true);
                appointmentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                appointmentDialog.show();

           */ }

        });
        viewAppointment = findViewById(R.id.viewAppointment);
        calendarTable = findViewById(R.id.calendarTable);

        calendarTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAppointment.setVisibility(VISIBLE);
            }
        });


        searchBack = findViewById(R.id.searchBack);
        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewSearch.setVisibility(View.INVISIBLE);
                facilityCover.setImageResource(0);
            }
        });


        calendarBack = findViewById(R.id.calendarBack);
        calendarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewAppointment.setVisibility(INVISIBLE);

            }
        });
        checkConnection();


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        MainActivity.this.mapboxMap = mapboxMap;
                        MainActivity.this.loadedStyle = style;
                        enableLocationComponent(style);

                        initDottedLineSourceAndLayer(style);

                        mapboxMap.getUiSettings().setAttributionEnabled(false);
                        mapboxMap.getUiSettings().setLogoEnabled(false);
                        mapboxMap.getUiSettings().setCompassGravity(Gravity.LEFT);
                        mapboxMap.getUiSettings().setCompassMargins(40,400,0,0);


                        buildingPlugin = new BuildingPlugin(mapView, mapboxMap, style);
                        buildingPlugin.setMinZoomLevel(15f);

                        buildingPlugin.setVisibility(true);

                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {


                                LinearLayout sideButtons = (LinearLayout) findViewById(R.id.sideButtons);

                                PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
                                List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, FACILITYMARKER_LAYER_ID);
                                if (!features.isEmpty()) {
                                    viewSelected.setVisibility(VISIBLE);
                                    sideButtons.setVisibility(GONE);

                                    CameraPosition position = new CameraPosition.Builder()
                                            .target(new LatLng(point))
                                            .zoom(17)
                                            .tilt(60)
                                            .build();


                                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);

                                    TextView selectedRatings = (TextView) findViewById(R.id.selectedRatings);
                                    TextView selectedCounts = (TextView) findViewById(R.id.selectedCounts);
                                    TextView selectedName = (TextView) findViewById(R.id.selectedName);
                                    TextView selectedDescription = (TextView) findViewById(R.id.selectedDescription);
                                    ImageView selectedImage = (ImageView) findViewById(R.id.selectedImage);
                                    Button selectedView = (Button) findViewById(R.id.selectedView);
                                    Button selectedDirections = (Button) findViewById(R.id.selectedDirections);

                                    Feature selectedFeature = features.get(0);
                                    String id = selectedFeature.id();

                                    selectedFeature.addBooleanProperty("selected", true);

                                    int x=0;
                                    do{
                                       if (search_myLists.get(x).getId().matches(id)){
                                           selectedName.setText(search_myLists.get(x).getName());
                                           selectedRatings.setText(String.format("%.1f", search_myLists.get(x).getRatings()));
                                           selectedCounts.setText(String.valueOf("("+search_myLists.get(x).getCount()+" reviews)"));
                                           selectedDescription.setText(search_myLists.get(x).getDetails());


                                           int finalX = x;

                                           selectedDirections.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (origin != null){
                                                       getRoute(origin,Point.fromLngLat(search_myLists.get(finalX).getMapLong(),search_myLists.get(finalX).getMapLat()));

                                                   }else{
                                                       Toast.makeText(context, "Current location is empty. Please enable your gps", Toast.LENGTH_SHORT).show();
                                                   }

                                               }
                                           });



                                           selectedView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   MainActivity.currentViewId = id;

                                                   MainActivity.viewSearch.setVisibility(View.VISIBLE);
                                                   if (id.matches(firebaseAuth.getCurrentUser().getUid())){
                                                       MainActivity.patientBar.setVisibility(View.GONE);
                                                       uploadGallery.setVisibility(VISIBLE);
                                                       uploadCover.setVisibility(VISIBLE);
                                                   }else{
                                                       MainActivity.patientBar.setVisibility(View.VISIBLE);
                                                       uploadGallery.setVisibility(GONE);
                                                       uploadCover.setVisibility(GONE);
                                                   }



                                                   MainActivity.searchFacilityName.setText(search_myLists.get(finalX).getName());
                                                   MainActivity.searchFacilityLocation.setText(search_myLists.get(finalX).getAddress());
                                                   MainActivity.searchRatings.setText(String.format("%.1f", search_myLists.get(finalX).getRatings()));
                                                   MainActivity.searchCounts.setText((String.valueOf("("+search_myLists.get(finalX).getCount()+" reviews)")));
                                                   MainActivity.searchFacilityDetails.setText(search_myLists.get(finalX).getDetails());
                                                   MainActivity.searchRatingBar.setRating(search_myLists.get(finalX).getMy_rating().floatValue());

                                                   db.collection("Users").document(currentViewId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                                                   displayGallery(v.getContext(),search_myLists.get(finalX).getId());

                                                   MainActivity.service_myLists.clear();
                                                   MainActivity.affiliates_myLists.clear();


                                                   services.displayServices(context,currentViewId);

                                                   affiliates.displayAffiliates(context,currentViewId);

                                                   StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                                   storageReference.child(currentViewId+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                       @Override
                                                       public void onSuccess(Uri uri) {

                                                           Glide.with(MainActivity.this).load(uri).placeholder(R.drawable.empty).centerCrop().dontAnimate().into(MainActivity.doctorImage);

                                                       }
                                                   }).addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(@NonNull Exception exception) {
                                                           Glide.with(MainActivity.this).load(R.drawable.doctor_profile).placeholder(R.drawable.empty).centerCrop().dontAnimate().into(MainActivity.doctorImage);

                                                       }
                                                   });



                                                   MainActivity.searchRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                                                       @Override
                                                       public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {

                                                           //          Toast.makeText(ct, String.valueOf(rating), Toast.LENGTH_SHORT).show();

                                                           Map<String, Object> childHash = new HashMap<>();
                                                           childHash.put(MainActivity.firebaseAuth.getCurrentUser().getUid(), rating);
                                                           Map<String, Object> parentHash = new HashMap<>();
                                                           parentHash.put("facility_ratings", childHash);


                                                           DocumentReference documentReference1 = db.collection("Facility").document(search_myLists.get(finalX).getId());
                                                           documentReference1.set(parentHash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void aVoid) {

                                                               }
                                                           });

                                                       }


                                                   });



                                               }
                                           });
                                           break;

                                       }
                                        x++;
                                    }while(x < search_myLists.size());


                                }else{
                                    CameraPosition position = new CameraPosition.Builder()
                                            .target(new LatLng(point))
                                            .zoom(16)
                                            .tilt(0)
                                            .build();

                                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);
                                    viewSelected.setVisibility(GONE);
                                    sideButtons.setVisibility(VISIBLE);

                                    hideRoute(loadedStyle);


                                }

                                return true;
                            }

                        });

                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(11.250681, 125.004918))
                                .zoom(16)
                                .build();


                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1);

                        displaySearchList();


                    }
                });

            }
        });


        loadUser();


    }



    public void loadUser(){
        if (firebaseAuth.getCurrentUser() != null){

            db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    if (value.exists()) {


                        loadId = value.getId();
                        loadName = value.getString("name");
                        loadAddress = value.getString("address");
                        loadNumber = value.getString("phone");

                        userName = findViewById(R.id.userName);
                        userNumber = findViewById(R.id.userNumber);
                        userAddress = findViewById(R.id.userAddress);

                        userName.setText(loadName);
                        userNumber.setText(loadNumber);
                        userAddress.setText(loadAddress);

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        storageReference.child(loadId+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(MainActivity.this).load(uri).centerCrop().dontAnimate().into(profileImage);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Glide.with(MainActivity.this).load(R.drawable.doctor_profile).centerCrop().dontAnimate().into(profileImage);

                            }
                        });

                        loadRequest();




                    }else{

                        Intent intent = new Intent(getApplicationContext(), Signin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                }
            });

        }else{
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }

    }




    public void displaySearchList(){


        Date currentTime = Calendar.getInstance().getTime();
        List<Feature> featurelist = new ArrayList<>();
        TextView profileMedicalName = (TextView) findViewById(R.id.profileFacilityName);
        TextView profileMedicalType = (TextView) findViewById(R.id.profileFacilityType);





        db.collection("Facility").orderBy("facility_name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {

                doctorType.clear();
                type_myLists.clear();

                nearby_myLists.clear();
                search_myLists.clear();
                mapboxMap.clear();

                loadedStyle.removeLayer(FACILITYMARKER_LAYER_ID);
                loadedStyle.removeSource(FACILITYGEOJSON_SOURCE_ID);
                loadedStyle.removeImage("facilityImage");


                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.testdoc);
                loadedStyle.addImage("facilityImage", icon);

                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Icon markerIcon = iconFactory.fromResource(R.drawable.testdoc);


               // Double rating = 0.0;
              //  Double my_rating= 0.0;

                if (error != null) {
                    Toast.makeText(MainActivity.this, "Listen failed: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                profilePatient.setVisibility(VISIBLE);
                profileMedical.setVisibility(INVISIBLE);

                for (DocumentSnapshot document : snapshots.getDocuments()) {
                    int count = 0;
                    Double rating = 0.0;
                    Double my_rating= 0.0;
                    //doctor



                    if (document.getId().matches(firebaseAuth.getCurrentUser().getUid())){
                        profilePatient.setVisibility(INVISIBLE);
                        profileMedical.setVisibility(VISIBLE);

                        facilityDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                View myLayout = LayoutInflater.from(v.getContext()).inflate(R.layout.delete_facility, null);

                                Button facilityDelete = (Button) myLayout.findViewById(R.id.facilityDelete);
                                Button facilityDismiss = (Button) myLayout.findViewById(R.id.facilityDismiss);
                                TextView facilityText = (TextView) myLayout.findViewById(R.id.facilityText);

                                facilityText.setText("Are you sure you want to permanently delete "+profileMedicalName.getText()+" ?");


                                facilityDelete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        db.collection("Facility").document(firebaseAuth.getCurrentUser().getUid())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        deleteDialog.dismiss();
                                                        Toast.makeText(context, "Successfully deleted", Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                    }
                                });

                                facilityDismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteDialog.dismiss();
                                    }
                                });


                                builder.setView(myLayout);
                                deleteDialog = builder.create();

                                deleteDialog.setCancelable(true);
                                deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                deleteDialog.show();

                            }
                        });

                        facilityView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });


                        profileMedicalName.setText(document.getString("facility_name"));
                        profileMedicalType.setText(document.getString("facility_details"));
                    }



                    Map<String, Object> list = (Map<String, Object>) document.get("facility_ratings");
                    for (Map.Entry<String, Object> e : list.entrySet()) {
                        count++;
                        rating += Double.parseDouble(e.getValue().toString());

                        if (e.getKey().matches(firebaseAuth.getCurrentUser().getUid())){
                            my_rating = Double.parseDouble(e.getValue().toString());
                            if (viewSearch.getVisibility() == VISIBLE){
                               // MainActivity.searchRatingBar.setRating(my_rating.floatValue());
                            }

                        }else{
                           // my_rating = 0.0;
                           // MainActivity.searchRatingBar.setRating(my_rating.floatValue());
                        }

                    }

                    Double finalRating = Double.parseDouble(String.valueOf(rating)) / Double.parseDouble(String.valueOf(count));

                    if (currentViewId != null && currentViewId.matches(document.getId())){

                        MainActivity.searchFacilityName.setText(document.getString("facility_name"));
                        MainActivity.searchFacilityLocation.setText(document.getString("facility_address"));
                        MainActivity.searchRatings.setText(String.format("%.1f", (double)finalRating));
                        MainActivity.searchCounts.setText((String.valueOf("("+count+" reviews)")));
                        MainActivity.searchFacilityDetails.setText(document.getString("facility_details"));


                    }


                    JsonObject properties = new JsonObject();
                    properties.addProperty("details", document.getString("facility_details"));


                    if (!doctorType.contains(document.getString("facility_details"))){
                        doctorType.add(document.getString("facility_details").trim());
                        type_myLists.add(new TypeList(document.getString("facility_details")));

                    }

                    featurelist.add(Feature.fromGeometry(Point.fromLngLat(document.getDouble("facility_longitude"),document.getDouble("facility_latitude")),properties,document.getId()));
                    search_myLists.add(new SearchList(document.getId(),document.getString("facility_name"),document.getString("facility_details"),document.getString("facility_address"),(double)finalRating,(double)my_rating,count,document.getDouble("facility_latitude"),document.getDouble("facility_longitude"),null));
                 /*
                    facilityMarker = mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(document.getDouble("facility_latitude"), document.getDouble("facility_longitude")))
                            .title(document.getString("facility_name"))
                            .setSnippet("Ratings: "+String.valueOf(finalRating))
                            .icon(markerIcon));



                  */


                }






                FeatureCollection featureCollection = FeatureCollection.fromFeatures(featurelist);
                facilityGeoJsonSource = new GeoJsonSource(FACILITYGEOJSON_SOURCE_ID, featureCollection);
                loadedStyle.addSource(facilityGeoJsonSource);


                facilitySymbolLayer = new SymbolLayer(FACILITYMARKER_LAYER_ID, FACILITYGEOJSON_SOURCE_ID)
                        .withProperties(iconImage("facilityImage"),
                              ///  PropertyFactory.textAnchor("".toString()),
                              //  PropertyFactory.textField(Expression.id()),
                              //  PropertyFactory.textSize(12f),
                              //  PropertyFactory.textColor(Color.RED),
                               // PropertyFactory.textKeepUpright(false),
                               // PropertyFactory.textAllowOverlap(true),


                                PropertyFactory.textIgnorePlacement(false),
                                // PropertyFactory.iconAnchor("top".toString()),
                                iconIgnorePlacement(true),
                                PropertyFactory.circleColor(Color.BLACK),
                                        //   PropertyFactory.iconOffset(new Float[]{0f,-9f}),
                                iconAllowOverlap(false),
                                ///     PropertyFactory.textOpacity(0.5f),
                                PropertyFactory.textSize(12f));



                search_adapter = new SearchAdapter(search_myLists, MainActivity.this);
                search_rv.setAdapter(search_adapter);
                search_adapter.notifyDataSetChanged();


                type_adapter = new TypeAdapter(type_myLists, MainActivity.this);
                type_rv.setAdapter(type_adapter);
                type_adapter.notifyDataSetChanged();

                //mapView.invalidate();
                loadedStyle.addLayer(facilitySymbolLayer);

            }
        });



    }


    public void displayAppointments(){
        ImageBadgeView appointmentNotif = findViewById(R.id.appointmentNotif);


        db.collection("Appointments").orderBy("appointment_start", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                calendarView.removeAllEvents();
                appointment_myLists.clear();
                int count = 0;

                if (error != null) {
                    Toast.makeText(MainActivity.this, "Listen failed: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }


                for (DocumentSnapshot document : snapshots.getDocuments()) {

                    if (firebaseAuth.getCurrentUser() != null){


                        if (firebaseAuth.getCurrentUser().getUid().matches(document.getString("appointment_patientID")) || firebaseAuth.getCurrentUser().getUid().matches(document.getString("appointment_doctorID"))){


                            Timestamp appointment_date = (Timestamp) document.getTimestamp("appointment_date");
                            Timestamp appointment_start = (Timestamp) document.getTimestamp("appointment_start");
                            Timestamp appointment_end = (Timestamp) document.getTimestamp("appointment_end");
                            Date date = appointment_date.toDate();
                            Date start = appointment_start.toDate();
                            Date end = appointment_end.toDate();


                            if (new Date().before(start)){


                                Map<String, Object> details = new HashMap<>();

                                details.put("date",date);
                                details.put("start",start);
                                details.put("end",end);
                                details.put("details",document.getString("appointment_details"));
                                details.put("patient_id",document.getString("appointment_patientID"));
                                details.put("doctor_id",document.getString("appointment_doctorID"));
                                details.put("patient_name",document.getString("appointment_patientName"));

                                Event event = new Event(Color.RED, Long.parseLong(String.valueOf(date.getTime())),details);
                                calendarView.addEvent(event);
                                appointment_myLists.add(new AppointmentList(document.getId(),date,start,end,document.getString("name"),document.getString("appointment_details"),document.getString("appointment_patientID"),document.getString("appointment_doctorID")));
                                count++;
                            }



                        }


                    }





                }


                chipNavigationBar.showBadge(R.id.appointment,count);

                appointmentNotif.setBadgeValue(count);


                LinearLayout schedule_empty_state = findViewById(R.id.schedule_empty_state);

                if (appointment_myLists.size() == 0){
                    schedule_empty_state.setVisibility(VISIBLE);
                }else{
                    schedule_empty_state.setVisibility(GONE);
                }


                appointment_adapter = new AppointmentAdapter(appointment_myLists, MainActivity.this);
                appointment_rv.setAdapter(appointment_adapter);
                appointment_adapter.notifyDataSetChanged();

                displayTimeTable();
            }
        });




    }


    public void displayTimeTable(){

        TextView calendarTitle = findViewById(R.id.calendarTitle);

        TimetableView timetable = (TimetableView) findViewById(R.id.timetable);

        List<Event> event= calendarView.getEvents(calendarView.getFirstDayOfCurrentMonth());
        if (event.isEmpty()){
            timetable.setVisibility(GONE);
        }else{

            for (Event e : event){



                String data = e.toString();
                String[] splitted = data.replace("[Event{", "").replace("data={", "").replace("}", "").replace("}", "").replace("]", "").split(",");

                Toast.makeText(MainActivity.this, String.valueOf(splitted), Toast.LENGTH_SHORT).show();
                for (String item : splitted)
                {

                    String[] separated = item.split("=");


                    if (separated[0].trim().matches("timeInMillis")){
                    //    bookCardTime.setText(DateFormat.format("hh:mm aa",new Date(Long.parseLong(separated[1]))));



                    }else if (separated[0].trim().matches("message")){

                     //   bookCardMessage.setText(separated[1]);

                    }
                    else if (separated[0].trim().matches("type")){
                     //   bookCardType.setText(separated[1]);

                    }else if (separated[0].trim().matches("client_id")){
                        String client_id = separated[1];

                    }else if (separated[0].trim().matches("team_id")){


                    }

                }

            }


        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

        calendarTitle.setText(simpleDateFormat.format(calendarView.getFirstDayOfCurrentMonth()));
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                final List<Event>[] events = new List[]{calendarView.getEvents(dateClicked)};
                if (events[0].isEmpty()){
                    timetable.setVisibility(GONE);
                }else {
                    timetable.removeAll();
                    ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                    timetable.setVisibility(VISIBLE);
                    Schedule schedule = new Schedule();


                    for (Event e : events[0]) {

                        final String[] data = {e.toString()};
                        String[] splitted = data[0].replace("Event{", "").replace("data={", "").replace("}", "").replace("}", "").replace("]", "").split(",");

                        for (String item : splitted) {

                            String[] separated = item.split("=");


                            if (separated[0].trim().matches("timeInMillis")) {



                            } else if (separated[0].trim().matches("date")) {



                            } else if (separated[0].trim().matches("doctor_id")) {
                                schedule.setClassTitle(separated[1].toString());

                            }else if (separated[0].trim().matches("patient_name")) {

                             //   schedule.setProfessorName(separated[1].toString());

                                schedule.setClassTitle(separated[1].toString());
                            } else if (separated[0].trim().matches("patient_id")) {




                            } else if (separated[0].trim().matches("start")) {
                                String hrString = String.valueOf(DateFormat.format("HH",new Date(separated[1])));
                                int hr = Integer.parseInt(hrString);

                                String mmString = String.valueOf(DateFormat.format("mm",new Date(separated[1])));
                                int mm = Integer.parseInt(mmString);

                               // Toast.makeText(context, String.valueOf(hr +":"+ mm), Toast.LENGTH_SHORT).show();

                                schedule.setStartTime(new Time(hr,mm));

                            }
                            else if (separated[0].trim().matches("end")) {

                                String hrString = String.valueOf(DateFormat.format("HH",new Date(separated[1])));
                                int hr = Integer.parseInt(hrString);

                                String mmString = String.valueOf(DateFormat.format("mm",new Date(separated[1])));
                                int mm = Integer.parseInt(mmString);

                             //   Toast.makeText(context, String.valueOf(hr +":"+ mm), Toast.LENGTH_SHORT).show();

                                schedule.setEndTime(new Time(hr,mm));

                            } else if (separated[0].trim().matches("details")) {
                                schedule.setClassPlace(separated[1].toString());

                            }

                            schedule.setDay(0);

                            schedules.add(schedule);
                            timetable.add(schedules);


                        }



                    }

                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
              //  calendarTitle.setText(simpleDateFormat.format(firstDayOfNewMonth));

            }
        });










    }


    public void sortNearby(){
        nearby_rv.setVisibility(INVISIBLE);
        nearby_myLists.clear();


        int n = 0;
        while (n < search_myLists.size()) {


            getDistance(origin,
                    Point.fromLngLat(search_myLists.get(n).getMapLong(),
                    search_myLists.get(n).getMapLat()),
                    n,
                    search_myLists.get(n).getId(),
                    search_myLists.get(n).getName(),
                    search_myLists.get(n).getDetails(),
                    search_myLists.get(n).getMapLat(),
                    search_myLists.get(n).getMapLong());
            /*
            try {
                getDistance(origin,Point.fromLngLat(nearby_myLists.get(n).getLongitude(),nearby_myLists.get(n).getLatitude()),n);
            }catch (Exception e){

                Toast.makeText(activity, "sort error", Toast.LENGTH_SHORT).show();

            }

             */
            n++;
        }




        nearby_rv.setVisibility(VISIBLE);


    }


    public static void displayGallery(Context context,String id){


         gallery =  db.collection("Facility").document(id).collection("gallery")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        gallery_myLists.clear();
                        if (e != null) {

                            return;
                        }

                        if (currentViewId.matches(loadId)){

                            galleryLayout.setVisibility(VISIBLE);


                            if (value.isEmpty()){
                                noGalleryText.setText("Gallery is empty");
                                gallery_rv.setVisibility(GONE);
                                noGalleryText.setVisibility(VISIBLE);


                             }else {
                                gallery_rv.setVisibility(VISIBLE);
                                noGalleryText.setText("No content to display");
                                noGalleryText.setVisibility(GONE);

                            }

                        }else{

                            if (value.isEmpty()){
                              galleryLayout.setVisibility(GONE);
                              gallery_rv.setVisibility(GONE);
                              noGalleryText.setVisibility(VISIBLE);


                            }else{
                             galleryLayout.setVisibility(VISIBLE);
                             gallery_rv.setVisibility(VISIBLE);
                             noGalleryText.setVisibility(GONE);
                        }

                        }



                        for (QueryDocumentSnapshot doc : value) {
                            gallery_myLists.add(new GalleryList(doc.getId(),currentViewId));

                        }

                        gallery_adapter = new GalleryAdapter(gallery_myLists, context);
                        gallery_rv.setAdapter(gallery_adapter);
                        gallery_adapter.notifyDataSetChanged();
                    }
                });


        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(currentViewId+"/cover.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(context).load(uri).centerCrop().placeholder(R.drawable.loading).dontAnimate().into(facilityCover);
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

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });



    }


    public void uploadCover(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        View myLayout = inflater.inflate(R.layout.upload_image, null);


        Button uploadButton = (Button)myLayout.findViewById(R.id.uploadButton);
        uploadImage = (ImageView)myLayout.findViewById(R.id.uploadImage);
        Drawable drawable = facilityCover.getDrawable();
        uploadImage.setImageDrawable(drawable);


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);

            }
        });



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                        .setLabel("Loading... Please wait")
                        .setCancellable(false)
                        .setMaxProgress(100)
                        .show();


                StorageReference storageRef = firebaseStorage.getReference();
                StorageReference parentRef = storageRef.child(firebaseAuth.getCurrentUser().getUid()+"/cover.jpg");

                uploadImage.setDrawingCacheEnabled(true);
                uploadImage.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) uploadImage.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytes = baos.toByteArray();

                UploadTask uploadTask = parentRef.putBytes(bytes);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        hud.setLabel("Uploading image: " + String.format("%.2f", progress) + "%");
                        hud.setProgress(Integer.valueOf((int) progress));
                        hud.setCancellable(false);
                        // Log.d(TAG, "Upload is " + progress + "% done");
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        //   Log.d(TAG, "Upload is paused");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(v.getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Glide.with(context).load(uploadImage.getDrawable()).centerCrop().placeholder(R.drawable.loading).dontAnimate().into(facilityCover);
                        Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show();
                        uploadDialog.dismiss();


                    }
                });


            }
        });




        builder.setView(myLayout);
        uploadDialog = builder.create();

        uploadDialog.setCancelable(true);
        uploadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        uploadDialog.show();

    }




    public void uploadGallery(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        View myLayout = inflater.inflate(R.layout.upload_image, null);


        Button uploadButton = (Button)myLayout.findViewById(R.id.uploadButton);
        uploadImage = (ImageView)myLayout.findViewById(R.id.uploadImage);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);

            }
        });



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                        .setLabel("Loading... Please wait")
                        .setCancellable(false)
                        .setMaxProgress(100)
                        .show();

                Map<String, Object> data = new HashMap<>();

                data.put("date_added",new Date());

                db.collection("Facility").document(firebaseAuth.getCurrentUser().getUid()).collection("gallery").add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {


                                //firebaseAuth.getCurrentUser().getUid()+"/cover.jpg"


                                StorageReference storageRef = firebaseStorage.getReference();
                                StorageReference parentRef = storageRef.child(firebaseAuth.getCurrentUser().getUid()+"/gallery/"+ documentReference.getId()+".jpg");

                                uploadImage.setDrawingCacheEnabled(true);
                                uploadImage.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) uploadImage.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] bytes = baos.toByteArray();

                                UploadTask uploadTask = parentRef.putBytes(bytes);

                                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        hud.setLabel("Uploading image: " + String.format("%.2f", progress) + "%");
                                        hud.setProgress(Integer.valueOf((int) progress));
                                        hud.setCancellable(false);
                                        // Log.d(TAG, "Upload is " + progress + "% done");
                                    }
                                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                        //   Log.d(TAG, "Upload is paused");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(v.getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                        displayGallery(context,currentViewId);
                                        uploadDialog.dismiss();




                                    }
                                });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }
        });


        builder.setView(myLayout);
        uploadDialog = builder.create();

        uploadDialog.setCancelable(true);
        uploadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        uploadDialog.show();

    }

    public static void hideKeyboard() {
        activity.getCurrentFocus();
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    public void filterByName(String text){
        List<SearchList> temp = new ArrayList();
        for(SearchList d: search_myLists){



            if(d.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }


        }


        search_adapter.updateList(temp);
    }

    static void filterByType(String text){
        List<SearchList> temp = new ArrayList();
        for(SearchList d: search_myLists){

            if(d.getDetails().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }

        }

        search_adapter.updateList(temp);
    }

    public void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
         //   getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }



    public static void setWindowFlag(MainActivity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK){

            try {

                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                uploadImage.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == 200 && resultCode == Activity.RESULT_OK){

            try {

                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);

                profileUploadLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.upload_profile_popup, null);
                ImageView photoView = (ImageView) profileUploadLayout.findViewById(R.id.photoView);
                Button photoUpload = (Button) profileUploadLayout.findViewById(R.id.photoUpload);

                photoView.setImageBitmap(selectedImage);

                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        gallery.setType("image/*");
                        startActivityForResult(gallery, 200);
                    }
                });

                photoUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        hud = KProgressHUD.create(MainActivity.this)
                                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                                .setLabel("Preparing image")
                                .setMaxProgress(100)
                                .show();



                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        StorageReference storageRef = firebaseStorage.getReference();
                        StorageReference parentRef = storageRef.child(MainActivity.loadId+"/userImage.jpg");

                        photoView.setDrawingCacheEnabled(true);
                        photoView.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] bytes = baos.toByteArray();

                        UploadTask uploadTask = parentRef.putBytes(bytes);

                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                hud.setLabel("Uploading profile picture: " + String.format("%.2f", progress) + "%");
                                hud.setProgress(Integer.valueOf((int) progress));
                                hud.setCancellable(false);
                                // Log.d(TAG, "Upload is " + progress + "% done");
                            }
                        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                //   Log.d(TAG, "Upload is paused");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                hud.dismiss();
                                Toast.makeText(v.getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                storageReference.child(loadId+"/userImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(MainActivity.this).load(uri).centerCrop().dontAnimate().into(profileImage);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Glide.with(MainActivity.this).load(R.drawable.doctor_profile).centerCrop().dontAnimate().into(profileImage);

                                    }
                                });

                                hud.dismiss();
                                profileDialog.dismiss();


                            }
                        });


                    }
                });

                builder.setView(profileUploadLayout);
                profileDialog = builder.create();

                profileDialog.setCancelable(true);
                profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                profileDialog.show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }



    }



    //LOCATION
/*
    public void runBackground() {


        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {


                            LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                            assert locationManager != null;
                            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                            if(isGpsEnabled == false && gpsLayoutIsShowing == false) {


                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                                gpsLayout = inflater.inflate(R.layout.enable_gps, null);


                                Button gpsSettings = (Button) gpsLayout.findViewById(R.id.gpsSettings);
                                gpsSettings.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(isGpsEnabled == false){
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(intent);
                                        }
                                        gpsDialog.dismiss();
                                        gpsLayoutIsShowing = false;


                                    }
                                });


                                builder.setView(gpsLayout);
                                gpsDialog = builder.create();

                                gpsDialog.setCancelable(false);
                                gpsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                gpsLayoutIsShowing = true;
                                gpsDialog.show();

                            }else if (isGpsEnabled == true){
                                gpsDialog.dismiss();
                                gpsLayoutIsShowing = false;

                            }





                            //  updateLocation();


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 10000 ms
    }


 */

    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(10000)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY).setFastestInterval(10000)
                .setMaxWaitTime(10000).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            enableLocationComponent(loadedStyle);
            return;
        }
        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }



    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "Location is needed", Toast.LENGTH_LONG).show();
            //finish();
        }
    }




    private static class MainActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivity> activityWeakReference;


        MainActivityLocationCallback(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    activity.cardNearby.setVisibility(GONE);
                    return;
                }else{
                    if (activity.nearbyExpand.getVisibility() == GONE){


                        activity.cardNearby.setVisibility(VISIBLE);
                    }
                }


                activity.origin = Point.fromLngLat(
                      result.getLastLocation().getLongitude(),
                        result.getLastLocation().getLatitude());






           //     activity.currentLat = result.getLastLocation().getLatitude();
           //     activity.currentLong = result.getLastLocation().getLongitude();


// Create a Toast which displays the new location's coordinates
                //   Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
                //         String.valueOf(result.getLastLocation().getLatitude()), String.valueOf(result.getLastLocation().getLongitude())),
                //         Toast.LENGTH_SHORT).show();




// Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }



    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponentOptions locationComponentOptions =
                    LocationComponentOptions.builder(this)
                            .accuracyAlpha(.6f)
                            .accuracyColor(ContextCompat.getColor(this, R.color.lightbrown))


                           // .pulseEnabled(true)

                           // .pulseMaxRadius(35)
                           // .pulseFadeEnabled(true)
                           // .pulseColor(ContextCompat.getColor(this, R.color.lightbrown))
                            // .pulseAlpha(.5f)
                            // .pulseInterpolator(new BounceInterpolator())
                            .build();

            LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(this, loadedMapStyle)
                    .locationComponentOptions(locationComponentOptions)
                    .build();

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING,2000,17.0,0.0,50.0,null);

            locationComponent.setRenderMode(RenderMode.COMPASS);
            initLocationEngine();

        } else {

            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }


    }



    @SuppressWarnings( {"MissingPermission"})
    private void getRoute(Point myorigin,Point mydestination) {
        MapboxDirections client = MapboxDirections.builder()
                .origin(myorigin)
                .destination(mydestination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Timber.d( "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.d( "No routes found");
                    return;
                }
                DirectionsRoute currentRoute = response.body().routes().get(0);
                initDottedLineSourceAndLayer(loadedStyle);
                drawNavigationPolylineRoute(response.body().routes().get(0),mydestination);





                //Toast.makeText(context, currentRoute.duration().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Timber.d("Error: %s", throwable.getMessage());
                if (!throwable.getMessage().equals("Coordinate is invalid: 0,0")) {
                    Toast.makeText(MainActivity.this,
                            "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDistance(Point myorigin,
                             Point mydestination,
                             Integer nearbyID,
                             String nearbyKey,
                             String nearbyName,
                             String nearbyDetails,
                             Double nearbyLat,
                             Double nearybyLong) {

        MapboxDirections client = MapboxDirections.builder()
                .origin(myorigin)
                .destination(mydestination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Timber.d( "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.d( "No routes found");
                    return;
                }


                Double distance = response.body().routes().get(0).distance();

                if (distance <= 5000){

                    nearby_myLists.add(new NearbyList(nearbyKey,
                            nearbyName,
                            nearbyDetails,
                            nearbyLat,
                            nearybyLong,
                            distance));

                }
                TextView nearbyEmpty = (TextView) findViewById(R.id.nearbyEmpty);
                if (nearby_myLists.isEmpty()){
                    nearbyEmpty.setVisibility(GONE);
                }else{
                    nearbyEmpty.setVisibility(VISIBLE);
                }

                if (nearbyID == search_myLists.size() -1 ){
                    showNearby();
                }


            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Timber.d("Error: %s", throwable.getMessage());
                if (!throwable.getMessage().equals("Coordinate is invalid: 0,0")) {
                    Toast.makeText(MainActivity.this,
                            "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkConnection(){
        CardView offlineNotification = (CardView) findViewById(R.id.offlineNotification);

        db.collection("Users")
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {

                        if (querySnapshot.getMetadata().isFromCache() == true){
                            offlineNotification.setVisibility(VISIBLE);
                           // hasConnection = false;

                        }else{
                            offlineNotification.setVisibility(GONE);
                         //   hasConnection = true;
                        }

                    }
                });


    }

    public void showNearby(){

        nearby_adapter = new NearbyAdapter(nearby_myLists, MainActivity.this);
        nearby_rv.setAdapter(nearby_adapter);
        nearby_adapter.notifyDataSetChanged();

    }


    private void drawNavigationPolylineRoute(final DirectionsRoute route,Point target) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(target.latitude(),target.longitude()))
                            .zoom(15)
                            .tilt(0)
                            .build();

                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 2000);
                    viewSelected.setVisibility(GONE);

                    List<Feature> directionsRouteFeatureList = new ArrayList<>();
                    LineString lineString = LineString.fromPolyline(route.geometry(), PRECISION_6);
                    List<Point> coordinates = lineString.coordinates();
                    for (int i = 0; i < coordinates.size(); i++) {
                        directionsRouteFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(coordinates)));
                    }
                    dashedLineDirectionsFeatureCollection = FeatureCollection.fromFeatures(directionsRouteFeatureList);
                    GeoJsonSource source = style.getSourceAs(SOURCE_ID);
                    if (source != null) {
                        source.setGeoJson(dashedLineDirectionsFeatureCollection);
                    }
                }
            });
        }
    }


    private void initDottedLineSourceAndLayer(@NonNull Style loadedMapStyle) {
        GeoJsonSource source = loadedMapStyle.getSourceAs(SOURCE_ID);
        if (source == null) {
            loadedMapStyle.addSource(new GeoJsonSource(SOURCE_ID));
            loadedMapStyle.addLayerBelow(
                    new LineLayer(
                            DIRECTIONS_LAYER_ID, SOURCE_ID).withProperties(
                            lineCap(Property.LINE_CAP_ROUND),
                            lineJoin(Property.LINE_JOIN_ROUND),
                            lineWidth(5f),
                            lineColor(Color.parseColor("#67595E"))
                    ), LAYER_BELOW_ID);
        }
    }
    public void hideRoute(@NonNull Style loadedMapStyle){
        dashedLineDirectionsFeatureCollection = null;
        GeoJsonSource source = loadedMapStyle.getSourceAs(SOURCE_ID);
        if (source != null) {
            source.setGeoJson(dashedLineDirectionsFeatureCollection);
        }
    }
    private Date parseDate(String date) {

        final String inputFormat = "HH:mm";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    public void displayEditServices(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        myLayout = inflater.inflate(R.layout.edit_services_popup, null);

        TextInputEditText editServices = (TextInputEditText)myLayout.findViewById(R.id.editServices);
        edit_service_rv = (RecyclerView)myLayout.findViewById(R.id.edit_services_rec);
        edit_service_rv.setHasFixedSize(true);
        edit_service_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        edit_service_adapter = new ServicesEditAdapter(service_myLists, myLayout.getContext());
        edit_service_rv.setAdapter(edit_service_adapter);
        edit_service_adapter.notifyDataSetChanged();


        Button serviceAdd = (Button) myLayout.findViewById(R.id.serviceAdd);
        serviceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editServices.getText().toString().isEmpty()){
                    editServices.setError("Provide some service");
                    editServices.requestFocus();
                    return;

                }

                Map<String, Object> data = new HashMap<>();
                data.put("service", editServices.getText().toString());


                db.collection("Facility").document(currentViewId).collection("Services").document()
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                edit_service_adapter = new ServicesEditAdapter(service_myLists, myLayout.getContext());
                                edit_service_rv.setAdapter(edit_service_adapter);
                                edit_service_adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });





        builder.setView(myLayout);
        editServicesDialog = builder.create();

        editServicesDialog.setCancelable(true);
        editServicesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editServicesDialog.show();

    }

    public void displayUsers(){


        users_rv = (RecyclerView) findViewById(R.id.users_rec);
        users_rv.setHasFixedSize(true);
        users_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        users_myLists = new ArrayList<>();

        Users users = new Users();
        users.displayUsers(context);
    }

    public void displayAddAffiliates(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        myLayout = inflater.inflate(R.layout.add_popup, null);


        doctors_rv = (RecyclerView) myLayout.findViewById(R.id.add_rec);
        doctors_rv.setHasFixedSize(true);
        doctors_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        doctors_myLists = new ArrayList<>();

        Add add = new Add();
        add.displayDoctors(context);

        Button addClose = (Button) myLayout.findViewById(R.id.addClose);
        addClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAffiliates.dismiss();

            }
        });



        builder.setView(myLayout);
        addAffiliates = builder.create();

        addAffiliates.setCancelable(true);
        addAffiliates.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addAffiliates.show();

    }

    public void loadRequest() {


        request_rv = (RecyclerView) findViewById(R.id.request_rec);
        request_rv.setHasFixedSize(true);
        request_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        request_myLists = new ArrayList<>();

        db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).collection("Request")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        int count = 0;
                        if (e != null) {

                            return;
                        }
                        request_myLists.clear();
                        if (!value.isEmpty()){
                            for (QueryDocumentSnapshot doc : value) {
                                request_myLists.add(new RequestList(doc.getId()));
                                count+=1;
                            }
                        }

                        if (count !=0){
                            chipNavigationBar.showBadge(R.id.profile,count);
                        }else{
                            chipNavigationBar.dismissBadge(R.id.profile);


                        }
                        ConstraintLayout affiliatesCard = (ConstraintLayout) findViewById(R.id.affiliatesCard);
                        if (request_myLists.isEmpty()){
                            affiliatesCard.setVisibility(GONE);

                        }else{
                            affiliatesCard.setVisibility(VISIBLE);
                        }


                        request_adapter = new RequestAdapter(request_myLists, context);
                        request_rv.setAdapter(request_adapter);
                        request_adapter.notifyDataSetChanged();

                    }
                });

    }




    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (appointmentExpand.getVisibility() == VISIBLE){
            appointmentExpand.setVisibility(GONE);
            return;
        }else if (appointmentLayout.getVisibility() == VISIBLE){
            appointmentLayout.setVisibility(GONE);
            return;
        }else if (viewAppointment.getVisibility() == VISIBLE){
            viewAppointment.setVisibility(GONE);
            return;
        }else if (viewSearch.getVisibility() == VISIBLE){
            viewSearch.setVisibility(GONE);
            return;
        }else if (viewSelected.getVisibility() == VISIBLE){
            chipNavigationBar.setItemSelected(R.id.map, true);
            return;
        }else if (setupForm.getVisibility() == VISIBLE){
            chipNavigationBar.setItemSelected(R.id.map, true);
            return;
        }else if (setupLocation.getVisibility() == VISIBLE){
            chipNavigationBar.setItemSelected(R.id.map, true);
            return;
        }else if (chipNavigationBar.getSelectedItemId()  != R.id.map){
            chipNavigationBar.setItemSelected(R.id.map, true);
            return;
        }

        super.onBackPressed();
    }
}