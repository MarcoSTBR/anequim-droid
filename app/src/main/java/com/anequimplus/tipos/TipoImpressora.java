package com.anequimplus.tipos;

public enum TipoImpressora {

    tpILocal(0), tpIV7(1), tpILio(2),  tpIUSB(3), tpElginM10(4);


    public int valor;

    TipoImpressora(int v) {
        valor = v;
    }
}
