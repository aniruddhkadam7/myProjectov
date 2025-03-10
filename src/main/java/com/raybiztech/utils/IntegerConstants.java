/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.utils;

/**
 *
 * @author user
 */
public enum IntegerConstants {
            CONSTANT0(0),CONSTANT1(1), CONSTANT2(2), CONSTANT3(3), CONSTANT10(10), CONSTANT1024(1024), CONSTANT11(
            11), CONSTANT15(15), CONSTANT16(16), CONSTANT14(14), CONSTANT17(17), CONSTANT1879(
            1879), CONSTANT19(19), CONSTANT1907(1907), CONSTANT1909(1909), CONSTANT1913(
            1913), CONSTANT29(29), CONSTANT22(22), CONSTANT1947(1947), CONSTANT1931(
            1931), CONSTANT1921(1921), CONSTANT1915(1915), CONSTANT4(4), CONSTANT5(
            5), CONSTANT6(6), CONSTANT7(7), CONSTANT8(8), CONSTANT9(9), CONSTANT1897(
            1897), CONSTANT1899(1899), CONSTANT31(31), CONSTANT37(37), CONSTANT44100(
            44100), CONSTANT47(47), CONSTANT49(49), CONSTANT512(512), CONSTANT53(
            53), CONSTANT55(55), CONSTANT56000(56000), CONSTANT59(59), CONSTANT61(
            61), CONSTANT67(67), CONSTANT8192(8192), CONSTANT97(97), CONSTANT12(
            12), CONSTANT13(13);

    private int typeCode;

    private IntegerConstants(int i) {
        typeCode = i;
    }

    /**
     * 
     * @return
     */
    public int getConstantValue() {
        return typeCode;
    }
}