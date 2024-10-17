package sergisa.drawer;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        FigureView view = new FigureView();
        view.setLocationRelativeTo(null);
    }
}
