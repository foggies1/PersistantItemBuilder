package net.foggies.testing;

import org.bukkit.persistence.PersistentDataType;

public class PDValue<T, Z> {

    private final PersistentDataType<T, Z> dataType;
    private final Z value;

    public PDValue(PersistentDataType<T, Z> dataType, Z value) {
        this.dataType = dataType;
        this.value = value;
    }

    public PersistentDataType<T, Z> getDataType() {
        return dataType;
    }

    public Z getValue() {
        return value;
    }
}
