package vn.ochabot.seaconnect.challenges;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.Nullable;
import vn.ochabot.seaconnect.R;
import vn.ochabot.seaconnect.core.base.BaseActivity;
import vn.ochabot.seaconnect.core.helpers.UserHelper;

import java.util.*;

/**
 * @author Thien.
 */
public class ChallengesActivity extends BaseActivity {

    FirebaseFirestore db;
    CollectionReference matches;
    MatchAdapter adapter = new MatchAdapter();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        matches = db.collection("matches");
        findViewById(R.id.create_match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMatch();
            }
        });
        recyclerView = findViewById(R.id.match_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        showMatches();
    }

    private void addMatch() {
        Map<String, Object> data = new HashMap<>();
        data.put(Match.HOST, UserHelper.getUserName());
        data.put(Match.TITLE, "Bi lac -zz");
        data.put(Match.MATCH_STATUS, 0);
        data.put(Match.TIME_STAMP, System.currentTimeMillis());
        data.put(Match.TEAM_1, new ArrayList<String>());
        data.put(Match.TEAM_2, new ArrayList<String>());
        matches.document(String.valueOf(System.nanoTime())).set(data).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "add failed.", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "add success");
            }
        });
    }

    private void showMatches() {
        matches.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot values, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e);
                    return;
                }
                adapter.mData.clear();
                for (QueryDocumentSnapshot doc : values) {
                    adapter.mData.add(doc);
                }
                Collections.sort(adapter.mData, new Comparator<QueryDocumentSnapshot>() {
                    @Override
                    public int compare(QueryDocumentSnapshot d1, QueryDocumentSnapshot d2) {
                        long time1 = 0;
                        if (d1.get("timestamp") != null) {
                            time1 = Long.valueOf(String.format("%s", d1.get("timestamp")));
                        }
                        long time2 = 0;
                        if (d2.get("timestamp") != null) {
                            time2 = Long.valueOf(String.format("%s", d2.get("timestamp")));
                        }
                        return (int) (time1 - time2);
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int contentView() {
        return R.layout.activity_challenge_layout;
    }

    @Override
    public int title() {
        return 0;
    }


    public static class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

        List<QueryDocumentSnapshot> mData = new ArrayList<>();

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_match_item, viewGroup, false);
            final MyViewHolder viewHolder = new MyViewHolder(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChallengesDetailActivity.class);
                    intent.putExtra(ChallengesDetailActivity.MATCH_ID, (String)view.getTag());
                    intent.putExtra(ChallengesDetailActivity.MATCH_HOST, viewHolder.hostTv.getText());
                    intent.putExtra(ChallengesDetailActivity.MATCH_TITLE, viewHolder.titleTv.getText());
                    Pair<View, String> p1 = Pair.create(view.findViewById(R.id.match_host), "match_host");
                    Pair<View, String> p2 = Pair.create(view.findViewById(R.id.match_title), "match_title");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) view.getContext(), p1, p2);
                    ActivityCompat.startActivity(view.getContext(), intent, options.toBundle());
                }
            });
            return viewHolder;
        }

        @Override
        public int getItemCount() {
            if (mData == null) {
                return 0;
            }
            return mData.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder vh, int i) {
            vh.itemView.setTag(mData.get(i).getId());
            vh.hostTv.setText(String.format("%s", mData.get(i).get(Match.HOST)));
            vh.titleTv.setText(String.format("%s", mData.get(i).get(Match.TITLE)));
        }


        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView hostTv;
            public TextView titleTv;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                hostTv = itemView.findViewById(R.id.match_host);
                titleTv = itemView.findViewById(R.id.match_title);
            }

        }
    }


}
