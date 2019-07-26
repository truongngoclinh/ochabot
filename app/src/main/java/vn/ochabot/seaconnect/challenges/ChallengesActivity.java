package vn.ochabot.seaconnect.challenges;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.Nullable;
import vn.ochabot.seaconnect.R;
import vn.ochabot.seaconnect.core.base.BaseActivity;

import java.util.*;

/**
 * @author Thien.
 */
public class ChallengesActivity extends BaseActivity {

    public static final String TAG = "SeaConnectLog";

    FirebaseFirestore db;
    CollectionReference matches;
    Map<String, QueryDocumentSnapshot> mData = new HashMap<>();
    MatchAdapter adapter = new MatchAdapter();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        matches = db.collection("matches");
        showMatches();
        findViewById(R.id.create_match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMatch();
            }
        });
    }

    private void addMatch() {
        Map<String, Object> data = new HashMap<>();
        data.put("host", "thien");
        data.put("title", "Bi lac - tran dau dinh cao");
        data.put("timestamp", System.currentTimeMillis());
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
                for (QueryDocumentSnapshot doc : values) {
                    mData.put(doc.getId(), doc);
                }
                adapter.mData = new ArrayList<>(mData.values());
                 Collections.sort(adapter.mData, new Comparator<QueryDocumentSnapshot>() {
                    @Override
                    public int compare(QueryDocumentSnapshot d1, QueryDocumentSnapshot d2) {
                        return (int) (Long.valueOf(d1.get("timestamp").toString()) - Long.valueOf(d2.get("timestamp").toString()));
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

        List<QueryDocumentSnapshot> mData;

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder queryDocumentSnapshotMyViewHolder, int i) {

        }


        public static class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


}
