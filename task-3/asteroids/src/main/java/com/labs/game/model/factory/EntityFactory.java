package com.labs.game.model.factory;

import com.labs.game.model.entities.GameEntity;
import com.labs.game.network.Data.EntityData;
import com.labs.game.model.Record;

import java.util.List;

public interface EntityFactory<T> {
    public GameEntity create(EntityData data, List<GameEntity> entities, Record record);
    public String getType();
}
