package com.main.pawtroller_psm

class Entidad {

    public var imgFoto:Int = -1;
    public var Nombre:String = "";
    public var Titulo:String = "";
    public var Fecha:String = "";
    public var Descripcion:String = "";

    constructor(imgFoto:Int,Nombre:String,Titulo:String,Fecha:String,Descripcion:String){

        this.imgFoto = imgFoto;
        this.Nombre = Nombre;
        this.Titulo = Titulo;
        this.Fecha = Fecha;
        this.Descripcion = Descripcion;

    }
}