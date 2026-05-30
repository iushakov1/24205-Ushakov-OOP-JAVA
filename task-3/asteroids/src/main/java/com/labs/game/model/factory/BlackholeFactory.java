package com.labs.game.model.factory;

import com.labs.game.model.Record;
import com.labs.game.model.entities.Blackhole;
import com.labs.game.model.entities.GameEntity;
import com.labs.game.network.Data.BlackholeData;
import com.labs.game.network.Data.EntityData;

import java.util.List;

public class BlackholeFactory implements EntityFactory<Blackhole>{

    int width;
    int height;

    public BlackholeFactory(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    public GameEntity create(EntityData data, List<GameEntity> entities, Record record) {
        BlackholeData blackholeData = (BlackholeData)data;

        Blackhole b = new Blackhole(blackholeData.x, blackholeData.y, blackholeData.radius, width, height);
        b.setId(data.id);
        return b;
    }

    @Override
    public String getType() {
        return "Blackhole";
    }
}
