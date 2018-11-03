package com.ncsu.csc512.jlwheele;

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
        if (line.length() > 8) {
            if (line.substring(0, 4).equalsIgnoreCase("name")) {
                if (line.charAt(4) == ':') {
                    if (line.charAt(6) == '\"') {
                        String tmp = line.substring(7);
                        if (tmp.charAt(tmp.length() - 1) == '\"') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <type> --> type COLON TYPE
    // type: “TYPE”
    public boolean type(String line) {
        if (line.length() > 8) {
            if (line.substring(0, 4).equalsIgnoreCase("type")) {
                if (line.charAt(4) == ':') {
                    if (line.charAt(6) == '\"') {
                        String tmp = line.substring(7);
                        if (tmp.charAt(tmp.length() - 1) == '\"') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <bottom> --> bottom COLON NAME
    // bottom: "NAME"
    public boolean bottom(String line) {
        if (line.length() > 10) {
            if (line.substring(0, 6).equalsIgnoreCase("bottom")) {
                if (line.charAt(6) == ':') {
                    if (line.charAt(8) == '\"') {
                        String tmp = line.substring(9);
                        if (tmp.charAt(tmp.length() - 1) == '\"') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <top> --> top COLON NAME
    // top: "NAME"
    public boolean top(String line) {
        if (line.length() > 7) {
            if (line.substring(0, 3).equalsIgnoreCase("top")) {
                if (line.charAt(3) == ':') {
                    if (line.charAt(5) == '\"') {
                        String tmp = line.substring(6);
                        if (tmp.charAt(tmp.length() - 1) == '\"') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
        if (line.length() > 6) {
            if (line.substring(0, 3).equalsIgnoreCase("eps")) {
                if (line.charAt(3) == ':') {
                    String tmp = line.substring(5);
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

    //    <weight_type> --> type COLON WEIGHT_TYPE
    public boolean weight_type(String line) {
        if (line.length() > 8) {
            if (line.substring(0, 4).equalsIgnoreCase("type")) {
                if (line.charAt(4) == ':') {
                    if (line.charAt(6) == '\"') {
                        String tmp = line.substring(7);
                        if (tmp.charAt(tmp.length() - 1) == '\"') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <dropout_ratio> --> dropout_ratio COLON NUMBER
    public boolean dropout_ratio(String line) {

        return false;
    }

    //    <global_pooling> --> global_pooling COLON BOOL
    public boolean global_pooling(String line) {

        return false;
    }

    //    <use_global_stats> --> use_global_stats COLON BOOL
    public boolean use_global_stats(String line) {

        return false;
    }

    //    <std> --> std COLON NUMBER
    public boolean std(String line) {

        return false;
    }

    //    <stride> --> stride COLON NUMBER
    public boolean stride(String line) {

        return false;
    }

    //    <lr_mult> --> lr_mult COLON NUMBER
    public boolean lr_mult(String line) {

        return false;
    }

    //    <decay_mult> --> decay_mult COLON NUMBER
    public boolean decay_mult(String line) {

        return false;
    }

    //    <bias_term> --> bias_term COLON BOOL
    public boolean bias_term(String line) {

        return false;
    }

    //    <num_output> --> num_output COLON NUMBER
    public boolean num_output(String line) {

        return false;
    }

    //    <pad> --> pad COLON NUMBER
    public boolean pad(String line) {

        return false;
    }

    //    <kernel_size> --> kernel_size COLON NUMBER
    public boolean kernel_size(String line) {

        return false;
    }

    //    <input> --> input COLON ID
    public boolean input(String line) {
        if (line.length() > 10) {
            if (line.substring(0, 5).equalsIgnoreCase("input")) {
                if (line.charAt(5) == ':') {
                    if (line.charAt(7) == '\"') {
                        String tmp = line.substring(8);
                        if (tmp.charAt(tmp.length() - 1) == '\"') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <dim> --> dim COLON NUMBER
    public boolean dim(String line) {

        return false;
    }


}
