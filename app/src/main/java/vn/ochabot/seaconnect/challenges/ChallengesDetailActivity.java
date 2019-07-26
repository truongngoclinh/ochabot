package vn.ochabot.seaconnect.challenges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.Nullable;
import vn.ochabot.seaconnect.R;
import vn.ochabot.seaconnect.core.base.BaseActivity;
import vn.ochabot.seaconnect.core.helpers.UserHelper;

import java.util.List;

/**
 * @author Thien.
 */
public class ChallengesDetailActivity extends BaseActivity {

    public static final String MATCH_ID = "match_id";
    public static final String MATCH_TITLE = "match_title";
    public static final String MATCH_HOST = "match_host";

    FirebaseFirestore db;
    DocumentReference match;
    String matchId;
    ViewGroup team1ViewGroup;
    ViewGroup team2ViewGroup;

    TextView btnJoinTeam1;
    TextView btnJoinTeam2;
    TextView btnAction1;
    TextView btnAction2;

    TextView matchHost;
    TextView matchTitle;

    DocumentSnapshot matchSnapShot;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        matchId = getIntent().getStringExtra(MATCH_ID);

        db = FirebaseFirestore.getInstance();
        match = db.collection("matches").document(matchId);

        btnAction1 = findViewById(R.id.action_1);
        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchSnapShot != null) {
                    if (matchSnapShot.get(Match.HOST).equals(UserHelper.getUserName())) {
                        match.delete();
                    }
                }
            }
        });

        btnAction2 = findViewById(R.id.action_2);
        btnAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchSnapShot != null) {
                    if (matchSnapShot.get(Match.HOST).equals(UserHelper.getUserName())) {
                        long matchStatus = (long) matchSnapShot.get(Match.MATCH_STATUS);
                        if (matchStatus == Match.STATUS_WAITING) {
                            match.update(Match.MATCH_STATUS, Match.STATUS_STARTED);
                        }
                    }
                }
            }
        });

        team1ViewGroup = findViewById(R.id.team_1);
        team2ViewGroup = findViewById(R.id.team_2);
        btnJoinTeam1 = findViewById(R.id.team_1_join);
        btnJoinTeam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchSnapShot != null) {
                    long matchStatus = (long) matchSnapShot.get(Match.MATCH_STATUS);
                    if (matchStatus == Match.STATUS_WAITING) {
                        WriteBatch batch = db.batch();
                        List<String> team1List = (List<String>) matchSnapShot.get(Match.TEAM_1);
                        if (!team1List.contains(UserHelper.getUserName())) {
                            team1List.add(UserHelper.getUserName());
                            batch.update(match, Match.TEAM_1, team1List);

                            List<String> team2List = (List<String>) matchSnapShot.get(Match.TEAM_2);
                            team2List.remove(UserHelper.getUserName());
                            batch.update(match, Match.TEAM_2, team2List);

                            batch.commit();
                        }
                    } else if (matchStatus == Match.STATUS_STARTED) {
                        match.update(Match.MATCH_STATUS, Match.STATUS_TEAM_1_WIN);
                        finish();
                    }
                }
            }
        });
        btnJoinTeam2 = findViewById(R.id.team_2_join);
        btnJoinTeam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchSnapShot != null) {
                    long matchStatus = (long) matchSnapShot.get(Match.MATCH_STATUS);
                    if (matchStatus == Match.STATUS_WAITING) {
                        WriteBatch batch = db.batch();
                        List<String> team1List = (List<String>) matchSnapShot.get(Match.TEAM_1);
                        if (team1List.contains(UserHelper.getUserName())) {
                            team1List.remove(UserHelper.getUserName());
                            batch.update(match, Match.TEAM_1, team1List);

                            List<String> team2List = (List<String>) matchSnapShot.get(Match.TEAM_2);
                            team2List.add(UserHelper.getUserName());
                            batch.update(match, Match.TEAM_2, team2List);

                            batch.commit();
                        }
                    } else if (matchStatus == Match.STATUS_STARTED) {
                        match.update(Match.MATCH_STATUS, Match.STATUS_TEAM_2_WIN);
                        finish();
                    }
                }
            }
        });

        matchHost = findViewById(R.id.match_host);
        matchTitle = findViewById(R.id.match_title);

        getMatch();
    }


    private void getMatch() {
        match.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e == null && documentSnapshot != null && documentSnapshot.exists()) {
                    updateUI(documentSnapshot);
                } else {
                    finish();
                }

            }
        });
    }

    private void updateUI(DocumentSnapshot documentSnapshot) {

        matchSnapShot = documentSnapshot;

        for (int i = team1ViewGroup.getChildCount() - 1; i > 1; i--) {
            team1ViewGroup.removeViewAt(i);
        }

        for (int i = team2ViewGroup.getChildCount() - 1; i > 1; i--) {
            team2ViewGroup.removeViewAt(i);
        }

        List<String> team1List = (List<String>) documentSnapshot.get(Match.TEAM_1);
        if (!team1List.isEmpty()) {
            for (int i = 0; i < team1List.size(); i++) {
                team1ViewGroup.addView(createMatchMember(team1List.get(i), team1ViewGroup));
            }
        }

        List<String> team2List = (List<String>) documentSnapshot.get(Match.TEAM_2);
        if (!team2List.isEmpty()) {
            for (int i = 0; i < team2List.size(); i++) {
                team2ViewGroup.addView(createMatchMember(team2List.get(i), team2ViewGroup));
            }
        }

        if (team1List.isEmpty() && team2List.isEmpty()) {
            team1List.add(UserHelper.getUserName());
            match.update(Match.TEAM_1, team1List);
        }

        long matchStatus = (long) matchSnapShot.get(Match.MATCH_STATUS);
        if (matchStatus == Match.STATUS_STARTED) {
            btnAction2.setVisibility(View.GONE);
            btnJoinTeam1.setText("Team 1 win");
            btnJoinTeam2.setText("Team 2 win");
        }
    }

    private View createMatchMember(String memberName, ViewGroup parent) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_match_member, parent, false);
        ((TextView) view.findViewById(R.id.match_member_name)).setText(memberName);
        return view;
    }

    @Override
    public int contentView() {
        return R.layout.activity_challenge_detail;
    }

    @Override
    public int title() {
        return 0;
    }


}
