package sergisa.drawer.settings;

public class SettingsItem<T> {
    public String key;
    public T value;
    public Notify notify;
    public StorePoint storePoint;

    public SettingsItem(String key, T value, Notify notify, StorePoint storePoint) {
        this.key = key;
        this.value = value;
        this.notify = notify;
        this.storePoint = storePoint;
    }

    public String getKey() {
        return key;
    }

    public SettingsItem<T> setKey(String key) {
        this.key = key;
        return this;
    }

    public T getValue() {
        return value;
    }

    public SettingsItem<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public Notify getNotify() {
        return notify;
    }

    public SettingsItem<T> setNotify(Notify notify) {
        this.notify = notify;
        return this;
    }

    public StorePoint getStorePoint() {
        return storePoint;
    }

    public SettingsItem<T> setStorePoint(StorePoint storePoint) {
        this.storePoint = storePoint;
        return this;
    }

    public enum Notify {
        NOTIFIABLE,
        SILENT
    }

    public enum StorePoint {
        APP,
        GLOBAL_OS,
        USER
    }

}
