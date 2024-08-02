package com.exercise.interview;

/**
 * Represents a backend instance with a unique address.
 */
public record BackendInstance(String address) {
    /**
     * Constructs a BackendInstance with the given address.
     *
     * @param address the unique address of the backend instance
     * @throws IllegalArgumentException if the address is null or empty
     */
    public BackendInstance {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
    }

    /**
     * Returns the address of the backend instance.
     *
     * @return the address
     */
    @Override
    public String address() {
        return address;
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
