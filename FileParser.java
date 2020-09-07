package co.com.jay.cenfileparser;

import java.io.*;
/**
 * Programa de linea de comandos para ajustar archivo texto
 * @author Jorge De La Torre Dávalos
 * 20-Oct-2011
 */
public class CENFileParser {
    /**
     * @author Jorge De La Torre Dávalos
     * @param ruta al archivo de texto completa
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            CENFileParser cenFileParser = new CENFileParser();
            cenFileParser.readReplace(args[0],"\"","");;//Comillas por Nada
            cenFileParser.readReplace(args[0]," ","	");//Espacios por Tabulacion
            System.out.println("Archivo Plano: "+args[0]+" Procesado Correctamente.");
        } else {
            System.out.println("Parametros de Entrada Incorrectos, verifique la ruta del archivo!!");
        }
    }

    /**
     * @author Jorge De La Torre Dávalos
     * @param archivoPlano Ruta al archivo de texto completa
     * @param cadenaAnterior Cadena o Caracter a Buscar
     * @param cadenaNueva Valor a colocar
     * @return vacio
     */
    public void readReplace(String archivoPlano, String cadenaAnterior, String cadenaNueva) {
        String lineaArchivo;
        StringBuilder constructorCadena = new StringBuilder();
        try {
            FileInputStream flujoDatos = new FileInputStream(archivoPlano);
            BufferedReader Lector = new BufferedReader(new InputStreamReader(flujoDatos));
            while ((lineaArchivo = Lector.readLine()) != null) {
                lineaArchivo = lineaArchivo.replaceAll(cadenaAnterior, cadenaNueva);
                constructorCadena.append(lineaArchivo).append("\r\n");//Nueva linea, Compatible con varios SOs
            }
            Lector.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(archivoPlano));
            out.write(constructorCadena.toString());
            out.close();
        }catch (Throwable e) {
            System.err.println("Error: " + e);
        }
        System.gc();
    }
}
