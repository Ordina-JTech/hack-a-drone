package nl.ordina.jtech.hackadrone.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.ordina.jtech.hackadrone.io.recognition.Prediction;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.shade.jackson.databind.ObjectMapper;

public class DeepLearning {

    private static final int HEIGHT = 224;
    private static final int WIDTH = 224;
    private static final int CHANNELS = 3;
    /**
     * Reference to Video Frame
     */
    private VideoFrame videoFrame;

    /**
     * Computational graph for deep learning
     */
    private ComputationGraph computationGraphVGG16;

    private NativeImageLoader nativeImageLoader;

    public DeepLearning() {
        this.nativeImageLoader = new NativeImageLoader(HEIGHT, WIDTH, CHANNELS);
        loadDataModel();
    }

    /**
     * Map each name of the output node to a human readable name from the imagenet class json
     */
    private static ArrayList<String> getLabels() {
        ArrayList<String> predictionLabels = null;

        try {
            File imagenetClassFile = new File("deeplearning/imagenet_class_index.json");
            HashMap<String, ArrayList<String>> jsonMap = (HashMap) (new ObjectMapper()).readValue(imagenetClassFile, HashMap.class);
            predictionLabels = new ArrayList(jsonMap.size());

            for (int i = 0; i < jsonMap.size(); ++i) {
                predictionLabels.add((String) ((ArrayList) jsonMap.get(String.valueOf(i))).get(1));
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }
        return predictionLabels;
    }

    /**
     * Shows the predicted items and the percentage of how certain the model is that it is this item.
     */
    void updateLabels(BufferedImage bufferedImage) {
        List<Prediction> predictionList = classify(bufferedImage);
        videoFrame.setFrameLabel(predictionList);
    }

    void setVideoFrame(VideoFrame videoFrame) {
        this.videoFrame = videoFrame;
    }

    /**
     * Load the dataset and the computational graph for deep learning
     */
    private void loadDataModel() {
        //TODO Deep Learning Challenge Part 1: load existing data model
    }

    /**
     * Classify images
     */
    private List<Prediction> classify(BufferedImage bufferedImage) {
        //TODO Deep Learning Challenge Part 3: Classify the image and return a top 5
        INDArray processedImage = processImage(cropImageToSquare(bufferedImage));

//        List<Prediction> predictions = decodePredictions(output[0]);

        return null;
    }

    /**
     * Pre process the image to create a matrix
     */
    private INDArray processImage(BufferedImage bufferedImage) {
        //TODO Deep Learning Challenge Part 2: Normalize the Image
        INDArray imageMatrix = null;

        normalizeImage(imageMatrix);

        return imageMatrix;
    }

    private void normalizeImage(final INDArray image) {
        DataNormalization scaler = new VGG16ImagePreProcessor();
        scaler.transform(image);
    }

    /**
     * Create and return a prediction object for the decodePredictions method.
     */
    private Prediction createPrediction(String label, double percentage) {
        return new Prediction(label, percentage);
    }

    /**
     * Rank the activation of the output nodes to create a top 5 of predictions
     */
    private List<Prediction> decodePredictions(INDArray predictions) {
        List<Prediction> predictionList = new ArrayList<>();
        int[] top5 = new int[5];
        float[] top5Prob = new float[5];

        ArrayList<String> labels = getLabels();
        int i = 0;

        for (INDArray currentBatch = predictions.getRow(0).dup(); i < 5; ++i) {

            top5[i] = Nd4j.argMax(currentBatch, 1).getInt(0, 0);
            top5Prob[i] = currentBatch.getFloat(0, top5[i]);
            currentBatch.putScalar(0, top5[i], 0.0D);

            predictionList.add(createPrediction(labels.get(top5[i]), (top5Prob[i] * 100.0F)));
        }

        return predictionList;

    }

    /**
     * Croppes an image to a square for a better recognition
     *
     * @param bufferedImage
     * @return
     */
    private BufferedImage cropImageToSquare(final BufferedImage bufferedImage) {
        int minWidhtHeight = Math.min(bufferedImage.getHeight(), bufferedImage.getWidth());
        int x = minWidhtHeight == bufferedImage.getWidth() ? 0 : (bufferedImage.getWidth() - minWidhtHeight) / 2;
        int y = minWidhtHeight == bufferedImage.getHeight() ? 0 : (bufferedImage.getHeight() - minWidhtHeight) / 2;
        return bufferedImage.getSubimage(x, y, minWidhtHeight, minWidhtHeight);
    }

}
