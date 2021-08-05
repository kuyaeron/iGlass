package com.example.iGlass;

public class PlatesItem2 {

    String name;
    int image;
    String protanopia;
    String deuteranopia;

    public PlatesItem2(String name, int image, String protanopia, String deuteranopia){
            this.name = name;
            this.image = image;
            this.protanopia = protanopia;
            this.deuteranopia = deuteranopia;
    }

    public String getName() { return name; }
    public String getProtanopia() { return protanopia; }
    public String getDeuteranopia() { return deuteranopia; }
    public int getImage() { return image; }
}
