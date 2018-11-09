package com.ncsu.csc512.jlwheele;

import java.util.Scanner;

public class PrototxtGrammar {

    public PrototxtGrammar() {

    }

//    <prototxt> --> <prototxt_defs> <layers>
public boolean prototxt() {

    return false;
}

//    <prototxt_defs> --> <name> <input> <input_shape>
public boolean prototxt_defs() {

    return false;
}

//    <input_shape> --> input_shape LEFT_BRACKET <dim> <dim> <dim> <dim> RIGHT_BRACKET
public boolean input_shape() {

    return false;
}

//    <layers> --> <layer>
//            | EMPTY
public boolean layers() {

    return false;
}

//    <layer> --> layer LEFT_BRACKET <layer_defs> RIGHT_BRACKET <layers>
public boolean layer() {

    return false;
}

//    <layer_defs> --> <name> <type> <bottom> <top> <param>
//            | <name> <type> <bottom> <bottom> <bottom> <bottom> <top> <param> (Concat)
public boolean layer_defs() {

    return false;
}

//    * <param> based on type
//    <param> --> param LEFT_BRACKET <param_defs> RIGHT_BRACKET <convolution_param> (Convolution)
//        | batch_norm_param LEFT_BRACKET <batch_norm_defs> RIGHT_BRACKET (BatchNorm)
//        | scale_param LEFT_BRACKET <scale_defs> RIGHT_BRACKET (Scale)
//        | pooling_param LEFT_BRACKET <pooling_defs> RIGHT_BRACKET (Pooling)
//        | dropout_param LEFT_BRACKET <dropout_defs> RIGHT_BRACKET (Dropout)
//        | reshape_param LEFT_BRACKET <reshape_defs> RIGHT_BRACKET (Reshape)
//        | EMPTY
public boolean param() {

    return false;
}

//    <convolution_param> --> convolution_param LEFT_BRACKET <convolution_defs> RIGHT_BRACKET
public boolean convolution_param() {

    return false;
}

//    <param_defs> --> <lr_mult> <decay_mult>
public boolean param_defs() {

    return false;
}

//    <convolution_defs> --> <bias_Term> <num_output> <pad> <kernel_siz> <stride> <weight_filler>
public boolean convolution_defs() {

    return false;
}

//    <weight_filler> --> weight_filler LEFT_BRACKET <weight_defs> RIGHT_BRACKET
public boolean weight_filler() {

    return false;
}

//    <weight_defs> --> <weight_type> <std>
public boolean weight_defs() {

    return false;
}

//    <batch_norm_defs> --> <use_global_stats> <eps>
public boolean batch_norm_defs() {

    return false;
}

//    <scale_defs> --> <bias_term>
public boolean scale_defs() {

    return false;
}

//    <pooling_defs> --> <pool> <kernel_size> <stride>
//            | <pool> <kernel_size> <stride> <pad>
//            | <pool> <global_pooling>
public boolean pooling_defs() {

    return false;
}

//    <dropout_defs> --> <dropout_ratio>
public boolean dropout_defs() {

    return false;
}

//    <reshape_defs> --> shape LEFT_BRACKET <dim> <dim> RIGHT_BRACKET
public boolean reshape_defs() {

    return false;
}

////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////

    //    <name> --> name COLON NAME
    // name: “NAME”
    public boolean name(String line) {
        return isPropertyString(line, "name");
    }

    //    <type> --> type COLON TYPE
    // type: “TYPE”
    public boolean type(String line) {
        return isPropertyString(line, "type");
    }

    //    <bottom> --> bottom COLON NAME
    // bottom: "NAME"
    public boolean bottom(String line) {
        return isPropertyString(line, "bottom");
    }

    //    <top> --> top COLON NAME
    // top: "NAME"
    public boolean top(String line) {
        return isPropertyString(line, "top");
    }

    //    <pool> --> pool COLON POOL
    public boolean pool(String line) {
        if (line.length() > 6) {
            if (line.substring(0, 4).equalsIgnoreCase("pool")) {
                if (line.charAt(4) == ':') {
                    String tmp = line.substring(6);
                    if (tmp.length() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //    <eps> --> eps COLON NUMBER
    public boolean eps(String line) {
        return isPropertyNumber(line, "eps");
    }

    //    <weight_type> --> type COLON WEIGHT_TYPE
    public boolean weight_type(String line) {
        return isPropertyString(line, "type");
    }

    //    <dropout_ratio> --> dropout_ratio COLON NUMBER
    public boolean dropout_ratio(String line) {
        return isPropertyNumber(line, "dropout_ratio");
    }

    //    <global_pooling> --> global_pooling COLON BOOL
    public boolean global_pooling(String line) {
        return isPropertyBoolean(line, "global_pooling");
    }

    //    <use_global_stats> --> use_global_stats COLON BOOL
    public boolean use_global_stats(String line) {
        return isPropertyBoolean(line, "use_global_stats");
    }

    //    <std> --> std COLON NUMBER
    public boolean std(String line) {
        return isPropertyNumber(line, "std");
    }

    //    <stride> --> stride COLON NUMBER
    public boolean stride(String line) {
        return isPropertyNumber(line, "stride");
    }

    //    <lr_mult> --> lr_mult COLON NUMBER
    public boolean lr_mult(String line) {
        return isPropertyNumber(line, "lr_mult");
    }

    //    <decay_mult> --> decay_mult COLON NUMBER
    public boolean decay_mult(String line) {
        return isPropertyNumber(line, "decay_mult");
    }

    //    <bias_term> --> bias_term COLON BOOL
    public boolean bias_term(String line) {
        return isPropertyBoolean(line, "bias_term");
    }

    //    <num_output> --> num_output COLON NUMBER
    public boolean num_output(String line) {
        return isPropertyNumber(line, "num_output");
    }

    //    <pad> --> pad COLON NUMBER
    public boolean pad(String line) {
        return isPropertyNumber(line, "pad");
    }

    //    <kernel_size> --> kernel_size COLON NUMBER
    public boolean kernel_size(String line) {
        return isPropertyNumber(line, "kernel_size");
    }

    //    <input> --> input COLON ID
    public boolean input(String line) {
        return isPropertyString(line, "input");
    }

    //    <dim> --> dim COLON NUMBER
    public boolean dim(String line) {
        return isPropertyNumber(line, "dim");
    }

    private boolean isPropertyNumber(String line, String id) {
        if (line.length() > (id.length() + 2)) {
            if (line.substring(0, id.length()).equalsIgnoreCase(id)) {
                if (line.charAt(id.length()) == ':') {
                    String tmp = line.substring(id.length() + 2);
                    try {
                        Integer.parseInt(tmp);
                        return true;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return false;
    }

    private boolean isPropertyString(String line, String id) {
        if (line.length() > (id.length() + 4)) {
            if (line.substring(0, id.length()).equalsIgnoreCase(id)) {
                if (line.charAt(id.length()) == ':') {
                    if (line.charAt(id.length() + 2) == '\"') {
                        String tmp = line.substring(id.length() + 3);
                        if (tmp.charAt(tmp.length() - 1) == '\"') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isPropertyBoolean(String line, String id) {
        if (line.length() > (id.length() + 2)) {
            if (line.substring(0, id.length()).equalsIgnoreCase(id)) {
                if (line.charAt(id.length()) == ':') {
                    String tmp = line.substring((id.length() + 2));
                    if (tmp.equalsIgnoreCase("true") || tmp.equalsIgnoreCase("false")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}