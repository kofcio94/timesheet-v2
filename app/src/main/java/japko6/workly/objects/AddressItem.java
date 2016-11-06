package japko6.workly.objects;

public class AddressItem {

    private String city;
    private String postalCode;
    private String street;

    public AddressItem(String city, String postalCode, String street) {
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }
}
