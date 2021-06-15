package com.amanvishnani.sim8085;

import com.amanvishnani.sim8085.domain.IData;
import com.amanvishnani.sim8085.domain.Impl.Data;
import com.amanvishnani.sim8085.domain.IAddress;
import com.amanvishnani.sim8085.domain.Impl.Address;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Aman Vishnani
 */
public class Util {
    
    public static String padThreeZeros(String hexData) {
        switch (hexData.length()) {
            case 1:
                hexData = "000" + hexData;
                break;
            case 2:
                hexData = "00" + hexData;
                break;
            case 3:
                hexData = "0" + hexData;
                break;
            default:
                break;
        }
        return hexData;
    }

}
