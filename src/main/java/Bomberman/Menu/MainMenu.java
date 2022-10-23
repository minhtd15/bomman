package Bomberman.Menu;

import Bomberman.UI.StageStartScene;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.input.view.KeyView;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static Bomberman.Sounds.SoundEffect.setSoundSwitch;
import static Bomberman.Sounds.SoundEffect.turnOffMusic;
import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;

/**
 *  Tong Duc Minh.
 */
public class MainMenu extends FXGLMenu {
    public MainMenu() {
        super(MenuType.MAIN_MENU);
        ImageView background = new ImageView();
        background.setImage(new Image("assets/textures/bom_wallpaper.jpg"));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(185, 19, 21));
        dropShadow.setHeight(8);
        dropShadow.setWidth(8);
        dropShadow.setOffsetX(8);
        dropShadow.setOffsetY(10);
        dropShadow.setSpread(10);

        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.rgb(248, 185, 54), 130);
        title.setEffect(dropShadow);
        centerText(title);

//        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 25);
//        version.setEffect(new DropShadow(3, 3, 3, Color.RED));
//        centerTextBind(version, 860, 250);

        var menuBox = new VBox(
                new MenuButton("New Game", 30, () -> newGame()),
                new MenuButton("Control", 30, () -> instruct()),
                new MenuButton("Sound", 30, () -> setSoundSwitch()),
                new MenuButton("Exit", 30, () -> fireExit())
        );

        menuBox.setAlignment(Pos.BOTTOM_LEFT);
        menuBox.setTranslateX(getAppWidth() * 0.35);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 60);
        menuBox.setSpacing(25);

        getContentRoot().getChildren().addAll(background, title, menuBox);
    }

    private void instruct() {
        GridPane pane = new GridPane();

        pane.addRow(0, getUIFactoryService().newText(" Movement      "),
                new HBox(new KeyView(W), new KeyView(S), new KeyView(A), new KeyView(D)));
        pane.addRow(1, getUIFactoryService().newText(" Placed Bomb      "),
                new KeyView(SPACE));

        getDialogService().showBox("How to Play", pane, getUIFactoryService().newButton("OK"));
    }


    public void newGame() {
        fireNewGame();
        getGameTimer().runOnceAfter(() -> {
            turnOffMusic();
            getSceneService().pushSubScene(new StageStartScene());
        }, Duration.millis(10));
    }
}
