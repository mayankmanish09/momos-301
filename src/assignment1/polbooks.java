package assignment1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.controls.ZoomToFitControl;


public class polbooks {

	private static int i,j = 0;
	private static BufferedReader br;
	public static void main(String[] args) throws IOException {
		try {

			// PARSING THE DATA INTO A GRAPH

			br = new BufferedReader(new FileReader("C:\\Users\\Raj\\workspace\\prefuse\\momos\\polbooks.gml"));
			String line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();		
			boolean bool;
			if(line.equals("  directed 0")){
				bool = false;
			}else{
				bool = true;
			}

			//MAKING TABLES TO STORE NODE DATA AND EDGE DATA

			Table nodeData = new Table();
			nodeData.addColumn("id", int.class);
			nodeData.addColumn("label", String.class);
			nodeData.addColumn("value", String.class);
			Table edgeData = new Table();
			edgeData.addColumn("con", String.class);
			edgeData.addColumn("source", int.class);
			edgeData.addColumn("target", int.class);

			//MAKING THE GRAPH

			Graph g = new Graph(nodeData, edgeData, bool,"source","target");
			while(line != null){
				if(line.equals("  node")){
					br.readLine();
					br.readLine();
					String label = br.readLine();
					String value = br.readLine();
					Node p = g.addNode();

					//FILLING INTO THE NODE DATA TABLE

					p.setInt("id", i);
					p.set("label",label.split("[\"]+")[1] );
					p.set("value",value.split("[\"]+")[1]);
					i++;
					//labelArray[i] = label.split("[\"]+")[1];
					//valueArray[i] = value.split("[\"]+")[1];
					line = br.readLine();
				}

				//MAKING EDGES OF THE GRAPH

				if(line.equals("  edge")){
					br.readLine();
					String source = br.readLine();
					String target = br.readLine();
					int s = Integer.parseInt(source.split("[source ]+")[1]);
					int t = Integer.parseInt(target.split("[target ]+")[1]);
					g.addEdge(s,t);
					j++;
				}
				line = br.readLine();
			}

			//FILLING INTO EDGE DATA TABLE
			int similarEdges = 0;
			int libncon = 0;
			for(int k = 0; k < j; k++){
				Edge edge = g.getEdge(k);
				String s1 = (String) edge.getSourceNode().get("value");
				String s2 = (String) edge.getTargetNode().get("value");
				if(s1.equals("c") && s2.equals("c")){
					edge.set("con", "a");
					similarEdges++;
				}else if(s1.equals("l") && s2.equals("l")){
					edge.set("con", "b");
					similarEdges++;
				}else{
					edge.set("con", "c");
					if(s1.equals("n") && s2.equals("n")) similarEdges++;
					if(s1.equals("l") && s2.equals("c")) libncon++;					
				}
			}

			float triad=0;
			for(int k=0;k<j;k++){
				int n1=g.getSourceNode(k);
				int n2=g.getTargetNode(k);
				for(int l=0;l<j;l++){
					if(g.getSourceNode(l)==n1 && l!=k){
						int n3=g.getAdjacentNode(l, n1);
						for(int m=0;m<j;m++){
							if(g.getSourceNode(m)==n3 && m!=l){
								int n4=g.getAdjacentNode(m, n3);
								if(n4==n2) triad++;
							}
						}
					}
				}
			}

			float cluscoeff=i*(i-1)/2;
			System.out.println("number of edges between nodes of the same type = "+similarEdges);
			System.out.println("number of edges between nodes of 'l' and 'c' type = "+libncon);
			System.out.println("Total number of edges = "+j);
			System.out.println("required ratio = "+(float)similarEdges/j);
			System.out.println("number of triads = "+ (int)triad);
			System.out.println("Clustering Coefficient = "+(float)(triad/cluscoeff));

			//SETTING UP THE VISUALISATION

			Visualization vis = new Visualization();
			vis.add("graph", g);
			LabelRenderer r = new LabelRenderer("id");
			r.setRoundedCorner(8, 8);
			vis.setRendererFactory(new DefaultRendererFactory(r));

			//MAKING PALETTES FOR USE FOR DATACOLORACTION

			int[] palette = new int[] {ColorLib.rgb(255,180,180), ColorLib.rgb(190,190,255), ColorLib.rgb(190, 255, 180)};
			int[] palette2 = new int[] {ColorLib.rgb(255,180,180), ColorLib.rgb(190,190,255), ColorLib.gray(150)};

			//ADDING DATA COLR ACTION FOR NODE AND EDGE DATA;
			//SETTING UP HIGHLIGHT FOR NEIGHBOR HIGHLIGHT

			DataColorAction fill = new DataColorAction("graph.nodes", "value",Constants.NOMINAL, VisualItem.FILLCOLOR, palette);
			fill.add(VisualItem.FIXED, ColorLib.rgb(255,100,100));
			fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255,200,125));
			ColorAction text = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0));
			//ColorAction edges = new ColorAction("graph.edges",VisualItem.STROKECOLOR, ColorLib.gray(200));
			DataColorAction edges = new DataColorAction("graph.edges","con", Constants.NOMINAL, VisualItem.STROKECOLOR, palette2);

			//MAKING ACTION LIST TO IMPLEMENT THE ACTIONS

			ActionList color = new ActionList();
			color.add(fill);
			color.add(text);
			color.add(edges);

			//MAKING AN ACTION LIST WHICH WILL WORK INFINITELY

			ActionList layout = new ActionList(Activity.INFINITY);
			//FORCED DIRECTED LAYOUT FOR MAKING CLUSTERS
			layout.add(new ForceDirectedLayout("graph"));
			layout.add(fill);
			layout.add(new RepaintAction());
			vis.putAction("color", color);
			vis.putAction("layout", layout);

			//SETTING UP THE DISPLAY

			Display display = new Display(vis);
			display.setSize(720, 500);
			//ADDING DRAG, ZOOM, PAN, CONTROLS
			display.addControlListener(new DragControl()); 
			display.addControlListener(new PanControl());  
			display.addControlListener(new ZoomControl());
			display.addControlListener(new WheelZoomControl());
			display.addControlListener(new ZoomToFitControl());
			display.addControlListener(new NeighborHighlightControl());
			display.addControlListener(new FinalControlListener());


			//SETTING UP AND RUNNING THE LAYOUT

			JFrame frame = new JFrame("ass1");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(display);
			frame.pack();        
			frame.setVisible(true);
			vis.run("color"); 
			vis.run("layout"); 

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
