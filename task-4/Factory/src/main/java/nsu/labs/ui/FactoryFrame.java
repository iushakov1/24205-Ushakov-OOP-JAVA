package nsu.labs.ui;

import nsu.labs.logic.ComponentSupplier;
import nsu.labs.logic.Dealer;
import nsu.labs.model.Accessory;
import nsu.labs.storage.Storage;
import nsu.labs.threadpool.ThreadPool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class FactoryFrame extends JFrame {
    private final ThreadPool threadPool;

    private final Storage<?> bodyStorage;
    private final Storage<?> motorStorage;
    private final Storage<?> accessoryStorage;
    private final Storage<?> carStorage;

    public FactoryFrame(
            Storage<?> bodies,
            Storage<?> motors,
            Storage<?> accessories,
            Storage<?> cars,
            ComponentSupplier<?> bodySup,
            ComponentSupplier<?> motorSup,
            ArrayList<ComponentSupplier<Accessory>> accSuppliers,
            Dealer[] dealers,
            ThreadPool pool,
            Runnable onShutdown)
    {
        super("Factory");
        this.threadPool = pool;
        this.bodyStorage = bodies;
        this.motorStorage = motors;
        this.accessoryStorage = accessories;
        this.carStorage = cars;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new GridLayout(2, 1));
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Storages and queue"));

        JLabel bodyLabel = new JLabel();
        JLabel motorLabel = new JLabel();
        JLabel accLabel = new JLabel();
        JLabel carLabel = new JLabel();
        JLabel queueLabel = new JLabel();

        infoPanel.add(new JLabel("Body:"));
        infoPanel.add(bodyLabel);

        infoPanel.add(new JLabel("Motor:"));
        infoPanel.add(motorLabel);

        infoPanel.add(new JLabel("Accessory:"));
        infoPanel.add(accLabel);

        infoPanel.add(new JLabel("Cars on storage:"));
        infoPanel.add(carLabel);

        infoPanel.add(new JLabel("Task in queue:"));
        infoPanel.add(queueLabel);

        JPanel controlPanel = new JPanel(new GridLayout(4, 1));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Speed (ms)"));

        controlPanel.add(createSliderPanel("Body's supply", 0, 5000, 1000, bodySup::setDelay));
        controlPanel.add(createSliderPanel("Motor's supply", 0, 5000, 1000, motorSup::setDelay));
        controlPanel.add(createSliderPanel("Accessory's supply", 0, 5000, 1000, val -> {
            for (ComponentSupplier<Accessory> s: accSuppliers) s.setDelay(val);
        }));
        controlPanel.add(createSliderPanel("Dealer request", 0, 5000, 2000, val -> {
            for (Dealer d : dealers) d.setDelay(val);
        }));

        add(infoPanel);
        add(controlPanel);

        Timer updateTimer = new Timer(100, e -> {
            bodyLabel.setText(bodyStorage.size() + " / " + bodyStorage.getCapacity());
            motorLabel.setText(motorStorage.size() + " / " + motorStorage.getCapacity());
            accLabel.setText(accessoryStorage.size() + " / " + accessoryStorage.getCapacity());
            carLabel.setText(carStorage.size() + " / " + carStorage.getCapacity());
            queueLabel.setText(String.valueOf(threadPool.getTaskQueueSize()));
        });
        updateTimer.start();

        addWindowListener(new factoryFrameAdapter(onShutdown));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createSliderPanel(String title, int min, int max, int init, java.util.function.Consumer<Integer> onChange) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title), BorderLayout.WEST);
        JSlider slider = new JSlider(min, max, init);
        slider.addChangeListener(e -> onChange.accept(slider.getValue()));
        p.add(slider, BorderLayout.CENTER);
        return p;
    }

    private class factoryFrameAdapter extends WindowAdapter{

        Runnable onShutdownCommand;

        public factoryFrameAdapter(Runnable onShutdown){
            onShutdownCommand = onShutdown;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            onShutdownCommand.run();
        }
    }
}
