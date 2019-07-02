package com.nolankuza.theultimatealliance.http;

import com.google.gson.GsonBuilder;
import com.nolankuza.theultimatealliance.structure.Event;
import com.nolankuza.theultimatealliance.structure.Match;
import com.nolankuza.theultimatealliance.structure.Team;
import com.nolankuza.theultimatealliance.util.Constants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class TheBlueAllianceClient {
    private static final String key = "snr4Q9HrfsAxc9g0E0xVLxOWmb2lKpchWVZ1gTcZhj9Gp5kRnB8ZCeNZOtbH9FVb";
    private static final String baseUri = "https://www.thebluealliance.com/api/v3";

    public TheBlueAllianceClient() {

    }

    public List<Event> getEvents(int teamNumber, int year) {
        List<Event> events = new ArrayList<>();
        Event[] eventArray = get("/team/frc" + teamNumber + "/events/" + year + "/simple", Event[].class);
        if(eventArray != null) {
            events = Arrays.asList(eventArray);
            Collections.sort(events, new Event.Compare());
        }
        return events;
    }

    public List<Match> getMatches(String eventKey) {
        List<Match> matches = new ArrayList<>();
        Match[] matchArray = get("/event/" + eventKey + "/matches/simple", Match[].class);
        if(matchArray != null) {
            matches = Arrays.asList(matchArray);
            Collections.sort(matches, new Match.Compare());
            for(Match match : matches) {
                match.build();
            }
        }
        return matches;
    }

    public List<Team> getTeams(String eventKey) {
        List<Team> teams = new ArrayList<>();
        Team[] teamArray = get("/event/" + eventKey + "/teams/simple", Team[].class);
        if(teamArray != null) {
            teams = Arrays.asList(teamArray);
            Collections.sort(teams, new Team.Compare());
        }
        return teams;
    }

    private <T> T get(String path, Class<T> type) {
        try {
            //Get reader from the URL
            URL url = new URL(baseUri + path);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestProperty("X-TBA-Auth-Key", key);
            con.setRequestProperty("User-Agent", "The Ultimate Alliance");
            Reader reader = new InputStreamReader(con.getInputStream(), "UTF-8");
            //Serialize it with Gson
            GsonBuilder gsonBuilder = new GsonBuilder();
            //gsonBuilder.registerTypeAdapter(Date.class, serializer);
            gsonBuilder.setDateFormat(Constants.EVENT_DATE_FMT_STR);
            return gsonBuilder.create().fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
