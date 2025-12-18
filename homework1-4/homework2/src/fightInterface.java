import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class fightInterface {
    private JDialog fightDialog;
    private boolean fightResult;
    private boolean decisionMade;

    public boolean showFightInterface(int monsterPower) {
        fightResult = false;
        decisionMade = false;

        // 创建战斗对话框
        fightDialog = new JDialog((Frame) null, "战斗界面", true);
        fightDialog.setLayout(new BorderLayout());
        fightDialog.setSize(400, 300);
        fightDialog.setLocationRelativeTo(null); // 居中显示

        // 创建内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 战斗信息
        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h2>遭遇怪物!</h2>"
                + "<p>怪物战斗力: " + (-monsterPower) + "</p>"
                + "<p>选择你的行动:</p>"
                + "</div></html>", SwingConstants.CENTER);
        contentPanel.add(infoLabel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton fightButton = new JButton("战斗");
        fightButton.setFont(new Font("宋体", Font.BOLD, 16));
        fightButton.setBackground(new Color(255, 100, 100));
        fightButton.setForeground(Color.WHITE);
        fightButton.setPreferredSize(new Dimension(100, 40));

        JButton runButton = new JButton("逃跑");
        runButton.setFont(new Font("宋体", Font.BOLD, 16));
        runButton.setBackground(new Color(100, 100, 255));
        runButton.setForeground(Color.WHITE);
        runButton.setPreferredSize(new Dimension(100, 40));

        buttonPanel.add(fightButton);
        buttonPanel.add(runButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        fightDialog.add(contentPanel);

        // 添加按钮事件
        fightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fightResult = true;
                decisionMade = true;
                fightDialog.dispose();
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fightResult = false;
                decisionMade = true;
                fightDialog.dispose();
            }
        });

        // 显示对话框并等待用户选择
        fightDialog.setVisible(true);

        // 等待用户做出决定
        while (!decisionMade) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return fightResult;
    }
}