package com.ncsu.csc512.jlwheele;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class TensorGenerator {

    private PrototxtData pData;
    private Path file;
    private Charset charset;
    private String[] lines;
    private String fileName;

    public TensorGenerator(String fileName, PrototxtData pData) {
        this.pData = pData;
        this.fileName = fileName + ".py";
        file = Paths.get(this.fileName);
        charset = Charset.forName("UTF-8");
        lines = new String[0];
    }

    public void generate() {
        System.out.println("Tensor Generator started");

        //initial definitions for py file
        addLine("from __future__ import print_function");
        addLine("import tensorflow as tf");
        addLine("slim = tf.contrib.slim");

        addLine("");
        addLine("#####################################################");
        addLine("### WOOTZ COMPILER v1.0   2018/12/04              ###");
        addLine("### NCSU CSC512 JAMES WHEELER (JLWHEELE)          ###");
        addLine("###                                               ###");
        addLine("### Below is code generated from prototxt         ###");
        addLine("#####################################################");
        addLine("");

        //todo add lines to generate tensor code


        addLine("");
        addLine("#####################################################");
        addLine("### Below is template code hard coded in compiler ###");
        addLine("#####################################################");
        addLine("# The code is applicable to any model. It is adapted from");
        addLine("# https://github.com/tensorflow/models/blob/master/research/slim/nets/inception_utils.py");
        addLine("");
        addLine("def default_arg_scope(is_training=True,");
        addLine("\tweight_decay=0.00004,");
        addLine("\tuse_batch_norm=True,");
        addLine("\tbatch_norm_decay=0.9997,");
        addLine("\tbatch_norm_epsilon=0.001,");
        addLine("\tbatch_norm_updates_collections=tf.GraphKeys.UPDATE_OPS):");
        addLine("");
        addLine(" batch_norm_params = {");
        addLine(" \t# Decay for the moving averages.");
        addLine(" \t'decay': batch_norm_decay,");
        addLine(" \t# epsilon to prevent 0s in variance.");
        addLine(" \t'epsilon': batch_norm_epsilon,");
        addLine(" \t# collection containing update_ops.");
        addLine(" \t'updates_collections': batch_norm_updates_collections,");
        addLine(" \t# use fused batch norm if possible.");
        addLine(" \t'fused': None,");
        addLine(" }");
        addLine("");
        addLine(" if use_batch_norm:");
        addLine(" \tnormalizer_fn = slim.batch_norm");
        addLine(" \tnormalizer_params = batch_norm_params");
        addLine(" else:");
        addLine(" \tnormalizer_fn = None");
        addLine(" \tnormalizer_params = {}");
        addLine("");
        addLine("  # Set training state");
        addLine("  with slim.arg_scope([slim.batch_norm, slim.dropout], is_training=is_training):");
        addLine(" \t# Set weight_decay for weights in Conv and FC layers.");
        addLine(" \twith slim.arg_scope([slim.conv2d, slim.fully_connected], weights_regularizer=slim.l2_regularizer(weight_decay)):");
        addLine(" \t\t# Set batch norm");
        addLine(" \t\twith slim.arg_scope([slim.conv2d], normalizer_fn=normalizer_fn, normalizer_params=normalizer_params):");
        addLine(" \t\t\t# Set default padding and stride");
        addLine(" \t\t\twith slim.arg_scope([slim.conv2d, slim.max_pool2d], stride=1, padding='SAME') as sc:");
        addLine(" \t\t\treturn sc");

        try {
            System.out.println("Writing file: " + fileName);
            Files.write(file, Arrays.asList(lines), charset);
//            Files.write(file, lines, charset, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("ERROR! writing file: " + e.toString());
        }
        System.out.println("...Done");
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
