package com.example.projectmodel;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TensorFlowClassifier {
    private Interpreter interpreter;
    private static final String MODEL_FILE = "model_with_tf_ops.tflite";
    private static final int INPUT_SIZE = 1152; // 128 * 9 as per your model's input layer
    private static final int OUTPUT_SIZE = 6;

    public TensorFlowClassifier(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, MODEL_FILE));
    }

    private MappedByteBuffer loadModelFile(Context context, String modelName) throws IOException {
        AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(modelName);
        FileInputStream inputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float[] predictProbabilities(float[] data) {
        if (data.length != INPUT_SIZE) throw new IllegalArgumentException("Data length must be 1152.");

        // Reshape the flat data array into [1][128][9]
        float[][][] inputData = new float[1][128][9];
        for (int i = 0; i < 128; i++) {
            System.arraycopy(data, i * 9, inputData[0][i], 0, 9);
        }
        float[][] outputData = new float[1][OUTPUT_SIZE];

        interpreter.run(inputData, outputData);
        return outputData[0];
    }

}
