/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.menuItem.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.rolefeature.business.MenuItem;

/**
 *
 * @author naresh
 */
@Component("manuItemBuilder")
public class ManuItemBuilder {
    
    
    
   public  List<MenuItem> builderMenuitemData(List<String> features, List<MenuItem> menuItems){
       
       List<MenuItem> items=new ArrayList<MenuItem>();
       
       for(MenuItem menuItem:menuItems){
           features.contains(menuItem.getMenuName());
       }
       
        return null;
   
   } 
    
}
