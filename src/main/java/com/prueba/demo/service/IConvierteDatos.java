package com.prueba.demo.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

}
