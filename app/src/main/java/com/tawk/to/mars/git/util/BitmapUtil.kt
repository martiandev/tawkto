package com.tawk.to.mars.git.util

import android.graphics.Bitmap
import android.graphics.Color
//Used for bitmap functions
class BitmapUtil {
    companion object
    {
        //Inverts the color of the bitmap
        suspend infix fun doInvert(src: Bitmap): Bitmap? {
            //the result to be returned following the size and configuration of the original bitmap
            val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
            //Refers to alpha
            var A: Int
            //Refers to red
            var R: Int
            //Refers to green
            var G: Int
            //Refers to blue
            var B: Int
            //Refers to the color of the target pixel
            var pixelColor: Int
            val height = src.height
            val width = src.width
            //loops trough the bitmap pixels vertically
            for (y in 0 until height) {
                //loops through the bitmap pixels horizontally
                for (x in 0 until width) {
                    //Retrieves the color of the target pixel
                    pixelColor = src.getPixel(x, y)
                    //Retains alpha or translucency of the pixel
                    A = Color.alpha(pixelColor)
                    //Subrtracts the value of each color (RGB) from the max value which is 255 to get the inverted value
                    R = 255 - Color.red(pixelColor)
                    G = 255 - Color.green(pixelColor)
                    B = 255 - Color.blue(pixelColor)
                    //adds the pixel to the output bitmap
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B))
                }
            }
            return bmOut
        }

    }
}