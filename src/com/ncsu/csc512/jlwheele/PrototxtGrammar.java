package com.ncsu.csc512.jlwheele;

import java.util.Scanner;

public class PrototxtGrammar {
    
    private String currLayerType;
    private PrototxtParser pParser;

    public PrototxtGrammar(PrototxtParser pParser) {
        System.out.println("Prototxt Grammar init");
        this.pParser = pParser;
        currLayerType = "";
    }

    //    <prototxt> --> <prototxt_defs> <layers>
    public boolean prototxt() {
        System.out.println("Prototxt Grammar started");
        if (prototxt_defs()) {
            if (layers()) {
                return true;
            }
        }
        return false;
    }

    //    <prototxt_defs> --> <name> <input> <input_shape>
    private boolean prototxt_defs() {
        if (name()) {
            pParser.addNode();
            if (input()) {
                pParser.addNode();
                if (input_shape()) {
                    pParser.addNode();
                    return true;
                }
            }
        }
        return false;
    }

    //    <input_shape> --> input_shape LEFT_BRACKET <dim> <dim> <dim> <dim> RIGHT_BRACKET
    private boolean input_shape() {
        if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                && pParser.getCurrToken().getValue().equals("input_shape")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals("{")) {
                pParser.nextToken();
                if (dim()) {
                    if (dim()) {
                        if (dim()) {
                            if (dim()) {
                                if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                        && pParser.getCurrToken().getValue().equals("}")) {
                                    pParser.nextToken();
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
    private boolean layers() {
        if (layer()) {
            return true;
        } else if (!pParser.hasNextToken()) {
            return true;
        }
        return false;
    }

    //    <layer> --> layer LEFT_BRACKET <layer_defs> RIGHT_BRACKET <layers>
    private boolean layer() {
        if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                && pParser.getCurrToken().getValue().equals("layer")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals("{")) {
                pParser.nextToken();
                if (layer_defs()) {
                    if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                            && pParser.getCurrToken().getValue().equals("}")) {
                        pParser.nextToken();
                        pParser.addNode();
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
    private boolean layer_defs() {
        if (name()) {
            if (type()) {
                if (bottom()) {
                    if (top()) {
                        if (pParser.getCurrToken().getValue().equals("}")
                            && (currLayerType.equals("Softmax") || currLayerType.equals("ReLU"))) {
                            return true;
                        } else if (param()) {
                            return true;
                        }
                    } else if (currLayerType.equals("Concat") && bottom()) {
                        if (bottom()) {
                            if (bottom()) {
                                if (top()) {
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

    //    * <param> based on type
    //    <param> --> param LEFT_BRACKET <param_defs> RIGHT_BRACKET <convolution_param> (Convolution)
    //        | batch_norm_param LEFT_BRACKET <batch_norm_defs> RIGHT_BRACKET (BatchNorm)
    //        | scale_param LEFT_BRACKET <scale_defs> RIGHT_BRACKET (Scale)
    //        | pooling_param LEFT_BRACKET <pooling_defs> RIGHT_BRACKET (Pooling)
    //        | dropout_param LEFT_BRACKET <dropout_defs> RIGHT_BRACKET (Dropout)
    //        | reshape_param LEFT_BRACKET <reshape_defs> RIGHT_BRACKET (Reshape)
    //        | EMPTY
    private boolean param() {
        if (currLayerType.equals("Convolution")) {
            if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                    && pParser.getCurrToken().getValue().equals("param")) {
                pParser.nextToken();
                if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && pParser.getCurrToken().getValue().equals("{")) {
                    pParser.nextToken();
                    if (param_defs()) {
                        if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && pParser.getCurrToken().getValue().equals("}")) {
                            pParser.nextToken();
                            if (convolution_param()) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (currLayerType.equals("BatchNorm")) {
            if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                    && pParser.getCurrToken().getValue().equals("batch_norm_param")) {
                pParser.nextToken();
                if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && pParser.getCurrToken().getValue().equals("{")) {
                    pParser.nextToken();
                    if (batch_norm_defs()) {
                        if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && pParser.getCurrToken().getValue().equals("}")) {
                            pParser.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Scale")) {
            if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                    && pParser.getCurrToken().getValue().equals("scale_param")) {
                pParser.nextToken();
                if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && pParser.getCurrToken().getValue().equals("{")) {
                    pParser.nextToken();
                    if (scale_defs()) {
                        if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && pParser.getCurrToken().getValue().equals("}")) {
                            pParser.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Pooling")) {
            if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                    && pParser.getCurrToken().getValue().equals("pooling_param")) {
                pParser.nextToken();
                if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && pParser.getCurrToken().getValue().equals("{")) {
                    pParser.nextToken();
                    if (pooling_defs()) {
                        if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && pParser.getCurrToken().getValue().equals("}")) {
                            pParser.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Dropout")) {
            if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                    && pParser.getCurrToken().getValue().equals("dropout_param")) {
                pParser.nextToken();
                if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && pParser.getCurrToken().getValue().equals("{")) {
                    pParser.nextToken();
                    if (dropout_defs()) {
                        if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && pParser.getCurrToken().getValue().equals("}")) {
                            pParser.nextToken();
                            return true;
                        }
                    }
                }
            }
        } else if (currLayerType.equals("Reshape")) {
            if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                    && pParser.getCurrToken().getValue().equals("reshape_param")) {
                pParser.nextToken();
                if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                        && pParser.getCurrToken().getValue().equals("{")) {
                    pParser.nextToken();
                    if (reshape_defs()) {
                        if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && pParser.getCurrToken().getValue().equals("}")) {
                            pParser.nextToken();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //    <convolution_param> --> convolution_param LEFT_BRACKET <convolution_defs> RIGHT_BRACKET
    private boolean convolution_param() {
        if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                && pParser.getCurrToken().getValue().equals("convolution_param")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals("{")) {
                pParser.nextToken();
                if (convolution_defs()) {
                    if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                            && pParser.getCurrToken().getValue().equals("}")) {
                        pParser.nextToken();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //    <param_defs> --> <lr_mult> <decay_mult>
    private boolean param_defs() {
        if (lr_mult()) {
            if (decay_mult()) {
                return true;
            }
        }
        return false;
    }

    //    <convolution_defs> --> <bias_Term> <num_output> <pad> <kernel_siz> <stride> <weight_filler>
    private boolean convolution_defs() {
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
    private boolean weight_filler() {
        if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                && pParser.getCurrToken().getValue().equals("weight_filler")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals("{")) {
                pParser.nextToken();
                if (weight_defs()) {
                    if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                            && pParser.getCurrToken().getValue().equals("}")) {
                        pParser.nextToken();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //    <weight_defs> --> <weight_type> <std>
    private boolean weight_defs() {
        if (weight_type()) {
            if (std()) {
                return true;
            }
        }
        return false;
    }

    //    <batch_norm_defs> --> <use_global_stats> <eps>
    private boolean batch_norm_defs() {
        if (use_global_stats()) {
            if (eps()) {
                return true;
            }
        }
        return false;
    }

    //    <scale_defs> --> <bias_term>
    private boolean scale_defs() {
        if (bias_term()) {
            return true;
        }
        return false;
    }

    //    <pooling_defs> --> <pool> <kernel_size> <stride>
    //            | <pool> <kernel_size> <stride> <pad>
    //            | <pool> <global_pooling>
    private boolean pooling_defs() {
        if (pool()) {
            if (kernel_size()) {
                if (stride()) {
                    if (pParser.getCurrToken().getValue().equals("}")) {
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
    private boolean dropout_defs() {
        if (dropout_ratio()) {
            return true;
        }
        return false;
    }

    //    <reshape_defs> --> shape LEFT_BRACKET <dim> <dim> RIGHT_BRACKET
    private boolean reshape_defs() {
        if ((pParser.getCurrToken().getType() == Token.VAL_TYPE)
                && pParser.getCurrToken().getValue().equals("shape")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals("{")) {
                pParser.nextToken();
                if (dim()) {
                    if (dim()) {
                        if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                                && pParser.getCurrToken().getValue().equals("}")) {
                            pParser.nextToken();
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
    private boolean name() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
            && pParser.getCurrToken().getValue().equals("name")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.STRING_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <type> --> type COLON TYPE
    // type: “TYPE”
    private boolean type() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("type")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.STRING_TYPE) {
                    currLayerType = pParser.getCurrToken().getValue();
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <bottom> --> bottom COLON NAME
    // bottom: "NAME"
    private boolean bottom() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("bottom")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.STRING_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <top> --> top COLON NAME
    // top: "NAME"
    private boolean top() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("top")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.STRING_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <pool> --> pool COLON POOL
    private boolean pool() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("pool")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NON_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <eps> --> eps COLON NUMBER
    private boolean eps() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("eps")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <weight_type> --> type COLON WEIGHT_TYPE
    private boolean weight_type() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("type")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.STRING_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <dropout_ratio> --> dropout_ratio COLON NUMBER
    private boolean dropout_ratio() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("dropout_ratio")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <global_pooling> --> global_pooling COLON BOOL
    private boolean global_pooling() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("global_pooling")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.BOOL_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <use_global_stats> --> use_global_stats COLON BOOL
    private boolean use_global_stats() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("use_global_stats")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.BOOL_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <std> --> std COLON NUMBER
    private boolean std() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("std")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <stride> --> stride COLON NUMBER
    private boolean stride() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("stride")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <lr_mult> --> lr_mult COLON NUMBER
    private boolean lr_mult() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("lr_mult")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <decay_mult> --> decay_mult COLON NUMBER
    private boolean decay_mult() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("decay_mult")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <bias_term> --> bias_term COLON BOOL
    private boolean bias_term() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("bias_term")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.BOOL_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <num_output> --> num_output COLON NUMBER
    private boolean num_output() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("num_output")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <pad> --> pad COLON NUMBER
    private boolean pad() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("pad")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <kernel_size> --> kernel_size COLON NUMBER
    private boolean kernel_size() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("kernel_size")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <input> --> input COLON ID
    private boolean input() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("input")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.STRING_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }

    //    <dim> --> dim COLON NUMBER
    private boolean dim() {
        if ((pParser.getCurrToken().getType() == Token.VAR_TYPE)
                && pParser.getCurrToken().getValue().equals("dim")) {
            pParser.nextToken();
            if ((pParser.getCurrToken().getType() == Token.SYMBOL_TYPE)
                    && pParser.getCurrToken().getValue().equals(":")) {
                pParser.nextToken();
                if (pParser.getCurrToken().getType() == Token.NUM_TYPE) {
                    pParser.nextToken();
                    return true;
                }
            }
        }
        return false;
    }
}