package Bomberman.Components.Enemy;

import Bomberman.Components.FlameComponent;
import javafx.util.Duration;

import static Bomberman.Contants.Contant.ENEMY_SPEED;
import static Bomberman.GameEntity.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class BALLOM extends Enemy {
    public BALLOM() {
        super(-ENEMY_SPEED, 0, 1, 3, "enemy1.png");

        onCollisionBegin(BAlLOM, BRICK, (enemy1, brick) -> {
            enemy1.getComponent(BALLOM.class).turn();
        });
        onCollisionBegin(BAlLOM, WALL, (enemy1, wall) -> {
            enemy1.getComponent(BALLOM.class).turn();
        });
        onCollisionBegin(BAlLOM, DOOR, (enemy1, door) -> {
            enemy1.getComponent(BALLOM.class).turn();
        });
        onCollisionBegin(BAlLOM, BOMB, (enemy1, bomb) -> {
            enemy1.getComponent(BALLOM.class).turn();
        });
        onCollision(BAlLOM, FLAME, (enemy1, flame) -> {
            if(flame.getComponent(FlameComponent.class).isActivation()) {
                enemy1.getComponent(BALLOM.class).setStateDie();
                getGameTimer().runOnceAfter(() -> {
                    enemy1.removeFromWorld();
                    set("enemies", getGameWorld().getGroup(BAlLOM,
                            ONIL).getSize());
                }, Duration.seconds(2.4));
            }
        });
    }

}
