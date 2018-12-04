package com.ncsu.csc512.jlwheele;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class TensorGenerator {

    private PrototxtData pData;
    private Path file;
    private Charset charset;
    private String[] lines;

    public TensorGenerator(String fileName, PrototxtData pData) {
        this.pData = pData;
        file = Paths.get(fileName + ".py");
        charset = Charset.forName("UTF-8");
        lines = new String[0];
    }

    public void generate() {
        System.out.println("Tensor Generator started");

        //initial definitions for py file
        addLine("from __future__ import print_function");
        addLine("import tensorflow as tf");
        addLine("slim = tf.contrib.slim");

        //todo add lines to generate tensor code

        addLine("### Below is code generated from prototxt ###");


        //todo add lines for hard-coded template

        addLine("### Below is template code hard coded in compiler ###");
//        def default_arg_scope(is_training=True,
//                weight_decay=0.00004,
//                use_batch_norm=True,
//                batch_norm_decay=0.9997,
//                batch_norm_epsilon=0.001,
//                batch_norm_updates_collections=tf.GraphKeys.UPDATE_OPS):
//
//        batch_norm_params = {
//      # Decay for the moving averages.
//        'decay': batch_norm_decay,
//      # epsilon to prevent 0s in variance.
//        'epsilon': batch_norm_epsilon,
//      # collection containing update_ops.
//        'updates_collections': batch_norm_updates_collections,
//      # use fused batch norm if possible.
//        'fused': None,
//  }
//        if use_batch_norm:
//        normalizer_fn = slim.batch_norm
//        normalizer_params = batch_norm_params
//  else:
//        normalizer_fn = None
//        normalizer_params = {}
//
//  # Set training state
//        with slim.arg_scope([slim.batch_norm, slim.dropout],
//        is_training=is_training):
//    # Set weight_decay for weights in Conv and FC layers.
//        with slim.arg_scope([slim.conv2d, slim.fully_connected],
//        weights_regularizer=slim.l2_regularizer(weight_decay)):
//      # Set batch norm
//        with slim.arg_scope(
//                [slim.conv2d],
//                normalizer_fn=normalizer_fn,
//                normalizer_params=normalizer_params):
//          # Set default padding and stride
//        with slim.arg_scope([slim.conv2d, slim.max_pool2d],
//        stride=1, padding='SAME') as sc:
//        return sc

        try {
            Files.write(file, Arrays.asList(lines), charset);
//            Files.write(file, lines, charset, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("ERROR! writing file: " + e.toString());
        }
    }

    private void addLine(String line) {
        int length = lines.length;
        String[] tmp = new String[length + 1];

        for (int i = 0; i < length; i++) {
            tmp[i] = lines[i];
        }

        tmp[length] = line;
        lines = tmp;
    }
}
