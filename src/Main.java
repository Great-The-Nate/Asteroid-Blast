import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final float LINEAR_ACCELERATION = 0.1f;
    private static final float LINEAR_DECELERATION = 0.1f;
    private static final float ANGULAR_ACCELERATION = 0.2f;
    private static final float ANGULAR_DECELERATION = 0.2f;
    private static final float MAX_LINEAR_VELOCITY = 5.0f;
    private static final float MAX_ANGULAR_VELOCITY = 5.0f;

    private static final int FRAMERATE = 60;

    private BufferedImage image;
    private int imageAngle;
    private Point imageCenter;
    private float imageX, imageY;
    private float linearVelocity, angularVelocity, dLinearVelocity, dAngularVelocity;
    private AffineTransform imageTransform;

    //private Player player;

    private HashMap<Integer, Boolean> keyMap;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        Main demo = new Main();
        window.add(demo);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        while (true) {
            try {
                demo.frameUpdate();
                window.repaint();
                Thread.sleep(1000 / FRAMERATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    Main() {
        setSize(800, 600);
        setPreferredSize(new Dimension(800, 600));
        setDoubleBuffered(true);
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyMap.put(e.getKeyCode(), false);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyMap.put(e.getKeyCode(), true);

            }
        });
        setFocusable(true);
        setVisible(true);

        linearVelocity = 0;
        angularVelocity = 0;

        keyMap = new HashMap<>();

        imageAngle = 0;

        //player = new Player();

        float scale = 0.5f;
        try {
            BufferedImage preScaleSprite = ImageIO.read(new File("resources/nobackgroundship.png"));
            final int width = Math.round(preScaleSprite.getWidth() * scale);
            final int height = Math.round(preScaleSprite.getHeight() * scale);
            Image scaledSprite = preScaleSprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.drawImage(scaledSprite, 0, 0, width, height, null);



        } catch (IOException e) {
            e.printStackTrace();
        }
        image.getScaledInstance(Math.round(image.getWidth() * scale), Math.round(image.getHeight() * scale), Image.SCALE_SMOOTH);
        imageX = (getWidth() - image.getWidth()) / 2;
        imageY = (getHeight() - image.getHeight()) / 2;
        imageCenter = new Point(image.getWidth() / 2, image.getHeight() / 2);

        imageTransform = AffineTransform.getRotateInstance(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);
    }

    public void frameUpdate() {
        if (keyMap.getOrDefault(KeyEvent.VK_LEFT, false)) {
            dAngularVelocity = angularVelocity - ANGULAR_ACCELERATION;
        } else if (keyMap.getOrDefault(KeyEvent.VK_RIGHT, false)) {
            dAngularVelocity = angularVelocity + ANGULAR_ACCELERATION;
        } else {
            if (dAngularVelocity > 0)
                dAngularVelocity = angularVelocity - ANGULAR_DECELERATION;
            else if (dAngularVelocity < 0)
                dAngularVelocity = angularVelocity + ANGULAR_DECELERATION;
            if (Math.round(dAngularVelocity) == 0)
                dAngularVelocity = 0;
        }
        if (keyMap.getOrDefault(KeyEvent.VK_UP, false)) {
            dLinearVelocity = linearVelocity + LINEAR_ACCELERATION;
        } else if (keyMap.getOrDefault(KeyEvent.VK_DOWN, false)) {
            dLinearVelocity = linearVelocity - LINEAR_ACCELERATION;
        } else {
            if (dLinearVelocity > 0)
                dLinearVelocity = linearVelocity - LINEAR_DECELERATION;
            else if (dLinearVelocity < 0)
                dLinearVelocity = linearVelocity + LINEAR_DECELERATION;
            if (Math.round(dLinearVelocity) == 0)
                dLinearVelocity = 0;
        }
        linearVelocity = Math.max(-MAX_LINEAR_VELOCITY, Math.min(dLinearVelocity, MAX_LINEAR_VELOCITY));
        angularVelocity = Math.max(-MAX_ANGULAR_VELOCITY, Math.min(dAngularVelocity, MAX_ANGULAR_VELOCITY));
        
        imageX += linearVelocity * Math.cos(Math.toRadians(imageAngle));
        imageY += linearVelocity * Math.sin(Math.toRadians(imageAngle));
        imageAngle += angularVelocity;
        imageAngle %= 360f;
        imageTransform.setToRotation(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransformOp affineOp = new AffineTransformOp(imageTransform, AffineTransformOp.TYPE_BILINEAR);
        //player.paint(g2d);
        g2d.drawImage(image, affineOp, Math.round(imageX), Math.round(imageY));
    }
}