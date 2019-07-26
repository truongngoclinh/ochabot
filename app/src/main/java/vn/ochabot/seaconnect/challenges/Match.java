package vn.ochabot.seaconnect.challenges;

/**
 * @author Thien.
 */
public interface Match {

    int STATUS_TEAM_1_WIN = -2;
    int STATUS_TEAM_2_WIN = -3;
    int STATUS_CANCELED = -1;
    int STATUS_WAITING = 0;
    int STATUS_STARTED = 1;


    String HOST = "host";
    String TITLE = "title";
    String TIME_STAMP = "time_stamp";
    String MATCH_STATUS = "match_status";
    String TEAM_1 = "team_1";
    String TEAM_2 = "team_2";
}
