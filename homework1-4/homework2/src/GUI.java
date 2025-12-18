import java.awt.*;
import javax.swing.*;
public class GUI {
	GameData gameData;
	JFrame f;
	JLabel[][] b;
    JPanel infoPanel;
    JLabel healthLabel, keyLabel, levelLabel;

	GUI(GameData gameData) {
		this.gameData = gameData;
		f = new JFrame("Magic Tower");

        JPanel mainPanel = new JPanel(new BorderLayout());


        JPanel mapPanel = new JPanel(new GridLayout(gameData.H, gameData.W));
        mapPanel.setPreferredSize(new Dimension(gameData.W * 100, gameData.H * 100));

		b = new JLabel[gameData.H][gameData.W];
		for (int i = 0; i < gameData.H; i++) {
			for (int j = 0; j < gameData.W; j++) {
                b[i][j] = new JLabel();
                b[i][j].setPreferredSize(new Dimension(100, 100));
                b[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                b[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                b[i][j].setVerticalAlignment(SwingConstants.CENTER);
                mapPanel.add(b[i][j]);
			}
		}
        infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        healthLabel = new JLabel("生命值: " + gameData.heroHealth);
        healthLabel.setFont(new Font("宋体", Font.BOLD, 16));
        healthLabel.setForeground(Color.RED);

        keyLabel = new JLabel("钥匙数量: " + gameData.keyNum);
        keyLabel.setFont(new Font("宋体", Font.BOLD, 16));
        keyLabel.setForeground(Color.BLUE);

        levelLabel = new JLabel("当前楼层: " + (gameData.currentLevel + 1));
        levelLabel.setFont(new Font("宋体", Font.BOLD, 16));
        levelLabel.setForeground(new Color(0, 150, 0));

        infoPanel.add(healthLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(keyLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(levelLabel);

        mainPanel.add(mapPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        f.add(mainPanel);
        f.pack();
        f.setLocationRelativeTo(null); 
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        refreshGUI();

        addRightClickMenu();
    }

    public void refreshGUI() {
        for (int i = 0; i < gameData.H; i++) {
            for (int j = 0; j < gameData.W; j++) {
                Image scaledImage = chooseImage(gameData.map[gameData.currentLevel][i][j]);
                b[i][j].setIcon(new ImageIcon(scaledImage));
            }
        }
        // 更新信息面板
        healthLabel.setText("生命值: " + gameData.heroHealth);
        keyLabel.setText("钥匙数量: " + gameData.keyNum);
        levelLabel.setText("当前楼层: " + (gameData.currentLevel + 1));
    }
	private static Image chooseImage(int index){
		ImageIcon[] icons = new ImageIcon[10];
		Image scaledImage;
		icons[0]= new ImageIcon("Wall.jpg");
		icons[1]= new ImageIcon("Floor.jpg");
		icons[2]= new ImageIcon("Key.jpg");
		icons[3]= new ImageIcon("Door.jpg");
		icons[4]= new ImageIcon("Stair.jpg");
		icons[5]= new ImageIcon("Exit.jpg");
		icons[6]= new ImageIcon("Hero.jpg");
		icons[7]= new ImageIcon("Potion.jpg");
		icons[8]= new ImageIcon("Monster.jpg");
		if(index>10)
			scaledImage = icons[7].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		else if(index<0)
			scaledImage = icons[8].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		else
			scaledImage = icons[index].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		return scaledImage;
	}
    private void addRightClickMenu() {
        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem menuItem = new JMenuItem("打开菜单 (0)");
        menuItem.addActionListener(e -> {
            if (MagicTowerMain.gameControl != null) {

                JOptionPane.showMessageDialog(f,
                        "请在控制台输入0来打开菜单",
                        "菜单提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        popupMenu.add(menuItem);

        JMenuItem restartItem = new JMenuItem("重新开始游戏");
        restartItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(f,
                    "确定要重新开始游戏吗？",
                    "重新开始", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                gameData.readMapFromFile("Map.in");
                refreshGUI();
                JOptionPane.showMessageDialog(f, "游戏已重新开始！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        popupMenu.add(restartItem);

        // 为所有格子添加右键菜单
        for (int i = 0; i < gameData.H; i++) {
            for (int j = 0; j < gameData.W; j++) {
                final int row = i;
                final int col = j;

                b[i][j].setComponentPopupMenu(popupMenu);

                // 添加鼠标悬停提示
                b[i][j].setToolTipText(getCellTooltip(gameData.map[gameData.currentLevel][i][j]));
            }
        }
    }

    // 添加获取格子提示文本的方法
    private String getCellTooltip(int cellValue) {
        switch (cellValue) {
            case 0: return "墙壁 - 无法通过";
            case 1: return "地板 - 可以行走";
            case 2: return "钥匙 - 收集后可以开门";
            case 3: return "门 - 需要钥匙才能打开";
            case 4: return "楼梯 - 通往下一层";
            case 5: return "出口 - 游戏目标";
            case 6: return "英雄 - 你自己";
            default:
                if (cellValue > 10) return "药水 - 恢复" + cellValue + "点生命值";
                if (cellValue < 0) return "怪物 - 战斗力" + (-cellValue);
                return "未知";
        }

    }

}
