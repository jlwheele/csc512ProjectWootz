package com.ncsu.csc512.jlwheele;

public class PrototxtGrammar {

    public PrototxtGrammar() {
    }

//    <prototxt> --> <prototxt_defs> <layers>
//
//    <prototxt_defs> --> <name> <input> <input_shape>
//
//    <input_shape> --> input_shape LEFT_BRACKET <dim> <dim> <dim> <dim> RIGHT_BRACKET
//
//    <layers> --> <layer>
//            | EMPTY
//
//    <layer> --> layer LEFT_BRACKET <layer_defs> RIGHT_BRACKET <layers>
//
//    <layer_defs> --> <name> <type> <bottom> <top> <param>
//            | <name> <type> <bottom> <bottom> <bottom> <bottom> <top> <param> (Concat)
//
//    * <param> based on type
//    <param> --> param LEFT_BRACKET <param_defs> RIGHT_BRACKET <convolution_param> (Convolution)
//        | batch_norm_param LEFT_BRACKET <batch_norm_defs> RIGHT_BRACKET (BatchNorm)
//        | scale_param LEFT_BRACKET <scale_defs> RIGHT_BRACKET (Scale)
//        | pooling_param LEFT_BRACKET <pooling_defs> RIGHT_BRACKET (Pooling)
//        | dropout_param LEFT_BRACKET <dropout_defs> RIGHT_BRACKET (Dropout)
//        | reshape_param LEFT_BRACKET <reshape_defs> RIGHT_BRACKET (Reshape)
//        | EMPTY
//
//    <convolution_param> --> convolution_param LEFT_BRACKET <convolution_defs> RIGHT_BRACKET
//
//    <param_defs> --> <lr_mult> <decay_mult>
//
//    <convolution_defs> --> <bias_Term> <num_output> <pad> <kernel_siz> <stride> <weight_filler>
//
//    <weight_filler> --> weight_filler LEFT_BRACKET <weight_defs> RIGHT_BRACKET
//
//    <weight_defs> --> <weight_type> <std>
//
//
//    <batch_norm_defs> --> <use_global_stats> <eps>
//
//    <scale_defs> --> <bias_term>
//
//    <pooling_defs> --> <pool> <kernel_size> <stride>
//            | <pool> <kernel_size> <stride> <pad>
//            | <pool> <global_pooling>
//

//    <dropout_defs> --> <dropout_ratio>
//

//
//    <reshape_defs> --> shape LEFT_BRACKET <dim> <dim> RIGHT_BRACKET


//    <name> --> name COLON NAME
//
//    <type> --> type COLON TYPE
//
//    <bottom> --> bottom COLON NAME
//
//    <top> --> top COLON NAME

//    <pool> --> pool COLON POOL

//    <eps> --> eps COLON NUMBER

//    <weight_type> --> type COLON WEIGHT_TYPE

//    <dropout_ratio> --> dropout_ratio COLON NUMBER

//    <global_pooling> --> global_pooling COLON BOOL

//    <use_global_stats> --> use_global_stats COLON BOOL

//    <std> --> std COLON NUMBER

//    <stride> --> stride COLON NUMBER

//    <lr_mult> --> lr_mult COLON NUMBER

//    <decay_mult> --> decay_mult COLON NUMBER

//    <bias_term> --> bias_term COLON BOOL

//    <num_output> --> num_output COLON NUMBER

//    <pad> --> pad COLON NUMBER

//    <kernel_size> --> kernel_size COLON NUMBER

//    <input> --> input COLON ID

//    <dim> --> dim COLON NUMBER


}
