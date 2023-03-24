package com.wt.backend.common;

/**
 * @author xcx
 * @date
 */
public class GateWayParams {
    volatile public static String gatewayAdress = "http://123.60.169.38:8090";
    volatile public static String refreshPath = "/route/refrash";
    volatile public static String reloadPath = "/wbset/reload";
    volatile public static String checkPath = "/check/check";


    public static String getParams() {
        return "{ " + "gatewayAdress: " + gatewayAdress + ", "
                    + "refreshPath: " + refreshPath   + " ,"
                    + "reloadPath: " + reloadPath   + " ,"
                    + "checkPath: " + checkPath   + " }" ;
    }
}
