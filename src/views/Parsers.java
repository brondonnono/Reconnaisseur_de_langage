/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import beans.Automata;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Ndadji
 */
public class Parsers {
    private static final mxStylesheet STYLE = new mxStylesheet();
    static{
        HashMap<String, Object> style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_TRIANGLE);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
        style.put(mxConstants.STYLE_STROKECOLOR, "#E3A646");
        STYLE.putCellStyle("PINK_INIT", style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_FILLCOLOR, "#E34646");
        style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        STYLE.putCellStyle("PINK_FINAL", style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_DOUBLE_ELLIPSE);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_FILLCOLOR, "#E34646");
        style.put(mxConstants.STYLE_STROKECOLOR, "#E3A646");
        STYLE.putCellStyle("PINK_BOTH", style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
        style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        STYLE.putCellStyle("PINK_NONE", style);
        
        style = new HashMap<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        style.put(mxConstants.STYLE_OPACITY, 50);
        style.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_LEFT);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_STROKECOLOR, "#E346AA");
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        STYLE.setDefaultEdgeStyle(style);
    }
    
    public static mxGraphComponent automataToMxGraph(Automata automata) throws Exception{
        mxGraphComponent graphComponent = null;
        Document xmlDocument = mxDomUtils.createDocument();
        Element node = null;
        ArrayList<Object> vertexes = new ArrayList<Object>();
        ArrayList<Object> states = automata.getStates();
        mxGraph graph = new mxGraph();
        graph.setStylesheet(STYLE);
	Object parent = graph.getDefaultParent(), obj = null;
        graph.getModel().beginUpdate();
        try{
            for(Object state : states){
                boolean isFinal = false, isInitial = false;
                String style = "NONE";
                if(state.equals(automata.getInitialState()))
                    isInitial = true;
                if(automata.getFinalStates().contains(state))
                    isFinal = true;
                if(isFinal && isInitial)
                    style = "BOTH";
                else{
                    if(isFinal)
                        style = "FINAL";
                    if(isInitial)
                        style = "INIT";
                }
                try{
                    node = xmlDocument.createElement(state.toString());
                }catch(Exception e){
                    node = xmlDocument.createElement("q" + state.toString());
                }
                obj = graph.insertVertex(parent, null, node.getNodeName(), 0, 0, 25, 25, "PINK_" + style);
                vertexes.add(obj);
            }
            for(Object state : states){
                for(Object symb : automata.getSymbols()){
                    for(Object dest : automata.getTransitionTable().get(state, symb)){
                        graph.insertEdge(parent, null, symb.toString(), vertexes.get(states.indexOf(state)), vertexes.get(states.indexOf(dest)));
                    }
                }
            }
        }finally
        {
            graph.getModel().endUpdate();
        }
        
        graph.setCellsResizable(false);
        graph.setMultigraph(true);
        graph.setStylesheet(STYLE);
        
        mxCircleLayout layout = new mxCircleLayout(graph, 250.0);
        layout.execute(parent);
        mxParallelEdgeLayout lay = new mxParallelEdgeLayout(graph);
        lay.execute(parent);
        
        graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        graphComponent.setToolTips(true);
        
        return graphComponent;
    }
}
