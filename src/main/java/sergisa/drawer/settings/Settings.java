package sergisa.drawer.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class Settings {
    private static Settings instance;
    private Map<String, Object> appContextSettingsStorage;
    private List<SettingChangeListener> listeners;
    private Map<String, SettingsItem<?>> defaults = new HashMap<>();
    Preferences RegistryPreferences = Preferences.userRoot();

    private Settings() {
        listeners = new ArrayList<>();
        appContextSettingsStorage = new HashMap<>();
        initDefaults();

        defaults.forEach((key, settingsItem) -> {
            if (settingsItem.value.getClass() == Boolean.class) {
                appContextSettingsStorage.put(key, RegistryPreferences.getBoolean(key, (boolean) settingsItem.value));
            }
            if (settingsItem.value.getClass() == String.class) {
                appContextSettingsStorage.put(key, RegistryPreferences.get(key, (String) settingsItem.value));
            }
            if (settingsItem.value.getClass() == Integer.class) {
                appContextSettingsStorage.put(key, RegistryPreferences.getInt(key, (int) settingsItem.value));
            }
        });
        RegistryPreferences.addPreferenceChangeListener(e -> {
            notifyListeners(e.getKey(), e.getNewValue());
        });
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public Settings addSettingChangeListener(SettingChangeListener listener) {
        this.listeners.add(listener);
        return this;
    }

    private <T> void notifyListeners(String key, T value) {
        if (!listeners.isEmpty()) {
            for (SettingChangeListener listener : listeners) {
                listener.onValueChanged(value);
            }
        }
    }

    public void storeValue(String key, Object value, SettingsItem.StorePoint storePoint, SettingsItem.Notify notifyLevel) {
        if (storePoint == SettingsItem.StorePoint.GLOBAL_OS) {
            if (value instanceof String) RegistryPreferences.put(key, (String) value);
            else if (value instanceof Integer) RegistryPreferences.putInt(key, (Integer) value);
            else if (value instanceof Boolean) RegistryPreferences.putBoolean(key, (Boolean) value);
        }
        appContextSettingsStorage.put(key, value);
        if (notifyLevel == SettingsItem.Notify.NOTIFIABLE) notifyListeners(key, value);

    }

    public void putBoolean(String key, boolean value) {
        storeValue(key, value, SettingsItem.StorePoint.APP, SettingsItem.Notify.NOTIFIABLE);
    }

    public void putInt(String key, int value) {
        storeValue(key, value, SettingsItem.StorePoint.APP, SettingsItem.Notify.NOTIFIABLE);
    }

    public void putString(String key, String value) {
        storeValue(key, value, SettingsItem.StorePoint.APP, SettingsItem.Notify.NOTIFIABLE);
    }

    public String getString(String key) {
        return (String) appContextSettingsStorage.get(key);
    }

    public int getInt(String key) {
        return (int) appContextSettingsStorage.get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) appContextSettingsStorage.get(key);
    }

    public SettingsItem<?> getSettingItem(String key) {
        return defaults.get(key);
    }

    public void initDefaults() {
        defaults.put("grid.show", new SettingsItem<Boolean>(
                "grid.show",
                true,
                SettingsItem.Notify.NOTIFIABLE,
                SettingsItem.StorePoint.GLOBAL_OS
        ));
        defaults.put("grid.step", new SettingsItem<Integer>(
                "grid.step",
                20,
                SettingsItem.Notify.NOTIFIABLE,
                SettingsItem.StorePoint.GLOBAL_OS

        ));
        defaults.put("crosshair.show", new SettingsItem<Boolean>(
                "crosshair.show",
                true,
                SettingsItem.Notify.NOTIFIABLE,
                SettingsItem.StorePoint.GLOBAL_OS

        ));
        defaults.put("setting.immediatelyApply", new SettingsItem<Boolean>(
                "setting.immediatelyApply",
                true,
                SettingsItem.Notify.NOTIFIABLE,
                SettingsItem.StorePoint.GLOBAL_OS

        ));
        defaults.put("cursor.show", new SettingsItem<Boolean>(
                "cursor.show",
                true,
                SettingsItem.Notify.NOTIFIABLE,
                SettingsItem.StorePoint.GLOBAL_OS

        ));
    }
}
