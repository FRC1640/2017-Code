import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;


public class Main {
	
	public static double time0 = 0;
	public static boolean stopped = true;
	
	public static void main(String [] args) {
		System.loadLibrary("ntcore");
		
		JFrame frame = new JFrame("Dashboard");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600, 500));
		frame.setLayout(new FlowLayout());
		
		XYSeries series1 = new XYSeries("velocity");
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.setAutoWidth(true);
		
		JFreeChart chart = ChartFactory.createXYLineChart("", "Time", "", dataset);
		
		ChartPanel panel = new ChartPanel(chart);
		
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roboRIO-1640-FRC.local");
		NetworkTable robotTable = NetworkTable.getTable("Graphing");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		robotTable.addTableListener("time", new ITableListener() {

			@Override
			public void valueChanged(ITable arg0, String arg1, Object arg2,
					boolean arg3) {
				if (robotTable.getNumber("velocity", -999) != -999 && robotTable.getNumber("time", -999) != -999) {
					series1.add(robotTable.getNumber("time", -999)-time0, robotTable.getNumber("velocity", -999));
				}
			}
			
		}, true);
		
		frame.getContentPane().add(panel);
		
		JLabel xRangeLabel = new JLabel("range");
		
		JTextField xRangeField = new JTextField();
		xRangeField.setPreferredSize(new Dimension(30, 20));
		
		JLabel xMinLabel = new JLabel("x min");
		
		JTextField xMinField = new JTextField();
		xMinField.setPreferredSize(new Dimension(30, 20));
		
		JLabel xMaxLabel = new JLabel("x max");
		
		JTextField xMaxField = new JTextField();
		xMaxField.setPreferredSize(new Dimension(30, 20));
		
		JLabel yMinLabel = new JLabel("y min");
		
		JTextField yMinField = new JTextField();
		yMinField.setPreferredSize(new Dimension(30, 20));
		
		JLabel yMaxLabel = new JLabel("y max");
		
		JTextField yMaxField = new JTextField();
		yMaxField.setPreferredSize(new Dimension(30, 20));
		
		JLabel setPointLabel = new JLabel("setPoint");
		
		JTextField setPointField = new JTextField();
		setPointField.setPreferredSize(new Dimension(30, 20));
		
		JLabel pLabel = new JLabel("p");
		
		JTextField pField = new JTextField();
		pField.setPreferredSize(new Dimension(30, 20));
		
		JLabel iLabel = new JLabel("i");
		
		JTextField iField = new JTextField();
		iField.setPreferredSize(new Dimension(30, 20));
		
		JLabel dLabel = new JLabel("d");
		
		JTextField dField = new JTextField();
		dField.setPreferredSize(new Dimension(30, 20));
		
		JLabel fLabel = new JLabel("f");
		
		JTextField fField = new JTextField();
		fField.setPreferredSize(new Dimension(30, 20));
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				XYPlot plot = chart.getXYPlot();
				ValueAxis xAxis = plot.getDomainAxis();
				ValueAxis yAxis = plot.getRangeAxis();
				
				String xRangeText = xRangeField.getText().toString();
				String xMinText = xMinField.getText().toString();
				String xMaxText = xMaxField.getText().toString();
				String yMinText = yMinField.getText().toString();
				String yMaxText = yMaxField.getText().toString();
				
				double xRange = 0, xMin = 0, xMax = 0, yMin = 0, yMax = 0;
				
				if (!xRangeText.isEmpty()) xRange = Double.parseDouble(xRangeText);
				if (!xMinText.isEmpty()) xMin = Double.parseDouble(xMinText);
				if (!xMaxText.isEmpty()) xMax = Double.parseDouble(xMaxText);
				if (!yMinText.isEmpty()) yMin = Double.parseDouble(yMinText);
				if (!yMaxText.isEmpty()) yMax = Double.parseDouble(yMaxText);
				
				if (xRange > 0) {
					xAxis.setFixedAutoRange(xRange);
				}
				
				else if (xMin < xMax) {
					xAxis.setRange(new Range(Double.parseDouble(xMinField.getText().toString()), 
							Double.parseDouble(xMaxField.getText().toString())));
				}
				else {
					xAxis.setAutoRange(true);
				}
				if (yMin < yMax) {
					yAxis.setRange(new Range(Double.parseDouble(yMinField.getText().toString()), 
							Double.parseDouble(yMaxField.getText().toString())));
				}
				else {
					yAxis.setAutoRange(true);
				}
				
				robotTable.putNumber("setPoint", 0);
				robotTable.putNumber("p", 0);
				robotTable.putNumber("i", 0);
				robotTable.putNumber("d", 0);
				robotTable.putNumber("f", 0);
			}
		});
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				series1.clear();
				time0 = robotTable.getNumber("time", 0);
			}
		});
		
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopped = !stopped;
				chart.getXYPlot().getDomainAxis().setAutoRange(stopped);
			}
		});
		
		frame.getContentPane().add(xRangeLabel);
		frame.getContentPane().add(xRangeField);
		frame.getContentPane().add(xMinLabel);
		frame.getContentPane().add(xMinField);
		frame.getContentPane().add(xMaxLabel);
		frame.getContentPane().add(xMaxField);
		frame.getContentPane().add(yMinLabel);
		frame.getContentPane().add(yMinField);
		frame.getContentPane().add(yMaxLabel);
		frame.getContentPane().add(yMaxField);
		frame.getContentPane().add(setPointLabel);
		frame.getContentPane().add(setPointField);
		frame.getContentPane().add(pLabel);
		frame.getContentPane().add(pField);
		frame.getContentPane().add(iLabel);
		frame.getContentPane().add(iField);
		frame.getContentPane().add(dLabel);
		frame.getContentPane().add(dField);
		frame.getContentPane().add(fLabel);
		frame.getContentPane().add(fField);
		
		frame.getContentPane().add(saveButton);
		
		frame.getContentPane().add(resetButton);
		
		frame.getContentPane().add(stopButton);
		
		frame.pack();
		frame.setVisible(true);
	}
}
