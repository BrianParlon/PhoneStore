package com.example.phonefinder;

public class Upload {

    private String name;
    private String imageUrl;
    private String category;
    private String manufacturer;
    private String stock;

    public Upload(){}

    public Upload(String name, String imageUrl,String category,String manufacturer, String stock){
       if(name.trim().equals("")){
           name="Empty";
       }
        this.name=name;
        this.imageUrl=imageUrl;
        this.manufacturer=manufacturer;
        this.category=category;
        this.stock=stock;
    }


    public String getName(){
        return name;

    }
    public void setName(String name){

        this.name=name;
    }

    public String getImageUrl(){
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {

        this.imageUrl=imageUrl;
    }
    public String getManufacturer(){
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer=manufacturer;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category) {
        this.category=category;
    }
    public String getStock(){
        return stock;
    }
    public void setStock(String stock) {
        this.stock=stock;
    }


}
