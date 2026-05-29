package com.labs.game.model.factory;

import com.labs.game.model.entities.Asteroid;
import com.labs.game.model.entities.GameEntity;
import com.labs.game.network.Data.AsteroidData;
import com.labs.game.network.Data.EntityData;
import com.labs.game.model.Record;
import java.util.List;

public class AsteroidFactory implements EntityFactory<AsteroidData>{
    @Override
    public GameEntity create(EntityData data, List<GameEntity> entities, Record record){

        AsteroidData asteroidData = (AsteroidData) data;

        Asteroid a = new Asteroid(data.x, data.y, data.radius, entities, record, asteroidData.seed);
        a.setId(data.id);
        return a;
    }

    @Override
    public String getType() { return "Asteroid"; }
}
