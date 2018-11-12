package com.ncsu.csc512.jlwheele;

import java.util.Scanner;

public class PrototxtGrammar {

    private TokenList tokenList;
    private String currLayerType;

    public PrototxtGrammar(TokenList tokenList) {
        this.tokenList = tokenList;
        currLayerType = "";
    }

    //    <prototxt> --> <prototxt_defs> <layers>
    public boolean prototxt() {
        if (prototxt_defs()) {
            if (layers()) {
                return true;
            }
        }
        return false;
    }

    //    <prototxt_defs> --> <name> <input> <input_shape>
    public boolean prototxt_defs() {
        if (name()) {
            if (input()) {
                if (input_shape()) {
                    return true;
                }
            }
        }
        return false;
    }

    //    <input_shape> --> input_shape LEFT_BRACKET <dim> <dim> <dim> <dim> RIGHT_BRACKET
    public boolean input_shape() {
        if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                && tokenList.getCurrToken().getValue().equals("input_shape")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("{")) {
                tokenList.nextToken();
                if (dim()) {
                    if (dim()) {
                        if (dim()) {
                            if (dim()) {
                                if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                        && tokenList.getCurrToken().getValue().equals("}")) {
                                    tokenList.nextToken();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <layers> --> <layer>
    //            | EMPTY
    public boolean layers() {
        if (layer()) {
            return true;
        } else if (!tokenList.hasNextToken()) {
            return true;
        }
        return false;
    }

    //    <layer> --> layer LEFT_BRACKET <layer_defs> RIGHT_BRACKET <layers>
    public boolean layer() {
        if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                && tokenList.getCurrToken().getValue().equals("layer")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("{")) {
                tokenList.nextToken();
                if (layer_defs()) {
                    if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                            && tokenList.getCurrToken().getValue().equals("}")) {
                        tokenList.nextToken();
                        if (layers()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <layer_defs> --> <name> <type> <bottom> <top> <param>
    //            | <name> <type> <bottom> <bottom> <bottom> <bottom> <top> (Concat)
    public boolean layer_defs() {
        if (name()) {
            if (type()) {
                if (bottom()) {
                    if (top()) {
                        if (tokenList.getCurrToken().getValue().equals("}")
                            && (currLayerType.equals("Softmax") || currLayerType.equals("ReLU"))) {
                            return true;
                        } else if (param()) {
                            return true;
                        }
                    } else if (currLayerType.equals("Concat") && bottom()) {
                        if (bottom()) {
                            if (bottom()) {
                                if (top()) {
                                    if (param()) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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
        if (currLayerType.equals("Convolution")) {
            if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("param")) {
                tokenList.nextToken();
                if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && tokenList.getCurrToken().getValue().equals("{")) {
                    tokenList.nextToken();
                    if (param_defs()) {
                        if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && tokenList.getCurrToken().getValue().equals("}")) {
                            tokenList.nextToken();
                            if (convolution_param()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (currLayerType.equals("BatchNorm")) {
            if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("batch_norm_param")) {
                tokenList.nextToken();
                if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && tokenList.getCurrToken().getValue().equals("{")) {
                    tokenList.nextToken();
                    if (batch_norm_defs()) {
                        if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && tokenList.getCurrToken().getValue().equals("}")) {
                            tokenList.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Scale")) {
            if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("scale_param")) {
                tokenList.nextToken();
                if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && tokenList.getCurrToken().getValue().equals("{")) {
                    tokenList.nextToken();
                    if (scale_defs()) {
                        if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && tokenList.getCurrToken().getValue().equals("}")) {
                            tokenList.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Pooling")) {
            if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("pooling_param")) {
                tokenList.nextToken();
                if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && tokenList.getCurrToken().getValue().equals("{")) {
                    tokenList.nextToken();
                    if (pooling_defs()) {
                        if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && tokenList.getCurrToken().getValue().equals("}")) {
                            tokenList.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Dropout")) {
            if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("dropout_param")) {
                tokenList.nextToken();
                if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && tokenList.getCurrToken().getValue().equals("{")) {
                    tokenList.nextToken();
                    if (dropout_defs()) {
                        if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && tokenList.getCurrToken().getValue().equals("}")) {
                            tokenList.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Reshape")) {
            if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("reshape_param")) {
                tokenList.nextToken();
                if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && tokenList.getCurrToken().getValue().equals("{")) {
                    tokenList.nextToken();
                    if (reshape_defs()) {
                        if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && tokenList.getCurrToken().getValue().equals("}")) {
                            tokenList.nextToken();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <convolution_param> --> convolution_param LEFT_BRACKET <convolution_defs> RIGHT_BRACKET
    public boolean convolution_param() {
        if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                && tokenList.getCurrToken().getValue().equals("convolution_param")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("{")) {
                tokenList.nextToken();
                if (convolution_defs()) {
                    if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                            && tokenList.getCurrToken().getValue().equals("}")) {
                        tokenList.nextToken();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //    <param_defs> --> <lr_mult> <decay_mult>
    public boolean param_defs() {
        if (lr_mult()) {
            if (decay_mult()) {
                return true;
            }
        }
        return false;
    }

    //    <convolution_defs> --> <bias_Term> <num_output> <pad> <kernel_siz> <stride> <weight_filler>
    public boolean convolution_defs() {
        if (bias_term()) {
            if (num_output()) {
                if (pad()) {
                    if (kernel_size()) {
                        if (stride()) {
                            if (weight_filler()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <weight_filler> --> weight_filler LEFT_BRACKET <weight_defs> RIGHT_BRACKET
    public boolean weight_filler() {
        if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                && tokenList.getCurrToken().getValue().equals("weight_filler")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("{")) {
                tokenList.nextToken();
                if (weight_defs()) {
                    if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                            && tokenList.getCurrToken().getValue().equals("}")) {
                        tokenList.nextToken();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //    <weight_defs> --> <weight_type> <std>
    public boolean weight_defs() {
        if (weight_type()) {
            if (std()) {
                return true;
            }
        }
        return false;
    }

    //    <batch_norm_defs> --> <use_global_stats> <eps>
    public boolean batch_norm_defs() {
        if (use_global_stats()) {
            if (eps()) {
                return true;
            }
        }
        return false;
    }

    //    <scale_defs> --> <bias_term>
    public boolean scale_defs() {
        if (bias_term()) {
            return true;
        }
        return false;
    }

    //    <pooling_defs> --> <pool> <kernel_size> <stride>
    //            | <pool> <kernel_size> <stride> <pad>
    //            | <pool> <global_pooling>
    public boolean pooling_defs() {
        if (pool()) {
            if (kernel_size()) {
                if (stride()) {
                    if (tokenList.getCurrToken().getValue().equals("}")) {
                        return true;
                    } else if (pad()) {
                        return true;
                    }
                }
            } else if (global_pooling()) {
                return true;
            }
        }
        return false;
    }

    //    <dropout_defs> --> <dropout_ratio>
    public boolean dropout_defs() {
        if (dropout_ratio()) {
            return true;
        }
        return false;
    }

    //    <reshape_defs> --> shape LEFT_BRACKET <dim> <dim> RIGHT_BRACKET
    public boolean reshape_defs() {
        if ((tokenList.getCurrToken().getType() == Token.VAL_TYPE)
                && tokenList.getCurrToken().getValue().equals("shape")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals("{")) {
                tokenList.nextToken();
                if (dim()) {
                    if (dim()) {
                        if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && tokenList.getCurrToken().getValue().equals("}")) {
                            tokenList.nextToken();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////

    //    <name> --> name COLON NAME
    // name: “NAME”
    public boolean name() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
            && tokenList.getCurrToken().getValue().equals("name")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.STRING_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <type> --> type COLON TYPE
    // type: “TYPE”
    public boolean type() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("type")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.STRING_TYPE) {
                    currLayerType = tokenList.getCurrToken().getValue();
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <bottom> --> bottom COLON NAME
    // bottom: "NAME"
    public boolean bottom() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("bottom")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.STRING_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <top> --> top COLON NAME
    // top: "NAME"
    public boolean top() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("top")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.STRING_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <pool> --> pool COLON POOL
    public boolean pool() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("pool")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.VAL_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <eps> --> eps COLON NUMBER
    public boolean eps() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("eps")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <weight_type> --> type COLON WEIGHT_TYPE
    public boolean weight_type() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("type")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.STRING_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <dropout_ratio> --> dropout_ratio COLON NUMBER
    public boolean dropout_ratio() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("dropout_ratio")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <global_pooling> --> global_pooling COLON BOOL
    public boolean global_pooling() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("global_pooling")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.BOOL_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <use_global_stats> --> use_global_stats COLON BOOL
    public boolean use_global_stats() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("use_global_stats")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.BOOL_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <std> --> std COLON NUMBER
    public boolean std() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("std")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <stride> --> stride COLON NUMBER
    public boolean stride() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("stride")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <lr_mult> --> lr_mult COLON NUMBER
    public boolean lr_mult() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("lr_mult")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <decay_mult> --> decay_mult COLON NUMBER
    public boolean decay_mult() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("decay_mult")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <bias_term> --> bias_term COLON BOOL
    public boolean bias_term() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("bias_term")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.BOOL_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <num_output> --> num_output COLON NUMBER
    public boolean num_output() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("num_output")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <pad> --> pad COLON NUMBER
    public boolean pad() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("pad")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <kernel_size> --> kernel_size COLON NUMBER
    public boolean kernel_size() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("kernel_size")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <input> --> input COLON ID
    public boolean input() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("input")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.STRING_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <dim> --> dim COLON NUMBER
    public boolean dim() {
        if ((tokenList.getCurrToken().getType() == Token.VAR_TYPE)
                && tokenList.getCurrToken().getValue().equals("dim")) {
            tokenList.nextToken();
            if ((tokenList.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && tokenList.getCurrToken().getValue().equals(":")) {
                tokenList.nextToken();
                if (tokenList.getCurrToken().getType() == Token.NUM_TYPE) {
                    tokenList.nextToken();
                    return true;
                }
            }
        }
        return false;
    }
}