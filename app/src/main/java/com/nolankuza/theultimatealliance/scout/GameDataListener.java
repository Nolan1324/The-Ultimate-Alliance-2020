package com.nolankuza.theultimatealliance.scout;

import com.nolankuza.theultimatealliance.structure.GameData;

public interface GameDataListener {
    void onDataUpdated(GameData.Data data);

    void onSave();
}
