package com.nolankuza.theultimatealliance.scout;

import com.nolankuza.theultimatealliance.model.gamedata.GameData;

public interface GameDataListener {
    void onDataUpdated(GameData.Data data);

    void onSave();

    void enableScrolling(boolean enable);

    void displayDefense(boolean display);
}
