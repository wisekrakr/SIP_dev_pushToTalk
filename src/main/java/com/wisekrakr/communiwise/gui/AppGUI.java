package com.wisekrakr.communiwise.gui;

import com.wisekrakr.communiwise.gui.utilities.CallTimer;
import com.wisekrakr.communiwise.operations.EventManager;
import com.wisekrakr.communiwise.user.SipAccountManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetSocketAddress;
import java.util.Map;

import static com.wisekrakr.communiwise.gui.utilities.SipAddressMaker.make;

public class AppGUI extends JFrame {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int DESIRED_HEIGHT = 200;
    private static final int DESIRED_WIDTH = 400;

    public static Color LIGHT_CYAN = new Color(176, 228, 234); // #b0e4ea
    public static Color DARK_CYAN = new Color(18, 95, 101); //#125f65 even darker #115358

    private final EventManager eventManager;
    private final Map<String, String> userInfo;
    private final String proxyName;
    private final InetSocketAddress proxyAddress;

    public AppGUI(EventManager eventManager, Map<String, String> userInfo, String proxyName, InetSocketAddress proxyAddress){
        this.eventManager = eventManager;
        this.userInfo = userInfo;
        this.proxyName = proxyName;
        this.proxyAddress = proxyAddress;
    }

    public void showGUI() {
        setAlwaysOnTop(true);
        setResizable(false);

        setBounds(screenSize.width + DESIRED_WIDTH, screenSize.height, DESIRED_WIDTH, DESIRED_HEIGHT);
        setFocusable(true);
        setLocationRelativeTo(null);

        /**
         * When clicking on the default close button of this frame, a OptionPane will pop up.
         * When confirmed, a BYE is send to the proxy and the frame is closed.
         */
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(AppGUI.this,
                        "Are you sure you want to end the call?", "End Call with " + proxyName +  "?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.out.println("Closing App");

                    eventManager.getPhone().hangup();
                }
            }
        });

        add(appTextPanel(), BorderLayout.NORTH);
        add(appButtonPanel(), BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBorder(new EmptyBorder(2,10,2,10));

        JLabel time = new JLabel();
        centerPanel.add(time);
        add(centerPanel, BorderLayout.CENTER);

        CallTimer callTimer = new CallTimer(time);
        callTimer.start();

        setVisible(true);
    }

    private JPanel appTextPanel(){
        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.setBackground(DARK_CYAN);
        textPanel.setBorder(new EmptyBorder(2,10,2,10));

        JLabel u = new JLabel("username: ");
        u.setForeground(LIGHT_CYAN);
        JLabel d = new JLabel("domain: ");
        d.setForeground(LIGHT_CYAN);
        JLabel p = new JLabel("in call with: ");
        p.setForeground(LIGHT_CYAN);

        JLabel username = new JLabel(userInfo.get(SipAccountManager.UserInfoPart.USERNAME.getInfoPart()));
        username.setForeground(LIGHT_CYAN);
        JLabel domain = new JLabel(userInfo.get(SipAccountManager.UserInfoPart.DOMAIN.getInfoPart()));
        domain.setForeground(LIGHT_CYAN);
        JLabel proxy = new JLabel(make(proxyName, proxyAddress.getHostName()));
        proxy.setForeground(LIGHT_CYAN);

        textPanel.add(u, BorderLayout.WEST);
        textPanel.add(username, BorderLayout.EAST);
        textPanel.add(d, BorderLayout.WEST);
        textPanel.add(domain, BorderLayout.EAST);
        textPanel.add(p, BorderLayout.WEST);
        textPanel.add(proxy, BorderLayout.EAST);

        return textPanel;
    }

    private JPanel appButtonPanel(){
        JPanel buttonPanel = new JPanel(new GridLayout());
        buttonPanel.setBackground(DARK_CYAN);

        JButton talkButton = new JButton("PUSH TO TALK");
        talkButton.setPreferredSize(new Dimension(getWidth(), DESIRED_HEIGHT/2));
        talkButton.setFont(new Font("Filmotype Meredith", Font.BOLD, 30));
        talkButton.setSelected(true);
        buttonPanel.add(talkButton);

        talkButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                talkButton.setText("TALKING!");
                talkButton.setForeground(new Color(12, 125, 20));

                eventManager.getSound().unmute();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                talkButton.setText("PUSH TO TALK");
                talkButton.setForeground(new Color(161, 21, 21));

                eventManager.getSound().mute();
            }
        });



        return buttonPanel;
    }

}
