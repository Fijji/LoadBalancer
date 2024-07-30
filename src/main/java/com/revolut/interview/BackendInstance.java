package com.revolut.interview;
/**
 * Represents a backend instance with a unique address.
 */
public class BackendInstance {
    private final String address;

    /**
     * Constructs a BackendInstance with the given address.
     *
     * @param address the unique address of the backend instance
     * @throws IllegalArgumentException if the address is null or empty
     */
    public BackendInstance(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        this.address = address;
    }

    /**
     * Returns the address of the backend instance.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BackendInstance that = (BackendInstance) obj;
        return address.equals(that.address);
    }

    @Override
    public String toString() {
        return "BackendInstance{" + "address='" + address + '\'' + '}';
    }
}
