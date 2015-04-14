package com.project.seqAnalysis.algorithms;

import com.project.seqAnalysis.algorithms.GraphOutput;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by astaputhra on 29/3/15.
 */
public class LineChart_AWT extends ApplicationFrame {


    public LineChart_AWT( String applicationTitle , String chartTitle,List<GraphOutput> graphOutputs)
    {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle, "MinSup", "Time", createDataset(graphOutputs),
                PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }

    private DefaultCategoryDataset createDataset(List<GraphOutput> graphOutputs)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(GraphOutput graphOutput : graphOutputs){
//            defaultCategoryDataset.addValue(graphOutput.getMinSup(), graphOutput.getType().toString(), graphOutput.getTime());
            dataset.addValue(10, "CloSpan", "12345");
            dataset.addValue(50, "CloSpan", "123456");
            dataset.addValue(70, "CloSpan", "123457");
            dataset.addValue(80, "CloSpan", "123458");
            dataset.addValue(90, "CloSpan", "12349");
            dataset.addValue(100, "CloSpan", "123423");
            /*dataset.addValue(30, "Spade", "542123");
            dataset.addValue(40, "Spade", "542123");
            dataset.addValue(50, "Span", "76543");
            dataset.addValue(60, "Span", "76543");
  */      }

        return dataset;
    }
}
