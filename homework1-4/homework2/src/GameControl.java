import javax.swing.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameControl {
    GameData gameData;
    Menu menu;
    GUI gui;
    fightInterface fightInterface; // 添加战斗界面引用

    GameControl(GameData gameData, Menu menu, GUI gui) {
        this.gameData = gameData;
        this.menu = menu;
        this.gui = gui;
        this.fightInterface = new fightInterface(); // 初始化战斗界面
    }
	void gameStart() {
		gui.f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				 char c = e.getKeyChar();
				 if(c=='a'||c=='s'||c=='d'||c=='w') {
					 handleInput(c);
					 gameData.printMap();
					 gui.refreshGUI();
				 }
			}
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
		});
		Scanner keyboardInput = new Scanner(System.in);
		while (true) {
			String input = keyboardInput.next();
			if (input.length() != 1 || (input.charAt(0) != 'a' && input.charAt(0) != 's' && input.charAt(0) != 'd'
					&& input.charAt(0) != 'w' && input.charAt(0) != '0')) {
				System.out.println("Wrong Input.");
				continue;
			}
			if (input.charAt(0) == '0')
				menu.enterMenu();
			else
				handleInput(input.charAt(0));
			gameData.printMap();
			gui.refreshGUI();
		}
	}
	void handleInput(char inC) {
		int tX = 0, tY = 0;
		if (inC == 'a') {
			tX = gameData.pX;
			tY = gameData.pY - 1;
		}
		if (inC == 's') {
			tX = gameData.pX + 1;
			tY = gameData.pY;
		}
		if (inC == 'd') {
			tX = gameData.pX;
			tY = gameData.pY + 1;
		}
		if (inC == 'w') {
			tX = gameData.pX - 1;
			tY = gameData.pY;
		}
        if (tX < 0 || tX >= gameData.H || tY < 0 || tY >= gameData.W) {
            return; // 超出边界，不移动
        }

        int targetCell = gameData.map[gameData.currentLevel][tX][tY];

        if (targetCell == 2) {
            gameData.keyNum++;
            moveHero(tX, tY);
        } else if (targetCell == 3 && gameData.keyNum > 0) {
            gameData.keyNum--;
            moveHero(tX, tY);
        } else if (targetCell == 4) {
            gameData.map[gameData.currentLevel][gameData.pX][gameData.pY] = 1;
            gameData.currentLevel++;
            for (int i = 0; i < gameData.H; i++)
                for (int j = 0; j < gameData.W; j++)
                    if (gameData.map[gameData.currentLevel][i][j] == 6) {
                        gameData.pX = i;
                        gameData.pY = j;
                    }
        } else if (targetCell == 5) {
            JOptionPane.showMessageDialog(gui.f, "恭喜！你赢得了游戏！", "游戏胜利", JOptionPane.INFORMATION_MESSAGE);
            System.out.print("You Win!!");
            System.exit(0);
        } else if (targetCell > 10) {
            gameData.heroHealth += targetCell;
            moveHero(tX, tY);
            JOptionPane.showMessageDialog(gui.f, "获得生命值: +" + targetCell, "生命恢复", JOptionPane.INFORMATION_MESSAGE);
        } else if (targetCell == 1) {
            moveHero(tX, tY);
        } else if (targetCell < 0) {
            // 遇到怪物，显示战斗界面
            boolean fight = fightInterface.showFightInterface(targetCell);

            if (fight) {
                // 选择战斗
                if (gameData.map[gameData.currentLevel][tX][tY] + gameData.heroHealth <= 0) {
                    JOptionPane.showMessageDialog(gui.f,
                            "那个怪物有 " + (-targetCell) + " 战斗力，你输了！！",
                            "游戏结束", JOptionPane.ERROR_MESSAGE);
                    System.out.print("That monster has " + (-targetCell) + " power, You Lose!!");
                    System.exit(0);
                } else {
                    gameData.heroHealth += targetCell;
                    moveHero(tX, tY);
                    JOptionPane.showMessageDialog(gui.f,
                            "战斗胜利！损失生命值: " + (-targetCell) + "\n剩余生命: " + gameData.heroHealth,
                            "战斗结果", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // 选择逃跑，不移动
                JOptionPane.showMessageDialog(gui.f, "成功逃跑！", "逃跑", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    public void handleMouseMovement(int targetX, int targetY) {
        // 计算方向
        int dX = targetX - gameData.pX;
        int dY = targetY - gameData.pY;

        char direction = ' ';
        if (dX == -1 && dY == 0) direction = 'w';
        else if (dX == 1 && dY == 0) direction = 's';
        else if (dX == 0 && dY == -1) direction = 'a';
        else if (dX == 0 && dY == 1) direction = 'd';

        if (direction != ' ') {
            handleInput(direction);
            gameData.printMap();
            gui.refreshGUI();
        }
    }

    void moveHero(int tX, int tY) {
        gameData.map[gameData.currentLevel][gameData.pX][gameData.pY] = 1;
        gameData.map[gameData.currentLevel][tX][tY] = 6;
        gameData.pX = tX;
        gameData.pY = tY;
    }
}

