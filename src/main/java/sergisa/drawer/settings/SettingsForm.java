package sergisa.drawer.settings;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import sergisa.drawer.ui.TitleLabel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingsForm extends JFrame implements ActionListener, ChangeListener {
    private JPanel root;
    private JCheckBox gridShowCheckBox;
    private JCheckBox cursorShowCheckBox;
    private JSpinner gridStepSpinner;
    private TitleLabel titleLabel1;
    private TitleLabel titleLabel2;
    private JButton saveButton;
    private JCheckBox immediateApplianceCheckbox;
    private JCheckBox coordinatorShowCheckBox;
    Map<JComponent, SettingsItem> settingControlsMap = new HashMap<>();
    SpinnerNumberModel gridStepSpinnerModel;
    Settings settings = Settings.getInstance();

    public SettingsForm() {
        gridStepSpinnerModel = new SpinnerNumberModel(0, 0, 50, 1);
        $$$setupUI$$$();
        setSize(500, 500);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("Settings");
        setContentPane(root);
        settingControlsMap.put(gridShowCheckBox, settings.getSettingItem("grid.show"));
        settingControlsMap.put(cursorShowCheckBox, settings.getSettingItem("cursor.show"));
        settingControlsMap.put(coordinatorShowCheckBox, settings.getSettingItem("crosshair.show"));
        settingControlsMap.put(gridStepSpinner, settings.getSettingItem("grid.step"));
        settingControlsMap.put(immediateApplianceCheckbox, settings.getSettingItem("setting.immediatelyApply"));

        updateSettingsFromVault();
        reapplyListeners(Settings.getInstance().getBoolean("setting.immediatelyApply"));
        immediateApplianceCheckbox.addActionListener(e -> {
            reapplyListeners(((JCheckBox) e.getSource()).isSelected());
            updateSettingsStorage();
        });
        saveButton.addActionListener(e -> {
            updateSettingsStorage();
            setVisible(false);
            dispose();
        });
    }

    public void updateSettingsStorage() {
        settingControlsMap.forEach((jComponent, paramString) -> {
            storeSettingToVault(jComponent);
        });
    }

    private void updateSettingsFromVault() {
        for (JComponent control : settingControlsMap.keySet()) {
            if (control instanceof JCheckBox) {
                ((JCheckBox) control).setSelected(settings.getBoolean(settingControlsMap.get(control).getKey()));
            } else if (control instanceof JSpinner) {
                ((JSpinner) control).setValue(settings.getInt(settingControlsMap.get(control).getKey()));
            }
        }
    }

    private void setListeners(JComponent controlUIElement) {
        if (controlUIElement instanceof JCheckBox) {
            ((JCheckBox) controlUIElement).addActionListener(this);
        } else if (controlUIElement instanceof JSpinner) {
            ((JSpinner) controlUIElement).addChangeListener(this);
        }
    }

    private void removeListeners(JComponent controlUIElemnt) {
        if (controlUIElemnt instanceof JCheckBox) {
            ((JCheckBox) controlUIElemnt).removeActionListener(this);
        } else if (controlUIElemnt instanceof JSpinner) {
            ((JSpinner) controlUIElemnt).removeChangeListener(this);
        }
    }

    private void reapplyListeners(boolean attaching) {
        for (JComponent settingControllingComponent : settingControlsMap.keySet()) {
            if (attaching) setListeners(settingControllingComponent);
            else removeListeners(settingControllingComponent);
        }
    }

    private void storeSettingToVault(JComponent controlUIElement) {
        if (controlUIElement instanceof JCheckBox changedCheckbox) {
            settings.storeValue(
                    settingControlsMap.get(changedCheckbox).getKey(),
                    changedCheckbox.isSelected(),
                    settingControlsMap.get(changedCheckbox).getStorePoint(),
                    settingControlsMap.get(changedCheckbox).getNotify()
            );
        } else if (controlUIElement instanceof JSpinner changedSpinner) {
            settings.storeValue(
                    settingControlsMap.get(changedSpinner).getKey(),
                    gridStepSpinnerModel.getNumber(),
                    settingControlsMap.get(changedSpinner).getStorePoint(),
                    settingControlsMap.get(changedSpinner).getNotify()
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox changedCheckbox = (JCheckBox) e.getSource();
        storeSettingToVault(changedCheckbox);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner changedSpinner = (JSpinner) e.getSource();
        storeSettingToVault(changedSpinner);
    }

    private void createUIComponents() {
        titleLabel1 = new TitleLabel("wef", SwingConstants.CENTER);
        titleLabel2 = new TitleLabel("wef", SwingConstants.CENTER);
        gridStepSpinner = new JSpinner(gridStepSpinnerModel);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        root = new JPanel();
        root.setLayout(new GridLayoutManager(9, 3, new Insets(8, 8, 8, 8), -1, -1));
        final Spacer spacer1 = new Spacer();
        root.add(spacer1, new GridConstraints(7, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Шаг сетки");
        root.add(label1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(55, 24), null, 2, false));
        cursorShowCheckBox = new JCheckBox();
        cursorShowCheckBox.setText("Показывать курсор");
        root.add(cursorShowCheckBox, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        final Spacer spacer2 = new Spacer();
        root.add(spacer2, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Сохранить");
        root.add(saveButton, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        immediateApplianceCheckbox = new JCheckBox();
        immediateApplianceCheckbox.setText("Моментально применять");
        root.add(immediateApplianceCheckbox, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gridShowCheckBox = new JCheckBox();
        gridShowCheckBox.setText("Показывать сетку");
        root.add(gridShowCheckBox, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
        Font titleLabel1Font = this.$$$getFont$$$(null, Font.BOLD, -1, titleLabel1.getFont());
        if (titleLabel1Font != null) titleLabel1.setFont(titleLabel1Font);
        titleLabel1.setHorizontalAlignment(2);
        titleLabel1.setHorizontalTextPosition(2);
        titleLabel1.setText("Настройки графики");
        root.add(titleLabel1, new GridConstraints(6, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(114, 34), null, 0, false));
        Font titleLabel2Font = this.$$$getFont$$$(null, Font.BOLD, -1, titleLabel2.getFont());
        if (titleLabel2Font != null) titleLabel2.setFont(titleLabel2Font);
        titleLabel2.setHorizontalAlignment(2);
        titleLabel2.setHorizontalTextPosition(2);
        titleLabel2.setText("Настройки сетки");
        root.add(titleLabel2, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        root.add(gridStepSpinner, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 24), null, 0, false));
        coordinatorShowCheckBox = new JCheckBox();
        coordinatorShowCheckBox.setText("Показывать координатор");
        root.add(coordinatorShowCheckBox, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 2, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }
}
