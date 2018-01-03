import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class InverseRegression {
	
	private static double a = 0.0, b = 0.0;
	
	public static void main(String [] args) {
		
		JFrame frame = new JFrame("Dashboard");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(900, 600));
		frame.setLayout(new FlowLayout());
		
		XYSeries series1, series2;
		JFreeChart chart;
		{
			series1 = new XYSeries("data");
			series2 = new XYSeries("model");
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(series1);
			dataset.addSeries(series2);
			dataset.setAutoWidth(true);
			
			chart = ChartFactory.createXYLineChart("", "centerY", "distance", dataset);
			chart.getXYPlot().getDomainAxis().setRange(240, 480);
			chart.getXYPlot().getRangeAxis().setRange(0, 156);
			
			ChartPanel panel = new ChartPanel(chart);
	
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			frame.getContentPane().add(panel);
		}
		
		JTextField resolution;
	{
		resolution = new JTextField("480");
		resolution.setPreferredSize(new Dimension(40, 20));
		frame.getContentPane().add(new JLabel("Resolution"));
		frame.getContentPane().add(resolution);
	}
		
			DefaultTableModel model;
			JTable table;
		{
			model = new DefaultTableModel() {
				private static final long serialVersionUID = -8517951691372878312L;
				String [] columnNames = {"centerY", "distance"};
				
				@Override
				public String getColumnName(int index) {
					return columnNames[index];
				}
				
				@Override
				public int getColumnCount() {
					return columnNames.length;
				}
			};
			table = new JTable(model);
			
			model.addColumn("centerY");
			model.addColumn("distance");
			model.addRow(new Object[] {0.0, 0.0});
			
			table.setPreferredScrollableViewportSize(new Dimension(200, 100));
			table.setFillsViewportHeight(true);
			
			table.getModel().addTableModelListener(new TableModelListener() {

				@Override
				public void tableChanged(TableModelEvent e) {
			        series1.clear();
			        ArrayList<double[]> data = new ArrayList<double[]>();
			        data.clear();
			        for (int i = 0; i < model.getRowCount(); i++) {
			        	double x = new Double(model.getValueAt(i, 0).toString());
			        	double y = new Double(model.getValueAt(i, 1).toString());
			        	series1.add(x, y);
			        	data.add(new double[]{x, y});
			        }
			        
			        if (!resolution.getText().toString().equals("")) {
				        double res = Double.valueOf(resolution.getText().toString());
				        
				        calcInverseReg(data, res);
				        series2.clear();
				        for (int i = 0; i < res; i++) {
				        	series2.add(i, a/(i-res/2)+b);
				        }
			        }
				}
				
			});
			
			frame.getContentPane().add(new JScrollPane(table));
		}
		
		{
			JButton addRow = new JButton("Add Row");
			addRow.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					model.addRow(new Object[] {0.0, 0.0});
				}
				
			});
			
			frame.getContentPane().add(addRow);
		}
		
		{
			JButton removeRow = new JButton("Remove Row");
			removeRow.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (model.getRowCount() > 1) {
						model.removeRow(model.getRowCount()-1);
					}
				}
			});
			
			frame.getContentPane().add(removeRow);
		}
		
		{
			JLabel xMinLabel = new JLabel("x min");
			
			JTextField xMinField = new JTextField("240");
			xMinField.setPreferredSize(new Dimension(30, 20));
			
			JLabel xMaxLabel = new JLabel("x max");
			
			JTextField xMaxField = new JTextField("480");
			xMaxField.setPreferredSize(new Dimension(30, 20));
			
			JLabel yMinLabel = new JLabel("y min");
			
			JTextField yMinField = new JTextField("0");
			yMinField.setPreferredSize(new Dimension(30, 20));
			
			JLabel yMaxLabel = new JLabel("y max");
			
			JTextField yMaxField = new JTextField("156");
			yMaxField.setPreferredSize(new Dimension(30, 20));
			
			JButton saveButton = new JButton("Save");
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					XYPlot plot = chart.getXYPlot();
					ValueAxis xAxis = plot.getDomainAxis();
					ValueAxis yAxis = plot.getRangeAxis();
					
					String xMinText = xMinField.getText().toString();
					String xMaxText = xMaxField.getText().toString();
					String yMinText = yMinField.getText().toString();
					String yMaxText = yMaxField.getText().toString();
					
					double xMin = 0, xMax = 0, yMin = 0, yMax = 0;
					
					if (!xMinText.isEmpty()) xMin = Double.parseDouble(xMinText);
					if (!xMaxText.isEmpty()) xMax = Double.parseDouble(xMaxText);
					if (!yMinText.isEmpty()) yMin = Double.parseDouble(yMinText);
					if (!yMaxText.isEmpty()) yMax = Double.parseDouble(yMaxText);
					
					if (xMin < xMax) {
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
				}
			});
			frame.getContentPane().add(xMinLabel);
			frame.getContentPane().add(xMinField);
			frame.getContentPane().add(xMaxLabel);
			frame.getContentPane().add(xMaxField);
			frame.getContentPane().add(yMinLabel);
			frame.getContentPane().add(yMinField);
			frame.getContentPane().add(yMaxLabel);
			frame.getContentPane().add(yMaxField);
			
			frame.getContentPane().add(saveButton);
		}
			
		frame.pack();
		frame.setVisible(true);
	}
	
	private static void calcInverseReg(ArrayList<double[]> data, double resolution) {
		
		double xMean = 0;
		double yMean = 0;
		for (int i = 0; i < data.size(); i++) {
			xMean += toLinear(data.get(i)[0], resolution)/data.size();
			yMean += data.get(i)[1]/data.size();
		}
		
		double numerator = 0;
		double denominator = 0;
		for (int i = 0; i < data.size(); i++) {
			double delta = toLinear(data.get(i)[0], resolution) - xMean;
			numerator += delta*(data.get(i)[1] - yMean);
			denominator += delta*delta;
		}
		
		a = numerator/denominator;
		b = yMean - a*xMean;
		
		System.out.println("A: " + a + ", B: " + b);
		
		double sumOfSquares = 0;
		double sumOfSquareResiduals = 0;
		for (int i = 0; i < data.size(); i++) {
			sumOfSquares += Math.pow(data.get(i)[1] - yMean, 2.0);
			sumOfSquareResiduals += Math.pow(a/(data.get(i)[0]-resolution/2)+b - data.get(i)[1], 2.0);
		}
		
		double r_2 = 1-sumOfSquareResiduals/sumOfSquares;
		System.out.println("R^2: " + r_2);
	}
	
	private static double toLinear(double x, double resolution) {
		if (x != resolution/2)
			return 1/(x-resolution/2);
		else 
			return 10000.0;
	}
}
