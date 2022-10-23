package Bomberman.Components;

import Bomberman.EntityState.State;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static Bomberman.Contants.Contant.TILED_SIZE;
import static Bomberman.EntityState.State.*;
import static Bomberman.GameEntity.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {
    private final int BONUS_SPEED = 100;

    private final int FRAME_SIZE = 45;
    private boolean bombInvalidation;
    private  AnimatedTexture texture;
    private State state;
    private PhysicsComponent physics;
    private int bombCounter;
    private PlayerSkin playerSkin;
    private AnimationChannel aniIdleDown, aniIdleRight, aniIdleUp, aniIdleLeft;
    private AnimationChannel aniWalkDown, aniWalkRight, aniWalkUp, aniWalkLeft;
    private AnimationChannel aniDie;

    public PlayerComponent() {
        state = State.STOP;
        bombInvalidation = false;
        bombCounter = 0;

        onCollisionBegin(PLAYER, POWERUP_FLAMES, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            inc("flame", 1);
        });
        onCollisionBegin(PLAYER, POWERUP_BOMBS, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            inc("bomb", 1);
        });
        onCollisionBegin(PLAYER, POWERUP_SPEED, (player, powerup) -> {
            powerup.removeFromWorld();
            handlePowerUpSpeed();
        });
        onCollisionBegin(PLAYER, POWERUP_FLAMEPASS, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            getGameWorld().getSingleton(PLAYER)
                    .getComponent(PlayerComponent.class)
                    .setSkin(PlayerSkin.GOLD);
        });
        onCollisionBegin(PLAYER, POWERUP_LIFE, (player, powerup) -> {
            powerup.removeFromWorld();
            play("powerup.wav");
            inc("life", 1);
        });
        setSkin(PlayerSkin.NORMAL);
        texture = new AnimatedTexture(aniIdleDown);
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    private void setSkin(PlayerSkin skin) {
        playerSkin = skin;
        if (playerSkin == PlayerSkin.NORMAL) {
            aniDie = new AnimationChannel(image("player_die.png"), 5, FRAME_SIZE, FRAME_SIZE, Duration.seconds(3.5), 0, 4);

            aniIdleDown = new AnimationChannel(image("player_down.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            aniIdleRight = new AnimationChannel(image("player_right.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            aniIdleUp = new AnimationChannel(image("player_up.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            aniIdleLeft = new AnimationChannel(image("player_left.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);

            aniWalkDown = new AnimationChannel(image("player_down.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            aniWalkRight = new AnimationChannel(image("player_right.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            aniWalkUp = new AnimationChannel(image("player_up.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            aniWalkLeft = new AnimationChannel(image("player_left.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
        } else if (playerSkin == PlayerSkin.GOLD) {
            aniDie = new AnimationChannel(image("player_die.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(3.5), 0, 4);

            aniIdleDown = new AnimationChannel(image("gold_player_down.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            aniIdleRight = new AnimationChannel(image("gold_player_right.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            aniIdleUp = new AnimationChannel(image("gold_player_up.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);
            aniIdleLeft = new AnimationChannel(image("gold_player_left.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 0);

            aniWalkDown = new AnimationChannel(image("gold_player_down.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            aniWalkRight = new AnimationChannel(image("gold_player_right.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            aniWalkUp = new AnimationChannel(image("gold_player_up.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
            aniWalkLeft = new AnimationChannel(image("gold_player_left.png"), 3, FRAME_SIZE, FRAME_SIZE, Duration.seconds(0.5), 0, 2);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (physics.getVelocityX() != 0) {

            physics.setVelocityX((int) physics.getVelocityX() * 0.8);

            if (FXGLMath.abs(physics.getVelocityX()) < 1) {
                physics.setVelocityX(0);
            }
        }

        if (physics.getVelocityY() != 0) {

            physics.setVelocityY((int) physics.getVelocityY() * 0.8);

            if (FXGLMath.abs(physics.getVelocityY()) < 1) {
                physics.setVelocityY(0);
            }
        }

        switch (state) {
            case UP:
                texture.loopNoOverride(aniWalkUp);
                break;
            case RIGHT:
                texture.loopNoOverride(aniWalkRight);
                break;
            case DOWN:
                texture.loopNoOverride(aniWalkDown);
                break;
            case LEFT:
                texture.loopNoOverride(aniWalkLeft);
                break;
            case STOP:
                if (texture.getAnimationChannel() == aniWalkDown) {
                    texture.loopNoOverride(aniIdleDown);
                } else if (texture.getAnimationChannel() == aniWalkUp) {
                    texture.loopNoOverride(aniIdleUp);
                } else if (texture.getAnimationChannel() == aniWalkLeft) {
                    texture.loopNoOverride(aniIdleLeft);
                } else if (texture.getAnimationChannel() == aniWalkRight) {
                    texture.loopNoOverride(aniIdleRight);
                }
                break;
            case DIE:
                texture.loopNoOverride(aniDie);
                break;
        }
    }

    public void up() {
        if (state != DIE) {
            state = UP;
            physics.setVelocityY(-geti("speed"));
        }
    }

    public void down() {
        if (state != DIE) {
            state = DOWN;
            physics.setVelocityY(geti("speed"));
        }
    }

    public void left() {
        if (state != DIE) {
            state = LEFT;
            physics.setVelocityX(-geti("speed"));
        }
    }

    public void right() {
        if (state != DIE) {
            state = RIGHT;
            physics.setVelocityX(geti("speed"));
        }
    }

    public void stop() {
        if (state != DIE) {
            state = STOP;
        }
    }

    public void placeBomb(int flames) {
        if (state != DIE) {
            if (bombCounter == geti("bomb")) {
                return;
            }
            bombCounter++;
            double bombLocationX = (double) (entity.getX() % TILED_SIZE > TILED_SIZE / 2
                    ? entity.getX() + TILED_SIZE - entity.getX() % TILED_SIZE + 1
                    : entity.getX() - entity.getX() % TILED_SIZE + 1);
            double bombLocationY = (double) (entity.getY() % TILED_SIZE > TILED_SIZE / 2
                    ? entity.getY() + TILED_SIZE - entity.getY() % TILED_SIZE + 1
                    : entity.getY() - entity.getY() % TILED_SIZE + 1);

            Entity bomb = spawn("bomb",new SpawnData(bombLocationX, bombLocationY));
            play("play_bomb.wav");
            getGameTimer().runOnceAfter(() -> {
                if (!bombInvalidation) {
                    bomb.getComponent(BombComponent.class).explode(flames);
                    play("explosion.wav");
                } else {
                    bomb.removeFromWorld();
                }
                bombCounter--;
            }, Duration.seconds(2.1));
        }
    }

    public void handlePowerUpSpeed() {
        play("powerup.wav");
        inc("speed", BONUS_SPEED);
        getGameTimer().runOnceAfter(() -> {
            inc("speed", -BONUS_SPEED);
        }, Duration.seconds(6));
    }

    public PlayerSkin getPlayerSkin() {
        return playerSkin;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setBombInvalidation(boolean bombInvalidation) {
        this.bombInvalidation = bombInvalidation;
    }

    public enum PlayerSkin {
        NORMAL, GOLD
    }
}

