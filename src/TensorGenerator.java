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
    private  PrototxtLayer[] prototxtLayers;
    private int gKernelSize;

    public TensorGenerator(String fileName, PrototxtData pData) {
        this.pData = pData;
        this.fileName = fileName + ".py";
        file = Paths.get(this.fileName);
        charset = Charset.forName("UTF-8");
        lines = new String[0];
        gKernelSize = 0;
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
        addLine("### WOOTZ COMPILER v1.0   2018/12/08              ###");
        addLine("### NCSU CSC512 JAMES WHEELER (JLWHEELE)          ###");
        addLine("###                                               ###");
        addLine("### Below is code generated from prototxt         ###");
        addLine("#####################################################\n");

        prototxtLayers = pData.getLayers();

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

            if (c > 1) {
                nextBottom = null;
                break;
            }

            if (currLayer != null) {
                if (currLayer.getType().equals(PrototxtLayer.TYPE_CONVOLUTION)) {
                    addConv2dLines(currLayer);
                } else {
                    addPool2dLines(currLayer);
                }

                nextBottom = currLayer.getTop();
            } else {
                System.out.println("ERROR! Failed to generate. Missing layer with bottom " + nextBottom);
                return;
            }
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
                if ((c == 1) && currLayer.getType().equals(PrototxtLayer.TYPE_POOLING)) {
                    addPool2dLines(currLayer);
                    if (((PoolingParam)currLayer.getParam()).isGlobalPooling())
                        addGlobalLines(currLayer);
                }
            }
        }

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

        if (input.equals("inputs"))
            gKernelSize = ks;
    }

    private void addPool2dLines(PrototxtLayer layer) {
        addLine("\t\t\tend_point = '" + layer.getTop() + "'");
        String netDef = "\t\t\tnet = slim.max_pool2d(net";
        if (((PoolingParam)layer.getParam()).getPool().equals("AVE"))
            netDef = "\t\t\tnet = slim.avg_pool2d(net";
        int ks = ((PoolingParam)layer.getParam()).getKernelSize();
        if (((PoolingParam)layer.getParam()).isGlobalPooling())
            ks = gKernelSize;
        netDef += ", [" + ks + ", " + ks + "]";
        netDef += ", stride=" + ((PoolingParam)layer.getParam()).getStride();
        netDef += ", scope=end_point)";
        addLine(netDef);
        addLine("\t\t\tend_points[end_point] = net\n");
    }

    private void addConcatLines(PrototxtLayer layer) {
        addLine("\t\t\tend_point = '" + layer.getTop() + "'");
        addLine("\t\t\twith tf.variable_scope(end_point):");

        String[] bottoms = layer.getBottoms();
        int b = 0;
        for (String bottom : bottoms) {
            addLine("\t\t\t\twith tf.variable_scope('Branch_" + b + "'):");
            for (PrototxtLayer layer1 : prototxtLayers) {
                if (layer1.getTop().equals(bottom) && layer1.getType().equals(PrototxtLayer.TYPE_CONVOLUTION)) {
                    boolean net = true;
                    int s1 = layer1.getTop().lastIndexOf("/");
                    int s2 = layer1.getBottom().lastIndexOf("/");
                    if (s1 == s2) {
                        for (PrototxtLayer layer2 : prototxtLayers) {
                            if (layer2.getTop().equals(layer1.getBottom()) && !layer2.getBottom().equals(layer1.getBottom())) {
                                if (layer2.getType().equals(PrototxtLayer.TYPE_CONVOLUTION))
                                    addBranchConv2dLines(layer2, b, true);
                                else
                                    addBranchPool2dLines(layer2, b, true);
                                break;
                            }
                        }
                        net = false;
                    }

                    addBranchConv2dLines(layer1, b, net);
                }
            }
            b++;
        }

        addLine("\t\t\t\tnet = tf.concat(axis=3, values=[branch_0, branch_1, branch_2, branch_3])");
        addLine("\t\t\tend_points[end_point] = net\n");
    }

    private void addBranchConv2dLines(PrototxtLayer layer, int branch, boolean net) {
        String s = "branch_" + branch;
        if (net)
            s = "net";

        String top = layer.getTop().split("/")[2];

        String branchDef = "\t\t\t\t\tbranch_" + branch + " = slim.conv2d(" + s;
        branchDef += ", " + ((ConvolutionParam)layer.getParam2()).getNumOutputs();
        int ks = ((ConvolutionParam)layer.getParam2()).getKernelSize();
        branchDef += ", [" + ks + ", " + ks + "]";
        branchDef += ", scope='" + top + "')";
        addLine(branchDef);
    }

    private void addBranchPool2dLines(PrototxtLayer layer, int branch, boolean net) {
        String s = "branch_" + branch;
        if (net)
            s = "net";

        String top = layer.getTop().split("/")[2];

        String branchDef = "\t\t\t\t\tbranch_" + branch + " = slim.max_pool2d(net";
        int ks = ((PoolingParam)layer.getParam()).getKernelSize();
        branchDef += ", [" + ks + ", " + ks + "]";
        branchDef += ", scope='" + top + "')";
        addLine(branchDef);
    }

    private void addGlobalLines(PrototxtLayer layer) {
        PrototxtLayer currLayer = null;
        for (PrototxtLayer layer1 : prototxtLayers) {
            if (layer1.getBottom().equals(layer.getTop())) {
                addLine("\t\t\tend_point = 'Logits'");
                addLine("\t\t\twith tf.variable_scope(end_point):");
                currLayer = layer1;
                break;
            }
        }

        if (currLayer.getType().equals(PrototxtLayer.TYPE_DROPOUT)) {
            addLine("\t\t\t\tnet = slim.dropout(net, 0.8, scope='" + currLayer.getName().replace("Logits/", "") + "')");
        }

        for (PrototxtLayer layer1 : prototxtLayers) {
            if (layer1.getBottom().equals(currLayer.getTop())) {
                if (layer1.getType().equals(PrototxtLayer.TYPE_CONVOLUTION)) {
                    String logitsDef = "\t\t\t\tlogits = slim.conv2d(net, num_classes";
                    int ks = ((ConvolutionParam)layer1.getParam2()).getKernelSize();
                    logitsDef += ", [" + ks + ", " + ks + "]";
                    logitsDef += ", activation_fn=None, normalizer_fn=None";
                    logitsDef += ", scope='" + layer1.getName().replace("Logits/", "") + "')";
                    addLine(logitsDef);
                } else if (layer1.getType().equals(PrototxtLayer.TYPE_RESHAPE)) {
                    addLine("\t\t\t\tlogits = tf.squeeze(logits, [1, 2], name='SpatialSqueeze')");
                    addLine("\t\t\t\tend_points[end_point] = logits");
                } else if (layer1.getType().equals(PrototxtLayer.TYPE_SOFTMAX)) {
                    addLine("\t\t\tend_points['" + layer1.getTop() + "'] = slim.softmax(logits, scope='" + layer1.getTop() + "')");
                }
            }
        }
    }
}
