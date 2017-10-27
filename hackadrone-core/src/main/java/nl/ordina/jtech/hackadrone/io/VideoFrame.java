package nl.ordina.jtech.hackadrone.io;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import nl.ordina.jtech.hackadrone.io.recognition.Prediction;

public class VideoFrame {

    private static final int FRAME_WIDTH = 720;
    private static final int FRAME_HEIGHT = 676;

    private JFrame frame;
    private JLabel label;
    private BufferedImage bufferedImage;

    public VideoFrame() {
        frame = new JFrame("Video Frame");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        label = new JLabel();
        frame.add(label);
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    public void hideFrame() {
        frame.setVisible(false);
    }

    public void updateVideoFrame(BufferedImage bufferedImage) {
        setBufferedImage(bufferedImage);
        label.setIcon(new ImageIcon(bufferedImage));
    }

    public void setFrameLabel(List<Prediction> predictionList) {
        label.setIcon(new ImageIcon(bufferedImage));
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setText(predictionList.toString());
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

}
