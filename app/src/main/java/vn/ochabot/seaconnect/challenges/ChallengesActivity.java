package vn.ochabot.seaconnect.challenges;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.Nullable;
import vn.ochabot.seaconnect.R;
import vn.ochabot.seaconnect.core.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Thien.
 */
public class ChallengesActivity extends BaseActivity {

    public static final String TAG = "SeaConnectLog";

    FirebaseFirestore db;
    CollectionReference matches;


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
        data.put("title","Bi lac - tran dau dinh cao");
        data.put("timestamp",System.currentTimeMillis());
        matches.document(String.valueOf(System.nanoTime())).set(data);
    }

    private void showMatches() {
        matches.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot values, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e);
                    return;
                }
                List<String> cities = new ArrayList<>();
                for (QueryDocumentSnapshot doc : values) {
                    if (doc.get("name") != null) {
                        cities.add(doc.getString("host"));
                    }
                }
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



    public static class User {
        public String userName;
        public int team;
    }

    public static class Match {

        public String host;
        public List<User> users;
        public String reward;
        public long timeStamp;
        public String title;
        public int winner;

    }


}
