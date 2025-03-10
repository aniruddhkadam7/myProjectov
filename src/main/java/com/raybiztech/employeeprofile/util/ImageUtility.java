/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.raybiztech.recruitment.controller.ImageDTO;

/**
 *
 * @author hari
 */
public class ImageUtility {

    public ImageDTO getImagebyPath(String imageUrl) {

        if (!"undefined".equalsIgnoreCase(imageUrl)
                && !"".equals(imageUrl) && imageUrl != null) {
            return new ImageDTO(getImageExtesion(imageUrl), getImageByteData(imageUrl), imageUrl);
        } else {
            return new ImageDTO(null, null, null);
        }
    }

    public String getImageExtesion(String imgName) {

        String fileExtension = null;
        if (imgName != null) {
            int i = imgName.lastIndexOf('.');
            if (i > 0) {
                fileExtension = imgName.substring(i + 1);

            }
        }
        return fileExtension;
    }

    public byte[] getImageByteData(String photoUrl) {
        if (photoUrl != null) {
            try {

                BufferedImage bufferedImage = ImageIO.read(new File(photoUrl));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, getImageExtesion(photoUrl), byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();

            } catch (IOException e) {
            }

        }
        return new byte[]{};

    }
}
