package com.nolankuza.theultimatealliance.scout;

import com.nolankuza.theultimatealliance.model.GameData;

public interface GameDataListener {
    void onDataUpdated(GameData.Data data);

    void onSave();
}
