package sergisa.drawer;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("sergisa.drawer");
        FlatLightLaf.setup();

        FigureView view = new FigureView();

        //view.setExtendedState(Frame.MAXIMIZED_BOTH);
        //view.setState(Frame.MAXIMIZED_BOTH);
        view.setLocationRelativeTo(null);

    }
}
