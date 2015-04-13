package com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items;

import com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items.abstractions.Abstraction_Qualitative;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items.creators.ItemAbstractionPairCreator;
import com.project.seqAnalysis.algorithms.sequentialpatterns.clospan_AGP.items.patterns.PatternCreator;


/**
 *
 * @author antonio
 */
public class RemoveStatics {

    public static void clear() {
        ItemAbstractionPairCreator.sclear();
        Abstraction_Qualitative.clear();
        PatternCreator.sclear();
    }
}
