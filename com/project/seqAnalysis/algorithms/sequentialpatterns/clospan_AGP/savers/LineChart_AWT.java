package com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.savers;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by astaputhra on 29/3/15.
 */
public class LineChart_AWT extends ApplicationFrame
{
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
        List<DefaultCategoryDataset> dataset = new ArrayList<>();
        DefaultKeyedValues2D keyedValues2D =  new DefaultKeyedValues2D();

        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        for(GraphOutput graphOutput : graphOutputs){
//            defaultCategoryDataset.addValue(graphOutput.getMinSup(), graphOutput.getType().toString(), graphOutput.getTime());
            defaultCategoryDataset.addValue(10, "CloSpan", "12345");
            defaultCategoryDataset.addValue(20, "CloSpan", "12345");
            defaultCategoryDataset.addValue(30, "Spade", "542123");
            defaultCategoryDataset.addValue(40, "Spade", "542123");
            defaultCategoryDataset.addValue(50, "Span", "76543");
            defaultCategoryDataset.addValue(60, "Span", "76543");
            dataset.add(defaultCategoryDataset);
        }

        return defaultCategoryDataset;
    }
}
