package org.kasource.spring.jms.support.converter.javabeans;


public class Tea {
    private String brand;
    private String taste;
    
    public Tea() {}
    
    public Tea(String brand, String taste) {
        this.brand = brand;
        this.taste = taste;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return the taste
     */
    public String getTaste() {
        return taste;
    }

    /**
     * @param taste the taste to set
     */
    public void setTaste(String taste) {
        this.taste = taste;
    }
}
