package com.example.androidproject.ui.ui.upcoming;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.androidproject.MainActivity;
import com.example.androidproject.R;
import com.example.androidproject.SimpleService;
import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.example.androidproject.reciever.DataForAlarm;
import com.example.androidproject.ui.AddTripActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.siddharthks.bubbles.FloatingBubblePermissions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAdapter extends FirebaseRecyclerAdapter<Trip, AddAdapter.MyViewHolder> {
    int i = 0;

    AlertDialog alertDialog;
    StringBuilder sb;
    public static int screen = 1;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public AddAdapter(@NonNull FirebaseRecyclerOptions<Trip> options) {
        super(options);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Trip trip) {

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRefreshLayout, String.valueOf(trip.getTripName()));
        viewBinderHelper.closeLayout(trip.getTripName());
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        user = firebaseAuth.getCurrentUser();

        DatabaseReference scoresRefh1 = FirebaseDatabase.getInstance().getReference().child("history" + MainActivity.storedPreference);
        scoresRefh1.keepSynced(true);
        DatabaseReference scoresRefh2 = FirebaseDatabase.getInstance().getReference().child("history" + MainActivity.storedUid);
        scoresRefh2.keepSynced(true);
        DatabaseReference scoresRefc1 = FirebaseDatabase.getInstance().getReference().child("tripCancel" + MainActivity.storedPreference);
        scoresRefc1.keepSynced(true);

        DatabaseReference scoresRefc2 = FirebaseDatabase.getInstance().getReference().child("tripCancel" + MainActivity.storedUid);
        scoresRefc2.keepSynced(true);
        DatabaseReference scoresReft1 = FirebaseDatabase.getInstance().getReference().child("trips" + MainActivity.storedPreference);
        scoresReft1.keepSynced(true);

        DatabaseReference scoresReft2 = FirebaseDatabase.getInstance().getReference().child("trips" + MainActivity.storedUid);
        scoresReft2.keepSynced(true);


//        if(trip.getUID().equals(user.getUid())) {

        holder.txvTripNam.setText(trip.getTripName());
        holder.txvEndPoint.setText(trip.getEndPoint());
        holder.txvStartPoint.setText(trip.getStartPoint());
        holder.tvTime.setText(trip.getAlarm());
        holder.tvDate.setText(trip.getDate());
        holder.setNotes(trip.getNotes());
        holder.tvSeRepeat.setText((trip.getRepeat()));
        holder.tvSetWay.setText(trip.getWay());

        holder.ivSetNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LayoutInflater li = LayoutInflater.from(getApplicationContext());
                    View promptsView = li.inflate(R.layout.dialog_edittext, null);
                    final EditText userNote = promptsView.findViewById(R.id.ed_note_dialog);
                    new AlertDialog.Builder(v.getContext()).setMessage(" Note")
                            .setView(promptsView)
                            .setNegativeButton("cancle", null)
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    sb = new StringBuilder();
                                    Log.i("AddAdapter", "onChildAdded: bbbbbbbbbb " + sb.toString());
                                    if (trip.getNotes() != null)
                                        sb.append(trip.getNotes());

                                    sb.append(userNote.getText().toString() + "#");

                                    //  String str= sb.toString();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("alarm", trip.getAlarm());
                                    map.put("date", trip.getDate());
                                    map.put("endPoint", trip.getEndPoint());
                                    map.put("startPoint", trip.getStartPoint());
                                    map.put("tripName", trip.getTripName());
                                    map.put("repeat", trip.getRepeat());
                                    map.put("way", trip.getWay());
                                    map.put("status", Data.UPCOMING);
                                    map.put("notes", sb.toString());
                                    //  trip.setNotes(sb.toString());
                                    Log.i("AddAdapter", "onChildAdded: result " + sb.toString());

                                    sb.equals("");
                                    sb.setLength(0);
                                    if (!MainActivity.storedPreference.equals("null")) {
                                        scoresReft1.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    } else if (!MainActivity.storedUid.equals("no id exist")) {

                                        scoresReft2.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {


                                            }
                                        });
                                    }


                                }

                                ;
                            }).create().show();


                }catch (Exception e){}

            }
        });


        if (trip.getStatus().equals(Data.UPCOMING)) {
            holder.tvStatus.setText(R.string.upComing);
        } else if (trip.getStatus().equals(Data.CANCEL)) {
            holder.tvStatus.setText(R.string.canceled);
            holder.btCancel.setVisibility(View.GONE);
        } else if (trip.getStatus().equals(Data.DONE)) {
            holder.tvStatus.setText(R.string.done);
            holder.btCancel.setVisibility(View.GONE);

        }

        holder.btCancel.setOnClickListener(v -> {


            try {
                Map<String, Object> map = new HashMap<>();
//            if (trip.getUID() != null)
//                map.put("UID", trip.getUID());
//            if (trip.getToken() != null)
//                map.put("UID", trip.getToken());

                map.put("alarm", trip.getAlarm());
                map.put("date", trip.getDate());
                map.put("endPoint", trip.getEndPoint());
                map.put("startPoint", trip.getStartPoint());
                map.put("tripName", trip.getTripName());
                map.put("repeat", trip.getRepeat());
                map.put("way", trip.getWay());
                map.put("status", Data.CANCEL);
                map.put("endLat", trip.getEndLat());
                map.put("latLogEnd", trip.getLatLogEnd());
                map.put("endLong", trip.getEndLong());
                map.put("startLat", trip.getStartLat());
                map.put("startLong", trip.getStartLong());

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txvEndPoint.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Cancel this Trip.");

                if (!MainActivity.storedPreference.equals("null")) {
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        scoresReft1.child(getRef(position).getKey()).removeValue();
                        scoresRefh1.push().setValue(map);

                        scoresRefc1.push().setValue(map)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(holder.txvEndPoint.getContext(), "Trip Cancel is Successfully.", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {
                                    Toast.makeText(holder.txvEndPoint.getContext(), "Error while Cancel", Toast.LENGTH_SHORT).show();

                                });

                        DataForAlarm.deleteAlarmForOneTrip(map);

                    });
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                        Toast.makeText(holder.tvTime.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    });
                    builder.show();


                } else if (!MainActivity.storedUid.equals("no id exist")) {

                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        scoresReft2.child(getRef(position).getKey()).removeValue();
                        scoresRefh2.push().setValue(map);

                        scoresRefc2.push().setValue(map)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(holder.txvEndPoint.getContext(), "Trip Cancel is Successfully.", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {
                                    Toast.makeText(holder.txvEndPoint.getContext(), "Error while Cancel", Toast.LENGTH_SHORT).show();

                                });

                        DataForAlarm.deleteAlarmForOneTrip(map);

                    });
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                        Toast.makeText(holder.tvTime.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    });
                    builder.show();


                }

            }catch (Exception e){


            }



        });///

        holder.ivNotes.setOnClickListener(v -> {

            // Toast.makeText(v.getContext(), "position: "+position, Toast.LENGTH_SHORT).show();


            if (i == 1) {
                holder.linearLayout.setVisibility(View.VISIBLE);
                i++;


            } else {
                holder.linearLayout.setVisibility(View.GONE);
                i = 1;
            }


        });


        holder.ivEdit.setOnClickListener(v -> {
            try {


//
//
//            Intent intent = new Intent(holder.btCancel.getContext(),AddTripActivity.class);
////            intent.putExtra("alarm",trip.getAlarm());
////            intent.putExtra("date",trip.getDate());
////            intent.putExtra("endPoint",trip.getEndPoint());
////            intent.putExtra("startPoint", trip.getStartPoint());
////            intent.putExtra("tripName", trip.getTripName());
////            intent.putExtra("repeat",trip.getRepeat());
////            intent.putExtra("way",trip.getWay());
////            intent.putExtra("endLat",trip.getEndLat());
////            intent.putExtra("latLogEnd",trip.getLatLogEnd());
////            intent.putExtra("endLong", trip.getEndLong());
////            intent.putExtra("startLat",trip.getStartLat());
////            intent.putExtra("startLong", trip.getStartLong());
//            intent.putExtra("update", "1");
//            holder.tvSetWay.getContext().startActivity(intent);
//
//            Data.alarm=trip.getAlarm();
//            Data.date=trip.getDate();
//            Data.endPoint=trip.getEndPoint();
//            Data.startPoint= trip.getStartPoint();
//            Data.tripName= trip.getTripName();
//            Data.repeat=trip.getRepeat();
//            Data.way=trip.getWay();
//            Data.endLat=trip.getEndLat();
//            Data.latLogEnd=trip.getLatLogEnd();
//            Data.endLong=trip.getEndLong();
//            Data.startLat=trip.getStartLat();
//            Data.startLong= trip.getStartLong();

                ////////////////////////////////////////////////////////////////////////////////

                DialogPlus dialog = DialogPlus.newDialog(holder.ivDelete.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_add_trip))
                        .setExpanded(true, 1800)
                        .create();
                dialog.show();
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int[] t1Hour = new int[1];
                final int[] t1Minut = new int[1];
                View view = dialog.getHolderView();
                EditText edEndPoint = view.findViewById(R.id.edEndPoint);
                EditText edTripName = view.findViewById(R.id.edTripName);
                EditText edStartPoint = view.findViewById(R.id.edStartPoint);
                ImageView imvDate = view.findViewById(R.id.imvDate);
                ImageView imvTime = view.findViewById(R.id.imvTime);
                TextView tevTitle = view.findViewById(R.id.tevTitle);
                TextView tvDate = view.findViewById(R.id.tvDate);
                TextView tvTime = view.findViewById(R.id.tvTime);

                edStartPoint.setFocusable(false);
                edEndPoint.setFocusable(false);
                AutoCompleteTextView repeat = view.findViewById(R.id.repeat);
                AutoCompleteTextView way = view.findViewById(R.id.way);
                Button btUpdate = view.findViewById(R.id.btAdd);

                edStartPoint.setText(trip.getStartPoint());
                edEndPoint.setText(trip.getEndPoint());
                edTripName.setText(trip.getTripName());
                tvDate.setText(trip.getDate());
                tvTime.setText(trip.getAlarm());
                repeat.setText(trip.getRepeat());
                way.setText(trip.getWay());
                ArrayAdapter<String> arrayAdapterRepeat;
                ArrayAdapter<String> arrayAdapterWay;
                // add list in repeat and way
                ArrayList<String> repeatList = new ArrayList<>();
                ArrayList<String> wayList = new ArrayList<>();
                wayList.add(imvDate.getContext().getString(R.string.oneWay));
                wayList.add(imvDate.getContext().getString(R.string.towWay));
                repeatList.add(imvDate.getContext().getString(R.string.noRepeat));
                repeatList.add(imvDate.getContext().getString(R.string.repeatDaily));
                repeatList.add(imvDate.getContext().getString(R.string.repeatWeekly));
                repeatList.add(imvDate.getContext().getString(R.string.repeatMonthly));
                arrayAdapterRepeat = new ArrayAdapter(holder.txvEndPoint.getContext(), R.layout.tv_entity, repeatList);
                repeat.setAdapter(arrayAdapterRepeat);
                arrayAdapterWay = new ArrayAdapter(holder.tvTime.getContext(), R.layout.tv_entity, wayList);
                way.setAdapter(arrayAdapterWay);

                //update title and button text
                tevTitle.setText(R.string.update_trip);
                btUpdate.setText(R.string.update_trip);

                way.setFocusable(false);
                repeat.setFocusable(false);
                //show timeDialog
                imvTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(holder.tvTime.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1Hour[0] = hourOfDay;
                                t1Minut[0] = minute;

                                calendar.set(0, 0, 0, t1Hour[0], t1Minut[0]);
                                tvTime.setText(DateFormat.format("hh:mm aa", calendar));

                            }
                        }, 12, 0, false);
                        timePickerDialog.updateTime(t1Hour[0], t1Minut[0]);
                        timePickerDialog.show();
                    }
                });
                imvDate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        DatePickerDialog datePickerDialog = new DatePickerDialog(holder.tvTime.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth
                                , AddTripActivity.listenerDate, year, month, day);
                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        datePickerDialog.show();
                    }
                });
                AddTripActivity.listenerDate = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        tvDate.setText(date);
                    }
                };
                btUpdate.setOnClickListener(v1 -> {

                    if (screen == 1) {
                        Map<String, Object> map = new HashMap<>();

                        map.put("alarm", tvTime.getText().toString());
                        map.put("date", tvDate.getText().toString());
                        map.put("endPoint", edEndPoint.getText().toString());
                        map.put("startPoint", edStartPoint.getText().toString());
                        map.put("tripName", edTripName.getText().toString());
                        map.put("repeat", repeat.getText().toString());
                        map.put("way", way.getText().toString());
                        map.put("status", Data.UPCOMING);
                        if (!MainActivity.storedPreference.equals("null")) {///
                            scoresReft1.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.tvTime.getContext(), "data Updated", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.tvTime.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            });
                            deleteAndFillAlarm();

                        } else if (!MainActivity.storedUid.equals("no id exist")) {

                            scoresReft2.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.tvTime.getContext(), "data Updated", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.tvTime.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            });
                            deleteAndFillAlarm();

                        }


                    } else if (screen == 2) {///
                        Map<String, Object> map = new HashMap<>();
                        map.put("alarm", tvTime.getText().toString());
                        map.put("date", tvDate.getText().toString());
                        map.put("endPoint", edEndPoint.getText().toString());
                        map.put("startPoint", edStartPoint.getText().toString());
                        map.put("tripName", edTripName.getText().toString());
                        map.put("repeat", repeat.getText().toString());
                        map.put("way", way.getText().toString());
                        map.put("status", Data.CANCEL);
                        if (!MainActivity.storedPreference.equals("null")) {
                            scoresRefh1.push().setValue(map);


                            scoresRefc1.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.tvTime.getContext(), "data Updated", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.tvTime.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            });
                            deleteAndFillAlarm();

                        }////
                        else if (!MainActivity.storedUid.equals("no id exist")) {
                            scoresRefh2.push().setValue(map);


                            scoresRefc2.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.tvTime.getContext(), "data Updated", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.tvTime.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            });

                            deleteAndFillAlarm();

                        }

                    }

                });


            }catch (Exception e){


            }



        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.txvEndPoint.getContext());
                    builder.setTitle("Are You Sure?");
                    builder.setMessage("Delete data cant be Undo.");
                    builder.setPositiveButton("Delete", (dialog, which) -> {
                        if (!MainActivity.storedPreference.equals("null")) {
                            if (screen == 1) {
                                scoresReft1.child(getRef(position).getKey()).removeValue();
                                deleteAndFillAlarm();
                            } else if (screen == 2) {
                                scoresRefc1.child(getRef(position).getKey()).removeValue();


                            } else if (screen == 3) {
                                scoresRefh1.child(getRef(position).getKey()).removeValue();


                            }
                        } else if (!MainActivity.storedUid.equals("no id exist")) {

                            if (screen == 1) {
                                scoresReft2.child(getRef(position).getKey()).removeValue();
                                deleteAndFillAlarm();

                            } else if (screen == 2) {
                                scoresRefc2.child(getRef(position).getKey()).removeValue();


                            } else if (screen == 3) {
                                scoresRefh2.child(getRef(position).getKey()).removeValue();


                            }

                        }

                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        Toast.makeText(holder.tvTime.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    });
                    builder.show();


                }catch (Exception e){

                }



            }



        });
        holder.btStart.setOnClickListener(v -> {
            try {

                DialogPlus dialog = DialogPlus.newDialog(holder.ivDelete.getContext())
                        .setContentHolder(new ViewHolder(R.layout.map_transportation))
                        .setExpanded(true, 1200)
                        .create();
                dialog.show();
                View view = dialog.getHolderView();
                TextView tvBicycle = view.findViewById(R.id.tvBicycle);
                TextView tvBus = view.findViewById(R.id.tvBus);
                TextView tvWalk = view.findViewById(R.id.tvWalk);
                TextView tvTwoWheeler = view.findViewById(R.id.tvTwoWheeler);


                String lat = trip.getEndPoint();
                tvBicycle.setOnClickListener(m -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "&mode=b"));
                    intent.setPackage("com.google.android.apps.maps");

                    if (intent.resolveActivity(tvBicycle.getContext().getPackageManager()) != null) {
                        tvBicycle.getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                });
                tvBus.setOnClickListener(m -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "&mode=d"));
                    intent.setPackage("com.google.android.apps.maps");

                    if (intent.resolveActivity(tvBicycle.getContext().getPackageManager()) != null) {
                        tvBicycle.getContext().startActivity(intent);
                        dialog.dismiss();

                    }
                });
                tvWalk.setOnClickListener(m -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "&mode=w"));
                    intent.setPackage("com.google.android.apps.maps");

                    if (intent.resolveActivity(tvBicycle.getContext().getPackageManager()) != null) {
                        tvBicycle.getContext().startActivity(intent);
                        dialog.dismiss();

                    }
                });
                tvTwoWheeler.setOnClickListener(m -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "&mode=l"));
                    intent.setPackage("com.google.android.apps.maps");

                    if (intent.resolveActivity(tvBicycle.getContext().getPackageManager()) != null) {
                        tvBicycle.getContext().startActivity(intent);
                        dialog.dismiss();


                    }
                });

                Map<String, Object> map = new HashMap<>();
                map.put("alarm", trip.getAlarm());
                map.put("date", trip.getDate());
                map.put("endPoint", trip.getEndPoint());
                map.put("startPoint", trip.getStartPoint());
                map.put("tripName", trip.getTripName());
                map.put("repeat", trip.getRepeat());
                map.put("way", trip.getWay());
                map.put("status", Data.DONE);
                map.put("endLat", trip.getEndLat());
                map.put("latLogEnd", trip.getLatLogEnd());
                map.put("endLong", trip.getEndLong());
                map.put("startLat", trip.getStartLat());
                map.put("startLong", trip.getStartLong());

                if (!MainActivity.storedPreference.equals("null")) {
                    if (screen == 1) {
                        scoresReft1.child(getRef(position).getKey()).removeValue();
                        DataForAlarm.deleteAlarmForOneTrip(map);

                    } else if (screen == 2) {
                        scoresRefc1.child(getRef(position).getKey()).removeValue();


                        scoresRefh1.push().setValue(map)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(holder.txvEndPoint.getContext(), "Trip Done is Successfully.", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {
                                    Toast.makeText(holder.txvEndPoint.getContext(), "Error while Cancel", Toast.LENGTH_SHORT).show();
                                });

                        FloatingBubblePermissions.startPermissionRequest((Activity) holder.btStart.getContext());
                        Intent intent = new Intent(getApplicationContext(), SimpleService.class);
                        intent.putExtra("note", trip.getNotes());
                        getApplicationContext().startService(intent);

                    }
                } else if (!MainActivity.storedUid.equals("no id exist")) {


                    if (screen == 1) {
                        scoresReft2.child(getRef(position).getKey()).removeValue();
                        DataForAlarm.deleteAlarmForOneTrip(map);

                    } else if (screen == 2) {
                        scoresRefc2.child(getRef(position).getKey()).removeValue();
                    }
                    scoresRefh2.push().setValue(map)
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(holder.txvEndPoint.getContext(), "Trip Cancel is Successfully.", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> {
                                Toast.makeText(holder.txvEndPoint.getContext(), "Error while Cancel", Toast.LENGTH_SHORT).show();

                            });


                    FloatingBubblePermissions.startPermissionRequest((Activity) holder.btStart.getContext());
                    Intent intent = new Intent(getApplicationContext(), SimpleService.class);
                    intent.putExtra("note", trip.getNotes());
                    getApplicationContext().startService(intent);

                }
                Toast.makeText(holder.txvEndPoint.getContext(), "Done while Cancel", Toast.LENGTH_SHORT).show();


            }catch (Exception e){

            }


        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txvStartPoint, txvEndPoint, txvTripNam, tvDate, tvTime, tvStatus, btStart, btCancel, tvSetWay, tvSeRepeat;
        ImageView ivNotes, ivDelete, ivEdit, ivSetNotes;
        SwipeRevealLayout swipeRefreshLayout;
        ChipGroup chipGroup;
        LinearLayout linearLayout;
        EditText edNote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            txvStartPoint = itemView.findViewById(R.id.txvStartPointt);
            txvTripNam = itemView.findViewById(R.id.txvTripName);
            txvEndPoint = itemView.findViewById(R.id.txvEndPoint);
            btStart = itemView.findViewById(R.id.btStart);
            ivNotes = itemView.findViewById(R.id.ivNotes);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            btCancel = itemView.findViewById(R.id.btCancel);
            chipGroup = itemView.findViewById(R.id.chipGroupp);
            tvSetWay = itemView.findViewById(R.id.tvSetWay);
            tvSeRepeat = itemView.findViewById(R.id.tvSetRepeat);
            ivSetNotes = itemView.findViewById(R.id.ivSetNotes);
            swipeRefreshLayout = itemView.findViewById(R.id.main_container);
            edNote = itemView.findViewById(R.id.ed_note_dialog);
        }

        public void setNotes(String s) {


            if (s != null) {
                String[] arrOfStr = s.split("#");
                for (String a : arrOfStr) {
                    Chip chip = new Chip(txvEndPoint.getContext());

                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(chip.getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Choice);
                    chip.setChipDrawable(chipDrawable);
                    chip.setPadding(10, 10, 10, 10);
                    chip.setText(a);

                    chipGroup.addView(chip);
                }
            }
        }
    }

    public static void deleteAndFillAlarm() {

        ArrayList<Trip> arrayTrips = new ArrayList<>();
        DataForAlarm.DeleteAllAlarms();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("trips" + Data.USER.getUid());

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    Trip trip = dataSnapshot.getValue(Trip.class);
                    arrayTrips.add(trip);
                }
                Log.i("Main", "onDeleteAndFill: Array Size is " + arrayTrips.size());
                //Log.i("Main", "onDeleteAndFill: Array is "+ arrayTrips);
                DataForAlarm.setDataForAlarm(arrayTrips);
            }
        });



    }
}