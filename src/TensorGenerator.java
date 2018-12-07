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
        addLine("from __future__ import absolute_import");
        addLine("from __future__ import division");
        addLine("from __future__ import print_function");
        addLine("import tensorflow as tf");
        addLine("slim = tf.contrib.slim\n");

        addLine("#####################################################");
        addLine("### WOOTZ COMPILER v1.0   2018/12/04              ###");
        addLine("### NCSU CSC512 JAMES WHEELER (JLWHEELE)          ###");
        addLine("###                                               ###");
        addLine("### Below is code generated from prototxt         ###");
        addLine("#####################################################\n");

        PrototxtLayer[] prototxtLayers = pData.getLayers();

        int num_classes = 0;
        for (PrototxtLayer layer: prototxtLayers) {
            if (layer.getType().equals(PrototxtLayer.TYPE_CONVOLUTION)
                && layer.getTop().equals("logits")
                && layer.getBottom().equals("logits")) {
                num_classes = ((ConvolutionParam)layer.getParam2()).getNumOutputs();
                break;
            }
        }

        String tensorFunc = "def " + file.getFileName().toString().replace(".py", "") + "(";
        tensorFunc += pData.getInput() + ", ";
        tensorFunc += "num_classes=" + num_classes +", ";
        tensorFunc += "is_training=True, ";
        tensorFunc += "reuse=None, ";
        tensorFunc += "scope='" + pData.getName() + "'";
        tensorFunc += "):\n";
        addLine(tensorFunc);

        addLine("\twith tf.variable_scope(scope, \"Model\", reuse=reuse):");
        addLine("\t\twith slim.arg_scope(default_arg_scope(is_training)):\n");
        addLine("\t\t\tend_points = {}\n");

        String nextBottom = "inputs";
        while (nextBottom != null) {
            PrototxtLayer currLayer = null;
            int c = 0;
            for (PrototxtLayer layer : prototxtLayers) {
                if (layer.getBottom().equals(nextBottom) && !layer.getTop().equals(nextBottom)) {
                    c++;
                    currLayer = layer;
                }
            }

            nextBottom = null;
            if (c > 1) {
                break;
            }

            if (currLayer.getType().equals(PrototxtLayer.TYPE_CONVOLUTION)) {
                addConv2dLines(currLayer);
            } else {
                addPool2dLines(currLayer);
            }

            nextBottom = currLayer.getTop();
        }

        addLine("\t\t\t##################################################");
        addLine("\t\t\t### Concat code");
        addLine("\t\t\t##################################################\n");

        for (PrototxtLayer layer : prototxtLayers) {
            if (layer.getType().equals(PrototxtLayer.TYPE_CONCAT)) {
                addConcatLines(layer);
                PrototxtLayer currLayer = null;
                int c = 0;
                for (PrototxtLayer layer1 : prototxtLayers) {
                    if (layer1.getBottom().equals(layer.getTop())) {
                        c++;
                        currLayer = layer1;
                    }
                }
                if ((c == 1) && currLayer.getType().equals(PrototxtLayer.TYPE_POOLING))
                    addPool2dLines(currLayer);
            }
        }

        //todo add lines to generate tensor code

        addLine("\treturn logits, end_points\n");
        addLine("inception_v1.default_image_size = " + pData.getInputShape()[2] + "\n");
        addLine("#####################################################");
        addLine("### Below is template code hard coded in compiler ###");
        addLine("#####################################################");
        addLine("# The code is applicable to any model. It is adapted from");
        addLine("# https://github.com/tensorflow/models/blob/master/research/slim/nets/inception_utils.py\n");
        addLine("def default_arg_scope(is_training=True,");
        addLine("\tweight_decay=0.00004,");
        addLine("\tuse_batch_norm=True,");
        addLine("\tbatch_norm_decay=0.9997,");
        addLine("\tbatch_norm_epsilon=0.001,");
        addLine("\tbatch_norm_updates_collections=tf.GraphKeys.UPDATE_OPS):\n");
        addLine(" batch_norm_params = {");
        addLine(" \t# Decay for the moving averages.");
        addLine(" \t'decay': batch_norm_decay,");
        addLine(" \t# epsilon to prevent 0s in variance.");
        addLine(" \t'epsilon': batch_norm_epsilon,");
        addLine(" \t# collection containing update_ops.");
        addLine(" \t'updates_collections': batch_norm_updates_collections,");
        addLine(" \t# use fused batch norm if possible.");
        addLine(" \t'fused': None,");
        addLine(" }\n");
        addLine(" if use_batch_norm:");
        addLine(" \tnormalizer_fn = slim.batch_norm");
        addLine(" \tnormalizer_params = batch_norm_params");
        addLine(" else:");
        addLine(" \tnormalizer_fn = None");
        addLine(" \tnormalizer_params = {}\n");
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

    private void addConv2dLines(PrototxtLayer layer) {
        addLine("\t\t\tend_point = '" + layer.getTop() + "'");
        String input = "net";
        if (layer.getBottom().equals("inputs"))
            input = "inputs";
        String netDef = "\t\t\tnet = slim.conv2d(" + input;
        netDef += ", " + ((ConvolutionParam)layer.getParam2()).getNumOutputs();
        int ks = ((ConvolutionParam)layer.getParam2()).getKernelSize();
        netDef += ", [" + ks + ", " + ks + "]";
        netDef += ", stride=" + ((ConvolutionParam)layer.getParam2()).getStride();
        netDef += ", scope=end_point)";
        addLine(netDef);
        addLine("\t\t\tend_points[end_point] = net\n");
    }

    private void addPool2dLines(PrototxtLayer layer) {
        addLine("\t\t\tend_point = '" + layer.getTop() + "'");
        String netDef = "\t\t\tnet = slim.max_pool2d(net";
        int ks = ((PoolingParam)layer.getParam()).getKernelSize();
        netDef += ", [" + ks + ", " + ks + "]";
        netDef += ", stride=" + ((PoolingParam)layer.getParam()).getStride();
        netDef += ", scope=end_point)";
        addLine(netDef);
        addLine("\t\t\tend_points[end_point] = net\n");
    }

    private void addConcatLines(PrototxtLayer layer) {
        addLine("\t\t\tend_point = '" + layer.getTop() + "'");
        addLine("\t\t\twith tf.variable_scope(end_point):");
        //todo add lines to generate tensor code
//        with tf.variable_scope('Branch_0'):
//          branch_0 = slim.conv2d(net, 64, [1, 1], scope='Conv2d_0a_1x1')
//        with tf.variable_scope('Branch_1'):
//          branch_1 = slim.conv2d(net, 96, [1, 1], scope='Conv2d_0a_1x1')
//          branch_1 = slim.conv2d(branch_1, 128, [3, 3], scope='Conv2d_0b_3x3')
//        with tf.variable_scope('Branch_2'):
//          branch_2 = slim.conv2d(net, 16, [1, 1], scope='Conv2d_0a_1x1')
//          branch_2 = slim.conv2d(branch_2, 32, [3, 3], scope='Conv2d_0b_3x3')
//        with tf.variable_scope('Branch_3'):
//          branch_3 = slim.max_pool2d(net, [3, 3], scope='MaxPool_0a_3x3')
//          branch_3 = slim.conv2d(branch_3, 32, [1, 1], scope='Conv2d_0b_1x1')
//        net = tf.concat(
//            axis=3, values=[branch_0, branch_1, branch_2, branch_3])

        addLine("\t\t\tend_points[end_point] = net\n");
    }
}
